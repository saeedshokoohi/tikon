package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.CompanyManager;
import com.eyeson.tikon.repository.CompanyManagerRepository;
import com.eyeson.tikon.service.CompanyManagerService;
import com.eyeson.tikon.repository.search.CompanyManagerSearchRepository;
import com.eyeson.tikon.web.rest.dto.CompanyManagerDTO;
import com.eyeson.tikon.web.rest.mapper.CompanyManagerMapper;

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
 * Test class for the CompanyManagerResource REST controller.
 *
 * @see CompanyManagerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class CompanyManagerResourceIntTest {


    private static final Integer DEFAULT_MANAGER_TYPE = 1;
    private static final Integer UPDATED_MANAGER_TYPE = 2;

    @Inject
    private CompanyManagerRepository companyManagerRepository;

    @Inject
    private CompanyManagerMapper companyManagerMapper;

    @Inject
    private CompanyManagerService companyManagerService;

    @Inject
    private CompanyManagerSearchRepository companyManagerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompanyManagerMockMvc;

    private CompanyManager companyManager;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyManagerResource companyManagerResource = new CompanyManagerResource();
        ReflectionTestUtils.setField(companyManagerResource, "companyManagerService", companyManagerService);
        ReflectionTestUtils.setField(companyManagerResource, "companyManagerMapper", companyManagerMapper);
        this.restCompanyManagerMockMvc = MockMvcBuilders.standaloneSetup(companyManagerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        companyManagerSearchRepository.deleteAll();
        companyManager = new CompanyManager();
        companyManager.setManagerType(DEFAULT_MANAGER_TYPE);
    }

    @Test
    @Transactional
    public void createCompanyManager() throws Exception {
        int databaseSizeBeforeCreate = companyManagerRepository.findAll().size();

        // Create the CompanyManager
        CompanyManagerDTO companyManagerDTO = companyManagerMapper.companyManagerToCompanyManagerDTO(companyManager);

        restCompanyManagerMockMvc.perform(post("/api/company-managers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyManagerDTO)))
                .andExpect(status().isCreated());

        // Validate the CompanyManager in the database
        List<CompanyManager> companyManagers = companyManagerRepository.findAll();
        assertThat(companyManagers).hasSize(databaseSizeBeforeCreate + 1);
        CompanyManager testCompanyManager = companyManagers.get(companyManagers.size() - 1);
        assertThat(testCompanyManager.getManagerType()).isEqualTo(DEFAULT_MANAGER_TYPE);

        // Validate the CompanyManager in ElasticSearch
        CompanyManager companyManagerEs = companyManagerSearchRepository.findOne(testCompanyManager.getId());
        assertThat(companyManagerEs).isEqualToComparingFieldByField(testCompanyManager);
    }

    @Test
    @Transactional
    public void getAllCompanyManagers() throws Exception {
        // Initialize the database
        companyManagerRepository.saveAndFlush(companyManager);

        // Get all the companyManagers
        restCompanyManagerMockMvc.perform(get("/api/company-managers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(companyManager.getId().intValue())))
                .andExpect(jsonPath("$.[*].managerType").value(hasItem(DEFAULT_MANAGER_TYPE)));
    }

    @Test
    @Transactional
    public void getCompanyManager() throws Exception {
        // Initialize the database
        companyManagerRepository.saveAndFlush(companyManager);

        // Get the companyManager
        restCompanyManagerMockMvc.perform(get("/api/company-managers/{id}", companyManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(companyManager.getId().intValue()))
            .andExpect(jsonPath("$.managerType").value(DEFAULT_MANAGER_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyManager() throws Exception {
        // Get the companyManager
        restCompanyManagerMockMvc.perform(get("/api/company-managers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyManager() throws Exception {
        // Initialize the database
        companyManagerRepository.saveAndFlush(companyManager);
        companyManagerSearchRepository.save(companyManager);
        int databaseSizeBeforeUpdate = companyManagerRepository.findAll().size();

        // Update the companyManager
        CompanyManager updatedCompanyManager = new CompanyManager();
        updatedCompanyManager.setId(companyManager.getId());
        updatedCompanyManager.setManagerType(UPDATED_MANAGER_TYPE);
        CompanyManagerDTO companyManagerDTO = companyManagerMapper.companyManagerToCompanyManagerDTO(updatedCompanyManager);

        restCompanyManagerMockMvc.perform(put("/api/company-managers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyManagerDTO)))
                .andExpect(status().isOk());

        // Validate the CompanyManager in the database
        List<CompanyManager> companyManagers = companyManagerRepository.findAll();
        assertThat(companyManagers).hasSize(databaseSizeBeforeUpdate);
        CompanyManager testCompanyManager = companyManagers.get(companyManagers.size() - 1);
        assertThat(testCompanyManager.getManagerType()).isEqualTo(UPDATED_MANAGER_TYPE);

        // Validate the CompanyManager in ElasticSearch
        CompanyManager companyManagerEs = companyManagerSearchRepository.findOne(testCompanyManager.getId());
        assertThat(companyManagerEs).isEqualToComparingFieldByField(testCompanyManager);
    }

    @Test
    @Transactional
    public void deleteCompanyManager() throws Exception {
        // Initialize the database
        companyManagerRepository.saveAndFlush(companyManager);
        companyManagerSearchRepository.save(companyManager);
        int databaseSizeBeforeDelete = companyManagerRepository.findAll().size();

        // Get the companyManager
        restCompanyManagerMockMvc.perform(delete("/api/company-managers/{id}", companyManager.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean companyManagerExistsInEs = companyManagerSearchRepository.exists(companyManager.getId());
        assertThat(companyManagerExistsInEs).isFalse();

        // Validate the database is empty
        List<CompanyManager> companyManagers = companyManagerRepository.findAll();
        assertThat(companyManagers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompanyManager() throws Exception {
        // Initialize the database
        companyManagerRepository.saveAndFlush(companyManager);
        companyManagerSearchRepository.save(companyManager);

        // Search the companyManager
        restCompanyManagerMockMvc.perform(get("/api/_search/company-managers?query=id:" + companyManager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyManager.getId().intValue())))
            .andExpect(jsonPath("$.[*].managerType").value(hasItem(DEFAULT_MANAGER_TYPE)));
    }
}
