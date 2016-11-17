package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ExtraSetting;
import com.eyeson.tikon.repository.ExtraSettingRepository;
import com.eyeson.tikon.service.ExtraSettingService;
import com.eyeson.tikon.repository.search.ExtraSettingSearchRepository;
import com.eyeson.tikon.web.rest.dto.ExtraSettingDTO;
import com.eyeson.tikon.web.rest.mapper.ExtraSettingMapper;

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
 * Test class for the ExtraSettingResource REST controller.
 *
 * @see ExtraSettingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ExtraSettingResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAA";
    private static final String UPDATED_KEY = "BBBBB";
    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private ExtraSettingRepository extraSettingRepository;

    @Inject
    private ExtraSettingMapper extraSettingMapper;

    @Inject
    private ExtraSettingService extraSettingService;

    @Inject
    private ExtraSettingSearchRepository extraSettingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExtraSettingMockMvc;

    private ExtraSetting extraSetting;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExtraSettingResource extraSettingResource = new ExtraSettingResource();
        ReflectionTestUtils.setField(extraSettingResource, "extraSettingService", extraSettingService);
        ReflectionTestUtils.setField(extraSettingResource, "extraSettingMapper", extraSettingMapper);
        this.restExtraSettingMockMvc = MockMvcBuilders.standaloneSetup(extraSettingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        extraSettingSearchRepository.deleteAll();
        extraSetting = new ExtraSetting();
        extraSetting.setKey(DEFAULT_KEY);
        extraSetting.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createExtraSetting() throws Exception {
        int databaseSizeBeforeCreate = extraSettingRepository.findAll().size();

        // Create the ExtraSetting
        ExtraSettingDTO extraSettingDTO = extraSettingMapper.extraSettingToExtraSettingDTO(extraSetting);

        restExtraSettingMockMvc.perform(post("/api/extra-settings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extraSettingDTO)))
                .andExpect(status().isCreated());

        // Validate the ExtraSetting in the database
        List<ExtraSetting> extraSettings = extraSettingRepository.findAll();
        assertThat(extraSettings).hasSize(databaseSizeBeforeCreate + 1);
        ExtraSetting testExtraSetting = extraSettings.get(extraSettings.size() - 1);
        assertThat(testExtraSetting.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testExtraSetting.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the ExtraSetting in ElasticSearch
        ExtraSetting extraSettingEs = extraSettingSearchRepository.findOne(testExtraSetting.getId());
        assertThat(extraSettingEs).isEqualToComparingFieldByField(testExtraSetting);
    }

    @Test
    @Transactional
    public void getAllExtraSettings() throws Exception {
        // Initialize the database
        extraSettingRepository.saveAndFlush(extraSetting);

        // Get all the extraSettings
        restExtraSettingMockMvc.perform(get("/api/extra-settings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(extraSetting.getId().intValue())))
                .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getExtraSetting() throws Exception {
        // Initialize the database
        extraSettingRepository.saveAndFlush(extraSetting);

        // Get the extraSetting
        restExtraSettingMockMvc.perform(get("/api/extra-settings/{id}", extraSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(extraSetting.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExtraSetting() throws Exception {
        // Get the extraSetting
        restExtraSettingMockMvc.perform(get("/api/extra-settings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtraSetting() throws Exception {
        // Initialize the database
        extraSettingRepository.saveAndFlush(extraSetting);
        extraSettingSearchRepository.save(extraSetting);
        int databaseSizeBeforeUpdate = extraSettingRepository.findAll().size();

        // Update the extraSetting
        ExtraSetting updatedExtraSetting = new ExtraSetting();
        updatedExtraSetting.setId(extraSetting.getId());
        updatedExtraSetting.setKey(UPDATED_KEY);
        updatedExtraSetting.setValue(UPDATED_VALUE);
        ExtraSettingDTO extraSettingDTO = extraSettingMapper.extraSettingToExtraSettingDTO(updatedExtraSetting);

        restExtraSettingMockMvc.perform(put("/api/extra-settings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(extraSettingDTO)))
                .andExpect(status().isOk());

        // Validate the ExtraSetting in the database
        List<ExtraSetting> extraSettings = extraSettingRepository.findAll();
        assertThat(extraSettings).hasSize(databaseSizeBeforeUpdate);
        ExtraSetting testExtraSetting = extraSettings.get(extraSettings.size() - 1);
        assertThat(testExtraSetting.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testExtraSetting.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the ExtraSetting in ElasticSearch
        ExtraSetting extraSettingEs = extraSettingSearchRepository.findOne(testExtraSetting.getId());
        assertThat(extraSettingEs).isEqualToComparingFieldByField(testExtraSetting);
    }

    @Test
    @Transactional
    public void deleteExtraSetting() throws Exception {
        // Initialize the database
        extraSettingRepository.saveAndFlush(extraSetting);
        extraSettingSearchRepository.save(extraSetting);
        int databaseSizeBeforeDelete = extraSettingRepository.findAll().size();

        // Get the extraSetting
        restExtraSettingMockMvc.perform(delete("/api/extra-settings/{id}", extraSetting.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean extraSettingExistsInEs = extraSettingSearchRepository.exists(extraSetting.getId());
        assertThat(extraSettingExistsInEs).isFalse();

        // Validate the database is empty
        List<ExtraSetting> extraSettings = extraSettingRepository.findAll();
        assertThat(extraSettings).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchExtraSetting() throws Exception {
        // Initialize the database
        extraSettingRepository.saveAndFlush(extraSetting);
        extraSettingSearchRepository.save(extraSetting);

        // Search the extraSetting
        restExtraSettingMockMvc.perform(get("/api/_search/extra-settings?query=id:" + extraSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extraSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }
}
