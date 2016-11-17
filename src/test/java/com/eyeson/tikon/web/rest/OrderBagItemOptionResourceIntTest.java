package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.OrderBagItemOption;
import com.eyeson.tikon.repository.OrderBagItemOptionRepository;
import com.eyeson.tikon.service.OrderBagItemOptionService;
import com.eyeson.tikon.repository.search.OrderBagItemOptionSearchRepository;
import com.eyeson.tikon.web.rest.dto.OrderBagItemOptionDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagItemOptionMapper;

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
 * Test class for the OrderBagItemOptionResource REST controller.
 *
 * @see OrderBagItemOptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class OrderBagItemOptionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_RESERVE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_RESERVE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_RESERVE_TIME_STR = dateTimeFormatter.format(DEFAULT_RESERVE_TIME);

    private static final Double DEFAULT_QTY = 1D;
    private static final Double UPDATED_QTY = 2D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Double DEFAULT_VAT = 1D;
    private static final Double UPDATED_VAT = 2D;

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    @Inject
    private OrderBagItemOptionRepository orderBagItemOptionRepository;

    @Inject
    private OrderBagItemOptionMapper orderBagItemOptionMapper;

    @Inject
    private OrderBagItemOptionService orderBagItemOptionService;

    @Inject
    private OrderBagItemOptionSearchRepository orderBagItemOptionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrderBagItemOptionMockMvc;

    private OrderBagItemOption orderBagItemOption;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderBagItemOptionResource orderBagItemOptionResource = new OrderBagItemOptionResource();
        ReflectionTestUtils.setField(orderBagItemOptionResource, "orderBagItemOptionService", orderBagItemOptionService);
        ReflectionTestUtils.setField(orderBagItemOptionResource, "orderBagItemOptionMapper", orderBagItemOptionMapper);
        this.restOrderBagItemOptionMockMvc = MockMvcBuilders.standaloneSetup(orderBagItemOptionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        orderBagItemOptionSearchRepository.deleteAll();
        orderBagItemOption = new OrderBagItemOption();
        orderBagItemOption.setReserveTime(DEFAULT_RESERVE_TIME);
        orderBagItemOption.setQty(DEFAULT_QTY);
        orderBagItemOption.setPrice(DEFAULT_PRICE);
        orderBagItemOption.setDiscount(DEFAULT_DISCOUNT);
        orderBagItemOption.setVat(DEFAULT_VAT);
        orderBagItemOption.setTotalPrice(DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void createOrderBagItemOption() throws Exception {
        int databaseSizeBeforeCreate = orderBagItemOptionRepository.findAll().size();

        // Create the OrderBagItemOption
        OrderBagItemOptionDTO orderBagItemOptionDTO = orderBagItemOptionMapper.orderBagItemOptionToOrderBagItemOptionDTO(orderBagItemOption);

        restOrderBagItemOptionMockMvc.perform(post("/api/order-bag-item-options")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderBagItemOptionDTO)))
                .andExpect(status().isCreated());

        // Validate the OrderBagItemOption in the database
        List<OrderBagItemOption> orderBagItemOptions = orderBagItemOptionRepository.findAll();
        assertThat(orderBagItemOptions).hasSize(databaseSizeBeforeCreate + 1);
        OrderBagItemOption testOrderBagItemOption = orderBagItemOptions.get(orderBagItemOptions.size() - 1);
        assertThat(testOrderBagItemOption.getReserveTime()).isEqualTo(DEFAULT_RESERVE_TIME);
        assertThat(testOrderBagItemOption.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testOrderBagItemOption.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrderBagItemOption.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testOrderBagItemOption.getVat()).isEqualTo(DEFAULT_VAT);
        assertThat(testOrderBagItemOption.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);

        // Validate the OrderBagItemOption in ElasticSearch
        OrderBagItemOption orderBagItemOptionEs = orderBagItemOptionSearchRepository.findOne(testOrderBagItemOption.getId());
        assertThat(orderBagItemOptionEs).isEqualToComparingFieldByField(testOrderBagItemOption);
    }

    @Test
    @Transactional
    public void getAllOrderBagItemOptions() throws Exception {
        // Initialize the database
        orderBagItemOptionRepository.saveAndFlush(orderBagItemOption);

        // Get all the orderBagItemOptions
        restOrderBagItemOptionMockMvc.perform(get("/api/order-bag-item-options?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderBagItemOption.getId().intValue())))
                .andExpect(jsonPath("$.[*].reserveTime").value(hasItem(DEFAULT_RESERVE_TIME_STR)))
                .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY.doubleValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT.doubleValue())))
                .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getOrderBagItemOption() throws Exception {
        // Initialize the database
        orderBagItemOptionRepository.saveAndFlush(orderBagItemOption);

        // Get the orderBagItemOption
        restOrderBagItemOptionMockMvc.perform(get("/api/order-bag-item-options/{id}", orderBagItemOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(orderBagItemOption.getId().intValue()))
            .andExpect(jsonPath("$.reserveTime").value(DEFAULT_RESERVE_TIME_STR))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.vat").value(DEFAULT_VAT.doubleValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderBagItemOption() throws Exception {
        // Get the orderBagItemOption
        restOrderBagItemOptionMockMvc.perform(get("/api/order-bag-item-options/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderBagItemOption() throws Exception {
        // Initialize the database
        orderBagItemOptionRepository.saveAndFlush(orderBagItemOption);
        orderBagItemOptionSearchRepository.save(orderBagItemOption);
        int databaseSizeBeforeUpdate = orderBagItemOptionRepository.findAll().size();

        // Update the orderBagItemOption
        OrderBagItemOption updatedOrderBagItemOption = new OrderBagItemOption();
        updatedOrderBagItemOption.setId(orderBagItemOption.getId());
        updatedOrderBagItemOption.setReserveTime(UPDATED_RESERVE_TIME);
        updatedOrderBagItemOption.setQty(UPDATED_QTY);
        updatedOrderBagItemOption.setPrice(UPDATED_PRICE);
        updatedOrderBagItemOption.setDiscount(UPDATED_DISCOUNT);
        updatedOrderBagItemOption.setVat(UPDATED_VAT);
        updatedOrderBagItemOption.setTotalPrice(UPDATED_TOTAL_PRICE);
        OrderBagItemOptionDTO orderBagItemOptionDTO = orderBagItemOptionMapper.orderBagItemOptionToOrderBagItemOptionDTO(updatedOrderBagItemOption);

        restOrderBagItemOptionMockMvc.perform(put("/api/order-bag-item-options")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderBagItemOptionDTO)))
                .andExpect(status().isOk());

        // Validate the OrderBagItemOption in the database
        List<OrderBagItemOption> orderBagItemOptions = orderBagItemOptionRepository.findAll();
        assertThat(orderBagItemOptions).hasSize(databaseSizeBeforeUpdate);
        OrderBagItemOption testOrderBagItemOption = orderBagItemOptions.get(orderBagItemOptions.size() - 1);
        assertThat(testOrderBagItemOption.getReserveTime()).isEqualTo(UPDATED_RESERVE_TIME);
        assertThat(testOrderBagItemOption.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testOrderBagItemOption.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrderBagItemOption.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOrderBagItemOption.getVat()).isEqualTo(UPDATED_VAT);
        assertThat(testOrderBagItemOption.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);

        // Validate the OrderBagItemOption in ElasticSearch
        OrderBagItemOption orderBagItemOptionEs = orderBagItemOptionSearchRepository.findOne(testOrderBagItemOption.getId());
        assertThat(orderBagItemOptionEs).isEqualToComparingFieldByField(testOrderBagItemOption);
    }

    @Test
    @Transactional
    public void deleteOrderBagItemOption() throws Exception {
        // Initialize the database
        orderBagItemOptionRepository.saveAndFlush(orderBagItemOption);
        orderBagItemOptionSearchRepository.save(orderBagItemOption);
        int databaseSizeBeforeDelete = orderBagItemOptionRepository.findAll().size();

        // Get the orderBagItemOption
        restOrderBagItemOptionMockMvc.perform(delete("/api/order-bag-item-options/{id}", orderBagItemOption.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean orderBagItemOptionExistsInEs = orderBagItemOptionSearchRepository.exists(orderBagItemOption.getId());
        assertThat(orderBagItemOptionExistsInEs).isFalse();

        // Validate the database is empty
        List<OrderBagItemOption> orderBagItemOptions = orderBagItemOptionRepository.findAll();
        assertThat(orderBagItemOptions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrderBagItemOption() throws Exception {
        // Initialize the database
        orderBagItemOptionRepository.saveAndFlush(orderBagItemOption);
        orderBagItemOptionSearchRepository.save(orderBagItemOption);

        // Search the orderBagItemOption
        restOrderBagItemOptionMockMvc.perform(get("/api/_search/order-bag-item-options?query=id:" + orderBagItemOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderBagItemOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].reserveTime").value(hasItem(DEFAULT_RESERVE_TIME_STR)))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].vat").value(hasItem(DEFAULT_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())));
    }
}
