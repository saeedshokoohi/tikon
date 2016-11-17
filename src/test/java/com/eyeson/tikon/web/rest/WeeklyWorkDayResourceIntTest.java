package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.WeeklyWorkDay;
import com.eyeson.tikon.repository.WeeklyWorkDayRepository;
import com.eyeson.tikon.service.WeeklyWorkDayService;
import com.eyeson.tikon.repository.search.WeeklyWorkDaySearchRepository;
import com.eyeson.tikon.web.rest.dto.WeeklyWorkDayDTO;
import com.eyeson.tikon.web.rest.mapper.WeeklyWorkDayMapper;

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

import com.eyeson.tikon.domain.enumeration.WeekDay;

/**
 * Test class for the WeeklyWorkDayResource REST controller.
 *
 * @see WeeklyWorkDayResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class WeeklyWorkDayResourceIntTest {


    private static final WeekDay DEFAULT_WEEKDAY = WeekDay.SATURDAY;
    private static final WeekDay UPDATED_WEEKDAY = WeekDay.SUNDAY;

    @Inject
    private WeeklyWorkDayRepository weeklyWorkDayRepository;

    @Inject
    private WeeklyWorkDayMapper weeklyWorkDayMapper;

    @Inject
    private WeeklyWorkDayService weeklyWorkDayService;

    @Inject
    private WeeklyWorkDaySearchRepository weeklyWorkDaySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWeeklyWorkDayMockMvc;

    private WeeklyWorkDay weeklyWorkDay;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WeeklyWorkDayResource weeklyWorkDayResource = new WeeklyWorkDayResource();
        ReflectionTestUtils.setField(weeklyWorkDayResource, "weeklyWorkDayService", weeklyWorkDayService);
        ReflectionTestUtils.setField(weeklyWorkDayResource, "weeklyWorkDayMapper", weeklyWorkDayMapper);
        this.restWeeklyWorkDayMockMvc = MockMvcBuilders.standaloneSetup(weeklyWorkDayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        weeklyWorkDaySearchRepository.deleteAll();
        weeklyWorkDay = new WeeklyWorkDay();
        weeklyWorkDay.setWeekday(DEFAULT_WEEKDAY);
    }

    @Test
    @Transactional
    public void createWeeklyWorkDay() throws Exception {
        int databaseSizeBeforeCreate = weeklyWorkDayRepository.findAll().size();

        // Create the WeeklyWorkDay
        WeeklyWorkDayDTO weeklyWorkDayDTO = weeklyWorkDayMapper.weeklyWorkDayToWeeklyWorkDayDTO(weeklyWorkDay);

        restWeeklyWorkDayMockMvc.perform(post("/api/weekly-work-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weeklyWorkDayDTO)))
                .andExpect(status().isCreated());

        // Validate the WeeklyWorkDay in the database
        List<WeeklyWorkDay> weeklyWorkDays = weeklyWorkDayRepository.findAll();
        assertThat(weeklyWorkDays).hasSize(databaseSizeBeforeCreate + 1);
        WeeklyWorkDay testWeeklyWorkDay = weeklyWorkDays.get(weeklyWorkDays.size() - 1);
        assertThat(testWeeklyWorkDay.getWeekday()).isEqualTo(DEFAULT_WEEKDAY);

        // Validate the WeeklyWorkDay in ElasticSearch
        WeeklyWorkDay weeklyWorkDayEs = weeklyWorkDaySearchRepository.findOne(testWeeklyWorkDay.getId());
        assertThat(weeklyWorkDayEs).isEqualToComparingFieldByField(testWeeklyWorkDay);
    }

    @Test
    @Transactional
    public void getAllWeeklyWorkDays() throws Exception {
        // Initialize the database
        weeklyWorkDayRepository.saveAndFlush(weeklyWorkDay);

        // Get all the weeklyWorkDays
        restWeeklyWorkDayMockMvc.perform(get("/api/weekly-work-days?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyWorkDay.getId().intValue())))
                .andExpect(jsonPath("$.[*].weekday").value(hasItem(DEFAULT_WEEKDAY.toString())));
    }

    @Test
    @Transactional
    public void getWeeklyWorkDay() throws Exception {
        // Initialize the database
        weeklyWorkDayRepository.saveAndFlush(weeklyWorkDay);

        // Get the weeklyWorkDay
        restWeeklyWorkDayMockMvc.perform(get("/api/weekly-work-days/{id}", weeklyWorkDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(weeklyWorkDay.getId().intValue()))
            .andExpect(jsonPath("$.weekday").value(DEFAULT_WEEKDAY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWeeklyWorkDay() throws Exception {
        // Get the weeklyWorkDay
        restWeeklyWorkDayMockMvc.perform(get("/api/weekly-work-days/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWeeklyWorkDay() throws Exception {
        // Initialize the database
        weeklyWorkDayRepository.saveAndFlush(weeklyWorkDay);
        weeklyWorkDaySearchRepository.save(weeklyWorkDay);
        int databaseSizeBeforeUpdate = weeklyWorkDayRepository.findAll().size();

        // Update the weeklyWorkDay
        WeeklyWorkDay updatedWeeklyWorkDay = new WeeklyWorkDay();
        updatedWeeklyWorkDay.setId(weeklyWorkDay.getId());
        updatedWeeklyWorkDay.setWeekday(UPDATED_WEEKDAY);
        WeeklyWorkDayDTO weeklyWorkDayDTO = weeklyWorkDayMapper.weeklyWorkDayToWeeklyWorkDayDTO(updatedWeeklyWorkDay);

        restWeeklyWorkDayMockMvc.perform(put("/api/weekly-work-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(weeklyWorkDayDTO)))
                .andExpect(status().isOk());

        // Validate the WeeklyWorkDay in the database
        List<WeeklyWorkDay> weeklyWorkDays = weeklyWorkDayRepository.findAll();
        assertThat(weeklyWorkDays).hasSize(databaseSizeBeforeUpdate);
        WeeklyWorkDay testWeeklyWorkDay = weeklyWorkDays.get(weeklyWorkDays.size() - 1);
        assertThat(testWeeklyWorkDay.getWeekday()).isEqualTo(UPDATED_WEEKDAY);

        // Validate the WeeklyWorkDay in ElasticSearch
        WeeklyWorkDay weeklyWorkDayEs = weeklyWorkDaySearchRepository.findOne(testWeeklyWorkDay.getId());
        assertThat(weeklyWorkDayEs).isEqualToComparingFieldByField(testWeeklyWorkDay);
    }

    @Test
    @Transactional
    public void deleteWeeklyWorkDay() throws Exception {
        // Initialize the database
        weeklyWorkDayRepository.saveAndFlush(weeklyWorkDay);
        weeklyWorkDaySearchRepository.save(weeklyWorkDay);
        int databaseSizeBeforeDelete = weeklyWorkDayRepository.findAll().size();

        // Get the weeklyWorkDay
        restWeeklyWorkDayMockMvc.perform(delete("/api/weekly-work-days/{id}", weeklyWorkDay.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean weeklyWorkDayExistsInEs = weeklyWorkDaySearchRepository.exists(weeklyWorkDay.getId());
        assertThat(weeklyWorkDayExistsInEs).isFalse();

        // Validate the database is empty
        List<WeeklyWorkDay> weeklyWorkDays = weeklyWorkDayRepository.findAll();
        assertThat(weeklyWorkDays).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWeeklyWorkDay() throws Exception {
        // Initialize the database
        weeklyWorkDayRepository.saveAndFlush(weeklyWorkDay);
        weeklyWorkDaySearchRepository.save(weeklyWorkDay);

        // Search the weeklyWorkDay
        restWeeklyWorkDayMockMvc.perform(get("/api/_search/weekly-work-days?query=id:" + weeklyWorkDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weeklyWorkDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].weekday").value(hasItem(DEFAULT_WEEKDAY.toString())));
    }
}
