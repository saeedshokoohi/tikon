package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.PriceInfo;
import com.eyeson.tikon.repository.PriceInfoRepository;
import com.eyeson.tikon.service.PriceInfoService;
import com.eyeson.tikon.repository.search.PriceInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.PriceInfoDTO;
import com.eyeson.tikon.web.rest.mapper.PriceInfoMapper;

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

import com.eyeson.tikon.domain.enumeration.PriceType;

/**
 * Test class for the PriceInfoResource REST controller.
 *
 * @see PriceInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class PriceInfoResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_FROM_VALID_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FROM_VALID_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FROM_VALID_DATE_STR = dateTimeFormatter.format(DEFAULT_FROM_VALID_DATE);

    private static final PriceType DEFAULT_PRICE_TYPE = PriceType.FIXED;
    private static final PriceType UPDATED_PRICE_TYPE = PriceType.SCHEDULE_BASED;

    @Inject
    private PriceInfoRepository priceInfoRepository;

    @Inject
    private PriceInfoMapper priceInfoMapper;

    @Inject
    private PriceInfoService priceInfoService;

    @Inject
    private PriceInfoSearchRepository priceInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPriceInfoMockMvc;

    private PriceInfo priceInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PriceInfoResource priceInfoResource = new PriceInfoResource();
        ReflectionTestUtils.setField(priceInfoResource, "priceInfoService", priceInfoService);
        ReflectionTestUtils.setField(priceInfoResource, "priceInfoMapper", priceInfoMapper);
        this.restPriceInfoMockMvc = MockMvcBuilders.standaloneSetup(priceInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        priceInfoSearchRepository.deleteAll();
        priceInfo = new PriceInfo();
        priceInfo.setFromValidDate(DEFAULT_FROM_VALID_DATE);
        priceInfo.setPriceType(DEFAULT_PRICE_TYPE);
    }

    @Test
    @Transactional
    public void createPriceInfo() throws Exception {
        int databaseSizeBeforeCreate = priceInfoRepository.findAll().size();

        // Create the PriceInfo
        PriceInfoDTO priceInfoDTO = priceInfoMapper.priceInfoToPriceInfoDTO(priceInfo);

        restPriceInfoMockMvc.perform(post("/api/price-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(priceInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the PriceInfo in the database
        List<PriceInfo> priceInfos = priceInfoRepository.findAll();
        assertThat(priceInfos).hasSize(databaseSizeBeforeCreate + 1);
        PriceInfo testPriceInfo = priceInfos.get(priceInfos.size() - 1);
        assertThat(testPriceInfo.getFromValidDate()).isEqualTo(DEFAULT_FROM_VALID_DATE);
        assertThat(testPriceInfo.getPriceType()).isEqualTo(DEFAULT_PRICE_TYPE);

        // Validate the PriceInfo in ElasticSearch
        PriceInfo priceInfoEs = priceInfoSearchRepository.findOne(testPriceInfo.getId());
        assertThat(priceInfoEs).isEqualToComparingFieldByField(testPriceInfo);
    }

    @Test
    @Transactional
    public void getAllPriceInfos() throws Exception {
        // Initialize the database
        priceInfoRepository.saveAndFlush(priceInfo);

        // Get all the priceInfos
        restPriceInfoMockMvc.perform(get("/api/price-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(priceInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].fromValidDate").value(hasItem(DEFAULT_FROM_VALID_DATE_STR)))
                .andExpect(jsonPath("$.[*].priceType").value(hasItem(DEFAULT_PRICE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPriceInfo() throws Exception {
        // Initialize the database
        priceInfoRepository.saveAndFlush(priceInfo);

        // Get the priceInfo
        restPriceInfoMockMvc.perform(get("/api/price-infos/{id}", priceInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(priceInfo.getId().intValue()))
            .andExpect(jsonPath("$.fromValidDate").value(DEFAULT_FROM_VALID_DATE_STR))
            .andExpect(jsonPath("$.priceType").value(DEFAULT_PRICE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPriceInfo() throws Exception {
        // Get the priceInfo
        restPriceInfoMockMvc.perform(get("/api/price-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePriceInfo() throws Exception {
        // Initialize the database
        priceInfoRepository.saveAndFlush(priceInfo);
        priceInfoSearchRepository.save(priceInfo);
        int databaseSizeBeforeUpdate = priceInfoRepository.findAll().size();

        // Update the priceInfo
        PriceInfo updatedPriceInfo = new PriceInfo();
        updatedPriceInfo.setId(priceInfo.getId());
        updatedPriceInfo.setFromValidDate(UPDATED_FROM_VALID_DATE);
        updatedPriceInfo.setPriceType(UPDATED_PRICE_TYPE);
        PriceInfoDTO priceInfoDTO = priceInfoMapper.priceInfoToPriceInfoDTO(updatedPriceInfo);

        restPriceInfoMockMvc.perform(put("/api/price-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(priceInfoDTO)))
                .andExpect(status().isOk());

        // Validate the PriceInfo in the database
        List<PriceInfo> priceInfos = priceInfoRepository.findAll();
        assertThat(priceInfos).hasSize(databaseSizeBeforeUpdate);
        PriceInfo testPriceInfo = priceInfos.get(priceInfos.size() - 1);
        assertThat(testPriceInfo.getFromValidDate()).isEqualTo(UPDATED_FROM_VALID_DATE);
        assertThat(testPriceInfo.getPriceType()).isEqualTo(UPDATED_PRICE_TYPE);

        // Validate the PriceInfo in ElasticSearch
        PriceInfo priceInfoEs = priceInfoSearchRepository.findOne(testPriceInfo.getId());
        assertThat(priceInfoEs).isEqualToComparingFieldByField(testPriceInfo);
    }

    @Test
    @Transactional
    public void deletePriceInfo() throws Exception {
        // Initialize the database
        priceInfoRepository.saveAndFlush(priceInfo);
        priceInfoSearchRepository.save(priceInfo);
        int databaseSizeBeforeDelete = priceInfoRepository.findAll().size();

        // Get the priceInfo
        restPriceInfoMockMvc.perform(delete("/api/price-infos/{id}", priceInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean priceInfoExistsInEs = priceInfoSearchRepository.exists(priceInfo.getId());
        assertThat(priceInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<PriceInfo> priceInfos = priceInfoRepository.findAll();
        assertThat(priceInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPriceInfo() throws Exception {
        // Initialize the database
        priceInfoRepository.saveAndFlush(priceInfo);
        priceInfoSearchRepository.save(priceInfo);

        // Search the priceInfo
        restPriceInfoMockMvc.perform(get("/api/_search/price-infos?query=id:" + priceInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromValidDate").value(hasItem(DEFAULT_FROM_VALID_DATE_STR)))
            .andExpect(jsonPath("$.[*].priceType").value(hasItem(DEFAULT_PRICE_TYPE.toString())));
    }
}
