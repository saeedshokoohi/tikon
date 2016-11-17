package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.Servant;
import com.eyeson.tikon.repository.ServantRepository;
import com.eyeson.tikon.service.ServantService;
import com.eyeson.tikon.repository.search.ServantSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServantDTO;
import com.eyeson.tikon.web.rest.mapper.ServantMapper;

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
 * Test class for the ServantResource REST controller.
 *
 * @see ServantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ServantResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    @Inject
    private ServantRepository servantRepository;

    @Inject
    private ServantMapper servantMapper;

    @Inject
    private ServantService servantService;

    @Inject
    private ServantSearchRepository servantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restServantMockMvc;

    private Servant servant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServantResource servantResource = new ServantResource();
        ReflectionTestUtils.setField(servantResource, "servantService", servantService);
        ReflectionTestUtils.setField(servantResource, "servantMapper", servantMapper);
        this.restServantMockMvc = MockMvcBuilders.standaloneSetup(servantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        servantSearchRepository.deleteAll();
        servant = new Servant();
        servant.setTitle(DEFAULT_TITLE);
        servant.setLevel(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void createServant() throws Exception {
        int databaseSizeBeforeCreate = servantRepository.findAll().size();

        // Create the Servant
        ServantDTO servantDTO = servantMapper.servantToServantDTO(servant);

        restServantMockMvc.perform(post("/api/servants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(servantDTO)))
                .andExpect(status().isCreated());

        // Validate the Servant in the database
        List<Servant> servants = servantRepository.findAll();
        assertThat(servants).hasSize(databaseSizeBeforeCreate + 1);
        Servant testServant = servants.get(servants.size() - 1);
        assertThat(testServant.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testServant.getLevel()).isEqualTo(DEFAULT_LEVEL);

        // Validate the Servant in ElasticSearch
        Servant servantEs = servantSearchRepository.findOne(testServant.getId());
        assertThat(servantEs).isEqualToComparingFieldByField(testServant);
    }

    @Test
    @Transactional
    public void getAllServants() throws Exception {
        // Initialize the database
        servantRepository.saveAndFlush(servant);

        // Get all the servants
        restServantMockMvc.perform(get("/api/servants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(servant.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    @Test
    @Transactional
    public void getServant() throws Exception {
        // Initialize the database
        servantRepository.saveAndFlush(servant);

        // Get the servant
        restServantMockMvc.perform(get("/api/servants/{id}", servant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(servant.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    public void getNonExistingServant() throws Exception {
        // Get the servant
        restServantMockMvc.perform(get("/api/servants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServant() throws Exception {
        // Initialize the database
        servantRepository.saveAndFlush(servant);
        servantSearchRepository.save(servant);
        int databaseSizeBeforeUpdate = servantRepository.findAll().size();

        // Update the servant
        Servant updatedServant = new Servant();
        updatedServant.setId(servant.getId());
        updatedServant.setTitle(UPDATED_TITLE);
        updatedServant.setLevel(UPDATED_LEVEL);
        ServantDTO servantDTO = servantMapper.servantToServantDTO(updatedServant);

        restServantMockMvc.perform(put("/api/servants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(servantDTO)))
                .andExpect(status().isOk());

        // Validate the Servant in the database
        List<Servant> servants = servantRepository.findAll();
        assertThat(servants).hasSize(databaseSizeBeforeUpdate);
        Servant testServant = servants.get(servants.size() - 1);
        assertThat(testServant.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testServant.getLevel()).isEqualTo(UPDATED_LEVEL);

        // Validate the Servant in ElasticSearch
        Servant servantEs = servantSearchRepository.findOne(testServant.getId());
        assertThat(servantEs).isEqualToComparingFieldByField(testServant);
    }

    @Test
    @Transactional
    public void deleteServant() throws Exception {
        // Initialize the database
        servantRepository.saveAndFlush(servant);
        servantSearchRepository.save(servant);
        int databaseSizeBeforeDelete = servantRepository.findAll().size();

        // Get the servant
        restServantMockMvc.perform(delete("/api/servants/{id}", servant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean servantExistsInEs = servantSearchRepository.exists(servant.getId());
        assertThat(servantExistsInEs).isFalse();

        // Validate the database is empty
        List<Servant> servants = servantRepository.findAll();
        assertThat(servants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServant() throws Exception {
        // Initialize the database
        servantRepository.saveAndFlush(servant);
        servantSearchRepository.save(servant);

        // Search the servant
        restServantMockMvc.perform(get("/api/_search/servants?query=id:" + servant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servant.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }
}
