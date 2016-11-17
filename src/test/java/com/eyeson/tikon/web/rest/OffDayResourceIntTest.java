package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.OffDay;
import com.eyeson.tikon.repository.OffDayRepository;
import com.eyeson.tikon.service.OffDayService;
import com.eyeson.tikon.repository.search.OffDaySearchRepository;
import com.eyeson.tikon.web.rest.dto.OffDayDTO;
import com.eyeson.tikon.web.rest.mapper.OffDayMapper;

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

import com.eyeson.tikon.domain.enumeration.OffDayType;

/**
 * Test class for the OffDayResource REST controller.
 *
 * @see OffDayResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class OffDayResourceIntTest {


    private static final LocalDate DEFAULT_OFF_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OFF_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final OffDayType DEFAULT_OFF_DAY_TYPE = OffDayType.WEEKEND;
    private static final OffDayType UPDATED_OFF_DAY_TYPE = OffDayType.NEW_YEAR_HOLYDAY;

    @Inject
    private OffDayRepository offDayRepository;

    @Inject
    private OffDayMapper offDayMapper;

    @Inject
    private OffDayService offDayService;

    @Inject
    private OffDaySearchRepository offDaySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOffDayMockMvc;

    private OffDay offDay;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OffDayResource offDayResource = new OffDayResource();
        ReflectionTestUtils.setField(offDayResource, "offDayService", offDayService);
        ReflectionTestUtils.setField(offDayResource, "offDayMapper", offDayMapper);
        this.restOffDayMockMvc = MockMvcBuilders.standaloneSetup(offDayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        offDaySearchRepository.deleteAll();
        offDay = new OffDay();
        offDay.setOffDate(DEFAULT_OFF_DATE);
        offDay.setOffDayType(DEFAULT_OFF_DAY_TYPE);
    }

    @Test
    @Transactional
    public void createOffDay() throws Exception {
        int databaseSizeBeforeCreate = offDayRepository.findAll().size();

        // Create the OffDay
        OffDayDTO offDayDTO = offDayMapper.offDayToOffDayDTO(offDay);

        restOffDayMockMvc.perform(post("/api/off-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offDayDTO)))
                .andExpect(status().isCreated());

        // Validate the OffDay in the database
        List<OffDay> offDays = offDayRepository.findAll();
        assertThat(offDays).hasSize(databaseSizeBeforeCreate + 1);
        OffDay testOffDay = offDays.get(offDays.size() - 1);
        assertThat(testOffDay.getOffDate()).isEqualTo(DEFAULT_OFF_DATE);
        assertThat(testOffDay.getOffDayType()).isEqualTo(DEFAULT_OFF_DAY_TYPE);

        // Validate the OffDay in ElasticSearch
        OffDay offDayEs = offDaySearchRepository.findOne(testOffDay.getId());
        assertThat(offDayEs).isEqualToComparingFieldByField(testOffDay);
    }

    @Test
    @Transactional
    public void getAllOffDays() throws Exception {
        // Initialize the database
        offDayRepository.saveAndFlush(offDay);

        // Get all the offDays
        restOffDayMockMvc.perform(get("/api/off-days?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(offDay.getId().intValue())))
                .andExpect(jsonPath("$.[*].offDate").value(hasItem(DEFAULT_OFF_DATE.toString())))
                .andExpect(jsonPath("$.[*].offDayType").value(hasItem(DEFAULT_OFF_DAY_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getOffDay() throws Exception {
        // Initialize the database
        offDayRepository.saveAndFlush(offDay);

        // Get the offDay
        restOffDayMockMvc.perform(get("/api/off-days/{id}", offDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(offDay.getId().intValue()))
            .andExpect(jsonPath("$.offDate").value(DEFAULT_OFF_DATE.toString()))
            .andExpect(jsonPath("$.offDayType").value(DEFAULT_OFF_DAY_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOffDay() throws Exception {
        // Get the offDay
        restOffDayMockMvc.perform(get("/api/off-days/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOffDay() throws Exception {
        // Initialize the database
        offDayRepository.saveAndFlush(offDay);
        offDaySearchRepository.save(offDay);
        int databaseSizeBeforeUpdate = offDayRepository.findAll().size();

        // Update the offDay
        OffDay updatedOffDay = new OffDay();
        updatedOffDay.setId(offDay.getId());
        updatedOffDay.setOffDate(UPDATED_OFF_DATE);
        updatedOffDay.setOffDayType(UPDATED_OFF_DAY_TYPE);
        OffDayDTO offDayDTO = offDayMapper.offDayToOffDayDTO(updatedOffDay);

        restOffDayMockMvc.perform(put("/api/off-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(offDayDTO)))
                .andExpect(status().isOk());

        // Validate the OffDay in the database
        List<OffDay> offDays = offDayRepository.findAll();
        assertThat(offDays).hasSize(databaseSizeBeforeUpdate);
        OffDay testOffDay = offDays.get(offDays.size() - 1);
        assertThat(testOffDay.getOffDate()).isEqualTo(UPDATED_OFF_DATE);
        assertThat(testOffDay.getOffDayType()).isEqualTo(UPDATED_OFF_DAY_TYPE);

        // Validate the OffDay in ElasticSearch
        OffDay offDayEs = offDaySearchRepository.findOne(testOffDay.getId());
        assertThat(offDayEs).isEqualToComparingFieldByField(testOffDay);
    }

    @Test
    @Transactional
    public void deleteOffDay() throws Exception {
        // Initialize the database
        offDayRepository.saveAndFlush(offDay);
        offDaySearchRepository.save(offDay);
        int databaseSizeBeforeDelete = offDayRepository.findAll().size();

        // Get the offDay
        restOffDayMockMvc.perform(delete("/api/off-days/{id}", offDay.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean offDayExistsInEs = offDaySearchRepository.exists(offDay.getId());
        assertThat(offDayExistsInEs).isFalse();

        // Validate the database is empty
        List<OffDay> offDays = offDayRepository.findAll();
        assertThat(offDays).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOffDay() throws Exception {
        // Initialize the database
        offDayRepository.saveAndFlush(offDay);
        offDaySearchRepository.save(offDay);

        // Search the offDay
        restOffDayMockMvc.perform(get("/api/_search/off-days?query=id:" + offDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].offDate").value(hasItem(DEFAULT_OFF_DATE.toString())))
            .andExpect(jsonPath("$.[*].offDayType").value(hasItem(DEFAULT_OFF_DAY_TYPE.toString())));
    }
}
