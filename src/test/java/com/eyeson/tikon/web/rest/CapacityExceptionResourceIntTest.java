package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.CapacityException;
import com.eyeson.tikon.repository.CapacityExceptionRepository;
import com.eyeson.tikon.service.CapacityExceptionService;
import com.eyeson.tikon.repository.search.CapacityExceptionSearchRepository;
import com.eyeson.tikon.web.rest.dto.CapacityExceptionDTO;
import com.eyeson.tikon.web.rest.mapper.CapacityExceptionMapper;

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
 * Test class for the CapacityExceptionResource REST controller.
 *
 * @see CapacityExceptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class CapacityExceptionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_EXCEPTION_TYPE = 1;
    private static final Integer UPDATED_EXCEPTION_TYPE = 2;

    private static final ZonedDateTime DEFAULT_RESERVE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_RESERVE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_RESERVE_TIME_STR = dateTimeFormatter.format(DEFAULT_RESERVE_TIME);

    private static final Integer DEFAULT_NEW_QTY = 1;
    private static final Integer UPDATED_NEW_QTY = 2;

    @Inject
    private CapacityExceptionRepository capacityExceptionRepository;

    @Inject
    private CapacityExceptionMapper capacityExceptionMapper;

    @Inject
    private CapacityExceptionService capacityExceptionService;

    @Inject
    private CapacityExceptionSearchRepository capacityExceptionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCapacityExceptionMockMvc;

    private CapacityException capacityException;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CapacityExceptionResource capacityExceptionResource = new CapacityExceptionResource();
        ReflectionTestUtils.setField(capacityExceptionResource, "capacityExceptionService", capacityExceptionService);
        ReflectionTestUtils.setField(capacityExceptionResource, "capacityExceptionMapper", capacityExceptionMapper);
        this.restCapacityExceptionMockMvc = MockMvcBuilders.standaloneSetup(capacityExceptionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        capacityExceptionSearchRepository.deleteAll();
        capacityException = new CapacityException();
        capacityException.setExceptionType(DEFAULT_EXCEPTION_TYPE);
        capacityException.setReserveTime(DEFAULT_RESERVE_TIME);
        capacityException.setNewQty(DEFAULT_NEW_QTY);
    }

    @Test
    @Transactional
    public void createCapacityException() throws Exception {
        int databaseSizeBeforeCreate = capacityExceptionRepository.findAll().size();

        // Create the CapacityException
        CapacityExceptionDTO capacityExceptionDTO = capacityExceptionMapper.capacityExceptionToCapacityExceptionDTO(capacityException);

        restCapacityExceptionMockMvc.perform(post("/api/capacity-exceptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(capacityExceptionDTO)))
                .andExpect(status().isCreated());

        // Validate the CapacityException in the database
        List<CapacityException> capacityExceptions = capacityExceptionRepository.findAll();
        assertThat(capacityExceptions).hasSize(databaseSizeBeforeCreate + 1);
        CapacityException testCapacityException = capacityExceptions.get(capacityExceptions.size() - 1);
        assertThat(testCapacityException.getExceptionType()).isEqualTo(DEFAULT_EXCEPTION_TYPE);
        assertThat(testCapacityException.getReserveTime()).isEqualTo(DEFAULT_RESERVE_TIME);
        assertThat(testCapacityException.getNewQty()).isEqualTo(DEFAULT_NEW_QTY);

        // Validate the CapacityException in ElasticSearch
        CapacityException capacityExceptionEs = capacityExceptionSearchRepository.findOne(testCapacityException.getId());
        assertThat(capacityExceptionEs).isEqualToComparingFieldByField(testCapacityException);
    }

    @Test
    @Transactional
    public void getAllCapacityExceptions() throws Exception {
        // Initialize the database
        capacityExceptionRepository.saveAndFlush(capacityException);

        // Get all the capacityExceptions
        restCapacityExceptionMockMvc.perform(get("/api/capacity-exceptions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(capacityException.getId().intValue())))
                .andExpect(jsonPath("$.[*].exceptionType").value(hasItem(DEFAULT_EXCEPTION_TYPE)))
                .andExpect(jsonPath("$.[*].reserveTime").value(hasItem(DEFAULT_RESERVE_TIME_STR)))
                .andExpect(jsonPath("$.[*].newQty").value(hasItem(DEFAULT_NEW_QTY)));
    }

    @Test
    @Transactional
    public void getCapacityException() throws Exception {
        // Initialize the database
        capacityExceptionRepository.saveAndFlush(capacityException);

        // Get the capacityException
        restCapacityExceptionMockMvc.perform(get("/api/capacity-exceptions/{id}", capacityException.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(capacityException.getId().intValue()))
            .andExpect(jsonPath("$.exceptionType").value(DEFAULT_EXCEPTION_TYPE))
            .andExpect(jsonPath("$.reserveTime").value(DEFAULT_RESERVE_TIME_STR))
            .andExpect(jsonPath("$.newQty").value(DEFAULT_NEW_QTY));
    }

    @Test
    @Transactional
    public void getNonExistingCapacityException() throws Exception {
        // Get the capacityException
        restCapacityExceptionMockMvc.perform(get("/api/capacity-exceptions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCapacityException() throws Exception {
        // Initialize the database
        capacityExceptionRepository.saveAndFlush(capacityException);
        capacityExceptionSearchRepository.save(capacityException);
        int databaseSizeBeforeUpdate = capacityExceptionRepository.findAll().size();

        // Update the capacityException
        CapacityException updatedCapacityException = new CapacityException();
        updatedCapacityException.setId(capacityException.getId());
        updatedCapacityException.setExceptionType(UPDATED_EXCEPTION_TYPE);
        updatedCapacityException.setReserveTime(UPDATED_RESERVE_TIME);
        updatedCapacityException.setNewQty(UPDATED_NEW_QTY);
        CapacityExceptionDTO capacityExceptionDTO = capacityExceptionMapper.capacityExceptionToCapacityExceptionDTO(updatedCapacityException);

        restCapacityExceptionMockMvc.perform(put("/api/capacity-exceptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(capacityExceptionDTO)))
                .andExpect(status().isOk());

        // Validate the CapacityException in the database
        List<CapacityException> capacityExceptions = capacityExceptionRepository.findAll();
        assertThat(capacityExceptions).hasSize(databaseSizeBeforeUpdate);
        CapacityException testCapacityException = capacityExceptions.get(capacityExceptions.size() - 1);
        assertThat(testCapacityException.getExceptionType()).isEqualTo(UPDATED_EXCEPTION_TYPE);
        assertThat(testCapacityException.getReserveTime()).isEqualTo(UPDATED_RESERVE_TIME);
        assertThat(testCapacityException.getNewQty()).isEqualTo(UPDATED_NEW_QTY);

        // Validate the CapacityException in ElasticSearch
        CapacityException capacityExceptionEs = capacityExceptionSearchRepository.findOne(testCapacityException.getId());
        assertThat(capacityExceptionEs).isEqualToComparingFieldByField(testCapacityException);
    }

    @Test
    @Transactional
    public void deleteCapacityException() throws Exception {
        // Initialize the database
        capacityExceptionRepository.saveAndFlush(capacityException);
        capacityExceptionSearchRepository.save(capacityException);
        int databaseSizeBeforeDelete = capacityExceptionRepository.findAll().size();

        // Get the capacityException
        restCapacityExceptionMockMvc.perform(delete("/api/capacity-exceptions/{id}", capacityException.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean capacityExceptionExistsInEs = capacityExceptionSearchRepository.exists(capacityException.getId());
        assertThat(capacityExceptionExistsInEs).isFalse();

        // Validate the database is empty
        List<CapacityException> capacityExceptions = capacityExceptionRepository.findAll();
        assertThat(capacityExceptions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCapacityException() throws Exception {
        // Initialize the database
        capacityExceptionRepository.saveAndFlush(capacityException);
        capacityExceptionSearchRepository.save(capacityException);

        // Search the capacityException
        restCapacityExceptionMockMvc.perform(get("/api/_search/capacity-exceptions?query=id:" + capacityException.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capacityException.getId().intValue())))
            .andExpect(jsonPath("$.[*].exceptionType").value(hasItem(DEFAULT_EXCEPTION_TYPE)))
            .andExpect(jsonPath("$.[*].reserveTime").value(hasItem(DEFAULT_RESERVE_TIME_STR)))
            .andExpect(jsonPath("$.[*].newQty").value(hasItem(DEFAULT_NEW_QTY)));
    }
}
