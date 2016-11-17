package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.CancelingInfo;
import com.eyeson.tikon.repository.CancelingInfoRepository;
import com.eyeson.tikon.service.CancelingInfoService;
import com.eyeson.tikon.repository.search.CancelingInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.CancelingInfoDTO;
import com.eyeson.tikon.web.rest.mapper.CancelingInfoMapper;

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
 * Test class for the CancelingInfoResource REST controller.
 *
 * @see CancelingInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class CancelingInfoResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Inject
    private CancelingInfoRepository cancelingInfoRepository;

    @Inject
    private CancelingInfoMapper cancelingInfoMapper;

    @Inject
    private CancelingInfoService cancelingInfoService;

    @Inject
    private CancelingInfoSearchRepository cancelingInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCancelingInfoMockMvc;

    private CancelingInfo cancelingInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CancelingInfoResource cancelingInfoResource = new CancelingInfoResource();
        ReflectionTestUtils.setField(cancelingInfoResource, "cancelingInfoService", cancelingInfoService);
        ReflectionTestUtils.setField(cancelingInfoResource, "cancelingInfoMapper", cancelingInfoMapper);
        this.restCancelingInfoMockMvc = MockMvcBuilders.standaloneSetup(cancelingInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cancelingInfoSearchRepository.deleteAll();
        cancelingInfo = new CancelingInfo();
        cancelingInfo.setDescription(DEFAULT_DESCRIPTION);
        cancelingInfo.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCancelingInfo() throws Exception {
        int databaseSizeBeforeCreate = cancelingInfoRepository.findAll().size();

        // Create the CancelingInfo
        CancelingInfoDTO cancelingInfoDTO = cancelingInfoMapper.cancelingInfoToCancelingInfoDTO(cancelingInfo);

        restCancelingInfoMockMvc.perform(post("/api/canceling-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cancelingInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the CancelingInfo in the database
        List<CancelingInfo> cancelingInfos = cancelingInfoRepository.findAll();
        assertThat(cancelingInfos).hasSize(databaseSizeBeforeCreate + 1);
        CancelingInfo testCancelingInfo = cancelingInfos.get(cancelingInfos.size() - 1);
        assertThat(testCancelingInfo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCancelingInfo.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the CancelingInfo in ElasticSearch
        CancelingInfo cancelingInfoEs = cancelingInfoSearchRepository.findOne(testCancelingInfo.getId());
        assertThat(cancelingInfoEs).isEqualToComparingFieldByField(testCancelingInfo);
    }

    @Test
    @Transactional
    public void getAllCancelingInfos() throws Exception {
        // Initialize the database
        cancelingInfoRepository.saveAndFlush(cancelingInfo);

        // Get all the cancelingInfos
        restCancelingInfoMockMvc.perform(get("/api/canceling-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cancelingInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getCancelingInfo() throws Exception {
        // Initialize the database
        cancelingInfoRepository.saveAndFlush(cancelingInfo);

        // Get the cancelingInfo
        restCancelingInfoMockMvc.perform(get("/api/canceling-infos/{id}", cancelingInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cancelingInfo.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingCancelingInfo() throws Exception {
        // Get the cancelingInfo
        restCancelingInfoMockMvc.perform(get("/api/canceling-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCancelingInfo() throws Exception {
        // Initialize the database
        cancelingInfoRepository.saveAndFlush(cancelingInfo);
        cancelingInfoSearchRepository.save(cancelingInfo);
        int databaseSizeBeforeUpdate = cancelingInfoRepository.findAll().size();

        // Update the cancelingInfo
        CancelingInfo updatedCancelingInfo = new CancelingInfo();
        updatedCancelingInfo.setId(cancelingInfo.getId());
        updatedCancelingInfo.setDescription(UPDATED_DESCRIPTION);
        updatedCancelingInfo.setStatus(UPDATED_STATUS);
        CancelingInfoDTO cancelingInfoDTO = cancelingInfoMapper.cancelingInfoToCancelingInfoDTO(updatedCancelingInfo);

        restCancelingInfoMockMvc.perform(put("/api/canceling-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cancelingInfoDTO)))
                .andExpect(status().isOk());

        // Validate the CancelingInfo in the database
        List<CancelingInfo> cancelingInfos = cancelingInfoRepository.findAll();
        assertThat(cancelingInfos).hasSize(databaseSizeBeforeUpdate);
        CancelingInfo testCancelingInfo = cancelingInfos.get(cancelingInfos.size() - 1);
        assertThat(testCancelingInfo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCancelingInfo.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the CancelingInfo in ElasticSearch
        CancelingInfo cancelingInfoEs = cancelingInfoSearchRepository.findOne(testCancelingInfo.getId());
        assertThat(cancelingInfoEs).isEqualToComparingFieldByField(testCancelingInfo);
    }

    @Test
    @Transactional
    public void deleteCancelingInfo() throws Exception {
        // Initialize the database
        cancelingInfoRepository.saveAndFlush(cancelingInfo);
        cancelingInfoSearchRepository.save(cancelingInfo);
        int databaseSizeBeforeDelete = cancelingInfoRepository.findAll().size();

        // Get the cancelingInfo
        restCancelingInfoMockMvc.perform(delete("/api/canceling-infos/{id}", cancelingInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean cancelingInfoExistsInEs = cancelingInfoSearchRepository.exists(cancelingInfo.getId());
        assertThat(cancelingInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<CancelingInfo> cancelingInfos = cancelingInfoRepository.findAll();
        assertThat(cancelingInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCancelingInfo() throws Exception {
        // Initialize the database
        cancelingInfoRepository.saveAndFlush(cancelingInfo);
        cancelingInfoSearchRepository.save(cancelingInfo);

        // Search the cancelingInfo
        restCancelingInfoMockMvc.perform(get("/api/_search/canceling-infos?query=id:" + cancelingInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cancelingInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
}
