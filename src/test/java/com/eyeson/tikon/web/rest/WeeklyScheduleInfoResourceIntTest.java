package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.WeeklyScheduleInfo;
import com.eyeson.tikon.repository.WeeklyScheduleInfoRepository;
import com.eyeson.tikon.service.WeeklyScheduleInfoService;
import com.eyeson.tikon.repository.search.WeeklyScheduleInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.WeeklyScheduleInfoDTO;
import com.eyeson.tikon.web.rest.mapper.WeeklyScheduleInfoMapper;

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
 * Test class for the WeeklyScheduleInfoResource REST controller.
 *
 * @see WeeklyScheduleInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class WeeklyScheduleInfoResourceIntTest {


    @Inject
    private WeeklyScheduleInfoRepository weeklyScheduleInfoRepository;

    @Inject
    private WeeklyScheduleInfoMapper weeklyScheduleInfoMapper;

    @Inject
    private WeeklyScheduleInfoService weeklyScheduleInfoService;

    @Inject
    private WeeklyScheduleInfoSearchRepository weeklyScheduleInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWeeklyScheduleInfoMockMvc;

    private WeeklyScheduleInfo weeklyScheduleInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WeeklyScheduleInfoResource weeklyScheduleInfoResource = new WeeklyScheduleInfoResource();
        ReflectionTestUtils.setField(weeklyScheduleInfoResource, "weeklyScheduleInfoService", weeklyScheduleInfoService);
        ReflectionTestUtils.setField(weeklyScheduleInfoResource, "weeklyScheduleInfoMapper", weeklyScheduleInfoMapper);
        this.restWeeklyScheduleInfoMockMvc = MockMvcBuilders.standaloneSetup(weeklyScheduleInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        weeklyScheduleInfoSearchRepository.deleteAll();
        weeklyScheduleInfo = new WeeklyScheduleInfo();
    }

    @Test
    @Transactional
    public void createWeeklyScheduleInfo() throws Exception {
        int databaseSizeBeforeCreate = weeklyScheduleInfoRepository.findAll().size();

        // Create the WeeklyScheduleInfo
        WeeklyScheduleInfoDTO weeklyScheduleInfoDTO = weeklyScheduleInfoMapper.weeklyScheduleInfoToWeeklyScheduleInfoDTO(weeklyScheduleInfo);

        restWeeklyScheduleInfoMockMvc.perform(post("/api/weekly-schedule-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weeklyScheduleInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the WeeklyScheduleInfo in the database
        List<WeeklyScheduleInfo> weeklyScheduleInfos = weeklyScheduleInfoRepository.findAll();
        assertThat(weeklyScheduleInfos).hasSize(databaseSizeBeforeCreate + 1);
        WeeklyScheduleInfo testWeeklyScheduleInfo = weeklyScheduleInfos.get(weeklyScheduleInfos.size() - 1);

        // Validate the WeeklyScheduleInfo in ElasticSearch
        WeeklyScheduleInfo weeklyScheduleInfoEs = weeklyScheduleInfoSearchRepository.findOne(testWeeklyScheduleInfo.getId());
        assertThat(weeklyScheduleInfoEs).isEqualToComparingFieldByField(testWeeklyScheduleInfo);
    }

    @Test
    @Transactional
    public void getAllWeeklyScheduleInfos() throws Exception {
        // Initialize the database
        weeklyScheduleInfoRepository.saveAndFlush(weeklyScheduleInfo);

        // Get all the weeklyScheduleInfos
        restWeeklyScheduleInfoMockMvc.perform(get("/api/weekly-schedule-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyScheduleInfo.getId().intValue())));
    }

    @Test
    @Transactional
    public void getWeeklyScheduleInfo() throws Exception {
        // Initialize the database
        weeklyScheduleInfoRepository.saveAndFlush(weeklyScheduleInfo);

        // Get the weeklyScheduleInfo
        restWeeklyScheduleInfoMockMvc.perform(get("/api/weekly-schedule-infos/{id}", weeklyScheduleInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(weeklyScheduleInfo.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWeeklyScheduleInfo() throws Exception {
        // Get the weeklyScheduleInfo
        restWeeklyScheduleInfoMockMvc.perform(get("/api/weekly-schedule-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeeklyScheduleInfo() throws Exception {
        // Initialize the database
        weeklyScheduleInfoRepository.saveAndFlush(weeklyScheduleInfo);
        weeklyScheduleInfoSearchRepository.save(weeklyScheduleInfo);
        int databaseSizeBeforeUpdate = weeklyScheduleInfoRepository.findAll().size();

        // Update the weeklyScheduleInfo
        WeeklyScheduleInfo updatedWeeklyScheduleInfo = new WeeklyScheduleInfo();
        updatedWeeklyScheduleInfo.setId(weeklyScheduleInfo.getId());
        WeeklyScheduleInfoDTO weeklyScheduleInfoDTO = weeklyScheduleInfoMapper.weeklyScheduleInfoToWeeklyScheduleInfoDTO(updatedWeeklyScheduleInfo);

        restWeeklyScheduleInfoMockMvc.perform(put("/api/weekly-schedule-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weeklyScheduleInfoDTO)))
                .andExpect(status().isOk());

        // Validate the WeeklyScheduleInfo in the database
        List<WeeklyScheduleInfo> weeklyScheduleInfos = weeklyScheduleInfoRepository.findAll();
        assertThat(weeklyScheduleInfos).hasSize(databaseSizeBeforeUpdate);
        WeeklyScheduleInfo testWeeklyScheduleInfo = weeklyScheduleInfos.get(weeklyScheduleInfos.size() - 1);

        // Validate the WeeklyScheduleInfo in ElasticSearch
        WeeklyScheduleInfo weeklyScheduleInfoEs = weeklyScheduleInfoSearchRepository.findOne(testWeeklyScheduleInfo.getId());
        assertThat(weeklyScheduleInfoEs).isEqualToComparingFieldByField(testWeeklyScheduleInfo);
    }

    @Test
    @Transactional
    public void deleteWeeklyScheduleInfo() throws Exception {
        // Initialize the database
        weeklyScheduleInfoRepository.saveAndFlush(weeklyScheduleInfo);
        weeklyScheduleInfoSearchRepository.save(weeklyScheduleInfo);
        int databaseSizeBeforeDelete = weeklyScheduleInfoRepository.findAll().size();

        // Get the weeklyScheduleInfo
        restWeeklyScheduleInfoMockMvc.perform(delete("/api/weekly-schedule-infos/{id}", weeklyScheduleInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean weeklyScheduleInfoExistsInEs = weeklyScheduleInfoSearchRepository.exists(weeklyScheduleInfo.getId());
        assertThat(weeklyScheduleInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<WeeklyScheduleInfo> weeklyScheduleInfos = weeklyScheduleInfoRepository.findAll();
        assertThat(weeklyScheduleInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWeeklyScheduleInfo() throws Exception {
        // Initialize the database
        weeklyScheduleInfoRepository.saveAndFlush(weeklyScheduleInfo);
        weeklyScheduleInfoSearchRepository.save(weeklyScheduleInfo);

        // Search the weeklyScheduleInfo
        restWeeklyScheduleInfoMockMvc.perform(get("/api/_search/weekly-schedule-infos?query=id:" + weeklyScheduleInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyScheduleInfo.getId().intValue())));
    }
}
