package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.OrderBag;
import com.eyeson.tikon.repository.OrderBagRepository;
import com.eyeson.tikon.service.OrderBagService;
import com.eyeson.tikon.repository.search.OrderBagSearchRepository;
import com.eyeson.tikon.web.rest.dto.OrderBagDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagMapper;

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
 * Test class for the OrderBagResource REST controller.
 *
 * @see OrderBagResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class OrderBagResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Inject
    private OrderBagRepository orderBagRepository;

    @Inject
    private OrderBagMapper orderBagMapper;

    @Inject
    private OrderBagService orderBagService;

    @Inject
    private OrderBagSearchRepository orderBagSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrderBagMockMvc;

    private OrderBag orderBag;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderBagResource orderBagResource = new OrderBagResource();
        ReflectionTestUtils.setField(orderBagResource, "orderBagService", orderBagService);
        ReflectionTestUtils.setField(orderBagResource, "orderBagMapper", orderBagMapper);
        this.restOrderBagMockMvc = MockMvcBuilders.standaloneSetup(orderBagResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        orderBagSearchRepository.deleteAll();
        orderBag = new OrderBag();
        orderBag.setCreateDate(DEFAULT_CREATE_DATE);
        orderBag.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createOrderBag() throws Exception {
        int databaseSizeBeforeCreate = orderBagRepository.findAll().size();

        // Create the OrderBag
        OrderBagDTO orderBagDTO = orderBagMapper.orderBagToOrderBagDTO(orderBag);

        restOrderBagMockMvc.perform(post("/api/order-bags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderBagDTO)))
                .andExpect(status().isCreated());

        // Validate the OrderBag in the database
        List<OrderBag> orderBags = orderBagRepository.findAll();
        assertThat(orderBags).hasSize(databaseSizeBeforeCreate + 1);
        OrderBag testOrderBag = orderBags.get(orderBags.size() - 1);
        assertThat(testOrderBag.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testOrderBag.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the OrderBag in ElasticSearch
        OrderBag orderBagEs = orderBagSearchRepository.findOne(testOrderBag.getId());
        assertThat(orderBagEs).isEqualToComparingFieldByField(testOrderBag);
    }

    @Test
    @Transactional
    public void getAllOrderBags() throws Exception {
        // Initialize the database
        orderBagRepository.saveAndFlush(orderBag);

        // Get all the orderBags
        restOrderBagMockMvc.perform(get("/api/order-bags?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderBag.getId().intValue())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getOrderBag() throws Exception {
        // Initialize the database
        orderBagRepository.saveAndFlush(orderBag);

        // Get the orderBag
        restOrderBagMockMvc.perform(get("/api/order-bags/{id}", orderBag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(orderBag.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingOrderBag() throws Exception {
        // Get the orderBag
        restOrderBagMockMvc.perform(get("/api/order-bags/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderBag() throws Exception {
        // Initialize the database
        orderBagRepository.saveAndFlush(orderBag);
        orderBagSearchRepository.save(orderBag);
        int databaseSizeBeforeUpdate = orderBagRepository.findAll().size();

        // Update the orderBag
        OrderBag updatedOrderBag = new OrderBag();
        updatedOrderBag.setId(orderBag.getId());
        updatedOrderBag.setCreateDate(UPDATED_CREATE_DATE);
        updatedOrderBag.setStatus(UPDATED_STATUS);
        OrderBagDTO orderBagDTO = orderBagMapper.orderBagToOrderBagDTO(updatedOrderBag);

        restOrderBagMockMvc.perform(put("/api/order-bags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderBagDTO)))
                .andExpect(status().isOk());

        // Validate the OrderBag in the database
        List<OrderBag> orderBags = orderBagRepository.findAll();
        assertThat(orderBags).hasSize(databaseSizeBeforeUpdate);
        OrderBag testOrderBag = orderBags.get(orderBags.size() - 1);
        assertThat(testOrderBag.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testOrderBag.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the OrderBag in ElasticSearch
        OrderBag orderBagEs = orderBagSearchRepository.findOne(testOrderBag.getId());
        assertThat(orderBagEs).isEqualToComparingFieldByField(testOrderBag);
    }

    @Test
    @Transactional
    public void deleteOrderBag() throws Exception {
        // Initialize the database
        orderBagRepository.saveAndFlush(orderBag);
        orderBagSearchRepository.save(orderBag);
        int databaseSizeBeforeDelete = orderBagRepository.findAll().size();

        // Get the orderBag
        restOrderBagMockMvc.perform(delete("/api/order-bags/{id}", orderBag.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean orderBagExistsInEs = orderBagSearchRepository.exists(orderBag.getId());
        assertThat(orderBagExistsInEs).isFalse();

        // Validate the database is empty
        List<OrderBag> orderBags = orderBagRepository.findAll();
        assertThat(orderBags).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrderBag() throws Exception {
        // Initialize the database
        orderBagRepository.saveAndFlush(orderBag);
        orderBagSearchRepository.save(orderBag);

        // Search the orderBag
        restOrderBagMockMvc.perform(get("/api/_search/order-bags?query=id:" + orderBag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderBag.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
}
