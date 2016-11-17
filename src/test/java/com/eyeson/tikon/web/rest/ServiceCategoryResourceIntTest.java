package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ServiceCategory;
import com.eyeson.tikon.repository.ServiceCategoryRepository;
import com.eyeson.tikon.service.ServiceCategoryService;
import com.eyeson.tikon.repository.search.ServiceCategorySearchRepository;
import com.eyeson.tikon.web.rest.dto.ServiceCategoryDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceCategoryMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ServiceCategoryResource REST controller.
 *
 * @see ServiceCategoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ServiceCategoryResourceIntTest {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBB";

    @Inject
    private ServiceCategoryRepository serviceCategoryRepository;

    @Inject
    private ServiceCategoryMapper serviceCategoryMapper;

    @Inject
    private ServiceCategoryService serviceCategoryService;

    @Inject
    private ServiceCategorySearchRepository serviceCategorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restServiceCategoryMockMvc;

    private ServiceCategory serviceCategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceCategoryResource serviceCategoryResource = new ServiceCategoryResource();
        ReflectionTestUtils.setField(serviceCategoryResource, "serviceCategoryService", serviceCategoryService);
        ReflectionTestUtils.setField(serviceCategoryResource, "serviceCategoryMapper", serviceCategoryMapper);
        this.restServiceCategoryMockMvc = MockMvcBuilders.standaloneSetup(serviceCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceCategorySearchRepository.deleteAll();
        serviceCategory = new ServiceCategory();
        serviceCategory.setCategoryName(DEFAULT_CATEGORY_NAME);
    }

    @Test
    @Transactional
    public void createServiceCategory() throws Exception {
        int databaseSizeBeforeCreate = serviceCategoryRepository.findAll().size();

        // Create the ServiceCategory
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.serviceCategoryToServiceCategoryDTO(serviceCategory);

        restServiceCategoryMockMvc.perform(post("/api/service-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceCategoryDTO)))
                .andExpect(status().isCreated());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategories = serviceCategoryRepository.findAll();
        assertThat(serviceCategories).hasSize(databaseSizeBeforeCreate + 1);
        ServiceCategory testServiceCategory = serviceCategories.get(serviceCategories.size() - 1);
        assertThat(testServiceCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);

        // Validate the ServiceCategory in ElasticSearch
        ServiceCategory serviceCategoryEs = serviceCategorySearchRepository.findOne(testServiceCategory.getId());
        assertThat(serviceCategoryEs).isEqualToComparingFieldByField(testServiceCategory);
    }

    @Test
    @Transactional
    public void getAllServiceCategories() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        // Get all the serviceCategories
        restServiceCategoryMockMvc.perform(get("/api/service-categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);

        // Get the serviceCategory
        restServiceCategoryMockMvc.perform(get("/api/service-categories/{id}", serviceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serviceCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceCategory() throws Exception {
        // Get the serviceCategory
        restServiceCategoryMockMvc.perform(get("/api/service-categories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);
        serviceCategorySearchRepository.save(serviceCategory);
        int databaseSizeBeforeUpdate = serviceCategoryRepository.findAll().size();

        // Update the serviceCategory
        ServiceCategory updatedServiceCategory = new ServiceCategory();
        updatedServiceCategory.setId(serviceCategory.getId());
        updatedServiceCategory.setCategoryName(UPDATED_CATEGORY_NAME);
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryMapper.serviceCategoryToServiceCategoryDTO(updatedServiceCategory);

        restServiceCategoryMockMvc.perform(put("/api/service-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceCategoryDTO)))
                .andExpect(status().isOk());

        // Validate the ServiceCategory in the database
        List<ServiceCategory> serviceCategories = serviceCategoryRepository.findAll();
        assertThat(serviceCategories).hasSize(databaseSizeBeforeUpdate);
        ServiceCategory testServiceCategory = serviceCategories.get(serviceCategories.size() - 1);
        assertThat(testServiceCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);

        // Validate the ServiceCategory in ElasticSearch
        ServiceCategory serviceCategoryEs = serviceCategorySearchRepository.findOne(testServiceCategory.getId());
        assertThat(serviceCategoryEs).isEqualToComparingFieldByField(testServiceCategory);
    }

    @Test
    @Transactional
    public void deleteServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);
        serviceCategorySearchRepository.save(serviceCategory);
        int databaseSizeBeforeDelete = serviceCategoryRepository.findAll().size();

        // Get the serviceCategory
        restServiceCategoryMockMvc.perform(delete("/api/service-categories/{id}", serviceCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean serviceCategoryExistsInEs = serviceCategorySearchRepository.exists(serviceCategory.getId());
        assertThat(serviceCategoryExistsInEs).isFalse();

        // Validate the database is empty
        List<ServiceCategory> serviceCategories = serviceCategoryRepository.findAll();
        assertThat(serviceCategories).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServiceCategory() throws Exception {
        // Initialize the database
        serviceCategoryRepository.saveAndFlush(serviceCategory);
        serviceCategorySearchRepository.save(serviceCategory);

        // Search the serviceCategory
        restServiceCategoryMockMvc.perform(get("/api/_search/service-categories?query=id:" + serviceCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME.toString())));
    }
}
