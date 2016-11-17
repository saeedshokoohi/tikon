package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.OrderBagServiceItem;
import com.eyeson.tikon.repository.OrderBagServiceItemRepository;
import com.eyeson.tikon.service.OrderBagServiceItemService;
import com.eyeson.tikon.repository.search.OrderBagServiceItemSearchRepository;
import com.eyeson.tikon.web.rest.dto.OrderBagServiceItemDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagServiceItemMapper;

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
 * Test class for the OrderBagServiceItemResource REST controller.
 *
 * @see OrderBagServiceItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class OrderBagServiceItemResourceIntTest {


    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Double DEFAULT_SUBTOTAL_SERVICE_PRICE = 1D;
    private static final Double UPDATED_SUBTOTAL_SERVICE_PRICE = 2D;

    private static final Double DEFAULT_SUBTOTAL_OPTION_PRICE = 1D;
    private static final Double UPDATED_SUBTOTAL_OPTION_PRICE = 2D;

    private static final Double DEFAULT_SUBTOTAL_VAT = 1D;
    private static final Double UPDATED_SUBTOTAL_VAT = 2D;

    private static final Double DEFAULT_SUBTOTAL_DISCOUNT = 1D;
    private static final Double UPDATED_SUBTOTAL_DISCOUNT = 2D;

    private static final Double DEFAULT_TOTAL_ROW_PRICE = 1D;
    private static final Double UPDATED_TOTAL_ROW_PRICE = 2D;

    @Inject
    private OrderBagServiceItemRepository orderBagServiceItemRepository;

    @Inject
    private OrderBagServiceItemMapper orderBagServiceItemMapper;

    @Inject
    private OrderBagServiceItemService orderBagServiceItemService;

    @Inject
    private OrderBagServiceItemSearchRepository orderBagServiceItemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrderBagServiceItemMockMvc;

    private OrderBagServiceItem orderBagServiceItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderBagServiceItemResource orderBagServiceItemResource = new OrderBagServiceItemResource();
        ReflectionTestUtils.setField(orderBagServiceItemResource, "orderBagServiceItemService", orderBagServiceItemService);
        ReflectionTestUtils.setField(orderBagServiceItemResource, "orderBagServiceItemMapper", orderBagServiceItemMapper);
        this.restOrderBagServiceItemMockMvc = MockMvcBuilders.standaloneSetup(orderBagServiceItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        orderBagServiceItemSearchRepository.deleteAll();
        orderBagServiceItem = new OrderBagServiceItem();
        orderBagServiceItem.setStatus(DEFAULT_STATUS);
        orderBagServiceItem.setSubtotalServicePrice(DEFAULT_SUBTOTAL_SERVICE_PRICE);
        orderBagServiceItem.setSubtotalOptionPrice(DEFAULT_SUBTOTAL_OPTION_PRICE);
        orderBagServiceItem.setSubtotalVAT(DEFAULT_SUBTOTAL_VAT);
        orderBagServiceItem.setSubtotalDiscount(DEFAULT_SUBTOTAL_DISCOUNT);
        orderBagServiceItem.setTotalRowPrice(DEFAULT_TOTAL_ROW_PRICE);
    }

    @Test
    @Transactional
    public void createOrderBagServiceItem() throws Exception {
        int databaseSizeBeforeCreate = orderBagServiceItemRepository.findAll().size();

        // Create the OrderBagServiceItem
        OrderBagServiceItemDTO orderBagServiceItemDTO = orderBagServiceItemMapper.orderBagServiceItemToOrderBagServiceItemDTO(orderBagServiceItem);

        restOrderBagServiceItemMockMvc.perform(post("/api/order-bag-service-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderBagServiceItemDTO)))
                .andExpect(status().isCreated());

        // Validate the OrderBagServiceItem in the database
        List<OrderBagServiceItem> orderBagServiceItems = orderBagServiceItemRepository.findAll();
        assertThat(orderBagServiceItems).hasSize(databaseSizeBeforeCreate + 1);
        OrderBagServiceItem testOrderBagServiceItem = orderBagServiceItems.get(orderBagServiceItems.size() - 1);
        assertThat(testOrderBagServiceItem.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrderBagServiceItem.getSubtotalServicePrice()).isEqualTo(DEFAULT_SUBTOTAL_SERVICE_PRICE);
        assertThat(testOrderBagServiceItem.getSubtotalOptionPrice()).isEqualTo(DEFAULT_SUBTOTAL_OPTION_PRICE);
        assertThat(testOrderBagServiceItem.getSubtotalVAT()).isEqualTo(DEFAULT_SUBTOTAL_VAT);
        assertThat(testOrderBagServiceItem.getSubtotalDiscount()).isEqualTo(DEFAULT_SUBTOTAL_DISCOUNT);
        assertThat(testOrderBagServiceItem.getTotalRowPrice()).isEqualTo(DEFAULT_TOTAL_ROW_PRICE);

        // Validate the OrderBagServiceItem in ElasticSearch
        OrderBagServiceItem orderBagServiceItemEs = orderBagServiceItemSearchRepository.findOne(testOrderBagServiceItem.getId());
        assertThat(orderBagServiceItemEs).isEqualToComparingFieldByField(testOrderBagServiceItem);
    }

    @Test
    @Transactional
    public void getAllOrderBagServiceItems() throws Exception {
        // Initialize the database
        orderBagServiceItemRepository.saveAndFlush(orderBagServiceItem);

        // Get all the orderBagServiceItems
        restOrderBagServiceItemMockMvc.perform(get("/api/order-bag-service-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderBagServiceItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
                .andExpect(jsonPath("$.[*].subtotalServicePrice").value(hasItem(DEFAULT_SUBTOTAL_SERVICE_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].subtotalOptionPrice").value(hasItem(DEFAULT_SUBTOTAL_OPTION_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].subtotalVAT").value(hasItem(DEFAULT_SUBTOTAL_VAT.doubleValue())))
                .andExpect(jsonPath("$.[*].subtotalDiscount").value(hasItem(DEFAULT_SUBTOTAL_DISCOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].totalRowPrice").value(hasItem(DEFAULT_TOTAL_ROW_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getOrderBagServiceItem() throws Exception {
        // Initialize the database
        orderBagServiceItemRepository.saveAndFlush(orderBagServiceItem);

        // Get the orderBagServiceItem
        restOrderBagServiceItemMockMvc.perform(get("/api/order-bag-service-items/{id}", orderBagServiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(orderBagServiceItem.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.subtotalServicePrice").value(DEFAULT_SUBTOTAL_SERVICE_PRICE.doubleValue()))
            .andExpect(jsonPath("$.subtotalOptionPrice").value(DEFAULT_SUBTOTAL_OPTION_PRICE.doubleValue()))
            .andExpect(jsonPath("$.subtotalVAT").value(DEFAULT_SUBTOTAL_VAT.doubleValue()))
            .andExpect(jsonPath("$.subtotalDiscount").value(DEFAULT_SUBTOTAL_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.totalRowPrice").value(DEFAULT_TOTAL_ROW_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrderBagServiceItem() throws Exception {
        // Get the orderBagServiceItem
        restOrderBagServiceItemMockMvc.perform(get("/api/order-bag-service-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderBagServiceItem() throws Exception {
        // Initialize the database
        orderBagServiceItemRepository.saveAndFlush(orderBagServiceItem);
        orderBagServiceItemSearchRepository.save(orderBagServiceItem);
        int databaseSizeBeforeUpdate = orderBagServiceItemRepository.findAll().size();

        // Update the orderBagServiceItem
        OrderBagServiceItem updatedOrderBagServiceItem = new OrderBagServiceItem();
        updatedOrderBagServiceItem.setId(orderBagServiceItem.getId());
        updatedOrderBagServiceItem.setStatus(UPDATED_STATUS);
        updatedOrderBagServiceItem.setSubtotalServicePrice(UPDATED_SUBTOTAL_SERVICE_PRICE);
        updatedOrderBagServiceItem.setSubtotalOptionPrice(UPDATED_SUBTOTAL_OPTION_PRICE);
        updatedOrderBagServiceItem.setSubtotalVAT(UPDATED_SUBTOTAL_VAT);
        updatedOrderBagServiceItem.setSubtotalDiscount(UPDATED_SUBTOTAL_DISCOUNT);
        updatedOrderBagServiceItem.setTotalRowPrice(UPDATED_TOTAL_ROW_PRICE);
        OrderBagServiceItemDTO orderBagServiceItemDTO = orderBagServiceItemMapper.orderBagServiceItemToOrderBagServiceItemDTO(updatedOrderBagServiceItem);

        restOrderBagServiceItemMockMvc.perform(put("/api/order-bag-service-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderBagServiceItemDTO)))
                .andExpect(status().isOk());

        // Validate the OrderBagServiceItem in the database
        List<OrderBagServiceItem> orderBagServiceItems = orderBagServiceItemRepository.findAll();
        assertThat(orderBagServiceItems).hasSize(databaseSizeBeforeUpdate);
        OrderBagServiceItem testOrderBagServiceItem = orderBagServiceItems.get(orderBagServiceItems.size() - 1);
        assertThat(testOrderBagServiceItem.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrderBagServiceItem.getSubtotalServicePrice()).isEqualTo(UPDATED_SUBTOTAL_SERVICE_PRICE);
        assertThat(testOrderBagServiceItem.getSubtotalOptionPrice()).isEqualTo(UPDATED_SUBTOTAL_OPTION_PRICE);
        assertThat(testOrderBagServiceItem.getSubtotalVAT()).isEqualTo(UPDATED_SUBTOTAL_VAT);
        assertThat(testOrderBagServiceItem.getSubtotalDiscount()).isEqualTo(UPDATED_SUBTOTAL_DISCOUNT);
        assertThat(testOrderBagServiceItem.getTotalRowPrice()).isEqualTo(UPDATED_TOTAL_ROW_PRICE);

        // Validate the OrderBagServiceItem in ElasticSearch
        OrderBagServiceItem orderBagServiceItemEs = orderBagServiceItemSearchRepository.findOne(testOrderBagServiceItem.getId());
        assertThat(orderBagServiceItemEs).isEqualToComparingFieldByField(testOrderBagServiceItem);
    }

    @Test
    @Transactional
    public void deleteOrderBagServiceItem() throws Exception {
        // Initialize the database
        orderBagServiceItemRepository.saveAndFlush(orderBagServiceItem);
        orderBagServiceItemSearchRepository.save(orderBagServiceItem);
        int databaseSizeBeforeDelete = orderBagServiceItemRepository.findAll().size();

        // Get the orderBagServiceItem
        restOrderBagServiceItemMockMvc.perform(delete("/api/order-bag-service-items/{id}", orderBagServiceItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean orderBagServiceItemExistsInEs = orderBagServiceItemSearchRepository.exists(orderBagServiceItem.getId());
        assertThat(orderBagServiceItemExistsInEs).isFalse();

        // Validate the database is empty
        List<OrderBagServiceItem> orderBagServiceItems = orderBagServiceItemRepository.findAll();
        assertThat(orderBagServiceItems).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrderBagServiceItem() throws Exception {
        // Initialize the database
        orderBagServiceItemRepository.saveAndFlush(orderBagServiceItem);
        orderBagServiceItemSearchRepository.save(orderBagServiceItem);

        // Search the orderBagServiceItem
        restOrderBagServiceItemMockMvc.perform(get("/api/_search/order-bag-service-items?query=id:" + orderBagServiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderBagServiceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].subtotalServicePrice").value(hasItem(DEFAULT_SUBTOTAL_SERVICE_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].subtotalOptionPrice").value(hasItem(DEFAULT_SUBTOTAL_OPTION_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].subtotalVAT").value(hasItem(DEFAULT_SUBTOTAL_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].subtotalDiscount").value(hasItem(DEFAULT_SUBTOTAL_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalRowPrice").value(hasItem(DEFAULT_TOTAL_ROW_PRICE.doubleValue())));
    }
}
