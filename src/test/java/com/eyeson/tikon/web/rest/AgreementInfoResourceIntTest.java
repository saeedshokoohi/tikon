package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.AgreementInfo;
import com.eyeson.tikon.repository.AgreementInfoRepository;
import com.eyeson.tikon.service.AgreementInfoService;
import com.eyeson.tikon.repository.search.AgreementInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.AgreementInfoDTO;
import com.eyeson.tikon.web.rest.mapper.AgreementInfoMapper;

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
 * Test class for the AgreementInfoResource REST controller.
 *
 * @see AgreementInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class AgreementInfoResourceIntTest {

    private static final String DEFAULT_RULES = "AAAAA";
    private static final String UPDATED_RULES = "BBBBB";
    private static final String DEFAULT_AGREEMENT = "AAAAA";
    private static final String UPDATED_AGREEMENT = "BBBBB";

    @Inject
    private AgreementInfoRepository agreementInfoRepository;

    @Inject
    private AgreementInfoMapper agreementInfoMapper;

    @Inject
    private AgreementInfoService agreementInfoService;

    @Inject
    private AgreementInfoSearchRepository agreementInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAgreementInfoMockMvc;

    private AgreementInfo agreementInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgreementInfoResource agreementInfoResource = new AgreementInfoResource();
        ReflectionTestUtils.setField(agreementInfoResource, "agreementInfoService", agreementInfoService);
        ReflectionTestUtils.setField(agreementInfoResource, "agreementInfoMapper", agreementInfoMapper);
        this.restAgreementInfoMockMvc = MockMvcBuilders.standaloneSetup(agreementInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        agreementInfoSearchRepository.deleteAll();
        agreementInfo = new AgreementInfo();
        agreementInfo.setRules(DEFAULT_RULES);
        agreementInfo.setAgreement(DEFAULT_AGREEMENT);
    }

    @Test
    @Transactional
    public void createAgreementInfo() throws Exception {
        int databaseSizeBeforeCreate = agreementInfoRepository.findAll().size();

        // Create the AgreementInfo
        AgreementInfoDTO agreementInfoDTO = agreementInfoMapper.agreementInfoToAgreementInfoDTO(agreementInfo);

        restAgreementInfoMockMvc.perform(post("/api/agreement-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agreementInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the AgreementInfo in the database
        List<AgreementInfo> agreementInfos = agreementInfoRepository.findAll();
        assertThat(agreementInfos).hasSize(databaseSizeBeforeCreate + 1);
        AgreementInfo testAgreementInfo = agreementInfos.get(agreementInfos.size() - 1);
        assertThat(testAgreementInfo.getRules()).isEqualTo(DEFAULT_RULES);
        assertThat(testAgreementInfo.getAgreement()).isEqualTo(DEFAULT_AGREEMENT);

        // Validate the AgreementInfo in ElasticSearch
        AgreementInfo agreementInfoEs = agreementInfoSearchRepository.findOne(testAgreementInfo.getId());
        assertThat(agreementInfoEs).isEqualToComparingFieldByField(testAgreementInfo);
    }

    @Test
    @Transactional
    public void getAllAgreementInfos() throws Exception {
        // Initialize the database
        agreementInfoRepository.saveAndFlush(agreementInfo);

        // Get all the agreementInfos
        restAgreementInfoMockMvc.perform(get("/api/agreement-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(agreementInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].rules").value(hasItem(DEFAULT_RULES.toString())))
                .andExpect(jsonPath("$.[*].agreement").value(hasItem(DEFAULT_AGREEMENT.toString())));
    }

    @Test
    @Transactional
    public void getAgreementInfo() throws Exception {
        // Initialize the database
        agreementInfoRepository.saveAndFlush(agreementInfo);

        // Get the agreementInfo
        restAgreementInfoMockMvc.perform(get("/api/agreement-infos/{id}", agreementInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(agreementInfo.getId().intValue()))
            .andExpect(jsonPath("$.rules").value(DEFAULT_RULES.toString()))
            .andExpect(jsonPath("$.agreement").value(DEFAULT_AGREEMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAgreementInfo() throws Exception {
        // Get the agreementInfo
        restAgreementInfoMockMvc.perform(get("/api/agreement-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgreementInfo() throws Exception {
        // Initialize the database
        agreementInfoRepository.saveAndFlush(agreementInfo);
        agreementInfoSearchRepository.save(agreementInfo);
        int databaseSizeBeforeUpdate = agreementInfoRepository.findAll().size();

        // Update the agreementInfo
        AgreementInfo updatedAgreementInfo = new AgreementInfo();
        updatedAgreementInfo.setId(agreementInfo.getId());
        updatedAgreementInfo.setRules(UPDATED_RULES);
        updatedAgreementInfo.setAgreement(UPDATED_AGREEMENT);
        AgreementInfoDTO agreementInfoDTO = agreementInfoMapper.agreementInfoToAgreementInfoDTO(updatedAgreementInfo);

        restAgreementInfoMockMvc.perform(put("/api/agreement-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agreementInfoDTO)))
                .andExpect(status().isOk());

        // Validate the AgreementInfo in the database
        List<AgreementInfo> agreementInfos = agreementInfoRepository.findAll();
        assertThat(agreementInfos).hasSize(databaseSizeBeforeUpdate);
        AgreementInfo testAgreementInfo = agreementInfos.get(agreementInfos.size() - 1);
        assertThat(testAgreementInfo.getRules()).isEqualTo(UPDATED_RULES);
        assertThat(testAgreementInfo.getAgreement()).isEqualTo(UPDATED_AGREEMENT);

        // Validate the AgreementInfo in ElasticSearch
        AgreementInfo agreementInfoEs = agreementInfoSearchRepository.findOne(testAgreementInfo.getId());
        assertThat(agreementInfoEs).isEqualToComparingFieldByField(testAgreementInfo);
    }

    @Test
    @Transactional
    public void deleteAgreementInfo() throws Exception {
        // Initialize the database
        agreementInfoRepository.saveAndFlush(agreementInfo);
        agreementInfoSearchRepository.save(agreementInfo);
        int databaseSizeBeforeDelete = agreementInfoRepository.findAll().size();

        // Get the agreementInfo
        restAgreementInfoMockMvc.perform(delete("/api/agreement-infos/{id}", agreementInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean agreementInfoExistsInEs = agreementInfoSearchRepository.exists(agreementInfo.getId());
        assertThat(agreementInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<AgreementInfo> agreementInfos = agreementInfoRepository.findAll();
        assertThat(agreementInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAgreementInfo() throws Exception {
        // Initialize the database
        agreementInfoRepository.saveAndFlush(agreementInfo);
        agreementInfoSearchRepository.save(agreementInfo);

        // Search the agreementInfo
        restAgreementInfoMockMvc.perform(get("/api/_search/agreement-infos?query=id:" + agreementInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agreementInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].rules").value(hasItem(DEFAULT_RULES.toString())))
            .andExpect(jsonPath("$.[*].agreement").value(hasItem(DEFAULT_AGREEMENT.toString())));
    }
}
