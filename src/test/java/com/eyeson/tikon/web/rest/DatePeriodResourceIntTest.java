package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.DatePeriod;
import com.eyeson.tikon.repository.DatePeriodRepository;
import com.eyeson.tikon.service.DatePeriodService;
import com.eyeson.tikon.repository.search.DatePeriodSearchRepository;
import com.eyeson.tikon.web.rest.dto.DatePeriodDTO;
import com.eyeson.tikon.web.rest.mapper.DatePeriodMapper;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DatePeriodResource REST controller.
 *
 * @see DatePeriodResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class DatePeriodResourceIntTest {


    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private DatePeriodRepository datePeriodRepository;

    @Inject
    private DatePeriodMapper datePeriodMapper;

    @Inject
    private DatePeriodService datePeriodService;

    @Inject
    private DatePeriodSearchRepository datePeriodSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDatePeriodMockMvc;

    private DatePeriod datePeriod;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DatePeriodResource datePeriodResource = new DatePeriodResource();
        ReflectionTestUtils.setField(datePeriodResource, "datePeriodService", datePeriodService);
        ReflectionTestUtils.setField(datePeriodResource, "datePeriodMapper", datePeriodMapper);
        this.restDatePeriodMockMvc = MockMvcBuilders.standaloneSetup(datePeriodResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        datePeriodSearchRepository.deleteAll();
        datePeriod = new DatePeriod();
        datePeriod.setFromDate(DEFAULT_FROM_DATE);
        datePeriod.setToDate(DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    public void createDatePeriod() throws Exception {
        int databaseSizeBeforeCreate = datePeriodRepository.findAll().size();

        // Create the DatePeriod
        DatePeriodDTO datePeriodDTO = datePeriodMapper.datePeriodToDatePeriodDTO(datePeriod);

        restDatePeriodMockMvc.perform(post("/api/date-periods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(datePeriodDTO)))
                .andExpect(status().isCreated());

        // Validate the DatePeriod in the database
        List<DatePeriod> datePeriods = datePeriodRepository.findAll();
        assertThat(datePeriods).hasSize(databaseSizeBeforeCreate + 1);
        DatePeriod testDatePeriod = datePeriods.get(datePeriods.size() - 1);
        assertThat(testDatePeriod.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testDatePeriod.getToDate()).isEqualTo(DEFAULT_TO_DATE);

        // Validate the DatePeriod in ElasticSearch
        DatePeriod datePeriodEs = datePeriodSearchRepository.findOne(testDatePeriod.getId());
        assertThat(datePeriodEs).isEqualToComparingFieldByField(testDatePeriod);
    }

    @Test
    @Transactional
    public void getAllDatePeriods() throws Exception {
        // Initialize the database
        datePeriodRepository.saveAndFlush(datePeriod);

        // Get all the datePeriods
        restDatePeriodMockMvc.perform(get("/api/date-periods?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(datePeriod.getId().intValue())))
                .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
                .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
    }

    @Test
    @Transactional
    public void getDatePeriod() throws Exception {
        // Initialize the database
        datePeriodRepository.saveAndFlush(datePeriod);

        // Get the datePeriod
        restDatePeriodMockMvc.perform(get("/api/date-periods/{id}", datePeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(datePeriod.getId().intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDatePeriod() throws Exception {
        // Get the datePeriod
        restDatePeriodMockMvc.perform(get("/api/date-periods/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDatePeriod() throws Exception {
        // Initialize the database
        datePeriodRepository.saveAndFlush(datePeriod);
        datePeriodSearchRepository.save(datePeriod);
        int databaseSizeBeforeUpdate = datePeriodRepository.findAll().size();

        // Update the datePeriod
        DatePeriod updatedDatePeriod = new DatePeriod();
        updatedDatePeriod.setId(datePeriod.getId());
        updatedDatePeriod.setFromDate(UPDATED_FROM_DATE);
        updatedDatePeriod.setToDate(UPDATED_TO_DATE);
        DatePeriodDTO datePeriodDTO = datePeriodMapper.datePeriodToDatePeriodDTO(updatedDatePeriod);

        restDatePeriodMockMvc.perform(put("/api/date-periods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(datePeriodDTO)))
                .andExpect(status().isOk());

        // Validate the DatePeriod in the database
        List<DatePeriod> datePeriods = datePeriodRepository.findAll();
        assertThat(datePeriods).hasSize(databaseSizeBeforeUpdate);
        DatePeriod testDatePeriod = datePeriods.get(datePeriods.size() - 1);
        assertThat(testDatePeriod.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testDatePeriod.getToDate()).isEqualTo(UPDATED_TO_DATE);

        // Validate the DatePeriod in ElasticSearch
        DatePeriod datePeriodEs = datePeriodSearchRepository.findOne(testDatePeriod.getId());
        assertThat(datePeriodEs).isEqualToComparingFieldByField(testDatePeriod);
    }

    @Test
    @Transactional
    public void deleteDatePeriod() throws Exception {
        // Initialize the database
        datePeriodRepository.saveAndFlush(datePeriod);
        datePeriodSearchRepository.save(datePeriod);
        int databaseSizeBeforeDelete = datePeriodRepository.findAll().size();

        // Get the datePeriod
        restDatePeriodMockMvc.perform(delete("/api/date-periods/{id}", datePeriod.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean datePeriodExistsInEs = datePeriodSearchRepository.exists(datePeriod.getId());
        assertThat(datePeriodExistsInEs).isFalse();

        // Validate the database is empty
        List<DatePeriod> datePeriods = datePeriodRepository.findAll();
        assertThat(datePeriods).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDatePeriod() throws Exception {
        // Initialize the database
        datePeriodRepository.saveAndFlush(datePeriod);
        datePeriodSearchRepository.save(datePeriod);

        // Search the datePeriod
        restDatePeriodMockMvc.perform(get("/api/_search/date-periods?query=id:" + datePeriod.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(datePeriod.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
    }
}
