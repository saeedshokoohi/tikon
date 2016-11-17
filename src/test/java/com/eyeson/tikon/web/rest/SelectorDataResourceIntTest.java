package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.SelectorData;
import com.eyeson.tikon.repository.SelectorDataRepository;
import com.eyeson.tikon.service.SelectorDataService;
import com.eyeson.tikon.repository.search.SelectorDataSearchRepository;
import com.eyeson.tikon.web.rest.dto.SelectorDataDTO;
import com.eyeson.tikon.web.rest.mapper.SelectorDataMapper;

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
 * Test class for the SelectorDataResource REST controller.
 *
 * @see SelectorDataResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class SelectorDataResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAA";
    private static final String UPDATED_KEY = "BBBBB";
    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    private static final Integer DEFAULT_ORDER_NO = 1;
    private static final Integer UPDATED_ORDER_NO = 2;

    @Inject
    private SelectorDataRepository selectorDataRepository;

    @Inject
    private SelectorDataMapper selectorDataMapper;

    @Inject
    private SelectorDataService selectorDataService;

    @Inject
    private SelectorDataSearchRepository selectorDataSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSelectorDataMockMvc;

    private SelectorData selectorData;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SelectorDataResource selectorDataResource = new SelectorDataResource();
        ReflectionTestUtils.setField(selectorDataResource, "selectorDataService", selectorDataService);
        ReflectionTestUtils.setField(selectorDataResource, "selectorDataMapper", selectorDataMapper);
        this.restSelectorDataMockMvc = MockMvcBuilders.standaloneSetup(selectorDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        selectorDataSearchRepository.deleteAll();
        selectorData = new SelectorData();
        selectorData.setKey(DEFAULT_KEY);
        selectorData.setText(DEFAULT_TEXT);
        selectorData.setOrderNo(DEFAULT_ORDER_NO);
    }

    @Test
    @Transactional
    public void createSelectorData() throws Exception {
        int databaseSizeBeforeCreate = selectorDataRepository.findAll().size();

        // Create the SelectorData
        SelectorDataDTO selectorDataDTO = selectorDataMapper.selectorDataToSelectorDataDTO(selectorData);

        restSelectorDataMockMvc.perform(post("/api/selector-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(selectorDataDTO)))
                .andExpect(status().isCreated());

        // Validate the SelectorData in the database
        List<SelectorData> selectorData = selectorDataRepository.findAll();
        assertThat(selectorData).hasSize(databaseSizeBeforeCreate + 1);
        SelectorData testSelectorData = selectorData.get(selectorData.size() - 1);
        assertThat(testSelectorData.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testSelectorData.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testSelectorData.getOrderNo()).isEqualTo(DEFAULT_ORDER_NO);

        // Validate the SelectorData in ElasticSearch
        SelectorData selectorDataEs = selectorDataSearchRepository.findOne(testSelectorData.getId());
        assertThat(selectorDataEs).isEqualToComparingFieldByField(testSelectorData);
    }

    @Test
    @Transactional
    public void getAllSelectorData() throws Exception {
        // Initialize the database
        selectorDataRepository.saveAndFlush(selectorData);

        // Get all the selectorData
        restSelectorDataMockMvc.perform(get("/api/selector-data?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(selectorData.getId().intValue())))
                .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO)));
    }

    @Test
    @Transactional
    public void getSelectorData() throws Exception {
        // Initialize the database
        selectorDataRepository.saveAndFlush(selectorData);

        // Get the selectorData
        restSelectorDataMockMvc.perform(get("/api/selector-data/{id}", selectorData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(selectorData.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.orderNo").value(DEFAULT_ORDER_NO));
    }

    @Test
    @Transactional
    public void getNonExistingSelectorData() throws Exception {
        // Get the selectorData
        restSelectorDataMockMvc.perform(get("/api/selector-data/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSelectorData() throws Exception {
        // Initialize the database
        selectorDataRepository.saveAndFlush(selectorData);
        selectorDataSearchRepository.save(selectorData);
        int databaseSizeBeforeUpdate = selectorDataRepository.findAll().size();

        // Update the selectorData
        SelectorData updatedSelectorData = new SelectorData();
        updatedSelectorData.setId(selectorData.getId());
        updatedSelectorData.setKey(UPDATED_KEY);
        updatedSelectorData.setText(UPDATED_TEXT);
        updatedSelectorData.setOrderNo(UPDATED_ORDER_NO);
        SelectorDataDTO selectorDataDTO = selectorDataMapper.selectorDataToSelectorDataDTO(updatedSelectorData);

        restSelectorDataMockMvc.perform(put("/api/selector-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(selectorDataDTO)))
                .andExpect(status().isOk());

        // Validate the SelectorData in the database
        List<SelectorData> selectorData = selectorDataRepository.findAll();
        assertThat(selectorData).hasSize(databaseSizeBeforeUpdate);
        SelectorData testSelectorData = selectorData.get(selectorData.size() - 1);
        assertThat(testSelectorData.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSelectorData.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testSelectorData.getOrderNo()).isEqualTo(UPDATED_ORDER_NO);

        // Validate the SelectorData in ElasticSearch
        SelectorData selectorDataEs = selectorDataSearchRepository.findOne(testSelectorData.getId());
        assertThat(selectorDataEs).isEqualToComparingFieldByField(testSelectorData);
    }

    @Test
    @Transactional
    public void deleteSelectorData() throws Exception {
        // Initialize the database
        selectorDataRepository.saveAndFlush(selectorData);
        selectorDataSearchRepository.save(selectorData);
        int databaseSizeBeforeDelete = selectorDataRepository.findAll().size();

        // Get the selectorData
        restSelectorDataMockMvc.perform(delete("/api/selector-data/{id}", selectorData.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean selectorDataExistsInEs = selectorDataSearchRepository.exists(selectorData.getId());
        assertThat(selectorDataExistsInEs).isFalse();

        // Validate the database is empty
        List<SelectorData> selectorData = selectorDataRepository.findAll();
        assertThat(selectorData).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSelectorData() throws Exception {
        // Initialize the database
        selectorDataRepository.saveAndFlush(selectorData);
        selectorDataSearchRepository.save(selectorData);

        // Search the selectorData
        restSelectorDataMockMvc.perform(get("/api/_search/selector-data?query=id:" + selectorData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selectorData.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].orderNo").value(hasItem(DEFAULT_ORDER_NO)));
    }
}
