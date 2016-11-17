package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ScheduleInfo;
import com.eyeson.tikon.repository.ScheduleInfoRepository;
import com.eyeson.tikon.service.ScheduleInfoService;
import com.eyeson.tikon.repository.search.ScheduleInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.ScheduleInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ScheduleInfoMapper;

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

import com.eyeson.tikon.domain.enumeration.ScheduleType;

/**
 * Test class for the ScheduleInfoResource REST controller.
 *
 * @see ScheduleInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ScheduleInfoResourceIntTest {


    private static final ScheduleType DEFAULT_SCHEDULE_TYPE = ScheduleType.PRIMARY;
    private static final ScheduleType UPDATED_SCHEDULE_TYPE = ScheduleType.EXCEPTIONAL;

    @Inject
    private ScheduleInfoRepository scheduleInfoRepository;

    @Inject
    private ScheduleInfoMapper scheduleInfoMapper;

    @Inject
    private ScheduleInfoService scheduleInfoService;

    @Inject
    private ScheduleInfoSearchRepository scheduleInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restScheduleInfoMockMvc;

    private ScheduleInfo scheduleInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScheduleInfoResource scheduleInfoResource = new ScheduleInfoResource();
        ReflectionTestUtils.setField(scheduleInfoResource, "scheduleInfoService", scheduleInfoService);
        ReflectionTestUtils.setField(scheduleInfoResource, "scheduleInfoMapper", scheduleInfoMapper);
        this.restScheduleInfoMockMvc = MockMvcBuilders.standaloneSetup(scheduleInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        scheduleInfoSearchRepository.deleteAll();
        scheduleInfo = new ScheduleInfo();
        scheduleInfo.setScheduleType(DEFAULT_SCHEDULE_TYPE);
    }

    @Test
    @Transactional
    public void createScheduleInfo() throws Exception {
        int databaseSizeBeforeCreate = scheduleInfoRepository.findAll().size();

        // Create the ScheduleInfo
        ScheduleInfoDTO scheduleInfoDTO = scheduleInfoMapper.scheduleInfoToScheduleInfoDTO(scheduleInfo);

        restScheduleInfoMockMvc.perform(post("/api/schedule-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scheduleInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the ScheduleInfo in the database
        List<ScheduleInfo> scheduleInfos = scheduleInfoRepository.findAll();
        assertThat(scheduleInfos).hasSize(databaseSizeBeforeCreate + 1);
        ScheduleInfo testScheduleInfo = scheduleInfos.get(scheduleInfos.size() - 1);
        assertThat(testScheduleInfo.getScheduleType()).isEqualTo(DEFAULT_SCHEDULE_TYPE);

        // Validate the ScheduleInfo in ElasticSearch
        ScheduleInfo scheduleInfoEs = scheduleInfoSearchRepository.findOne(testScheduleInfo.getId());
        assertThat(scheduleInfoEs).isEqualToComparingFieldByField(testScheduleInfo);
    }

    @Test
    @Transactional
    public void getAllScheduleInfos() throws Exception {
        // Initialize the database
        scheduleInfoRepository.saveAndFlush(scheduleInfo);

        // Get all the scheduleInfos
        restScheduleInfoMockMvc.perform(get("/api/schedule-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].scheduleType").value(hasItem(DEFAULT_SCHEDULE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getScheduleInfo() throws Exception {
        // Initialize the database
        scheduleInfoRepository.saveAndFlush(scheduleInfo);

        // Get the scheduleInfo
        restScheduleInfoMockMvc.perform(get("/api/schedule-infos/{id}", scheduleInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(scheduleInfo.getId().intValue()))
            .andExpect(jsonPath("$.scheduleType").value(DEFAULT_SCHEDULE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingScheduleInfo() throws Exception {
        // Get the scheduleInfo
        restScheduleInfoMockMvc.perform(get("/api/schedule-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScheduleInfo() throws Exception {
        // Initialize the database
        scheduleInfoRepository.saveAndFlush(scheduleInfo);
        scheduleInfoSearchRepository.save(scheduleInfo);
        int databaseSizeBeforeUpdate = scheduleInfoRepository.findAll().size();

        // Update the scheduleInfo
        ScheduleInfo updatedScheduleInfo = new ScheduleInfo();
        updatedScheduleInfo.setId(scheduleInfo.getId());
        updatedScheduleInfo.setScheduleType(UPDATED_SCHEDULE_TYPE);
        ScheduleInfoDTO scheduleInfoDTO = scheduleInfoMapper.scheduleInfoToScheduleInfoDTO(updatedScheduleInfo);

        restScheduleInfoMockMvc.perform(put("/api/schedule-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scheduleInfoDTO)))
                .andExpect(status().isOk());

        // Validate the ScheduleInfo in the database
        List<ScheduleInfo> scheduleInfos = scheduleInfoRepository.findAll();
        assertThat(scheduleInfos).hasSize(databaseSizeBeforeUpdate);
        ScheduleInfo testScheduleInfo = scheduleInfos.get(scheduleInfos.size() - 1);
        assertThat(testScheduleInfo.getScheduleType()).isEqualTo(UPDATED_SCHEDULE_TYPE);

        // Validate the ScheduleInfo in ElasticSearch
        ScheduleInfo scheduleInfoEs = scheduleInfoSearchRepository.findOne(testScheduleInfo.getId());
        assertThat(scheduleInfoEs).isEqualToComparingFieldByField(testScheduleInfo);
    }

    @Test
    @Transactional
    public void deleteScheduleInfo() throws Exception {
        // Initialize the database
        scheduleInfoRepository.saveAndFlush(scheduleInfo);
        scheduleInfoSearchRepository.save(scheduleInfo);
        int databaseSizeBeforeDelete = scheduleInfoRepository.findAll().size();

        // Get the scheduleInfo
        restScheduleInfoMockMvc.perform(delete("/api/schedule-infos/{id}", scheduleInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean scheduleInfoExistsInEs = scheduleInfoSearchRepository.exists(scheduleInfo.getId());
        assertThat(scheduleInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<ScheduleInfo> scheduleInfos = scheduleInfoRepository.findAll();
        assertThat(scheduleInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchScheduleInfo() throws Exception {
        // Initialize the database
        scheduleInfoRepository.saveAndFlush(scheduleInfo);
        scheduleInfoSearchRepository.save(scheduleInfo);

        // Search the scheduleInfo
        restScheduleInfoMockMvc.perform(get("/api/_search/schedule-infos?query=id:" + scheduleInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].scheduleType").value(hasItem(DEFAULT_SCHEDULE_TYPE.toString())));
    }
}
