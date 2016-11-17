package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ServiceOptionInfo;
import com.eyeson.tikon.repository.ServiceOptionInfoRepository;
import com.eyeson.tikon.service.ServiceOptionInfoService;
import com.eyeson.tikon.repository.search.ServiceOptionInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServiceOptionInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceOptionInfoMapper;

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
 * Test class for the ServiceOptionInfoResource REST controller.
 *
 * @see ServiceOptionInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ServiceOptionInfoResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    @Inject
    private ServiceOptionInfoRepository serviceOptionInfoRepository;

    @Inject
    private ServiceOptionInfoMapper serviceOptionInfoMapper;

    @Inject
    private ServiceOptionInfoService serviceOptionInfoService;

    @Inject
    private ServiceOptionInfoSearchRepository serviceOptionInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restServiceOptionInfoMockMvc;

    private ServiceOptionInfo serviceOptionInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceOptionInfoResource serviceOptionInfoResource = new ServiceOptionInfoResource();
        ReflectionTestUtils.setField(serviceOptionInfoResource, "serviceOptionInfoService", serviceOptionInfoService);
        ReflectionTestUtils.setField(serviceOptionInfoResource, "serviceOptionInfoMapper", serviceOptionInfoMapper);
        this.restServiceOptionInfoMockMvc = MockMvcBuilders.standaloneSetup(serviceOptionInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceOptionInfoSearchRepository.deleteAll();
        serviceOptionInfo = new ServiceOptionInfo();
        serviceOptionInfo.setTitle(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createServiceOptionInfo() throws Exception {
        int databaseSizeBeforeCreate = serviceOptionInfoRepository.findAll().size();

        // Create the ServiceOptionInfo
        ServiceOptionInfoDTO serviceOptionInfoDTO = serviceOptionInfoMapper.serviceOptionInfoToServiceOptionInfoDTO(serviceOptionInfo);

        restServiceOptionInfoMockMvc.perform(post("/api/service-option-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceOptionInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the ServiceOptionInfo in the database
        List<ServiceOptionInfo> serviceOptionInfos = serviceOptionInfoRepository.findAll();
        assertThat(serviceOptionInfos).hasSize(databaseSizeBeforeCreate + 1);
        ServiceOptionInfo testServiceOptionInfo = serviceOptionInfos.get(serviceOptionInfos.size() - 1);
        assertThat(testServiceOptionInfo.getTitle()).isEqualTo(DEFAULT_TITLE);

        // Validate the ServiceOptionInfo in ElasticSearch
        ServiceOptionInfo serviceOptionInfoEs = serviceOptionInfoSearchRepository.findOne(testServiceOptionInfo.getId());
        assertThat(serviceOptionInfoEs).isEqualToComparingFieldByField(testServiceOptionInfo);
    }

    @Test
    @Transactional
    public void getAllServiceOptionInfos() throws Exception {
        // Initialize the database
        serviceOptionInfoRepository.saveAndFlush(serviceOptionInfo);

        // Get all the serviceOptionInfos
        restServiceOptionInfoMockMvc.perform(get("/api/service-option-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOptionInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    @Transactional
    public void getServiceOptionInfo() throws Exception {
        // Initialize the database
        serviceOptionInfoRepository.saveAndFlush(serviceOptionInfo);

        // Get the serviceOptionInfo
        restServiceOptionInfoMockMvc.perform(get("/api/service-option-infos/{id}", serviceOptionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serviceOptionInfo.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceOptionInfo() throws Exception {
        // Get the serviceOptionInfo
        restServiceOptionInfoMockMvc.perform(get("/api/service-option-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceOptionInfo() throws Exception {
        // Initialize the database
        serviceOptionInfoRepository.saveAndFlush(serviceOptionInfo);
        serviceOptionInfoSearchRepository.save(serviceOptionInfo);
        int databaseSizeBeforeUpdate = serviceOptionInfoRepository.findAll().size();

        // Update the serviceOptionInfo
        ServiceOptionInfo updatedServiceOptionInfo = new ServiceOptionInfo();
        updatedServiceOptionInfo.setId(serviceOptionInfo.getId());
        updatedServiceOptionInfo.setTitle(UPDATED_TITLE);
        ServiceOptionInfoDTO serviceOptionInfoDTO = serviceOptionInfoMapper.serviceOptionInfoToServiceOptionInfoDTO(updatedServiceOptionInfo);

        restServiceOptionInfoMockMvc.perform(put("/api/service-option-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceOptionInfoDTO)))
                .andExpect(status().isOk());

        // Validate the ServiceOptionInfo in the database
        List<ServiceOptionInfo> serviceOptionInfos = serviceOptionInfoRepository.findAll();
        assertThat(serviceOptionInfos).hasSize(databaseSizeBeforeUpdate);
        ServiceOptionInfo testServiceOptionInfo = serviceOptionInfos.get(serviceOptionInfos.size() - 1);
        assertThat(testServiceOptionInfo.getTitle()).isEqualTo(UPDATED_TITLE);

        // Validate the ServiceOptionInfo in ElasticSearch
        ServiceOptionInfo serviceOptionInfoEs = serviceOptionInfoSearchRepository.findOne(testServiceOptionInfo.getId());
        assertThat(serviceOptionInfoEs).isEqualToComparingFieldByField(testServiceOptionInfo);
    }

    @Test
    @Transactional
    public void deleteServiceOptionInfo() throws Exception {
        // Initialize the database
        serviceOptionInfoRepository.saveAndFlush(serviceOptionInfo);
        serviceOptionInfoSearchRepository.save(serviceOptionInfo);
        int databaseSizeBeforeDelete = serviceOptionInfoRepository.findAll().size();

        // Get the serviceOptionInfo
        restServiceOptionInfoMockMvc.perform(delete("/api/service-option-infos/{id}", serviceOptionInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean serviceOptionInfoExistsInEs = serviceOptionInfoSearchRepository.exists(serviceOptionInfo.getId());
        assertThat(serviceOptionInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<ServiceOptionInfo> serviceOptionInfos = serviceOptionInfoRepository.findAll();
        assertThat(serviceOptionInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServiceOptionInfo() throws Exception {
        // Initialize the database
        serviceOptionInfoRepository.saveAndFlush(serviceOptionInfo);
        serviceOptionInfoSearchRepository.save(serviceOptionInfo);

        // Search the serviceOptionInfo
        restServiceOptionInfoMockMvc.perform(get("/api/_search/service-option-infos?query=id:" + serviceOptionInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceOptionInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
}
