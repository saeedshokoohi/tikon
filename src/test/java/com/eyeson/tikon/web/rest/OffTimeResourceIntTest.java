package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.OffTime;
import com.eyeson.tikon.repository.OffTimeRepository;
import com.eyeson.tikon.service.OffTimeService;
import com.eyeson.tikon.repository.search.OffTimeSearchRepository;
import com.eyeson.tikon.web.rest.dto.OffTimeDTO;
import com.eyeson.tikon.web.rest.mapper.OffTimeMapper;

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

import com.eyeson.tikon.domain.enumeration.OffTimeType;

/**
 * Test class for the OffTimeResource REST controller.
 *
 * @see OffTimeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class OffTimeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_I = 1L;
    private static final Long UPDATED_I = 2L;

    private static final ZonedDateTime DEFAULT_FROM_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FROM_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FROM_TIME_STR = dateTimeFormatter.format(DEFAULT_FROM_TIME);

    private static final ZonedDateTime DEFAULT_TO_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TO_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TO_TIME_STR = dateTimeFormatter.format(DEFAULT_TO_TIME);

    private static final OffTimeType DEFAULT_OFF_TIME_TYPE = OffTimeType.REST_TIME;
    private static final OffTimeType UPDATED_OFF_TIME_TYPE = OffTimeType.DINNER_TIME;

    @Inject
    private OffTimeRepository offTimeRepository;

    @Inject
    private OffTimeMapper offTimeMapper;

    @Inject
    private OffTimeService offTimeService;

    @Inject
    private OffTimeSearchRepository offTimeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOffTimeMockMvc;

    private OffTime offTime;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OffTimeResource offTimeResource = new OffTimeResource();
        ReflectionTestUtils.setField(offTimeResource, "offTimeService", offTimeService);
        ReflectionTestUtils.setField(offTimeResource, "offTimeMapper", offTimeMapper);
        this.restOffTimeMockMvc = MockMvcBuilders.standaloneSetup(offTimeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        offTimeSearchRepository.deleteAll();
        offTime = new OffTime();
        offTime.setI(DEFAULT_I);
        offTime.setFromTime(DEFAULT_FROM_TIME);
        offTime.setToTime(DEFAULT_TO_TIME);
        offTime.setOffTimeType(DEFAULT_OFF_TIME_TYPE);
    }

    @Test
    @Transactional
    public void createOffTime() throws Exception {
        int databaseSizeBeforeCreate = offTimeRepository.findAll().size();

        // Create the OffTime
        OffTimeDTO offTimeDTO = offTimeMapper.offTimeToOffTimeDTO(offTime);

        restOffTimeMockMvc.perform(post("/api/off-times")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offTimeDTO)))
                .andExpect(status().isCreated());

        // Validate the OffTime in the database
        List<OffTime> offTimes = offTimeRepository.findAll();
        assertThat(offTimes).hasSize(databaseSizeBeforeCreate + 1);
        OffTime testOffTime = offTimes.get(offTimes.size() - 1);
        assertThat(testOffTime.getI()).isEqualTo(DEFAULT_I);
        assertThat(testOffTime.getFromTime()).isEqualTo(DEFAULT_FROM_TIME);
        assertThat(testOffTime.getToTime()).isEqualTo(DEFAULT_TO_TIME);
        assertThat(testOffTime.getOffTimeType()).isEqualTo(DEFAULT_OFF_TIME_TYPE);

        // Validate the OffTime in ElasticSearch
        OffTime offTimeEs = offTimeSearchRepository.findOne(testOffTime.getId());
        assertThat(offTimeEs).isEqualToComparingFieldByField(testOffTime);
    }

    @Test
    @Transactional
    public void getAllOffTimes() throws Exception {
        // Initialize the database
        offTimeRepository.saveAndFlush(offTime);

        // Get all the offTimes
        restOffTimeMockMvc.perform(get("/api/off-times?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(offTime.getId().intValue())))
                .andExpect(jsonPath("$.[*].i").value(hasItem(DEFAULT_I.intValue())))
                .andExpect(jsonPath("$.[*].fromTime").value(hasItem(DEFAULT_FROM_TIME_STR)))
                .andExpect(jsonPath("$.[*].toTime").value(hasItem(DEFAULT_TO_TIME_STR)))
                .andExpect(jsonPath("$.[*].offTimeType").value(hasItem(DEFAULT_OFF_TIME_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getOffTime() throws Exception {
        // Initialize the database
        offTimeRepository.saveAndFlush(offTime);

        // Get the offTime
        restOffTimeMockMvc.perform(get("/api/off-times/{id}", offTime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(offTime.getId().intValue()))
            .andExpect(jsonPath("$.i").value(DEFAULT_I.intValue()))
            .andExpect(jsonPath("$.fromTime").value(DEFAULT_FROM_TIME_STR))
            .andExpect(jsonPath("$.toTime").value(DEFAULT_TO_TIME_STR))
            .andExpect(jsonPath("$.offTimeType").value(DEFAULT_OFF_TIME_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOffTime() throws Exception {
        // Get the offTime
        restOffTimeMockMvc.perform(get("/api/off-times/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOffTime() throws Exception {
        // Initialize the database
        offTimeRepository.saveAndFlush(offTime);
        offTimeSearchRepository.save(offTime);
        int databaseSizeBeforeUpdate = offTimeRepository.findAll().size();

        // Update the offTime
        OffTime updatedOffTime = new OffTime();
        updatedOffTime.setId(offTime.getId());
        updatedOffTime.setI(UPDATED_I);
        updatedOffTime.setFromTime(UPDATED_FROM_TIME);
        updatedOffTime.setToTime(UPDATED_TO_TIME);
        updatedOffTime.setOffTimeType(UPDATED_OFF_TIME_TYPE);
        OffTimeDTO offTimeDTO = offTimeMapper.offTimeToOffTimeDTO(updatedOffTime);

        restOffTimeMockMvc.perform(put("/api/off-times")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offTimeDTO)))
                .andExpect(status().isOk());

        // Validate the OffTime in the database
        List<OffTime> offTimes = offTimeRepository.findAll();
        assertThat(offTimes).hasSize(databaseSizeBeforeUpdate);
        OffTime testOffTime = offTimes.get(offTimes.size() - 1);
        assertThat(testOffTime.getI()).isEqualTo(UPDATED_I);
        assertThat(testOffTime.getFromTime()).isEqualTo(UPDATED_FROM_TIME);
        assertThat(testOffTime.getToTime()).isEqualTo(UPDATED_TO_TIME);
        assertThat(testOffTime.getOffTimeType()).isEqualTo(UPDATED_OFF_TIME_TYPE);

        // Validate the OffTime in ElasticSearch
        OffTime offTimeEs = offTimeSearchRepository.findOne(testOffTime.getId());
        assertThat(offTimeEs).isEqualToComparingFieldByField(testOffTime);
    }

    @Test
    @Transactional
    public void deleteOffTime() throws Exception {
        // Initialize the database
        offTimeRepository.saveAndFlush(offTime);
        offTimeSearchRepository.save(offTime);
        int databaseSizeBeforeDelete = offTimeRepository.findAll().size();

        // Get the offTime
        restOffTimeMockMvc.perform(delete("/api/off-times/{id}", offTime.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean offTimeExistsInEs = offTimeSearchRepository.exists(offTime.getId());
        assertThat(offTimeExistsInEs).isFalse();

        // Validate the database is empty
        List<OffTime> offTimes = offTimeRepository.findAll();
        assertThat(offTimes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOffTime() throws Exception {
        // Initialize the database
        offTimeRepository.saveAndFlush(offTime);
        offTimeSearchRepository.save(offTime);

        // Search the offTime
        restOffTimeMockMvc.perform(get("/api/_search/off-times?query=id:" + offTime.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offTime.getId().intValue())))
            .andExpect(jsonPath("$.[*].i").value(hasItem(DEFAULT_I.intValue())))
            .andExpect(jsonPath("$.[*].fromTime").value(hasItem(DEFAULT_FROM_TIME_STR)))
            .andExpect(jsonPath("$.[*].toTime").value(hasItem(DEFAULT_TO_TIME_STR)))
            .andExpect(jsonPath("$.[*].offTimeType").value(hasItem(DEFAULT_OFF_TIME_TYPE.toString())));
    }
}
