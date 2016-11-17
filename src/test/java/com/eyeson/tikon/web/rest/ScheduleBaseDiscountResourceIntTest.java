package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ScheduleBaseDiscount;
import com.eyeson.tikon.repository.ScheduleBaseDiscountRepository;
import com.eyeson.tikon.service.ScheduleBaseDiscountService;
import com.eyeson.tikon.repository.search.ScheduleBaseDiscountSearchRepository;
import com.eyeson.tikon.web.rest.dto.ScheduleBaseDiscountDTO;
import com.eyeson.tikon.web.rest.mapper.ScheduleBaseDiscountMapper;

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
 * Test class for the ScheduleBaseDiscountResource REST controller.
 *
 * @see ScheduleBaseDiscountResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ScheduleBaseDiscountResourceIntTest {


    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Inject
    private ScheduleBaseDiscountRepository scheduleBaseDiscountRepository;

    @Inject
    private ScheduleBaseDiscountMapper scheduleBaseDiscountMapper;

    @Inject
    private ScheduleBaseDiscountService scheduleBaseDiscountService;

    @Inject
    private ScheduleBaseDiscountSearchRepository scheduleBaseDiscountSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restScheduleBaseDiscountMockMvc;

    private ScheduleBaseDiscount scheduleBaseDiscount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScheduleBaseDiscountResource scheduleBaseDiscountResource = new ScheduleBaseDiscountResource();
        ReflectionTestUtils.setField(scheduleBaseDiscountResource, "scheduleBaseDiscountService", scheduleBaseDiscountService);
        ReflectionTestUtils.setField(scheduleBaseDiscountResource, "scheduleBaseDiscountMapper", scheduleBaseDiscountMapper);
        this.restScheduleBaseDiscountMockMvc = MockMvcBuilders.standaloneSetup(scheduleBaseDiscountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        scheduleBaseDiscountSearchRepository.deleteAll();
        scheduleBaseDiscount = new ScheduleBaseDiscount();
        scheduleBaseDiscount.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createScheduleBaseDiscount() throws Exception {
        int databaseSizeBeforeCreate = scheduleBaseDiscountRepository.findAll().size();

        // Create the ScheduleBaseDiscount
        ScheduleBaseDiscountDTO scheduleBaseDiscountDTO = scheduleBaseDiscountMapper.scheduleBaseDiscountToScheduleBaseDiscountDTO(scheduleBaseDiscount);

        restScheduleBaseDiscountMockMvc.perform(post("/api/schedule-base-discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scheduleBaseDiscountDTO)))
                .andExpect(status().isCreated());

        // Validate the ScheduleBaseDiscount in the database
        List<ScheduleBaseDiscount> scheduleBaseDiscounts = scheduleBaseDiscountRepository.findAll();
        assertThat(scheduleBaseDiscounts).hasSize(databaseSizeBeforeCreate + 1);
        ScheduleBaseDiscount testScheduleBaseDiscount = scheduleBaseDiscounts.get(scheduleBaseDiscounts.size() - 1);
        assertThat(testScheduleBaseDiscount.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the ScheduleBaseDiscount in ElasticSearch
        ScheduleBaseDiscount scheduleBaseDiscountEs = scheduleBaseDiscountSearchRepository.findOne(testScheduleBaseDiscount.getId());
        assertThat(scheduleBaseDiscountEs).isEqualToComparingFieldByField(testScheduleBaseDiscount);
    }

    @Test
    @Transactional
    public void getAllScheduleBaseDiscounts() throws Exception {
        // Initialize the database
        scheduleBaseDiscountRepository.saveAndFlush(scheduleBaseDiscount);

        // Get all the scheduleBaseDiscounts
        restScheduleBaseDiscountMockMvc.perform(get("/api/schedule-base-discounts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleBaseDiscount.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getScheduleBaseDiscount() throws Exception {
        // Initialize the database
        scheduleBaseDiscountRepository.saveAndFlush(scheduleBaseDiscount);

        // Get the scheduleBaseDiscount
        restScheduleBaseDiscountMockMvc.perform(get("/api/schedule-base-discounts/{id}", scheduleBaseDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(scheduleBaseDiscount.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingScheduleBaseDiscount() throws Exception {
        // Get the scheduleBaseDiscount
        restScheduleBaseDiscountMockMvc.perform(get("/api/schedule-base-discounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScheduleBaseDiscount() throws Exception {
        // Initialize the database
        scheduleBaseDiscountRepository.saveAndFlush(scheduleBaseDiscount);
        scheduleBaseDiscountSearchRepository.save(scheduleBaseDiscount);
        int databaseSizeBeforeUpdate = scheduleBaseDiscountRepository.findAll().size();

        // Update the scheduleBaseDiscount
        ScheduleBaseDiscount updatedScheduleBaseDiscount = new ScheduleBaseDiscount();
        updatedScheduleBaseDiscount.setId(scheduleBaseDiscount.getId());
        updatedScheduleBaseDiscount.setAmount(UPDATED_AMOUNT);
        ScheduleBaseDiscountDTO scheduleBaseDiscountDTO = scheduleBaseDiscountMapper.scheduleBaseDiscountToScheduleBaseDiscountDTO(updatedScheduleBaseDiscount);

        restScheduleBaseDiscountMockMvc.perform(put("/api/schedule-base-discounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scheduleBaseDiscountDTO)))
                .andExpect(status().isOk());

        // Validate the ScheduleBaseDiscount in the database
        List<ScheduleBaseDiscount> scheduleBaseDiscounts = scheduleBaseDiscountRepository.findAll();
        assertThat(scheduleBaseDiscounts).hasSize(databaseSizeBeforeUpdate);
        ScheduleBaseDiscount testScheduleBaseDiscount = scheduleBaseDiscounts.get(scheduleBaseDiscounts.size() - 1);
        assertThat(testScheduleBaseDiscount.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the ScheduleBaseDiscount in ElasticSearch
        ScheduleBaseDiscount scheduleBaseDiscountEs = scheduleBaseDiscountSearchRepository.findOne(testScheduleBaseDiscount.getId());
        assertThat(scheduleBaseDiscountEs).isEqualToComparingFieldByField(testScheduleBaseDiscount);
    }

    @Test
    @Transactional
    public void deleteScheduleBaseDiscount() throws Exception {
        // Initialize the database
        scheduleBaseDiscountRepository.saveAndFlush(scheduleBaseDiscount);
        scheduleBaseDiscountSearchRepository.save(scheduleBaseDiscount);
        int databaseSizeBeforeDelete = scheduleBaseDiscountRepository.findAll().size();

        // Get the scheduleBaseDiscount
        restScheduleBaseDiscountMockMvc.perform(delete("/api/schedule-base-discounts/{id}", scheduleBaseDiscount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean scheduleBaseDiscountExistsInEs = scheduleBaseDiscountSearchRepository.exists(scheduleBaseDiscount.getId());
        assertThat(scheduleBaseDiscountExistsInEs).isFalse();

        // Validate the database is empty
        List<ScheduleBaseDiscount> scheduleBaseDiscounts = scheduleBaseDiscountRepository.findAll();
        assertThat(scheduleBaseDiscounts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchScheduleBaseDiscount() throws Exception {
        // Initialize the database
        scheduleBaseDiscountRepository.saveAndFlush(scheduleBaseDiscount);
        scheduleBaseDiscountSearchRepository.save(scheduleBaseDiscount);

        // Search the scheduleBaseDiscount
        restScheduleBaseDiscountMockMvc.perform(get("/api/_search/schedule-base-discounts?query=id:" + scheduleBaseDiscount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleBaseDiscount.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
}
