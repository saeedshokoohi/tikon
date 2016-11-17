package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.Company;
import com.eyeson.tikon.repository.CompanyRepository;
import com.eyeson.tikon.service.CompanyService;
import com.eyeson.tikon.repository.search.CompanySearchRepository;
import com.eyeson.tikon.web.rest.dto.CompanyDTO;
import com.eyeson.tikon.web.rest.mapper.CompanyMapper;

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

import com.eyeson.tikon.domain.enumeration.ActivityType;

/**
 * Test class for the CompanyResource REST controller.
 *
 * @see CompanyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class CompanyResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    private static final ActivityType DEFAULT_ACTIVITY_TYPE = ActivityType.BEAUTY;
    private static final ActivityType UPDATED_ACTIVITY_TYPE = ActivityType.TRAVEL;
    private static final String DEFAULT_WEB_SITE_URL = "AAAAA";
    private static final String UPDATED_WEB_SITE_URL = "BBBBB";
    private static final String DEFAULT_KEY_URL = "AAAAA";
    private static final String UPDATED_KEY_URL = "BBBBB";

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private CompanyMapper companyMapper;

    @Inject
    private CompanyService companyService;

    @Inject
    private CompanySearchRepository companySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompanyMockMvc;

    private Company company;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyResource companyResource = new CompanyResource();
        ReflectionTestUtils.setField(companyResource, "companyService", companyService);
        ReflectionTestUtils.setField(companyResource, "companyMapper", companyMapper);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        companySearchRepository.deleteAll();
        company = new Company();
        company.setTitle(DEFAULT_TITLE);
        company.setDescription(DEFAULT_DESCRIPTION);
        company.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        company.setActivityType(DEFAULT_ACTIVITY_TYPE);
        company.setWebSiteUrl(DEFAULT_WEB_SITE_URL);
        company.setKeyUrl(DEFAULT_KEY_URL);
    }

    @Test
    @Transactional
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(company);

        restCompanyMockMvc.perform(post("/api/companies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
                .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companies.get(companies.size() - 1);
        assertThat(testCompany.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCompany.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCompany.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCompany.getActivityType()).isEqualTo(DEFAULT_ACTIVITY_TYPE);
        assertThat(testCompany.getWebSiteUrl()).isEqualTo(DEFAULT_WEB_SITE_URL);
        assertThat(testCompany.getKeyUrl()).isEqualTo(DEFAULT_KEY_URL);

        // Validate the Company in ElasticSearch
        Company companyEs = companySearchRepository.findOne(testCompany.getId());
        assertThat(companyEs).isEqualToComparingFieldByField(testCompany);
    }

    @Test
    @Transactional
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companies
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
                .andExpect(jsonPath("$.[*].webSiteUrl").value(hasItem(DEFAULT_WEB_SITE_URL.toString())))
                .andExpect(jsonPath("$.[*].keyUrl").value(hasItem(DEFAULT_KEY_URL.toString())));
    }

    @Test
    @Transactional
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.activityType").value(DEFAULT_ACTIVITY_TYPE.toString()))
            .andExpect(jsonPath("$.webSiteUrl").value(DEFAULT_WEB_SITE_URL.toString()))
            .andExpect(jsonPath("$.keyUrl").value(DEFAULT_KEY_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        companySearchRepository.save(company);
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = new Company();
        updatedCompany.setId(company.getId());
        updatedCompany.setTitle(UPDATED_TITLE);
        updatedCompany.setDescription(UPDATED_DESCRIPTION);
        updatedCompany.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedCompany.setActivityType(UPDATED_ACTIVITY_TYPE);
        updatedCompany.setWebSiteUrl(UPDATED_WEB_SITE_URL);
        updatedCompany.setKeyUrl(UPDATED_KEY_URL);
        CompanyDTO companyDTO = companyMapper.companyToCompanyDTO(updatedCompany);

        restCompanyMockMvc.perform(put("/api/companies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyDTO)))
                .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companies.get(companies.size() - 1);
        assertThat(testCompany.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCompany.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCompany.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCompany.getActivityType()).isEqualTo(UPDATED_ACTIVITY_TYPE);
        assertThat(testCompany.getWebSiteUrl()).isEqualTo(UPDATED_WEB_SITE_URL);
        assertThat(testCompany.getKeyUrl()).isEqualTo(UPDATED_KEY_URL);

        // Validate the Company in ElasticSearch
        Company companyEs = companySearchRepository.findOne(testCompany.getId());
        assertThat(companyEs).isEqualToComparingFieldByField(testCompany);
    }

    @Test
    @Transactional
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        companySearchRepository.save(company);
        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Get the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean companyExistsInEs = companySearchRepository.exists(company.getId());
        assertThat(companyExistsInEs).isFalse();

        // Validate the database is empty
        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);
        companySearchRepository.save(company);

        // Search the company
        restCompanyMockMvc.perform(get("/api/_search/companies?query=id:" + company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].webSiteUrl").value(hasItem(DEFAULT_WEB_SITE_URL.toString())))
            .andExpect(jsonPath("$.[*].keyUrl").value(hasItem(DEFAULT_KEY_URL.toString())));
    }
}
