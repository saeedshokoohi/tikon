package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.PaymentLog;
import com.eyeson.tikon.repository.PaymentLogRepository;
import com.eyeson.tikon.service.PaymentLogService;
import com.eyeson.tikon.repository.search.PaymentLogSearchRepository;
import com.eyeson.tikon.web.rest.dto.PaymentLogDTO;
import com.eyeson.tikon.web.rest.mapper.PaymentLogMapper;

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

import com.eyeson.tikon.domain.enumeration.PaymentType;

/**
 * Test class for the PaymentLogResource REST controller.
 *
 * @see PaymentLogResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class PaymentLogResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TRACE_CODE = "AAAAA";
    private static final String UPDATED_TRACE_CODE = "BBBBB";

    private static final PaymentType DEFAULT_PAYMENT_TYPE = PaymentType.CASH;
    private static final PaymentType UPDATED_PAYMENT_TYPE = PaymentType.CREDIT_CARD;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    @Inject
    private PaymentLogRepository paymentLogRepository;

    @Inject
    private PaymentLogMapper paymentLogMapper;

    @Inject
    private PaymentLogService paymentLogService;

    @Inject
    private PaymentLogSearchRepository paymentLogSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPaymentLogMockMvc;

    private PaymentLog paymentLog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaymentLogResource paymentLogResource = new PaymentLogResource();
        ReflectionTestUtils.setField(paymentLogResource, "paymentLogService", paymentLogService);
        ReflectionTestUtils.setField(paymentLogResource, "paymentLogMapper", paymentLogMapper);
        this.restPaymentLogMockMvc = MockMvcBuilders.standaloneSetup(paymentLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        paymentLogSearchRepository.deleteAll();
        paymentLog = new PaymentLog();
        paymentLog.setTraceCode(DEFAULT_TRACE_CODE);
        paymentLog.setPaymentType(DEFAULT_PAYMENT_TYPE);
        paymentLog.setCreateDate(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createPaymentLog() throws Exception {
        int databaseSizeBeforeCreate = paymentLogRepository.findAll().size();

        // Create the PaymentLog
        PaymentLogDTO paymentLogDTO = paymentLogMapper.paymentLogToPaymentLogDTO(paymentLog);

        restPaymentLogMockMvc.perform(post("/api/payment-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paymentLogDTO)))
                .andExpect(status().isCreated());

        // Validate the PaymentLog in the database
        List<PaymentLog> paymentLogs = paymentLogRepository.findAll();
        assertThat(paymentLogs).hasSize(databaseSizeBeforeCreate + 1);
        PaymentLog testPaymentLog = paymentLogs.get(paymentLogs.size() - 1);
        assertThat(testPaymentLog.getTraceCode()).isEqualTo(DEFAULT_TRACE_CODE);
        assertThat(testPaymentLog.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testPaymentLog.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);

        // Validate the PaymentLog in ElasticSearch
        PaymentLog paymentLogEs = paymentLogSearchRepository.findOne(testPaymentLog.getId());
        assertThat(paymentLogEs).isEqualToComparingFieldByField(testPaymentLog);
    }

    @Test
    @Transactional
    public void getAllPaymentLogs() throws Exception {
        // Initialize the database
        paymentLogRepository.saveAndFlush(paymentLog);

        // Get all the paymentLogs
        restPaymentLogMockMvc.perform(get("/api/payment-logs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(paymentLog.getId().intValue())))
                .andExpect(jsonPath("$.[*].traceCode").value(hasItem(DEFAULT_TRACE_CODE.toString())))
                .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)));
    }

    @Test
    @Transactional
    public void getPaymentLog() throws Exception {
        // Initialize the database
        paymentLogRepository.saveAndFlush(paymentLog);

        // Get the paymentLog
        restPaymentLogMockMvc.perform(get("/api/payment-logs/{id}", paymentLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(paymentLog.getId().intValue()))
            .andExpect(jsonPath("$.traceCode").value(DEFAULT_TRACE_CODE.toString()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentLog() throws Exception {
        // Get the paymentLog
        restPaymentLogMockMvc.perform(get("/api/payment-logs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentLog() throws Exception {
        // Initialize the database
        paymentLogRepository.saveAndFlush(paymentLog);
        paymentLogSearchRepository.save(paymentLog);
        int databaseSizeBeforeUpdate = paymentLogRepository.findAll().size();

        // Update the paymentLog
        PaymentLog updatedPaymentLog = new PaymentLog();
        updatedPaymentLog.setId(paymentLog.getId());
        updatedPaymentLog.setTraceCode(UPDATED_TRACE_CODE);
        updatedPaymentLog.setPaymentType(UPDATED_PAYMENT_TYPE);
        updatedPaymentLog.setCreateDate(UPDATED_CREATE_DATE);
        PaymentLogDTO paymentLogDTO = paymentLogMapper.paymentLogToPaymentLogDTO(updatedPaymentLog);

        restPaymentLogMockMvc.perform(put("/api/payment-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paymentLogDTO)))
                .andExpect(status().isOk());

        // Validate the PaymentLog in the database
        List<PaymentLog> paymentLogs = paymentLogRepository.findAll();
        assertThat(paymentLogs).hasSize(databaseSizeBeforeUpdate);
        PaymentLog testPaymentLog = paymentLogs.get(paymentLogs.size() - 1);
        assertThat(testPaymentLog.getTraceCode()).isEqualTo(UPDATED_TRACE_CODE);
        assertThat(testPaymentLog.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPaymentLog.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);

        // Validate the PaymentLog in ElasticSearch
        PaymentLog paymentLogEs = paymentLogSearchRepository.findOne(testPaymentLog.getId());
        assertThat(paymentLogEs).isEqualToComparingFieldByField(testPaymentLog);
    }

    @Test
    @Transactional
    public void deletePaymentLog() throws Exception {
        // Initialize the database
        paymentLogRepository.saveAndFlush(paymentLog);
        paymentLogSearchRepository.save(paymentLog);
        int databaseSizeBeforeDelete = paymentLogRepository.findAll().size();

        // Get the paymentLog
        restPaymentLogMockMvc.perform(delete("/api/payment-logs/{id}", paymentLog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean paymentLogExistsInEs = paymentLogSearchRepository.exists(paymentLog.getId());
        assertThat(paymentLogExistsInEs).isFalse();

        // Validate the database is empty
        List<PaymentLog> paymentLogs = paymentLogRepository.findAll();
        assertThat(paymentLogs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPaymentLog() throws Exception {
        // Initialize the database
        paymentLogRepository.saveAndFlush(paymentLog);
        paymentLogSearchRepository.save(paymentLog);

        // Search the paymentLog
        restPaymentLogMockMvc.perform(get("/api/_search/payment-logs?query=id:" + paymentLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].traceCode").value(hasItem(DEFAULT_TRACE_CODE.toString())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)));
    }
}
