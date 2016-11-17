package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.SettingInfo;
import com.eyeson.tikon.repository.SettingInfoRepository;
import com.eyeson.tikon.service.SettingInfoService;
import com.eyeson.tikon.repository.search.SettingInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.SettingInfoDTO;
import com.eyeson.tikon.web.rest.mapper.SettingInfoMapper;

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
 * Test class for the SettingInfoResource REST controller.
 *
 * @see SettingInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class SettingInfoResourceIntTest {

    private static final String DEFAULT_SETTING_NAME = "AAAAA";
    private static final String UPDATED_SETTING_NAME = "BBBBB";

    @Inject
    private SettingInfoRepository settingInfoRepository;

    @Inject
    private SettingInfoMapper settingInfoMapper;

    @Inject
    private SettingInfoService settingInfoService;

    @Inject
    private SettingInfoSearchRepository settingInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSettingInfoMockMvc;

    private SettingInfo settingInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SettingInfoResource settingInfoResource = new SettingInfoResource();
        ReflectionTestUtils.setField(settingInfoResource, "settingInfoService", settingInfoService);
        ReflectionTestUtils.setField(settingInfoResource, "settingInfoMapper", settingInfoMapper);
        this.restSettingInfoMockMvc = MockMvcBuilders.standaloneSetup(settingInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        settingInfoSearchRepository.deleteAll();
        settingInfo = new SettingInfo();
        settingInfo.setSettingName(DEFAULT_SETTING_NAME);
    }

    @Test
    @Transactional
    public void createSettingInfo() throws Exception {
        int databaseSizeBeforeCreate = settingInfoRepository.findAll().size();

        // Create the SettingInfo
        SettingInfoDTO settingInfoDTO = settingInfoMapper.settingInfoToSettingInfoDTO(settingInfo);

        restSettingInfoMockMvc.perform(post("/api/setting-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(settingInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the SettingInfo in the database
        List<SettingInfo> settingInfos = settingInfoRepository.findAll();
        assertThat(settingInfos).hasSize(databaseSizeBeforeCreate + 1);
        SettingInfo testSettingInfo = settingInfos.get(settingInfos.size() - 1);
        assertThat(testSettingInfo.getSettingName()).isEqualTo(DEFAULT_SETTING_NAME);

        // Validate the SettingInfo in ElasticSearch
        SettingInfo settingInfoEs = settingInfoSearchRepository.findOne(testSettingInfo.getId());
        assertThat(settingInfoEs).isEqualToComparingFieldByField(testSettingInfo);
    }

    @Test
    @Transactional
    public void getAllSettingInfos() throws Exception {
        // Initialize the database
        settingInfoRepository.saveAndFlush(settingInfo);

        // Get all the settingInfos
        restSettingInfoMockMvc.perform(get("/api/setting-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(settingInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].settingName").value(hasItem(DEFAULT_SETTING_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSettingInfo() throws Exception {
        // Initialize the database
        settingInfoRepository.saveAndFlush(settingInfo);

        // Get the settingInfo
        restSettingInfoMockMvc.perform(get("/api/setting-infos/{id}", settingInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(settingInfo.getId().intValue()))
            .andExpect(jsonPath("$.settingName").value(DEFAULT_SETTING_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSettingInfo() throws Exception {
        // Get the settingInfo
        restSettingInfoMockMvc.perform(get("/api/setting-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSettingInfo() throws Exception {
        // Initialize the database
        settingInfoRepository.saveAndFlush(settingInfo);
        settingInfoSearchRepository.save(settingInfo);
        int databaseSizeBeforeUpdate = settingInfoRepository.findAll().size();

        // Update the settingInfo
        SettingInfo updatedSettingInfo = new SettingInfo();
        updatedSettingInfo.setId(settingInfo.getId());
        updatedSettingInfo.setSettingName(UPDATED_SETTING_NAME);
        SettingInfoDTO settingInfoDTO = settingInfoMapper.settingInfoToSettingInfoDTO(updatedSettingInfo);

        restSettingInfoMockMvc.perform(put("/api/setting-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(settingInfoDTO)))
                .andExpect(status().isOk());

        // Validate the SettingInfo in the database
        List<SettingInfo> settingInfos = settingInfoRepository.findAll();
        assertThat(settingInfos).hasSize(databaseSizeBeforeUpdate);
        SettingInfo testSettingInfo = settingInfos.get(settingInfos.size() - 1);
        assertThat(testSettingInfo.getSettingName()).isEqualTo(UPDATED_SETTING_NAME);

        // Validate the SettingInfo in ElasticSearch
        SettingInfo settingInfoEs = settingInfoSearchRepository.findOne(testSettingInfo.getId());
        assertThat(settingInfoEs).isEqualToComparingFieldByField(testSettingInfo);
    }

    @Test
    @Transactional
    public void deleteSettingInfo() throws Exception {
        // Initialize the database
        settingInfoRepository.saveAndFlush(settingInfo);
        settingInfoSearchRepository.save(settingInfo);
        int databaseSizeBeforeDelete = settingInfoRepository.findAll().size();

        // Get the settingInfo
        restSettingInfoMockMvc.perform(delete("/api/setting-infos/{id}", settingInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean settingInfoExistsInEs = settingInfoSearchRepository.exists(settingInfo.getId());
        assertThat(settingInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<SettingInfo> settingInfos = settingInfoRepository.findAll();
        assertThat(settingInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSettingInfo() throws Exception {
        // Initialize the database
        settingInfoRepository.saveAndFlush(settingInfo);
        settingInfoSearchRepository.save(settingInfo);

        // Search the settingInfo
        restSettingInfoMockMvc.perform(get("/api/_search/setting-infos?query=id:" + settingInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settingInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].settingName").value(hasItem(DEFAULT_SETTING_NAME.toString())));
    }
}
