package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ThemeSettingInfo;
import com.eyeson.tikon.repository.ThemeSettingInfoRepository;
import com.eyeson.tikon.service.ThemeSettingInfoService;
import com.eyeson.tikon.repository.search.ThemeSettingInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.ThemeSettingInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ThemeSettingInfoMapper;

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
 * Test class for the ThemeSettingInfoResource REST controller.
 *
 * @see ThemeSettingInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ThemeSettingInfoResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private ThemeSettingInfoRepository themeSettingInfoRepository;

    @Inject
    private ThemeSettingInfoMapper themeSettingInfoMapper;

    @Inject
    private ThemeSettingInfoService themeSettingInfoService;

    @Inject
    private ThemeSettingInfoSearchRepository themeSettingInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restThemeSettingInfoMockMvc;

    private ThemeSettingInfo themeSettingInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ThemeSettingInfoResource themeSettingInfoResource = new ThemeSettingInfoResource();
        ReflectionTestUtils.setField(themeSettingInfoResource, "themeSettingInfoService", themeSettingInfoService);
        ReflectionTestUtils.setField(themeSettingInfoResource, "themeSettingInfoMapper", themeSettingInfoMapper);
        this.restThemeSettingInfoMockMvc = MockMvcBuilders.standaloneSetup(themeSettingInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        themeSettingInfoSearchRepository.deleteAll();
        themeSettingInfo = new ThemeSettingInfo();
        themeSettingInfo.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createThemeSettingInfo() throws Exception {
        int databaseSizeBeforeCreate = themeSettingInfoRepository.findAll().size();

        // Create the ThemeSettingInfo
        ThemeSettingInfoDTO themeSettingInfoDTO = themeSettingInfoMapper.themeSettingInfoToThemeSettingInfoDTO(themeSettingInfo);

        restThemeSettingInfoMockMvc.perform(post("/api/theme-setting-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(themeSettingInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the ThemeSettingInfo in the database
        List<ThemeSettingInfo> themeSettingInfos = themeSettingInfoRepository.findAll();
        assertThat(themeSettingInfos).hasSize(databaseSizeBeforeCreate + 1);
        ThemeSettingInfo testThemeSettingInfo = themeSettingInfos.get(themeSettingInfos.size() - 1);
        assertThat(testThemeSettingInfo.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the ThemeSettingInfo in ElasticSearch
        ThemeSettingInfo themeSettingInfoEs = themeSettingInfoSearchRepository.findOne(testThemeSettingInfo.getId());
        assertThat(themeSettingInfoEs).isEqualToComparingFieldByField(testThemeSettingInfo);
    }

    @Test
    @Transactional
    public void getAllThemeSettingInfos() throws Exception {
        // Initialize the database
        themeSettingInfoRepository.saveAndFlush(themeSettingInfo);

        // Get all the themeSettingInfos
        restThemeSettingInfoMockMvc.perform(get("/api/theme-setting-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(themeSettingInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getThemeSettingInfo() throws Exception {
        // Initialize the database
        themeSettingInfoRepository.saveAndFlush(themeSettingInfo);

        // Get the themeSettingInfo
        restThemeSettingInfoMockMvc.perform(get("/api/theme-setting-infos/{id}", themeSettingInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(themeSettingInfo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingThemeSettingInfo() throws Exception {
        // Get the themeSettingInfo
        restThemeSettingInfoMockMvc.perform(get("/api/theme-setting-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateThemeSettingInfo() throws Exception {
        // Initialize the database
        themeSettingInfoRepository.saveAndFlush(themeSettingInfo);
        themeSettingInfoSearchRepository.save(themeSettingInfo);
        int databaseSizeBeforeUpdate = themeSettingInfoRepository.findAll().size();

        // Update the themeSettingInfo
        ThemeSettingInfo updatedThemeSettingInfo = new ThemeSettingInfo();
        updatedThemeSettingInfo.setId(themeSettingInfo.getId());
        updatedThemeSettingInfo.setName(UPDATED_NAME);
        ThemeSettingInfoDTO themeSettingInfoDTO = themeSettingInfoMapper.themeSettingInfoToThemeSettingInfoDTO(updatedThemeSettingInfo);

        restThemeSettingInfoMockMvc.perform(put("/api/theme-setting-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(themeSettingInfoDTO)))
                .andExpect(status().isOk());

        // Validate the ThemeSettingInfo in the database
        List<ThemeSettingInfo> themeSettingInfos = themeSettingInfoRepository.findAll();
        assertThat(themeSettingInfos).hasSize(databaseSizeBeforeUpdate);
        ThemeSettingInfo testThemeSettingInfo = themeSettingInfos.get(themeSettingInfos.size() - 1);
        assertThat(testThemeSettingInfo.getName()).isEqualTo(UPDATED_NAME);

        // Validate the ThemeSettingInfo in ElasticSearch
        ThemeSettingInfo themeSettingInfoEs = themeSettingInfoSearchRepository.findOne(testThemeSettingInfo.getId());
        assertThat(themeSettingInfoEs).isEqualToComparingFieldByField(testThemeSettingInfo);
    }

    @Test
    @Transactional
    public void deleteThemeSettingInfo() throws Exception {
        // Initialize the database
        themeSettingInfoRepository.saveAndFlush(themeSettingInfo);
        themeSettingInfoSearchRepository.save(themeSettingInfo);
        int databaseSizeBeforeDelete = themeSettingInfoRepository.findAll().size();

        // Get the themeSettingInfo
        restThemeSettingInfoMockMvc.perform(delete("/api/theme-setting-infos/{id}", themeSettingInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean themeSettingInfoExistsInEs = themeSettingInfoSearchRepository.exists(themeSettingInfo.getId());
        assertThat(themeSettingInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<ThemeSettingInfo> themeSettingInfos = themeSettingInfoRepository.findAll();
        assertThat(themeSettingInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchThemeSettingInfo() throws Exception {
        // Initialize the database
        themeSettingInfoRepository.saveAndFlush(themeSettingInfo);
        themeSettingInfoSearchRepository.save(themeSettingInfo);

        // Search the themeSettingInfo
        restThemeSettingInfoMockMvc.perform(get("/api/_search/theme-setting-infos?query=id:" + themeSettingInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(themeSettingInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
