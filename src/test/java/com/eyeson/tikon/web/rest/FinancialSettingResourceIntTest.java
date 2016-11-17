package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.FinancialSetting;
import com.eyeson.tikon.repository.FinancialSettingRepository;
import com.eyeson.tikon.service.FinancialSettingService;
import com.eyeson.tikon.repository.search.FinancialSettingSearchRepository;
import com.eyeson.tikon.web.rest.dto.FinancialSettingDTO;
import com.eyeson.tikon.web.rest.mapper.FinancialSettingMapper;

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

import com.eyeson.tikon.domain.enumeration.Currency;

/**
 * Test class for the FinancialSettingResource REST controller.
 *
 * @see FinancialSettingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class FinancialSettingResourceIntTest {


    private static final Double DEFAULT_TAX_PERCENTAGE = 1D;
    private static final Double UPDATED_TAX_PERCENTAGE = 2D;

    private static final Boolean DEFAULT_HAS_DISCOUNT = false;
    private static final Boolean UPDATED_HAS_DISCOUNT = true;

    private static final Currency DEFAULT_CURRENCY = Currency.RIAL;
    private static final Currency UPDATED_CURRENCY = Currency.RIAL;

    @Inject
    private FinancialSettingRepository financialSettingRepository;

    @Inject
    private FinancialSettingMapper financialSettingMapper;

    @Inject
    private FinancialSettingService financialSettingService;

    @Inject
    private FinancialSettingSearchRepository financialSettingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFinancialSettingMockMvc;

    private FinancialSetting financialSetting;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FinancialSettingResource financialSettingResource = new FinancialSettingResource();
        ReflectionTestUtils.setField(financialSettingResource, "financialSettingService", financialSettingService);
        ReflectionTestUtils.setField(financialSettingResource, "financialSettingMapper", financialSettingMapper);
        this.restFinancialSettingMockMvc = MockMvcBuilders.standaloneSetup(financialSettingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        financialSettingSearchRepository.deleteAll();
        financialSetting = new FinancialSetting();
        financialSetting.setTaxPercentage(DEFAULT_TAX_PERCENTAGE);
        financialSetting.setHasDiscount(DEFAULT_HAS_DISCOUNT);
        financialSetting.setCurrency(DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    public void createFinancialSetting() throws Exception {
        int databaseSizeBeforeCreate = financialSettingRepository.findAll().size();

        // Create the FinancialSetting
        FinancialSettingDTO financialSettingDTO = financialSettingMapper.financialSettingToFinancialSettingDTO(financialSetting);

        restFinancialSettingMockMvc.perform(post("/api/financial-settings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(financialSettingDTO)))
                .andExpect(status().isCreated());

        // Validate the FinancialSetting in the database
        List<FinancialSetting> financialSettings = financialSettingRepository.findAll();
        assertThat(financialSettings).hasSize(databaseSizeBeforeCreate + 1);
        FinancialSetting testFinancialSetting = financialSettings.get(financialSettings.size() - 1);
        assertThat(testFinancialSetting.getTaxPercentage()).isEqualTo(DEFAULT_TAX_PERCENTAGE);
        assertThat(testFinancialSetting.isHasDiscount()).isEqualTo(DEFAULT_HAS_DISCOUNT);
        assertThat(testFinancialSetting.getCurrency()).isEqualTo(DEFAULT_CURRENCY);

        // Validate the FinancialSetting in ElasticSearch
        FinancialSetting financialSettingEs = financialSettingSearchRepository.findOne(testFinancialSetting.getId());
        assertThat(financialSettingEs).isEqualToComparingFieldByField(testFinancialSetting);
    }

    @Test
    @Transactional
    public void getAllFinancialSettings() throws Exception {
        // Initialize the database
        financialSettingRepository.saveAndFlush(financialSetting);

        // Get all the financialSettings
        restFinancialSettingMockMvc.perform(get("/api/financial-settings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(financialSetting.getId().intValue())))
                .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
                .andExpect(jsonPath("$.[*].hasDiscount").value(hasItem(DEFAULT_HAS_DISCOUNT.booleanValue())))
                .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }

    @Test
    @Transactional
    public void getFinancialSetting() throws Exception {
        // Initialize the database
        financialSettingRepository.saveAndFlush(financialSetting);

        // Get the financialSetting
        restFinancialSettingMockMvc.perform(get("/api/financial-settings/{id}", financialSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(financialSetting.getId().intValue()))
            .andExpect(jsonPath("$.taxPercentage").value(DEFAULT_TAX_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.hasDiscount").value(DEFAULT_HAS_DISCOUNT.booleanValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFinancialSetting() throws Exception {
        // Get the financialSetting
        restFinancialSettingMockMvc.perform(get("/api/financial-settings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFinancialSetting() throws Exception {
        // Initialize the database
        financialSettingRepository.saveAndFlush(financialSetting);
        financialSettingSearchRepository.save(financialSetting);
        int databaseSizeBeforeUpdate = financialSettingRepository.findAll().size();

        // Update the financialSetting
        FinancialSetting updatedFinancialSetting = new FinancialSetting();
        updatedFinancialSetting.setId(financialSetting.getId());
        updatedFinancialSetting.setTaxPercentage(UPDATED_TAX_PERCENTAGE);
        updatedFinancialSetting.setHasDiscount(UPDATED_HAS_DISCOUNT);
        updatedFinancialSetting.setCurrency(UPDATED_CURRENCY);
        FinancialSettingDTO financialSettingDTO = financialSettingMapper.financialSettingToFinancialSettingDTO(updatedFinancialSetting);

        restFinancialSettingMockMvc.perform(put("/api/financial-settings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(financialSettingDTO)))
                .andExpect(status().isOk());

        // Validate the FinancialSetting in the database
        List<FinancialSetting> financialSettings = financialSettingRepository.findAll();
        assertThat(financialSettings).hasSize(databaseSizeBeforeUpdate);
        FinancialSetting testFinancialSetting = financialSettings.get(financialSettings.size() - 1);
        assertThat(testFinancialSetting.getTaxPercentage()).isEqualTo(UPDATED_TAX_PERCENTAGE);
        assertThat(testFinancialSetting.isHasDiscount()).isEqualTo(UPDATED_HAS_DISCOUNT);
        assertThat(testFinancialSetting.getCurrency()).isEqualTo(UPDATED_CURRENCY);

        // Validate the FinancialSetting in ElasticSearch
        FinancialSetting financialSettingEs = financialSettingSearchRepository.findOne(testFinancialSetting.getId());
        assertThat(financialSettingEs).isEqualToComparingFieldByField(testFinancialSetting);
    }

    @Test
    @Transactional
    public void deleteFinancialSetting() throws Exception {
        // Initialize the database
        financialSettingRepository.saveAndFlush(financialSetting);
        financialSettingSearchRepository.save(financialSetting);
        int databaseSizeBeforeDelete = financialSettingRepository.findAll().size();

        // Get the financialSetting
        restFinancialSettingMockMvc.perform(delete("/api/financial-settings/{id}", financialSetting.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean financialSettingExistsInEs = financialSettingSearchRepository.exists(financialSetting.getId());
        assertThat(financialSettingExistsInEs).isFalse();

        // Validate the database is empty
        List<FinancialSetting> financialSettings = financialSettingRepository.findAll();
        assertThat(financialSettings).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFinancialSetting() throws Exception {
        // Initialize the database
        financialSettingRepository.saveAndFlush(financialSetting);
        financialSettingSearchRepository.save(financialSetting);

        // Search the financialSetting
        restFinancialSettingMockMvc.perform(get("/api/_search/financial-settings?query=id:" + financialSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].hasDiscount").value(hasItem(DEFAULT_HAS_DISCOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())));
    }
}
