package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ServiceOptionItem;
import com.eyeson.tikon.repository.ServiceOptionItemRepository;
import com.eyeson.tikon.service.ServiceOptionItemService;
import com.eyeson.tikon.repository.search.ServiceOptionItemSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServiceOptionItemDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceOptionItemMapper;

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
 * Test class for the ServiceOptionItemResource REST controller.
 *
 * @see ServiceOptionItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ServiceOptionItemResourceIntTest {

    private static final String DEFAULT_OPTION_NAME = "AAAAA";
    private static final String UPDATED_OPTION_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ServiceOptionItemRepository serviceOptionItemRepository;

    @Inject
    private ServiceOptionItemMapper serviceOptionItemMapper;

    @Inject
    private ServiceOptionItemService serviceOptionItemService;

    @Inject
    private ServiceOptionItemSearchRepository serviceOptionItemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restServiceOptionItemMockMvc;

    private ServiceOptionItem serviceOptionItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceOptionItemResource serviceOptionItemResource = new ServiceOptionItemResource();
        ReflectionTestUtils.setField(serviceOptionItemResource, "serviceOptionItemService", serviceOptionItemService);
        ReflectionTestUtils.setField(serviceOptionItemResource, "serviceOptionItemMapper", serviceOptionItemMapper);
        this.restServiceOptionItemMockMvc = MockMvcBuilders.standaloneSetup(serviceOptionItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceOptionItemSearchRepository.deleteAll();
        serviceOptionItem = new ServiceOptionItem();
        serviceOptionItem.setOptionName(DEFAULT_OPTION_NAME);
        serviceOptionItem.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createServiceOptionItem() throws Exception {
        int databaseSizeBeforeCreate = serviceOptionItemRepository.findAll().size();

        // Create the ServiceOptionItem
        ServiceOptionItemDTO serviceOptionItemDTO = serviceOptionItemMapper.serviceOptionItemToServiceOptionItemDTO(serviceOptionItem);

        restServiceOptionItemMockMvc.perform(post("/api/service-option-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceOptionItemDTO)))
                .andExpect(status().isCreated());

        // Validate the ServiceOptionItem in the database
        List<ServiceOptionItem> serviceOptionItems = serviceOptionItemRepository.findAll();
        assertThat(serviceOptionItems).hasSize(databaseSizeBeforeCreate + 1);
        ServiceOptionItem testServiceOptionItem = serviceOptionItems.get(serviceOptionItems.size() - 1);
        assertThat(testServiceOptionItem.getOptionName()).isEqualTo(DEFAULT_OPTION_NAME);
        assertThat(testServiceOptionItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ServiceOptionItem in ElasticSearch
        ServiceOptionItem serviceOptionItemEs = serviceOptionItemSearchRepository.findOne(testServiceOptionItem.getId());
        assertThat(serviceOptionItemEs).isEqualToComparingFieldByField(testServiceOptionItem);
    }

    @Test
    @Transactional
    public void getAllServiceOptionItems() throws Exception {
        // Initialize the database
        serviceOptionItemRepository.saveAndFlush(serviceOptionItem);

        // Get all the serviceOptionItems
        restServiceOptionItemMockMvc.perform(get("/api/service-option-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOptionItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].optionName").value(hasItem(DEFAULT_OPTION_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getServiceOptionItem() throws Exception {
        // Initialize the database
        serviceOptionItemRepository.saveAndFlush(serviceOptionItem);

        // Get the serviceOptionItem
        restServiceOptionItemMockMvc.perform(get("/api/service-option-items/{id}", serviceOptionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serviceOptionItem.getId().intValue()))
            .andExpect(jsonPath("$.optionName").value(DEFAULT_OPTION_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceOptionItem() throws Exception {
        // Get the serviceOptionItem
        restServiceOptionItemMockMvc.perform(get("/api/service-option-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceOptionItem() throws Exception {
        // Initialize the database
        serviceOptionItemRepository.saveAndFlush(serviceOptionItem);
        serviceOptionItemSearchRepository.save(serviceOptionItem);
        int databaseSizeBeforeUpdate = serviceOptionItemRepository.findAll().size();

        // Update the serviceOptionItem
        ServiceOptionItem updatedServiceOptionItem = new ServiceOptionItem();
        updatedServiceOptionItem.setId(serviceOptionItem.getId());
        updatedServiceOptionItem.setOptionName(UPDATED_OPTION_NAME);
        updatedServiceOptionItem.setDescription(UPDATED_DESCRIPTION);
        ServiceOptionItemDTO serviceOptionItemDTO = serviceOptionItemMapper.serviceOptionItemToServiceOptionItemDTO(updatedServiceOptionItem);

        restServiceOptionItemMockMvc.perform(put("/api/service-option-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceOptionItemDTO)))
                .andExpect(status().isOk());

        // Validate the ServiceOptionItem in the database
        List<ServiceOptionItem> serviceOptionItems = serviceOptionItemRepository.findAll();
        assertThat(serviceOptionItems).hasSize(databaseSizeBeforeUpdate);
        ServiceOptionItem testServiceOptionItem = serviceOptionItems.get(serviceOptionItems.size() - 1);
        assertThat(testServiceOptionItem.getOptionName()).isEqualTo(UPDATED_OPTION_NAME);
        assertThat(testServiceOptionItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ServiceOptionItem in ElasticSearch
        ServiceOptionItem serviceOptionItemEs = serviceOptionItemSearchRepository.findOne(testServiceOptionItem.getId());
        assertThat(serviceOptionItemEs).isEqualToComparingFieldByField(testServiceOptionItem);
    }

    @Test
    @Transactional
    public void deleteServiceOptionItem() throws Exception {
        // Initialize the database
        serviceOptionItemRepository.saveAndFlush(serviceOptionItem);
        serviceOptionItemSearchRepository.save(serviceOptionItem);
        int databaseSizeBeforeDelete = serviceOptionItemRepository.findAll().size();

        // Get the serviceOptionItem
        restServiceOptionItemMockMvc.perform(delete("/api/service-option-items/{id}", serviceOptionItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean serviceOptionItemExistsInEs = serviceOptionItemSearchRepository.exists(serviceOptionItem.getId());
        assertThat(serviceOptionItemExistsInEs).isFalse();

        // Validate the database is empty
        List<ServiceOptionItem> serviceOptionItems = serviceOptionItemRepository.findAll();
        assertThat(serviceOptionItems).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServiceOptionItem() throws Exception {
        // Initialize the database
        serviceOptionItemRepository.saveAndFlush(serviceOptionItem);
        serviceOptionItemSearchRepository.save(serviceOptionItem);

        // Search the serviceOptionItem
        restServiceOptionItemMockMvc.perform(get("/api/_search/service-option-items?query=id:" + serviceOptionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOptionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].optionName").value(hasItem(DEFAULT_OPTION_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
