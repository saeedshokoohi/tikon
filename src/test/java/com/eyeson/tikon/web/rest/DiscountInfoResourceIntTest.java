package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.DiscountInfo;
import com.eyeson.tikon.repository.DiscountInfoRepository;
import com.eyeson.tikon.service.DiscountInfoService;
import com.eyeson.tikon.repository.search.DiscountInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.DiscountInfoDTO;
import com.eyeson.tikon.web.rest.mapper.DiscountInfoMapper;

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

import com.eyeson.tikon.domain.enumeration.DiscountType;

/**
 * Test class for the DiscountInfoResource REST controller.
 *
 * @see DiscountInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class DiscountInfoResourceIntTest {


    private static final Double DEFAULT_FIXED_DISCOUNT = 1D;
    private static final Double UPDATED_FIXED_DISCOUNT = 2D;

    private static final DiscountType DEFAULT_DISCOUNT_TYPE = DiscountType.FIXED;
    private static final DiscountType UPDATED_DISCOUNT_TYPE = DiscountType.SCHEDULE_BASED;

    @Inject
    private DiscountInfoRepository discountInfoRepository;

    @Inject
    private DiscountInfoMapper discountInfoMapper;

    @Inject
    private DiscountInfoService discountInfoService;

    @Inject
    private DiscountInfoSearchRepository discountInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDiscountInfoMockMvc;

    private DiscountInfo discountInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiscountInfoResource discountInfoResource = new DiscountInfoResource();
        ReflectionTestUtils.setField(discountInfoResource, "discountInfoService", discountInfoService);
        ReflectionTestUtils.setField(discountInfoResource, "discountInfoMapper", discountInfoMapper);
        this.restDiscountInfoMockMvc = MockMvcBuilders.standaloneSetup(discountInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        discountInfoSearchRepository.deleteAll();
        discountInfo = new DiscountInfo();
        discountInfo.setFixedDiscount(DEFAULT_FIXED_DISCOUNT);
        discountInfo.setDiscountType(DEFAULT_DISCOUNT_TYPE);
    }

    @Test
    @Transactional
    public void createDiscountInfo() throws Exception {
        int databaseSizeBeforeCreate = discountInfoRepository.findAll().size();

        // Create the DiscountInfo
        DiscountInfoDTO discountInfoDTO = discountInfoMapper.discountInfoToDiscountInfoDTO(discountInfo);

        restDiscountInfoMockMvc.perform(post("/api/discount-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discountInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the DiscountInfo in the database
        List<DiscountInfo> discountInfos = discountInfoRepository.findAll();
        assertThat(discountInfos).hasSize(databaseSizeBeforeCreate + 1);
        DiscountInfo testDiscountInfo = discountInfos.get(discountInfos.size() - 1);
        assertThat(testDiscountInfo.getFixedDiscount()).isEqualTo(DEFAULT_FIXED_DISCOUNT);
        assertThat(testDiscountInfo.getDiscountType()).isEqualTo(DEFAULT_DISCOUNT_TYPE);

        // Validate the DiscountInfo in ElasticSearch
        DiscountInfo discountInfoEs = discountInfoSearchRepository.findOne(testDiscountInfo.getId());
        assertThat(discountInfoEs).isEqualToComparingFieldByField(testDiscountInfo);
    }

    @Test
    @Transactional
    public void getAllDiscountInfos() throws Exception {
        // Initialize the database
        discountInfoRepository.saveAndFlush(discountInfo);

        // Get all the discountInfos
        restDiscountInfoMockMvc.perform(get("/api/discount-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(discountInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].fixedDiscount").value(hasItem(DEFAULT_FIXED_DISCOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getDiscountInfo() throws Exception {
        // Initialize the database
        discountInfoRepository.saveAndFlush(discountInfo);

        // Get the discountInfo
        restDiscountInfoMockMvc.perform(get("/api/discount-infos/{id}", discountInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(discountInfo.getId().intValue()))
            .andExpect(jsonPath("$.fixedDiscount").value(DEFAULT_FIXED_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.discountType").value(DEFAULT_DISCOUNT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiscountInfo() throws Exception {
        // Get the discountInfo
        restDiscountInfoMockMvc.perform(get("/api/discount-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiscountInfo() throws Exception {
        // Initialize the database
        discountInfoRepository.saveAndFlush(discountInfo);
        discountInfoSearchRepository.save(discountInfo);
        int databaseSizeBeforeUpdate = discountInfoRepository.findAll().size();

        // Update the discountInfo
        DiscountInfo updatedDiscountInfo = new DiscountInfo();
        updatedDiscountInfo.setId(discountInfo.getId());
        updatedDiscountInfo.setFixedDiscount(UPDATED_FIXED_DISCOUNT);
        updatedDiscountInfo.setDiscountType(UPDATED_DISCOUNT_TYPE);
        DiscountInfoDTO discountInfoDTO = discountInfoMapper.discountInfoToDiscountInfoDTO(updatedDiscountInfo);

        restDiscountInfoMockMvc.perform(put("/api/discount-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(discountInfoDTO)))
                .andExpect(status().isOk());

        // Validate the DiscountInfo in the database
        List<DiscountInfo> discountInfos = discountInfoRepository.findAll();
        assertThat(discountInfos).hasSize(databaseSizeBeforeUpdate);
        DiscountInfo testDiscountInfo = discountInfos.get(discountInfos.size() - 1);
        assertThat(testDiscountInfo.getFixedDiscount()).isEqualTo(UPDATED_FIXED_DISCOUNT);
        assertThat(testDiscountInfo.getDiscountType()).isEqualTo(UPDATED_DISCOUNT_TYPE);

        // Validate the DiscountInfo in ElasticSearch
        DiscountInfo discountInfoEs = discountInfoSearchRepository.findOne(testDiscountInfo.getId());
        assertThat(discountInfoEs).isEqualToComparingFieldByField(testDiscountInfo);
    }

    @Test
    @Transactional
    public void deleteDiscountInfo() throws Exception {
        // Initialize the database
        discountInfoRepository.saveAndFlush(discountInfo);
        discountInfoSearchRepository.save(discountInfo);
        int databaseSizeBeforeDelete = discountInfoRepository.findAll().size();

        // Get the discountInfo
        restDiscountInfoMockMvc.perform(delete("/api/discount-infos/{id}", discountInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean discountInfoExistsInEs = discountInfoSearchRepository.exists(discountInfo.getId());
        assertThat(discountInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<DiscountInfo> discountInfos = discountInfoRepository.findAll();
        assertThat(discountInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDiscountInfo() throws Exception {
        // Initialize the database
        discountInfoRepository.saveAndFlush(discountInfo);
        discountInfoSearchRepository.save(discountInfo);

        // Search the discountInfo
        restDiscountInfoMockMvc.perform(get("/api/_search/discount-infos?query=id:" + discountInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(discountInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].fixedDiscount").value(hasItem(DEFAULT_FIXED_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].discountType").value(hasItem(DEFAULT_DISCOUNT_TYPE.toString())));
    }
}
