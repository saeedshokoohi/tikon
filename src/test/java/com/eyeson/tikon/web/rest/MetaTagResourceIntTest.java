package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.MetaTag;
import com.eyeson.tikon.repository.MetaTagRepository;
import com.eyeson.tikon.service.MetaTagService;
import com.eyeson.tikon.repository.search.MetaTagSearchRepository;
import com.eyeson.tikon.web.rest.dto.MetaTagDTO;
import com.eyeson.tikon.web.rest.mapper.MetaTagMapper;

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
 * Test class for the MetaTagResource REST controller.
 *
 * @see MetaTagResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class MetaTagResourceIntTest {

    private static final String DEFAULT_TAG = "AAAAA";
    private static final String UPDATED_TAG = "BBBBB";

    @Inject
    private MetaTagRepository metaTagRepository;

    @Inject
    private MetaTagMapper metaTagMapper;

    @Inject
    private MetaTagService metaTagService;

    @Inject
    private MetaTagSearchRepository metaTagSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMetaTagMockMvc;

    private MetaTag metaTag;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MetaTagResource metaTagResource = new MetaTagResource();
        ReflectionTestUtils.setField(metaTagResource, "metaTagService", metaTagService);
        ReflectionTestUtils.setField(metaTagResource, "metaTagMapper", metaTagMapper);
        this.restMetaTagMockMvc = MockMvcBuilders.standaloneSetup(metaTagResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        metaTagSearchRepository.deleteAll();
        metaTag = new MetaTag();
        metaTag.setTag(DEFAULT_TAG);
    }

    @Test
    @Transactional
    public void createMetaTag() throws Exception {
        int databaseSizeBeforeCreate = metaTagRepository.findAll().size();

        // Create the MetaTag
        MetaTagDTO metaTagDTO = metaTagMapper.metaTagToMetaTagDTO(metaTag);

        restMetaTagMockMvc.perform(post("/api/meta-tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metaTagDTO)))
                .andExpect(status().isCreated());

        // Validate the MetaTag in the database
        List<MetaTag> metaTags = metaTagRepository.findAll();
        assertThat(metaTags).hasSize(databaseSizeBeforeCreate + 1);
        MetaTag testMetaTag = metaTags.get(metaTags.size() - 1);
        assertThat(testMetaTag.getTag()).isEqualTo(DEFAULT_TAG);

        // Validate the MetaTag in ElasticSearch
        MetaTag metaTagEs = metaTagSearchRepository.findOne(testMetaTag.getId());
        assertThat(metaTagEs).isEqualToComparingFieldByField(testMetaTag);
    }

    @Test
    @Transactional
    public void getAllMetaTags() throws Exception {
        // Initialize the database
        metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTags
        restMetaTagMockMvc.perform(get("/api/meta-tags?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(metaTag.getId().intValue())))
                .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())));
    }

    @Test
    @Transactional
    public void getMetaTag() throws Exception {
        // Initialize the database
        metaTagRepository.saveAndFlush(metaTag);

        // Get the metaTag
        restMetaTagMockMvc.perform(get("/api/meta-tags/{id}", metaTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(metaTag.getId().intValue()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMetaTag() throws Exception {
        // Get the metaTag
        restMetaTagMockMvc.perform(get("/api/meta-tags/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetaTag() throws Exception {
        // Initialize the database
        metaTagRepository.saveAndFlush(metaTag);
        metaTagSearchRepository.save(metaTag);
        int databaseSizeBeforeUpdate = metaTagRepository.findAll().size();

        // Update the metaTag
        MetaTag updatedMetaTag = new MetaTag();
        updatedMetaTag.setId(metaTag.getId());
        updatedMetaTag.setTag(UPDATED_TAG);
        MetaTagDTO metaTagDTO = metaTagMapper.metaTagToMetaTagDTO(updatedMetaTag);

        restMetaTagMockMvc.perform(put("/api/meta-tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metaTagDTO)))
                .andExpect(status().isOk());

        // Validate the MetaTag in the database
        List<MetaTag> metaTags = metaTagRepository.findAll();
        assertThat(metaTags).hasSize(databaseSizeBeforeUpdate);
        MetaTag testMetaTag = metaTags.get(metaTags.size() - 1);
        assertThat(testMetaTag.getTag()).isEqualTo(UPDATED_TAG);

        // Validate the MetaTag in ElasticSearch
        MetaTag metaTagEs = metaTagSearchRepository.findOne(testMetaTag.getId());
        assertThat(metaTagEs).isEqualToComparingFieldByField(testMetaTag);
    }

    @Test
    @Transactional
    public void deleteMetaTag() throws Exception {
        // Initialize the database
        metaTagRepository.saveAndFlush(metaTag);
        metaTagSearchRepository.save(metaTag);
        int databaseSizeBeforeDelete = metaTagRepository.findAll().size();

        // Get the metaTag
        restMetaTagMockMvc.perform(delete("/api/meta-tags/{id}", metaTag.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean metaTagExistsInEs = metaTagSearchRepository.exists(metaTag.getId());
        assertThat(metaTagExistsInEs).isFalse();

        // Validate the database is empty
        List<MetaTag> metaTags = metaTagRepository.findAll();
        assertThat(metaTags).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMetaTag() throws Exception {
        // Initialize the database
        metaTagRepository.saveAndFlush(metaTag);
        metaTagSearchRepository.save(metaTag);

        // Search the metaTag
        restMetaTagMockMvc.perform(get("/api/_search/meta-tags?query=id:" + metaTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())));
    }
}
