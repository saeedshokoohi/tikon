package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.InvoiceInfo;
import com.eyeson.tikon.repository.InvoiceInfoRepository;
import com.eyeson.tikon.service.InvoiceInfoService;
import com.eyeson.tikon.repository.search.InvoiceInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.InvoiceInfoDTO;
import com.eyeson.tikon.web.rest.mapper.InvoiceInfoMapper;

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


/**
 * Test class for the InvoiceInfoResource REST controller.
 *
 * @see InvoiceInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class InvoiceInfoResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    @Inject
    private InvoiceInfoRepository invoiceInfoRepository;

    @Inject
    private InvoiceInfoMapper invoiceInfoMapper;

    @Inject
    private InvoiceInfoService invoiceInfoService;

    @Inject
    private InvoiceInfoSearchRepository invoiceInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInvoiceInfoMockMvc;

    private InvoiceInfo invoiceInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InvoiceInfoResource invoiceInfoResource = new InvoiceInfoResource();
        ReflectionTestUtils.setField(invoiceInfoResource, "invoiceInfoService", invoiceInfoService);
        ReflectionTestUtils.setField(invoiceInfoResource, "invoiceInfoMapper", invoiceInfoMapper);
        this.restInvoiceInfoMockMvc = MockMvcBuilders.standaloneSetup(invoiceInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        invoiceInfoSearchRepository.deleteAll();
        invoiceInfo = new InvoiceInfo();
        invoiceInfo.setCreateDate(DEFAULT_CREATE_DATE);
    }

    @Test
    @Transactional
    public void createInvoiceInfo() throws Exception {
        int databaseSizeBeforeCreate = invoiceInfoRepository.findAll().size();

        // Create the InvoiceInfo
        InvoiceInfoDTO invoiceInfoDTO = invoiceInfoMapper.invoiceInfoToInvoiceInfoDTO(invoiceInfo);

        restInvoiceInfoMockMvc.perform(post("/api/invoice-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invoiceInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the InvoiceInfo in the database
        List<InvoiceInfo> invoiceInfos = invoiceInfoRepository.findAll();
        assertThat(invoiceInfos).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceInfo testInvoiceInfo = invoiceInfos.get(invoiceInfos.size() - 1);
        assertThat(testInvoiceInfo.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);

        // Validate the InvoiceInfo in ElasticSearch
        InvoiceInfo invoiceInfoEs = invoiceInfoSearchRepository.findOne(testInvoiceInfo.getId());
        assertThat(invoiceInfoEs).isEqualToComparingFieldByField(testInvoiceInfo);
    }

    @Test
    @Transactional
    public void getAllInvoiceInfos() throws Exception {
        // Initialize the database
        invoiceInfoRepository.saveAndFlush(invoiceInfo);

        // Get all the invoiceInfos
        restInvoiceInfoMockMvc.perform(get("/api/invoice-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)));
    }

    @Test
    @Transactional
    public void getInvoiceInfo() throws Exception {
        // Initialize the database
        invoiceInfoRepository.saveAndFlush(invoiceInfo);

        // Get the invoiceInfo
        restInvoiceInfoMockMvc.perform(get("/api/invoice-infos/{id}", invoiceInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(invoiceInfo.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingInvoiceInfo() throws Exception {
        // Get the invoiceInfo
        restInvoiceInfoMockMvc.perform(get("/api/invoice-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceInfo() throws Exception {
        // Initialize the database
        invoiceInfoRepository.saveAndFlush(invoiceInfo);
        invoiceInfoSearchRepository.save(invoiceInfo);
        int databaseSizeBeforeUpdate = invoiceInfoRepository.findAll().size();

        // Update the invoiceInfo
        InvoiceInfo updatedInvoiceInfo = new InvoiceInfo();
        updatedInvoiceInfo.setId(invoiceInfo.getId());
        updatedInvoiceInfo.setCreateDate(UPDATED_CREATE_DATE);
        InvoiceInfoDTO invoiceInfoDTO = invoiceInfoMapper.invoiceInfoToInvoiceInfoDTO(updatedInvoiceInfo);

        restInvoiceInfoMockMvc.perform(put("/api/invoice-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invoiceInfoDTO)))
                .andExpect(status().isOk());

        // Validate the InvoiceInfo in the database
        List<InvoiceInfo> invoiceInfos = invoiceInfoRepository.findAll();
        assertThat(invoiceInfos).hasSize(databaseSizeBeforeUpdate);
        InvoiceInfo testInvoiceInfo = invoiceInfos.get(invoiceInfos.size() - 1);
        assertThat(testInvoiceInfo.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);

        // Validate the InvoiceInfo in ElasticSearch
        InvoiceInfo invoiceInfoEs = invoiceInfoSearchRepository.findOne(testInvoiceInfo.getId());
        assertThat(invoiceInfoEs).isEqualToComparingFieldByField(testInvoiceInfo);
    }

    @Test
    @Transactional
    public void deleteInvoiceInfo() throws Exception {
        // Initialize the database
        invoiceInfoRepository.saveAndFlush(invoiceInfo);
        invoiceInfoSearchRepository.save(invoiceInfo);
        int databaseSizeBeforeDelete = invoiceInfoRepository.findAll().size();

        // Get the invoiceInfo
        restInvoiceInfoMockMvc.perform(delete("/api/invoice-infos/{id}", invoiceInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean invoiceInfoExistsInEs = invoiceInfoSearchRepository.exists(invoiceInfo.getId());
        assertThat(invoiceInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<InvoiceInfo> invoiceInfos = invoiceInfoRepository.findAll();
        assertThat(invoiceInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInvoiceInfo() throws Exception {
        // Initialize the database
        invoiceInfoRepository.saveAndFlush(invoiceInfo);
        invoiceInfoSearchRepository.save(invoiceInfo);

        // Search the invoiceInfo
        restInvoiceInfoMockMvc.perform(get("/api/_search/invoice-infos?query=id:" + invoiceInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)));
    }
}
