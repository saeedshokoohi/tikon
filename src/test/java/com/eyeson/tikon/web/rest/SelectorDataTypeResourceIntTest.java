package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.SelectorDataType;
import com.eyeson.tikon.repository.SelectorDataTypeRepository;
import com.eyeson.tikon.service.SelectorDataTypeService;
import com.eyeson.tikon.repository.search.SelectorDataTypeSearchRepository;
import com.eyeson.tikon.web.rest.dto.SelectorDataTypeDTO;
import com.eyeson.tikon.web.rest.mapper.SelectorDataTypeMapper;

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
 * Test class for the SelectorDataTypeResource REST controller.
 *
 * @see SelectorDataTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class SelectorDataTypeResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAA";
    private static final String UPDATED_KEY = "BBBBB";
    private static final String DEFAULT_TYPE_NAME = "AAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBB";

    @Inject
    private SelectorDataTypeRepository selectorDataTypeRepository;

    @Inject
    private SelectorDataTypeMapper selectorDataTypeMapper;

    @Inject
    private SelectorDataTypeService selectorDataTypeService;

    @Inject
    private SelectorDataTypeSearchRepository selectorDataTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSelectorDataTypeMockMvc;

    private SelectorDataType selectorDataType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SelectorDataTypeResource selectorDataTypeResource = new SelectorDataTypeResource();
        ReflectionTestUtils.setField(selectorDataTypeResource, "selectorDataTypeService", selectorDataTypeService);
        ReflectionTestUtils.setField(selectorDataTypeResource, "selectorDataTypeMapper", selectorDataTypeMapper);
        this.restSelectorDataTypeMockMvc = MockMvcBuilders.standaloneSetup(selectorDataTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        selectorDataTypeSearchRepository.deleteAll();
        selectorDataType = new SelectorDataType();
        selectorDataType.setKey(DEFAULT_KEY);
        selectorDataType.setTypeName(DEFAULT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void createSelectorDataType() throws Exception {
        int databaseSizeBeforeCreate = selectorDataTypeRepository.findAll().size();

        // Create the SelectorDataType
        SelectorDataTypeDTO selectorDataTypeDTO = selectorDataTypeMapper.selectorDataTypeToSelectorDataTypeDTO(selectorDataType);

        restSelectorDataTypeMockMvc.perform(post("/api/selector-data-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(selectorDataTypeDTO)))
                .andExpect(status().isCreated());

        // Validate the SelectorDataType in the database
        List<SelectorDataType> selectorDataTypes = selectorDataTypeRepository.findAll();
        assertThat(selectorDataTypes).hasSize(databaseSizeBeforeCreate + 1);
        SelectorDataType testSelectorDataType = selectorDataTypes.get(selectorDataTypes.size() - 1);
        assertThat(testSelectorDataType.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testSelectorDataType.getTypeName()).isEqualTo(DEFAULT_TYPE_NAME);

        // Validate the SelectorDataType in ElasticSearch
        SelectorDataType selectorDataTypeEs = selectorDataTypeSearchRepository.findOne(testSelectorDataType.getId());
        assertThat(selectorDataTypeEs).isEqualToComparingFieldByField(testSelectorDataType);
    }

    @Test
    @Transactional
    public void getAllSelectorDataTypes() throws Exception {
        // Initialize the database
        selectorDataTypeRepository.saveAndFlush(selectorDataType);

        // Get all the selectorDataTypes
        restSelectorDataTypeMockMvc.perform(get("/api/selector-data-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(selectorDataType.getId().intValue())))
                .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
                .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSelectorDataType() throws Exception {
        // Initialize the database
        selectorDataTypeRepository.saveAndFlush(selectorDataType);

        // Get the selectorDataType
        restSelectorDataTypeMockMvc.perform(get("/api/selector-data-types/{id}", selectorDataType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(selectorDataType.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSelectorDataType() throws Exception {
        // Get the selectorDataType
        restSelectorDataTypeMockMvc.perform(get("/api/selector-data-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSelectorDataType() throws Exception {
        // Initialize the database
        selectorDataTypeRepository.saveAndFlush(selectorDataType);
        selectorDataTypeSearchRepository.save(selectorDataType);
        int databaseSizeBeforeUpdate = selectorDataTypeRepository.findAll().size();

        // Update the selectorDataType
        SelectorDataType updatedSelectorDataType = new SelectorDataType();
        updatedSelectorDataType.setId(selectorDataType.getId());
        updatedSelectorDataType.setKey(UPDATED_KEY);
        updatedSelectorDataType.setTypeName(UPDATED_TYPE_NAME);
        SelectorDataTypeDTO selectorDataTypeDTO = selectorDataTypeMapper.selectorDataTypeToSelectorDataTypeDTO(updatedSelectorDataType);

        restSelectorDataTypeMockMvc.perform(put("/api/selector-data-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(selectorDataTypeDTO)))
                .andExpect(status().isOk());

        // Validate the SelectorDataType in the database
        List<SelectorDataType> selectorDataTypes = selectorDataTypeRepository.findAll();
        assertThat(selectorDataTypes).hasSize(databaseSizeBeforeUpdate);
        SelectorDataType testSelectorDataType = selectorDataTypes.get(selectorDataTypes.size() - 1);
        assertThat(testSelectorDataType.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSelectorDataType.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);

        // Validate the SelectorDataType in ElasticSearch
        SelectorDataType selectorDataTypeEs = selectorDataTypeSearchRepository.findOne(testSelectorDataType.getId());
        assertThat(selectorDataTypeEs).isEqualToComparingFieldByField(testSelectorDataType);
    }

    @Test
    @Transactional
    public void deleteSelectorDataType() throws Exception {
        // Initialize the database
        selectorDataTypeRepository.saveAndFlush(selectorDataType);
        selectorDataTypeSearchRepository.save(selectorDataType);
        int databaseSizeBeforeDelete = selectorDataTypeRepository.findAll().size();

        // Get the selectorDataType
        restSelectorDataTypeMockMvc.perform(delete("/api/selector-data-types/{id}", selectorDataType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean selectorDataTypeExistsInEs = selectorDataTypeSearchRepository.exists(selectorDataType.getId());
        assertThat(selectorDataTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<SelectorDataType> selectorDataTypes = selectorDataTypeRepository.findAll();
        assertThat(selectorDataTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSelectorDataType() throws Exception {
        // Initialize the database
        selectorDataTypeRepository.saveAndFlush(selectorDataType);
        selectorDataTypeSearchRepository.save(selectorDataType);

        // Search the selectorDataType
        restSelectorDataTypeMockMvc.perform(get("/api/_search/selector-data-types?query=id:" + selectorDataType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(selectorDataType.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME.toString())));
    }
}
