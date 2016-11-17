package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.PriceInfoDtail;
import com.eyeson.tikon.repository.PriceInfoDtailRepository;
import com.eyeson.tikon.service.PriceInfoDtailService;
import com.eyeson.tikon.repository.search.PriceInfoDtailSearchRepository;
import com.eyeson.tikon.web.rest.dto.PriceInfoDtailDTO;
import com.eyeson.tikon.web.rest.mapper.PriceInfoDtailMapper;

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
 * Test class for the PriceInfoDtailResource REST controller.
 *
 * @see PriceInfoDtailResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class PriceInfoDtailResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final Integer DEFAULT_CAPACITY_RATIO = 1;
    private static final Integer UPDATED_CAPACITY_RATIO = 2;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_COWORKER_PRICE = 1D;
    private static final Double UPDATED_COWORKER_PRICE = 2D;

    @Inject
    private PriceInfoDtailRepository priceInfoDtailRepository;

    @Inject
    private PriceInfoDtailMapper priceInfoDtailMapper;

    @Inject
    private PriceInfoDtailService priceInfoDtailService;

    @Inject
    private PriceInfoDtailSearchRepository priceInfoDtailSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPriceInfoDtailMockMvc;

    private PriceInfoDtail priceInfoDtail;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PriceInfoDtailResource priceInfoDtailResource = new PriceInfoDtailResource();
        ReflectionTestUtils.setField(priceInfoDtailResource, "priceInfoDtailService", priceInfoDtailService);
        ReflectionTestUtils.setField(priceInfoDtailResource, "priceInfoDtailMapper", priceInfoDtailMapper);
        this.restPriceInfoDtailMockMvc = MockMvcBuilders.standaloneSetup(priceInfoDtailResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        priceInfoDtailSearchRepository.deleteAll();
        priceInfoDtail = new PriceInfoDtail();
        priceInfoDtail.setTitle(DEFAULT_TITLE);
        priceInfoDtail.setCapacityRatio(DEFAULT_CAPACITY_RATIO);
        priceInfoDtail.setPrice(DEFAULT_PRICE);
        priceInfoDtail.setCoworkerPrice(DEFAULT_COWORKER_PRICE);
    }

    @Test
    @Transactional
    public void createPriceInfoDtail() throws Exception {
        int databaseSizeBeforeCreate = priceInfoDtailRepository.findAll().size();

        // Create the PriceInfoDtail
        PriceInfoDtailDTO priceInfoDtailDTO = priceInfoDtailMapper.priceInfoDtailToPriceInfoDtailDTO(priceInfoDtail);

        restPriceInfoDtailMockMvc.perform(post("/api/price-info-dtails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(priceInfoDtailDTO)))
                .andExpect(status().isCreated());

        // Validate the PriceInfoDtail in the database
        List<PriceInfoDtail> priceInfoDtails = priceInfoDtailRepository.findAll();
        assertThat(priceInfoDtails).hasSize(databaseSizeBeforeCreate + 1);
        PriceInfoDtail testPriceInfoDtail = priceInfoDtails.get(priceInfoDtails.size() - 1);
        assertThat(testPriceInfoDtail.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPriceInfoDtail.getCapacityRatio()).isEqualTo(DEFAULT_CAPACITY_RATIO);
        assertThat(testPriceInfoDtail.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPriceInfoDtail.getCoworkerPrice()).isEqualTo(DEFAULT_COWORKER_PRICE);

        // Validate the PriceInfoDtail in ElasticSearch
        PriceInfoDtail priceInfoDtailEs = priceInfoDtailSearchRepository.findOne(testPriceInfoDtail.getId());
        assertThat(priceInfoDtailEs).isEqualToComparingFieldByField(testPriceInfoDtail);
    }

    @Test
    @Transactional
    public void getAllPriceInfoDtails() throws Exception {
        // Initialize the database
        priceInfoDtailRepository.saveAndFlush(priceInfoDtail);

        // Get all the priceInfoDtails
        restPriceInfoDtailMockMvc.perform(get("/api/price-info-dtails?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(priceInfoDtail.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].capacityRatio").value(hasItem(DEFAULT_CAPACITY_RATIO)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].coworkerPrice").value(hasItem(DEFAULT_COWORKER_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getPriceInfoDtail() throws Exception {
        // Initialize the database
        priceInfoDtailRepository.saveAndFlush(priceInfoDtail);

        // Get the priceInfoDtail
        restPriceInfoDtailMockMvc.perform(get("/api/price-info-dtails/{id}", priceInfoDtail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(priceInfoDtail.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.capacityRatio").value(DEFAULT_CAPACITY_RATIO))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.coworkerPrice").value(DEFAULT_COWORKER_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPriceInfoDtail() throws Exception {
        // Get the priceInfoDtail
        restPriceInfoDtailMockMvc.perform(get("/api/price-info-dtails/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePriceInfoDtail() throws Exception {
        // Initialize the database
        priceInfoDtailRepository.saveAndFlush(priceInfoDtail);
        priceInfoDtailSearchRepository.save(priceInfoDtail);
        int databaseSizeBeforeUpdate = priceInfoDtailRepository.findAll().size();

        // Update the priceInfoDtail
        PriceInfoDtail updatedPriceInfoDtail = new PriceInfoDtail();
        updatedPriceInfoDtail.setId(priceInfoDtail.getId());
        updatedPriceInfoDtail.setTitle(UPDATED_TITLE);
        updatedPriceInfoDtail.setCapacityRatio(UPDATED_CAPACITY_RATIO);
        updatedPriceInfoDtail.setPrice(UPDATED_PRICE);
        updatedPriceInfoDtail.setCoworkerPrice(UPDATED_COWORKER_PRICE);
        PriceInfoDtailDTO priceInfoDtailDTO = priceInfoDtailMapper.priceInfoDtailToPriceInfoDtailDTO(updatedPriceInfoDtail);

        restPriceInfoDtailMockMvc.perform(put("/api/price-info-dtails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(priceInfoDtailDTO)))
                .andExpect(status().isOk());

        // Validate the PriceInfoDtail in the database
        List<PriceInfoDtail> priceInfoDtails = priceInfoDtailRepository.findAll();
        assertThat(priceInfoDtails).hasSize(databaseSizeBeforeUpdate);
        PriceInfoDtail testPriceInfoDtail = priceInfoDtails.get(priceInfoDtails.size() - 1);
        assertThat(testPriceInfoDtail.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPriceInfoDtail.getCapacityRatio()).isEqualTo(UPDATED_CAPACITY_RATIO);
        assertThat(testPriceInfoDtail.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPriceInfoDtail.getCoworkerPrice()).isEqualTo(UPDATED_COWORKER_PRICE);

        // Validate the PriceInfoDtail in ElasticSearch
        PriceInfoDtail priceInfoDtailEs = priceInfoDtailSearchRepository.findOne(testPriceInfoDtail.getId());
        assertThat(priceInfoDtailEs).isEqualToComparingFieldByField(testPriceInfoDtail);
    }

    @Test
    @Transactional
    public void deletePriceInfoDtail() throws Exception {
        // Initialize the database
        priceInfoDtailRepository.saveAndFlush(priceInfoDtail);
        priceInfoDtailSearchRepository.save(priceInfoDtail);
        int databaseSizeBeforeDelete = priceInfoDtailRepository.findAll().size();

        // Get the priceInfoDtail
        restPriceInfoDtailMockMvc.perform(delete("/api/price-info-dtails/{id}", priceInfoDtail.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean priceInfoDtailExistsInEs = priceInfoDtailSearchRepository.exists(priceInfoDtail.getId());
        assertThat(priceInfoDtailExistsInEs).isFalse();

        // Validate the database is empty
        List<PriceInfoDtail> priceInfoDtails = priceInfoDtailRepository.findAll();
        assertThat(priceInfoDtails).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPriceInfoDtail() throws Exception {
        // Initialize the database
        priceInfoDtailRepository.saveAndFlush(priceInfoDtail);
        priceInfoDtailSearchRepository.save(priceInfoDtail);

        // Search the priceInfoDtail
        restPriceInfoDtailMockMvc.perform(get("/api/_search/price-info-dtails?query=id:" + priceInfoDtail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceInfoDtail.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].capacityRatio").value(hasItem(DEFAULT_CAPACITY_RATIO)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].coworkerPrice").value(hasItem(DEFAULT_COWORKER_PRICE.doubleValue())));
    }
}
