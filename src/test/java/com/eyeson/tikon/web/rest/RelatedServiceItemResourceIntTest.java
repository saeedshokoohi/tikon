package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.RelatedServiceItem;
import com.eyeson.tikon.repository.RelatedServiceItemRepository;
import com.eyeson.tikon.service.RelatedServiceItemService;
import com.eyeson.tikon.repository.search.RelatedServiceItemSearchRepository;
import com.eyeson.tikon.web.rest.dto.RelatedServiceItemDTO;
import com.eyeson.tikon.web.rest.mapper.RelatedServiceItemMapper;

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
 * Test class for the RelatedServiceItemResource REST controller.
 *
 * @see RelatedServiceItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class RelatedServiceItemResourceIntTest {


    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;

    @Inject
    private RelatedServiceItemRepository relatedServiceItemRepository;

    @Inject
    private RelatedServiceItemMapper relatedServiceItemMapper;

    @Inject
    private RelatedServiceItemService relatedServiceItemService;

    @Inject
    private RelatedServiceItemSearchRepository relatedServiceItemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRelatedServiceItemMockMvc;

    private RelatedServiceItem relatedServiceItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RelatedServiceItemResource relatedServiceItemResource = new RelatedServiceItemResource();
        ReflectionTestUtils.setField(relatedServiceItemResource, "relatedServiceItemService", relatedServiceItemService);
        ReflectionTestUtils.setField(relatedServiceItemResource, "relatedServiceItemMapper", relatedServiceItemMapper);
        this.restRelatedServiceItemMockMvc = MockMvcBuilders.standaloneSetup(relatedServiceItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        relatedServiceItemSearchRepository.deleteAll();
        relatedServiceItem = new RelatedServiceItem();
        relatedServiceItem.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createRelatedServiceItem() throws Exception {
        int databaseSizeBeforeCreate = relatedServiceItemRepository.findAll().size();

        // Create the RelatedServiceItem
        RelatedServiceItemDTO relatedServiceItemDTO = relatedServiceItemMapper.relatedServiceItemToRelatedServiceItemDTO(relatedServiceItem);

        restRelatedServiceItemMockMvc.perform(post("/api/related-service-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(relatedServiceItemDTO)))
                .andExpect(status().isCreated());

        // Validate the RelatedServiceItem in the database
        List<RelatedServiceItem> relatedServiceItems = relatedServiceItemRepository.findAll();
        assertThat(relatedServiceItems).hasSize(databaseSizeBeforeCreate + 1);
        RelatedServiceItem testRelatedServiceItem = relatedServiceItems.get(relatedServiceItems.size() - 1);
        assertThat(testRelatedServiceItem.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the RelatedServiceItem in ElasticSearch
        RelatedServiceItem relatedServiceItemEs = relatedServiceItemSearchRepository.findOne(testRelatedServiceItem.getId());
        assertThat(relatedServiceItemEs).isEqualToComparingFieldByField(testRelatedServiceItem);
    }

    @Test
    @Transactional
    public void getAllRelatedServiceItems() throws Exception {
        // Initialize the database
        relatedServiceItemRepository.saveAndFlush(relatedServiceItem);

        // Get all the relatedServiceItems
        restRelatedServiceItemMockMvc.perform(get("/api/related-service-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(relatedServiceItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    public void getRelatedServiceItem() throws Exception {
        // Initialize the database
        relatedServiceItemRepository.saveAndFlush(relatedServiceItem);

        // Get the relatedServiceItem
        restRelatedServiceItemMockMvc.perform(get("/api/related-service-items/{id}", relatedServiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(relatedServiceItem.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingRelatedServiceItem() throws Exception {
        // Get the relatedServiceItem
        restRelatedServiceItemMockMvc.perform(get("/api/related-service-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelatedServiceItem() throws Exception {
        // Initialize the database
        relatedServiceItemRepository.saveAndFlush(relatedServiceItem);
        relatedServiceItemSearchRepository.save(relatedServiceItem);
        int databaseSizeBeforeUpdate = relatedServiceItemRepository.findAll().size();

        // Update the relatedServiceItem
        RelatedServiceItem updatedRelatedServiceItem = new RelatedServiceItem();
        updatedRelatedServiceItem.setId(relatedServiceItem.getId());
        updatedRelatedServiceItem.setType(UPDATED_TYPE);
        RelatedServiceItemDTO relatedServiceItemDTO = relatedServiceItemMapper.relatedServiceItemToRelatedServiceItemDTO(updatedRelatedServiceItem);

        restRelatedServiceItemMockMvc.perform(put("/api/related-service-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(relatedServiceItemDTO)))
                .andExpect(status().isOk());

        // Validate the RelatedServiceItem in the database
        List<RelatedServiceItem> relatedServiceItems = relatedServiceItemRepository.findAll();
        assertThat(relatedServiceItems).hasSize(databaseSizeBeforeUpdate);
        RelatedServiceItem testRelatedServiceItem = relatedServiceItems.get(relatedServiceItems.size() - 1);
        assertThat(testRelatedServiceItem.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the RelatedServiceItem in ElasticSearch
        RelatedServiceItem relatedServiceItemEs = relatedServiceItemSearchRepository.findOne(testRelatedServiceItem.getId());
        assertThat(relatedServiceItemEs).isEqualToComparingFieldByField(testRelatedServiceItem);
    }

    @Test
    @Transactional
    public void deleteRelatedServiceItem() throws Exception {
        // Initialize the database
        relatedServiceItemRepository.saveAndFlush(relatedServiceItem);
        relatedServiceItemSearchRepository.save(relatedServiceItem);
        int databaseSizeBeforeDelete = relatedServiceItemRepository.findAll().size();

        // Get the relatedServiceItem
        restRelatedServiceItemMockMvc.perform(delete("/api/related-service-items/{id}", relatedServiceItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean relatedServiceItemExistsInEs = relatedServiceItemSearchRepository.exists(relatedServiceItem.getId());
        assertThat(relatedServiceItemExistsInEs).isFalse();

        // Validate the database is empty
        List<RelatedServiceItem> relatedServiceItems = relatedServiceItemRepository.findAll();
        assertThat(relatedServiceItems).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRelatedServiceItem() throws Exception {
        // Initialize the database
        relatedServiceItemRepository.saveAndFlush(relatedServiceItem);
        relatedServiceItemSearchRepository.save(relatedServiceItem);

        // Search the relatedServiceItem
        restRelatedServiceItemMockMvc.perform(get("/api/_search/related-service-items?query=id:" + relatedServiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatedServiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
}
