package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.CompanySocialNetworkInfo;
import com.eyeson.tikon.repository.CompanySocialNetworkInfoRepository;
import com.eyeson.tikon.service.CompanySocialNetworkInfoService;
import com.eyeson.tikon.repository.search.CompanySocialNetworkInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.CompanySocialNetworkInfoDTO;
import com.eyeson.tikon.web.rest.mapper.CompanySocialNetworkInfoMapper;

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
 * Test class for the CompanySocialNetworkInfoResource REST controller.
 *
 * @see CompanySocialNetworkInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class CompanySocialNetworkInfoResourceIntTest {


    private static final Integer DEFAULT_ORDER_NUMBER = 1;
    private static final Integer UPDATED_ORDER_NUMBER = 2;

    @Inject
    private CompanySocialNetworkInfoRepository companySocialNetworkInfoRepository;

    @Inject
    private CompanySocialNetworkInfoMapper companySocialNetworkInfoMapper;

    @Inject
    private CompanySocialNetworkInfoService companySocialNetworkInfoService;

    @Inject
    private CompanySocialNetworkInfoSearchRepository companySocialNetworkInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompanySocialNetworkInfoMockMvc;

    private CompanySocialNetworkInfo companySocialNetworkInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanySocialNetworkInfoResource companySocialNetworkInfoResource = new CompanySocialNetworkInfoResource();
        ReflectionTestUtils.setField(companySocialNetworkInfoResource, "companySocialNetworkInfoService", companySocialNetworkInfoService);
        ReflectionTestUtils.setField(companySocialNetworkInfoResource, "companySocialNetworkInfoMapper", companySocialNetworkInfoMapper);
        this.restCompanySocialNetworkInfoMockMvc = MockMvcBuilders.standaloneSetup(companySocialNetworkInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        companySocialNetworkInfoSearchRepository.deleteAll();
        companySocialNetworkInfo = new CompanySocialNetworkInfo();
        companySocialNetworkInfo.setOrderNumber(DEFAULT_ORDER_NUMBER);
    }

    @Test
    @Transactional
    public void createCompanySocialNetworkInfo() throws Exception {
        int databaseSizeBeforeCreate = companySocialNetworkInfoRepository.findAll().size();

        // Create the CompanySocialNetworkInfo
        CompanySocialNetworkInfoDTO companySocialNetworkInfoDTO = companySocialNetworkInfoMapper.companySocialNetworkInfoToCompanySocialNetworkInfoDTO(companySocialNetworkInfo);

        restCompanySocialNetworkInfoMockMvc.perform(post("/api/company-social-network-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companySocialNetworkInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the CompanySocialNetworkInfo in the database
        List<CompanySocialNetworkInfo> companySocialNetworkInfos = companySocialNetworkInfoRepository.findAll();
        assertThat(companySocialNetworkInfos).hasSize(databaseSizeBeforeCreate + 1);
        CompanySocialNetworkInfo testCompanySocialNetworkInfo = companySocialNetworkInfos.get(companySocialNetworkInfos.size() - 1);
        assertThat(testCompanySocialNetworkInfo.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);

        // Validate the CompanySocialNetworkInfo in ElasticSearch
        CompanySocialNetworkInfo companySocialNetworkInfoEs = companySocialNetworkInfoSearchRepository.findOne(testCompanySocialNetworkInfo.getId());
        assertThat(companySocialNetworkInfoEs).isEqualToComparingFieldByField(testCompanySocialNetworkInfo);
    }

    @Test
    @Transactional
    public void getAllCompanySocialNetworkInfos() throws Exception {
        // Initialize the database
        companySocialNetworkInfoRepository.saveAndFlush(companySocialNetworkInfo);

        // Get all the companySocialNetworkInfos
        restCompanySocialNetworkInfoMockMvc.perform(get("/api/company-social-network-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(companySocialNetworkInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)));
    }

    @Test
    @Transactional
    public void getCompanySocialNetworkInfo() throws Exception {
        // Initialize the database
        companySocialNetworkInfoRepository.saveAndFlush(companySocialNetworkInfo);

        // Get the companySocialNetworkInfo
        restCompanySocialNetworkInfoMockMvc.perform(get("/api/company-social-network-infos/{id}", companySocialNetworkInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(companySocialNetworkInfo.getId().intValue()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingCompanySocialNetworkInfo() throws Exception {
        // Get the companySocialNetworkInfo
        restCompanySocialNetworkInfoMockMvc.perform(get("/api/company-social-network-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanySocialNetworkInfo() throws Exception {
        // Initialize the database
        companySocialNetworkInfoRepository.saveAndFlush(companySocialNetworkInfo);
        companySocialNetworkInfoSearchRepository.save(companySocialNetworkInfo);
        int databaseSizeBeforeUpdate = companySocialNetworkInfoRepository.findAll().size();

        // Update the companySocialNetworkInfo
        CompanySocialNetworkInfo updatedCompanySocialNetworkInfo = new CompanySocialNetworkInfo();
        updatedCompanySocialNetworkInfo.setId(companySocialNetworkInfo.getId());
        updatedCompanySocialNetworkInfo.setOrderNumber(UPDATED_ORDER_NUMBER);
        CompanySocialNetworkInfoDTO companySocialNetworkInfoDTO = companySocialNetworkInfoMapper.companySocialNetworkInfoToCompanySocialNetworkInfoDTO(updatedCompanySocialNetworkInfo);

        restCompanySocialNetworkInfoMockMvc.perform(put("/api/company-social-network-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companySocialNetworkInfoDTO)))
                .andExpect(status().isOk());

        // Validate the CompanySocialNetworkInfo in the database
        List<CompanySocialNetworkInfo> companySocialNetworkInfos = companySocialNetworkInfoRepository.findAll();
        assertThat(companySocialNetworkInfos).hasSize(databaseSizeBeforeUpdate);
        CompanySocialNetworkInfo testCompanySocialNetworkInfo = companySocialNetworkInfos.get(companySocialNetworkInfos.size() - 1);
        assertThat(testCompanySocialNetworkInfo.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);

        // Validate the CompanySocialNetworkInfo in ElasticSearch
        CompanySocialNetworkInfo companySocialNetworkInfoEs = companySocialNetworkInfoSearchRepository.findOne(testCompanySocialNetworkInfo.getId());
        assertThat(companySocialNetworkInfoEs).isEqualToComparingFieldByField(testCompanySocialNetworkInfo);
    }

    @Test
    @Transactional
    public void deleteCompanySocialNetworkInfo() throws Exception {
        // Initialize the database
        companySocialNetworkInfoRepository.saveAndFlush(companySocialNetworkInfo);
        companySocialNetworkInfoSearchRepository.save(companySocialNetworkInfo);
        int databaseSizeBeforeDelete = companySocialNetworkInfoRepository.findAll().size();

        // Get the companySocialNetworkInfo
        restCompanySocialNetworkInfoMockMvc.perform(delete("/api/company-social-network-infos/{id}", companySocialNetworkInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean companySocialNetworkInfoExistsInEs = companySocialNetworkInfoSearchRepository.exists(companySocialNetworkInfo.getId());
        assertThat(companySocialNetworkInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<CompanySocialNetworkInfo> companySocialNetworkInfos = companySocialNetworkInfoRepository.findAll();
        assertThat(companySocialNetworkInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompanySocialNetworkInfo() throws Exception {
        // Initialize the database
        companySocialNetworkInfoRepository.saveAndFlush(companySocialNetworkInfo);
        companySocialNetworkInfoSearchRepository.save(companySocialNetworkInfo);

        // Search the companySocialNetworkInfo
        restCompanySocialNetworkInfoMockMvc.perform(get("/api/_search/company-social-network-infos?query=id:" + companySocialNetworkInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companySocialNetworkInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)));
    }
}
