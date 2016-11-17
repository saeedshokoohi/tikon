package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.OrderBagServiceItemDtail;
import com.eyeson.tikon.repository.OrderBagServiceItemDtailRepository;
import com.eyeson.tikon.service.OrderBagServiceItemDtailService;
import com.eyeson.tikon.repository.search.OrderBagServiceItemDtailSearchRepository;
import com.eyeson.tikon.web.rest.dto.OrderBagServiceItemDtailDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagServiceItemDtailMapper;

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
 * Test class for the OrderBagServiceItemDtailResource REST controller.
 *
 * @see OrderBagServiceItemDtailResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class OrderBagServiceItemDtailResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_RESERVE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_RESERVE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_RESERVE_TIME_STR = dateTimeFormatter.format(DEFAULT_RESERVE_TIME);

    private static final Double DEFAULT_GTY = 1D;
    private static final Double UPDATED_GTY = 2D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Double DEFAULT_VAT = 1D;
    private static final Double UPDATED_VAT = 2D;

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    @Inject
    private OrderBagServiceItemDtailRepository orderBagServiceItemDtailRepository;

    @Inject
    private OrderBagServiceItemDtailMapper orderBagServiceItemDtailMapper;

    @Inject
    private OrderBagServiceItemDtailService orderBagServiceItemDtailService;

    @Inject
    private OrderBagServiceItemDtailSearchRepository orderBagServiceItemDtailSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrderBagServiceItemDtailMockMvc;

    private OrderBagServiceItemDtail orderBagServiceItemDtail;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderBagServiceItemDtailResource orderBagServiceItemDtailResource = new OrderBagServiceItemDtailResource();
        ReflectionTestUtils.setField(orderBagServiceItemDtailResource, "orderBagServiceItemDtailService", orderBagServiceItemDtailService);
        ReflectionTestUtils.setField(orderBagServiceItemDtailResource, "orderBagServiceItemDtailMapper", orderBagServiceItemDtailMapper);
        this.restOrderBagServiceItemDtailMockMvc = MockMvcBuilders.standaloneSetup(orderBagServiceItemDtailResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        orderBagServiceItemDtailSearchRepository.deleteAll();
        orderBagServiceItemDtail = new OrderBagServiceItemDtail();
        orderBagServiceItemDtail.setReserveTime(DEFAULT_RESERVE_TIME);
        orderBagServiceItemDtail.setGty(DEFAULT_GTY);
        orderBagServiceItemDtail.setPrice(DEFAULT_PRICE);
        orderBagServiceItemDtail.setDiscount(DEFAULT_DISCOUNT);
        orderBagServiceItemDtail.setVat(DEFAULT_VAT);
        orderBagServiceItemDtail.setTotalPrice(DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void createOrderBagServiceItemDtail() throws Exception {
        int databaseSizeBeforeCreate = orderBagServiceItemDtailRepository.findAll().size();

        // Create the OrderBagServiceItemDtail
        OrderBagServiceItemDtailDTO orderBagServiceItemDtailDTO = orderBagServiceItemDtailMapper.orderBagServiceItemDtailToOrderBagServiceItemDtailDTO(orderBagServiceItemDtail);

        restOrderBagServiceItemDtailMockMvc.perform(post("/api/order-bag-service-item-dtails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderBagServiceItemDtailDTO)))
                .andExpect(status().isCreated());

        // Validate the OrderBagServiceItemDtail in the database
        List<OrderBagServiceItemDtail> orderBagServiceItemDtails = orderBagServiceItemDtailRepository.findAll();
        assertThat(orderBagServiceItemDtails).hasSize(databaseSizeBeforeCreate + 1);
        OrderBagServiceItemDtail testOrderBagServiceItemDtail = orderBagServiceItemDtails.get(orderBagServiceItemDtails.size() - 1);
        assertThat(testOrderBagServiceItemDtail.getReserveTime()).isEqualTo(DEFAULT_RESERVE_TIME);
        assertThat(testOrderBagServiceItemDtail.getGty()).isEqualTo(DEFAULT_GTY);
        assertThat(testOrderBagServiceItemDtail.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrderBagServiceItemDtail.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testOrderBagServiceItemDtail.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testOrderBagServiceItemDtail.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);

        // Validate the OrderBagServiceItemDtail in ElasticSearch
        OrderBagServiceItemDtail orderBagServiceItemDtailEs = orderBagServiceItemDtailSearchRepository.findOne(testOrderBagServiceItemDtail.getId());
        assertThat(orderBagServiceItemDtailEs).isEqualToComparingFieldByField(testOrderBagServiceItemDtail);
    }

    @Test
    @Transactional
    public void getAllOrderBagServiceItemDtails() throws Exception {
        // Initialize the database
        orderBagServiceItemDtailRepository.saveAndFlush(orderBagServiceItemDtail);

        // Get all the orderBagServiceItemDtails
        restOrderBagServiceItemDtailMockMvc.perform(get("/api/order-bag-service-item-dtails?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderBagServiceItemDtail.getId().intValue())))
                .andExpect(jsonPath("$.[*].reserveTime").value(hasItem(DEFAULT_RESERVE_TIME_STR)))
                .andExpect(jsonPath("$.[*].gty").value(hasItem(DEFAULT_GTY.doubleValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT.doubleValue())))
                .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getOrderBagServiceItemDtail() throws Exception {
        // Initialize the database
        orderBagServiceItemDtailRepository.saveAndFlush(orderBagServiceItemDtail);

        // Get the orderBagServiceItemDtail
        restOrderBagServiceItemDtailMockMvc.perform(get("/api/order-bag-service-item-dtails/{id}", orderBagServiceItemDtail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(orderBagServiceItemDtail.getId().intValue()))
            .andExpect(jsonPath("$.reserveTime").value(DEFAULT_RESERVE_TIME_STR))
            .andExpect(jsonPath("$.gty").value(DEFAULT_GTY.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.vat").value(DEFAULT_VAT.doubleValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderBagServiceItemDtail() throws Exception {
        // Get the orderBagServiceItemDtail
        restOrderBagServiceItemDtailMockMvc.perform(get("/api/order-bag-service-item-dtails/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderBagServiceItemDtail() throws Exception {
        // Initialize the database
        orderBagServiceItemDtailRepository.saveAndFlush(orderBagServiceItemDtail);
        orderBagServiceItemDtailSearchRepository.save(orderBagServiceItemDtail);
        int databaseSizeBeforeUpdate = orderBagServiceItemDtailRepository.findAll().size();

        // Update the orderBagServiceItemDtail
        OrderBagServiceItemDtail updatedOrderBagServiceItemDtail = new OrderBagServiceItemDtail();
        updatedOrderBagServiceItemDtail.setId(orderBagServiceItemDtail.getId());
        updatedOrderBagServiceItemDtail.setReserveTime(UPDATED_RESERVE_TIME);
        updatedOrderBagServiceItemDtail.setGty(UPDATED_GTY);
        updatedOrderBagServiceItemDtail.setPrice(UPDATED_PRICE);
        updatedOrderBagServiceItemDtail.setDiscount(UPDATED_DISCOUNT);
        updatedOrderBagServiceItemDtail.setVat(UPDATED_VAT);
        updatedOrderBagServiceItemDtail.setTotalPrice(UPDATED_TOTAL_PRICE);
        OrderBagServiceItemDtailDTO orderBagServiceItemDtailDTO = orderBagServiceItemDtailMapper.orderBagServiceItemDtailToOrderBagServiceItemDtailDTO(updatedOrderBagServiceItemDtail);

        restOrderBagServiceItemDtailMockMvc.perform(put("/api/order-bag-service-item-dtails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderBagServiceItemDtailDTO)))
                .andExpect(status().isOk());

        // Validate the OrderBagServiceItemDtail in the database
        List<OrderBagServiceItemDtail> orderBagServiceItemDtails = orderBagServiceItemDtailRepository.findAll();
        assertThat(orderBagServiceItemDtails).hasSize(databaseSizeBeforeUpdate);
        OrderBagServiceItemDtail testOrderBagServiceItemDtail = orderBagServiceItemDtails.get(orderBagServiceItemDtails.size() - 1);
        assertThat(testOrderBagServiceItemDtail.getReserveTime()).isEqualTo(UPDATED_RESERVE_TIME);
        assertThat(testOrderBagServiceItemDtail.getGty()).isEqualTo(UPDATED_GTY);
        assertThat(testOrderBagServiceItemDtail.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrderBagServiceItemDtail.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOrderBagServiceItemDtail.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testOrderBagServiceItemDtail.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);

        // Validate the OrderBagServiceItemDtail in ElasticSearch
        OrderBagServiceItemDtail orderBagServiceItemDtailEs = orderBagServiceItemDtailSearchRepository.findOne(testOrderBagServiceItemDtail.getId());
        assertThat(orderBagServiceItemDtailEs).isEqualToComparingFieldByField(testOrderBagServiceItemDtail);
    }

    @Test
    @Transactional
    public void deleteOrderBagServiceItemDtail() throws Exception {
        // Initialize the database
        orderBagServiceItemDtailRepository.saveAndFlush(orderBagServiceItemDtail);
        orderBagServiceItemDtailSearchRepository.save(orderBagServiceItemDtail);
        int databaseSizeBeforeDelete = orderBagServiceItemDtailRepository.findAll().size();

        // Get the orderBagServiceItemDtail
        restOrderBagServiceItemDtailMockMvc.perform(delete("/api/order-bag-service-item-dtails/{id}", orderBagServiceItemDtail.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean orderBagServiceItemDtailExistsInEs = orderBagServiceItemDtailSearchRepository.exists(orderBagServiceItemDtail.getId());
        assertThat(orderBagServiceItemDtailExistsInEs).isFalse();

        // Validate the database is empty
        List<OrderBagServiceItemDtail> orderBagServiceItemDtails = orderBagServiceItemDtailRepository.findAll();
        assertThat(orderBagServiceItemDtails).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrderBagServiceItemDtail() throws Exception {
        // Initialize the database
        orderBagServiceItemDtailRepository.saveAndFlush(orderBagServiceItemDtail);
        orderBagServiceItemDtailSearchRepository.save(orderBagServiceItemDtail);

        // Search the orderBagServiceItemDtail
        restOrderBagServiceItemDtailMockMvc.perform(get("/api/_search/order-bag-service-item-dtails?query=id:" + orderBagServiceItemDtail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderBagServiceItemDtail.getId().intValue())))
            .andExpect(jsonPath("$.[*].reserveTime").value(hasItem(DEFAULT_RESERVE_TIME_STR)))
            .andExpect(jsonPath("$.[*].gty").value(hasItem(DEFAULT_GTY.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())));
    }
}
