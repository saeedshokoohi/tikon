package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.CustomerRank;
import com.eyeson.tikon.repository.CustomerRankRepository;
import com.eyeson.tikon.service.CustomerRankService;
import com.eyeson.tikon.repository.search.CustomerRankSearchRepository;
import com.eyeson.tikon.web.rest.dto.CustomerRankDTO;
import com.eyeson.tikon.web.rest.mapper.CustomerRankMapper;

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
 * Test class for the CustomerRankResource REST controller.
 *
 * @see CustomerRankResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerRankResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final Double DEFAULT_RANK_VALUE = 1D;
    private static final Double UPDATED_RANK_VALUE = 2D;

    @Inject
    private CustomerRankRepository customerRankRepository;

    @Inject
    private CustomerRankMapper customerRankMapper;

    @Inject
    private CustomerRankService customerRankService;

    @Inject
    private CustomerRankSearchRepository customerRankSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerRankMockMvc;

    private CustomerRank customerRank;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerRankResource customerRankResource = new CustomerRankResource();
        ReflectionTestUtils.setField(customerRankResource, "customerRankService", customerRankService);
        ReflectionTestUtils.setField(customerRankResource, "customerRankMapper", customerRankMapper);
        this.restCustomerRankMockMvc = MockMvcBuilders.standaloneSetup(customerRankResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerRankSearchRepository.deleteAll();
        customerRank = new CustomerRank();
        customerRank.setCreateDate(DEFAULT_CREATE_DATE);
        customerRank.setRankValue(DEFAULT_RANK_VALUE);
    }

    @Test
    @Transactional
    public void createCustomerRank() throws Exception {
        int databaseSizeBeforeCreate = customerRankRepository.findAll().size();

        // Create the CustomerRank
        CustomerRankDTO customerRankDTO = customerRankMapper.customerRankToCustomerRankDTO(customerRank);

        restCustomerRankMockMvc.perform(post("/api/customer-ranks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerRankDTO)))
                .andExpect(status().isCreated());

        // Validate the CustomerRank in the database
        List<CustomerRank> customerRanks = customerRankRepository.findAll();
        assertThat(customerRanks).hasSize(databaseSizeBeforeCreate + 1);
        CustomerRank testCustomerRank = customerRanks.get(customerRanks.size() - 1);
        assertThat(testCustomerRank.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCustomerRank.getRankValue()).isEqualTo(DEFAULT_RANK_VALUE);

        // Validate the CustomerRank in ElasticSearch
        CustomerRank customerRankEs = customerRankSearchRepository.findOne(testCustomerRank.getId());
        assertThat(customerRankEs).isEqualToComparingFieldByField(testCustomerRank);
    }

    @Test
    @Transactional
    public void getAllCustomerRanks() throws Exception {
        // Initialize the database
        customerRankRepository.saveAndFlush(customerRank);

        // Get all the customerRanks
        restCustomerRankMockMvc.perform(get("/api/customer-ranks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerRank.getId().intValue())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].rankValue").value(hasItem(DEFAULT_RANK_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    public void getCustomerRank() throws Exception {
        // Initialize the database
        customerRankRepository.saveAndFlush(customerRank);

        // Get the customerRank
        restCustomerRankMockMvc.perform(get("/api/customer-ranks/{id}", customerRank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerRank.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.rankValue").value(DEFAULT_RANK_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerRank() throws Exception {
        // Get the customerRank
        restCustomerRankMockMvc.perform(get("/api/customer-ranks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerRank() throws Exception {
        // Initialize the database
        customerRankRepository.saveAndFlush(customerRank);
        customerRankSearchRepository.save(customerRank);
        int databaseSizeBeforeUpdate = customerRankRepository.findAll().size();

        // Update the customerRank
        CustomerRank updatedCustomerRank = new CustomerRank();
        updatedCustomerRank.setId(customerRank.getId());
        updatedCustomerRank.setCreateDate(UPDATED_CREATE_DATE);
        updatedCustomerRank.setRankValue(UPDATED_RANK_VALUE);
        CustomerRankDTO customerRankDTO = customerRankMapper.customerRankToCustomerRankDTO(updatedCustomerRank);

        restCustomerRankMockMvc.perform(put("/api/customer-ranks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerRankDTO)))
                .andExpect(status().isOk());

        // Validate the CustomerRank in the database
        List<CustomerRank> customerRanks = customerRankRepository.findAll();
        assertThat(customerRanks).hasSize(databaseSizeBeforeUpdate);
        CustomerRank testCustomerRank = customerRanks.get(customerRanks.size() - 1);
        assertThat(testCustomerRank.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCustomerRank.getRankValue()).isEqualTo(UPDATED_RANK_VALUE);

        // Validate the CustomerRank in ElasticSearch
        CustomerRank customerRankEs = customerRankSearchRepository.findOne(testCustomerRank.getId());
        assertThat(customerRankEs).isEqualToComparingFieldByField(testCustomerRank);
    }

    @Test
    @Transactional
    public void deleteCustomerRank() throws Exception {
        // Initialize the database
        customerRankRepository.saveAndFlush(customerRank);
        customerRankSearchRepository.save(customerRank);
        int databaseSizeBeforeDelete = customerRankRepository.findAll().size();

        // Get the customerRank
        restCustomerRankMockMvc.perform(delete("/api/customer-ranks/{id}", customerRank.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean customerRankExistsInEs = customerRankSearchRepository.exists(customerRank.getId());
        assertThat(customerRankExistsInEs).isFalse();

        // Validate the database is empty
        List<CustomerRank> customerRanks = customerRankRepository.findAll();
        assertThat(customerRanks).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomerRank() throws Exception {
        // Initialize the database
        customerRankRepository.saveAndFlush(customerRank);
        customerRankSearchRepository.save(customerRank);

        // Search the customerRank
        restCustomerRankMockMvc.perform(get("/api/_search/customer-ranks?query=id:" + customerRank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerRank.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
            .andExpect(jsonPath("$.[*].rankValue").value(hasItem(DEFAULT_RANK_VALUE.doubleValue())));
    }
}
