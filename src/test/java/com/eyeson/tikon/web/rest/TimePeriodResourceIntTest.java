package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.TimePeriod;
import com.eyeson.tikon.repository.TimePeriodRepository;
import com.eyeson.tikon.service.TimePeriodService;
import com.eyeson.tikon.repository.search.TimePeriodSearchRepository;
import com.eyeson.tikon.web.rest.dto.TimePeriodDTO;
import com.eyeson.tikon.web.rest.mapper.TimePeriodMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TimePeriodResource REST controller.
 *
 * @see TimePeriodResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class TimePeriodResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_TIME_STR = dateTimeFormatter.format(DEFAULT_START_TIME);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_TIME_STR = dateTimeFormatter.format(DEFAULT_END_TIME);

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Integer DEFAULT_GAPTIME = 1;
    private static final Integer UPDATED_GAPTIME = 2;

    @Inject
    private TimePeriodRepository timePeriodRepository;

    @Inject
    private TimePeriodMapper timePeriodMapper;

    @Inject
    private TimePeriodService timePeriodService;

    @Inject
    private TimePeriodSearchRepository timePeriodSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTimePeriodMockMvc;

    private TimePeriod timePeriod;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TimePeriodResource timePeriodResource = new TimePeriodResource();
        ReflectionTestUtils.setField(timePeriodResource, "timePeriodService", timePeriodService);
        ReflectionTestUtils.setField(timePeriodResource, "timePeriodMapper", timePeriodMapper);
        this.restTimePeriodMockMvc = MockMvcBuilders.standaloneSetup(timePeriodResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        timePeriodSearchRepository.deleteAll();
        timePeriod = new TimePeriod();
        timePeriod.setStartTime(DEFAULT_START_TIME);
        timePeriod.setEndTime(DEFAULT_END_TIME);
        timePeriod.setDuration(DEFAULT_DURATION);
        timePeriod.setGaptime(DEFAULT_GAPTIME);
    }

    @Test
    @Transactional
    public void createTimePeriod() throws Exception {
        int databaseSizeBeforeCreate = timePeriodRepository.findAll().size();

        // Create the TimePeriod
        TimePeriodDTO timePeriodDTO = timePeriodMapper.timePeriodToTimePeriodDTO(timePeriod);

        restTimePeriodMockMvc.perform(post("/api/time-periods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timePeriodDTO)))
                .andExpect(status().isCreated());

        // Validate the TimePeriod in the database
        List<TimePeriod> timePeriods = timePeriodRepository.findAll();
        assertThat(timePeriods).hasSize(databaseSizeBeforeCreate + 1);
        TimePeriod testTimePeriod = timePeriods.get(timePeriods.size() - 1);
        assertThat(testTimePeriod.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testTimePeriod.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testTimePeriod.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testTimePeriod.getGaptime()).isEqualTo(DEFAULT_GAPTIME);

        // Validate the TimePeriod in ElasticSearch
        TimePeriod timePeriodEs = timePeriodSearchRepository.findOne(testTimePeriod.getId());
        assertThat(timePeriodEs).isEqualToComparingFieldByField(testTimePeriod);
    }

    @Test
    @Transactional
    public void getAllTimePeriods() throws Exception {
        // Initialize the database
        timePeriodRepository.saveAndFlush(timePeriod);

        // Get all the timePeriods
        restTimePeriodMockMvc.perform(get("/api/time-periods?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(timePeriod.getId().intValue())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)))
                .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
                .andExpect(jsonPath("$.[*].gaptime").value(hasItem(DEFAULT_GAPTIME)));
    }

    @Test
    @Transactional
    public void getTimePeriod() throws Exception {
        // Initialize the database
        timePeriodRepository.saveAndFlush(timePeriod);

        // Get the timePeriod
        restTimePeriodMockMvc.perform(get("/api/time-periods/{id}", timePeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(timePeriod.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_STR))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME_STR))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.gaptime").value(DEFAULT_GAPTIME));
    }

    @Test
    @Transactional
    public void getNonExistingTimePeriod() throws Exception {
        // Get the timePeriod
        restTimePeriodMockMvc.perform(get("/api/time-periods/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimePeriod() throws Exception {
        // Initialize the database
        timePeriodRepository.saveAndFlush(timePeriod);
        timePeriodSearchRepository.save(timePeriod);
        int databaseSizeBeforeUpdate = timePeriodRepository.findAll().size();

        // Update the timePeriod
        TimePeriod updatedTimePeriod = new TimePeriod();
        updatedTimePeriod.setId(timePeriod.getId());
        updatedTimePeriod.setStartTime(UPDATED_START_TIME);
        updatedTimePeriod.setEndTime(UPDATED_END_TIME);
        updatedTimePeriod.setDuration(UPDATED_DURATION);
        updatedTimePeriod.setGaptime(UPDATED_GAPTIME);
        TimePeriodDTO timePeriodDTO = timePeriodMapper.timePeriodToTimePeriodDTO(updatedTimePeriod);

        restTimePeriodMockMvc.perform(put("/api/time-periods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timePeriodDTO)))
                .andExpect(status().isOk());

        // Validate the TimePeriod in the database
        List<TimePeriod> timePeriods = timePeriodRepository.findAll();
        assertThat(timePeriods).hasSize(databaseSizeBeforeUpdate);
        TimePeriod testTimePeriod = timePeriods.get(timePeriods.size() - 1);
        assertThat(testTimePeriod.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testTimePeriod.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testTimePeriod.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testTimePeriod.getGaptime()).isEqualTo(UPDATED_GAPTIME);

        // Validate the TimePeriod in ElasticSearch
        TimePeriod timePeriodEs = timePeriodSearchRepository.findOne(testTimePeriod.getId());
        assertThat(timePeriodEs).isEqualToComparingFieldByField(testTimePeriod);
    }

    @Test
    @Transactional
    public void deleteTimePeriod() throws Exception {
        // Initialize the database
        timePeriodRepository.saveAndFlush(timePeriod);
        timePeriodSearchRepository.save(timePeriod);
        int databaseSizeBeforeDelete = timePeriodRepository.findAll().size();

        // Get the timePeriod
        restTimePeriodMockMvc.perform(delete("/api/time-periods/{id}", timePeriod.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean timePeriodExistsInEs = timePeriodSearchRepository.exists(timePeriod.getId());
        assertThat(timePeriodExistsInEs).isFalse();

        // Validate the database is empty
        List<TimePeriod> timePeriods = timePeriodRepository.findAll();
        assertThat(timePeriods).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTimePeriod() throws Exception {
        // Initialize the database
        timePeriodRepository.saveAndFlush(timePeriod);
        timePeriodSearchRepository.save(timePeriod);

        // Search the timePeriod
        restTimePeriodMockMvc.perform(get("/api/_search/time-periods?query=id:" + timePeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timePeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].gaptime").value(hasItem(DEFAULT_GAPTIME)));
    }
}
