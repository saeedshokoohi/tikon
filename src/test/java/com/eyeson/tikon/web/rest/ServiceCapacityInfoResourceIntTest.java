package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ServiceCapacityInfo;
import com.eyeson.tikon.repository.ServiceCapacityInfoRepository;
import com.eyeson.tikon.service.ServiceCapacityInfoService;
import com.eyeson.tikon.repository.search.ServiceCapacityInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServiceCapacityInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceCapacityInfoMapper;

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
 * Test class for the ServiceCapacityInfoResource REST controller.
 *
 * @see ServiceCapacityInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ServiceCapacityInfoResourceIntTest {


    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;

    @Inject
    private ServiceCapacityInfoRepository serviceCapacityInfoRepository;

    @Inject
    private ServiceCapacityInfoMapper serviceCapacityInfoMapper;

    @Inject
    private ServiceCapacityInfoService serviceCapacityInfoService;

    @Inject
    private ServiceCapacityInfoSearchRepository serviceCapacityInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restServiceCapacityInfoMockMvc;

    private ServiceCapacityInfo serviceCapacityInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceCapacityInfoResource serviceCapacityInfoResource = new ServiceCapacityInfoResource();
        ReflectionTestUtils.setField(serviceCapacityInfoResource, "serviceCapacityInfoService", serviceCapacityInfoService);
        ReflectionTestUtils.setField(serviceCapacityInfoResource, "serviceCapacityInfoMapper", serviceCapacityInfoMapper);
        this.restServiceCapacityInfoMockMvc = MockMvcBuilders.standaloneSetup(serviceCapacityInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceCapacityInfoSearchRepository.deleteAll();
        serviceCapacityInfo = new ServiceCapacityInfo();
        serviceCapacityInfo.setQty(DEFAULT_QTY);
    }

    @Test
    @Transactional
    public void createServiceCapacityInfo() throws Exception {
        int databaseSizeBeforeCreate = serviceCapacityInfoRepository.findAll().size();

        // Create the ServiceCapacityInfo
        ServiceCapacityInfoDTO serviceCapacityInfoDTO = serviceCapacityInfoMapper.serviceCapacityInfoToServiceCapacityInfoDTO(serviceCapacityInfo);

        restServiceCapacityInfoMockMvc.perform(post("/api/service-capacity-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceCapacityInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the ServiceCapacityInfo in the database
        List<ServiceCapacityInfo> serviceCapacityInfos = serviceCapacityInfoRepository.findAll();
        assertThat(serviceCapacityInfos).hasSize(databaseSizeBeforeCreate + 1);
        ServiceCapacityInfo testServiceCapacityInfo = serviceCapacityInfos.get(serviceCapacityInfos.size() - 1);
        assertThat(testServiceCapacityInfo.getQty()).isEqualTo(DEFAULT_QTY);

        // Validate the ServiceCapacityInfo in ElasticSearch
        ServiceCapacityInfo serviceCapacityInfoEs = serviceCapacityInfoSearchRepository.findOne(testServiceCapacityInfo.getId());
        assertThat(serviceCapacityInfoEs).isEqualToComparingFieldByField(testServiceCapacityInfo);
    }

    @Test
    @Transactional
    public void getAllServiceCapacityInfos() throws Exception {
        // Initialize the database
        serviceCapacityInfoRepository.saveAndFlush(serviceCapacityInfo);

        // Get all the serviceCapacityInfos
        restServiceCapacityInfoMockMvc.perform(get("/api/service-capacity-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceCapacityInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)));
    }

    @Test
    @Transactional
    public void getServiceCapacityInfo() throws Exception {
        // Initialize the database
        serviceCapacityInfoRepository.saveAndFlush(serviceCapacityInfo);

        // Get the serviceCapacityInfo
        restServiceCapacityInfoMockMvc.perform(get("/api/service-capacity-infos/{id}", serviceCapacityInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serviceCapacityInfo.getId().intValue()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY));
    }

    @Test
    @Transactional
    public void getNonExistingServiceCapacityInfo() throws Exception {
        // Get the serviceCapacityInfo
        restServiceCapacityInfoMockMvc.perform(get("/api/service-capacity-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceCapacityInfo() throws Exception {
        // Initialize the database
        serviceCapacityInfoRepository.saveAndFlush(serviceCapacityInfo);
        serviceCapacityInfoSearchRepository.save(serviceCapacityInfo);
        int databaseSizeBeforeUpdate = serviceCapacityInfoRepository.findAll().size();

        // Update the serviceCapacityInfo
        ServiceCapacityInfo updatedServiceCapacityInfo = new ServiceCapacityInfo();
        updatedServiceCapacityInfo.setId(serviceCapacityInfo.getId());
        updatedServiceCapacityInfo.setQty(UPDATED_QTY);
        ServiceCapacityInfoDTO serviceCapacityInfoDTO = serviceCapacityInfoMapper.serviceCapacityInfoToServiceCapacityInfoDTO(updatedServiceCapacityInfo);

        restServiceCapacityInfoMockMvc.perform(put("/api/service-capacity-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceCapacityInfoDTO)))
                .andExpect(status().isOk());

        // Validate the ServiceCapacityInfo in the database
        List<ServiceCapacityInfo> serviceCapacityInfos = serviceCapacityInfoRepository.findAll();
        assertThat(serviceCapacityInfos).hasSize(databaseSizeBeforeUpdate);
        ServiceCapacityInfo testServiceCapacityInfo = serviceCapacityInfos.get(serviceCapacityInfos.size() - 1);
        assertThat(testServiceCapacityInfo.getQty()).isEqualTo(UPDATED_QTY);

        // Validate the ServiceCapacityInfo in ElasticSearch
        ServiceCapacityInfo serviceCapacityInfoEs = serviceCapacityInfoSearchRepository.findOne(testServiceCapacityInfo.getId());
        assertThat(serviceCapacityInfoEs).isEqualToComparingFieldByField(testServiceCapacityInfo);
    }

    @Test
    @Transactional
    public void deleteServiceCapacityInfo() throws Exception {
        // Initialize the database
        serviceCapacityInfoRepository.saveAndFlush(serviceCapacityInfo);
        serviceCapacityInfoSearchRepository.save(serviceCapacityInfo);
        int databaseSizeBeforeDelete = serviceCapacityInfoRepository.findAll().size();

        // Get the serviceCapacityInfo
        restServiceCapacityInfoMockMvc.perform(delete("/api/service-capacity-infos/{id}", serviceCapacityInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean serviceCapacityInfoExistsInEs = serviceCapacityInfoSearchRepository.exists(serviceCapacityInfo.getId());
        assertThat(serviceCapacityInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<ServiceCapacityInfo> serviceCapacityInfos = serviceCapacityInfoRepository.findAll();
        assertThat(serviceCapacityInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServiceCapacityInfo() throws Exception {
        // Initialize the database
        serviceCapacityInfoRepository.saveAndFlush(serviceCapacityInfo);
        serviceCapacityInfoSearchRepository.save(serviceCapacityInfo);

        // Search the serviceCapacityInfo
        restServiceCapacityInfoMockMvc.perform(get("/api/_search/service-capacity-infos?query=id:" + serviceCapacityInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceCapacityInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)));
    }
}
