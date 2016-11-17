package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ServiceItem;
import com.eyeson.tikon.repository.ServiceItemRepository;
import com.eyeson.tikon.service.ServiceItemService;
import com.eyeson.tikon.repository.search.ServiceItemSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServiceItemDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceItemMapper;

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

import com.eyeson.tikon.domain.enumeration.ServiceItemType;

/**
 * Test class for the ServiceItemResource REST controller.
 *
 * @see ServiceItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ServiceItemResourceIntTest {

    private static final String DEFAULT_ITEM_TITLE = "AAAAA";
    private static final String UPDATED_ITEM_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Double DEFAULT_MIN_PRE_RESERVE_TIME = 1D;
    private static final Double UPDATED_MIN_PRE_RESERVE_TIME = 2D;

    private static final Double DEFAULT_MAX_PRE_RESERVE_TIME = 1D;
    private static final Double UPDATED_MAX_PRE_RESERVE_TIME = 2D;

    private static final Boolean DEFAULT_HAS_WAITING_LIST = false;
    private static final Boolean UPDATED_HAS_WAITING_LIST = true;

    private static final Boolean DEFAULT_MUST_GET_PARTICIPANT_INFO = false;
    private static final Boolean UPDATED_MUST_GET_PARTICIPANT_INFO = true;

    private static final Boolean DEFAULT_CAN_BE_CANCELED = false;
    private static final Boolean UPDATED_CAN_BE_CANCELED = true;

    private static final Double DEFAULT_MIN_PRE_CANCEL_TIME = 1D;
    private static final Double UPDATED_MIN_PRE_CANCEL_TIME = 2D;

    private static final Integer DEFAULT_PAYMENT_TYPE = 1;
    private static final Integer UPDATED_PAYMENT_TYPE = 2;

    private static final ServiceItemType DEFAULT_SERVICE_TYPE = ServiceItemType.INDEPENDENT_SERVICE;
    private static final ServiceItemType UPDATED_SERVICE_TYPE = ServiceItemType.SERVICE_PACKAGE;

    @Inject
    private ServiceItemRepository serviceItemRepository;

    @Inject
    private ServiceItemMapper serviceItemMapper;

    @Inject
    private ServiceItemService serviceItemService;

    @Inject
    private ServiceItemSearchRepository serviceItemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restServiceItemMockMvc;

    private ServiceItem serviceItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceItemResource serviceItemResource = new ServiceItemResource();
        ReflectionTestUtils.setField(serviceItemResource, "serviceItemService", serviceItemService);
        ReflectionTestUtils.setField(serviceItemResource, "serviceItemMapper", serviceItemMapper);
        this.restServiceItemMockMvc = MockMvcBuilders.standaloneSetup(serviceItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceItemSearchRepository.deleteAll();
        serviceItem = new ServiceItem();
        serviceItem.setItemTitle(DEFAULT_ITEM_TITLE);
        serviceItem.setDescription(DEFAULT_DESCRIPTION);
        serviceItem.setMinPreReserveTime(DEFAULT_MIN_PRE_RESERVE_TIME);
        serviceItem.setMaxPreReserveTime(DEFAULT_MAX_PRE_RESERVE_TIME);
        serviceItem.setHasWaitingList(DEFAULT_HAS_WAITING_LIST);
        serviceItem.setMustGetParticipantInfo(DEFAULT_MUST_GET_PARTICIPANT_INFO);
        serviceItem.setCanBeCanceled(DEFAULT_CAN_BE_CANCELED);
        serviceItem.setMinPreCancelTime(DEFAULT_MIN_PRE_CANCEL_TIME);
        serviceItem.setPaymentType(DEFAULT_PAYMENT_TYPE);
        serviceItem.setServiceType(DEFAULT_SERVICE_TYPE);
    }

    @Test
    @Transactional
    public void createServiceItem() throws Exception {
        int databaseSizeBeforeCreate = serviceItemRepository.findAll().size();

        // Create the ServiceItem
        ServiceItemDTO serviceItemDTO = serviceItemMapper.serviceItemToServiceItemDTO(serviceItem);

        restServiceItemMockMvc.perform(post("/api/service-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceItemDTO)))
                .andExpect(status().isCreated());

        // Validate the ServiceItem in the database
        List<ServiceItem> serviceItems = serviceItemRepository.findAll();
        assertThat(serviceItems).hasSize(databaseSizeBeforeCreate + 1);
        ServiceItem testServiceItem = serviceItems.get(serviceItems.size() - 1);
        assertThat(testServiceItem.getItemTitle()).isEqualTo(DEFAULT_ITEM_TITLE);
        assertThat(testServiceItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testServiceItem.getMinPreReserveTime()).isEqualTo(DEFAULT_MIN_PRE_RESERVE_TIME);
        assertThat(testServiceItem.getMaxPreReserveTime()).isEqualTo(DEFAULT_MAX_PRE_RESERVE_TIME);
        assertThat(testServiceItem.isHasWaitingList()).isEqualTo(DEFAULT_HAS_WAITING_LIST);
        assertThat(testServiceItem.isMustGetParticipantInfo()).isEqualTo(DEFAULT_MUST_GET_PARTICIPANT_INFO);
        assertThat(testServiceItem.isCanBeCanceled()).isEqualTo(DEFAULT_CAN_BE_CANCELED);
        assertThat(testServiceItem.getMinPreCancelTime()).isEqualTo(DEFAULT_MIN_PRE_CANCEL_TIME);
        assertThat(testServiceItem.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testServiceItem.getServiceType()).isEqualTo(DEFAULT_SERVICE_TYPE);

        // Validate the ServiceItem in ElasticSearch
        ServiceItem serviceItemEs = serviceItemSearchRepository.findOne(testServiceItem.getId());
        assertThat(serviceItemEs).isEqualToComparingFieldByField(testServiceItem);
    }

    @Test
    @Transactional
    public void getAllServiceItems() throws Exception {
        // Initialize the database
        serviceItemRepository.saveAndFlush(serviceItem);

        // Get all the serviceItems
        restServiceItemMockMvc.perform(get("/api/service-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].itemTitle").value(hasItem(DEFAULT_ITEM_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].minPreReserveTime").value(hasItem(DEFAULT_MIN_PRE_RESERVE_TIME.doubleValue())))
                .andExpect(jsonPath("$.[*].maxPreReserveTime").value(hasItem(DEFAULT_MAX_PRE_RESERVE_TIME.doubleValue())))
                .andExpect(jsonPath("$.[*].hasWaitingList").value(hasItem(DEFAULT_HAS_WAITING_LIST.booleanValue())))
                .andExpect(jsonPath("$.[*].mustGetParticipantInfo").value(hasItem(DEFAULT_MUST_GET_PARTICIPANT_INFO.booleanValue())))
                .andExpect(jsonPath("$.[*].canBeCanceled").value(hasItem(DEFAULT_CAN_BE_CANCELED.booleanValue())))
                .andExpect(jsonPath("$.[*].minPreCancelTime").value(hasItem(DEFAULT_MIN_PRE_CANCEL_TIME.doubleValue())))
                .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE)))
                .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getServiceItem() throws Exception {
        // Initialize the database
        serviceItemRepository.saveAndFlush(serviceItem);

        // Get the serviceItem
        restServiceItemMockMvc.perform(get("/api/service-items/{id}", serviceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serviceItem.getId().intValue()))
            .andExpect(jsonPath("$.itemTitle").value(DEFAULT_ITEM_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.minPreReserveTime").value(DEFAULT_MIN_PRE_RESERVE_TIME.doubleValue()))
            .andExpect(jsonPath("$.maxPreReserveTime").value(DEFAULT_MAX_PRE_RESERVE_TIME.doubleValue()))
            .andExpect(jsonPath("$.hasWaitingList").value(DEFAULT_HAS_WAITING_LIST.booleanValue()))
            .andExpect(jsonPath("$.mustGetParticipantInfo").value(DEFAULT_MUST_GET_PARTICIPANT_INFO.booleanValue()))
            .andExpect(jsonPath("$.canBeCanceled").value(DEFAULT_CAN_BE_CANCELED.booleanValue()))
            .andExpect(jsonPath("$.minPreCancelTime").value(DEFAULT_MIN_PRE_CANCEL_TIME.doubleValue()))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceItem() throws Exception {
        // Get the serviceItem
        restServiceItemMockMvc.perform(get("/api/service-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceItem() throws Exception {
        // Initialize the database
        serviceItemRepository.saveAndFlush(serviceItem);
        serviceItemSearchRepository.save(serviceItem);
        int databaseSizeBeforeUpdate = serviceItemRepository.findAll().size();

        // Update the serviceItem
        ServiceItem updatedServiceItem = new ServiceItem();
        updatedServiceItem.setId(serviceItem.getId());
        updatedServiceItem.setItemTitle(UPDATED_ITEM_TITLE);
        updatedServiceItem.setDescription(UPDATED_DESCRIPTION);
        updatedServiceItem.setMinPreReserveTime(UPDATED_MIN_PRE_RESERVE_TIME);
        updatedServiceItem.setMaxPreReserveTime(UPDATED_MAX_PRE_RESERVE_TIME);
        updatedServiceItem.setHasWaitingList(UPDATED_HAS_WAITING_LIST);
        updatedServiceItem.setMustGetParticipantInfo(UPDATED_MUST_GET_PARTICIPANT_INFO);
        updatedServiceItem.setCanBeCanceled(UPDATED_CAN_BE_CANCELED);
        updatedServiceItem.setMinPreCancelTime(UPDATED_MIN_PRE_CANCEL_TIME);
        updatedServiceItem.setPaymentType(UPDATED_PAYMENT_TYPE);
        updatedServiceItem.setServiceType(UPDATED_SERVICE_TYPE);
        ServiceItemDTO serviceItemDTO = serviceItemMapper.serviceItemToServiceItemDTO(updatedServiceItem);

        restServiceItemMockMvc.perform(put("/api/service-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceItemDTO)))
                .andExpect(status().isOk());

        // Validate the ServiceItem in the database
        List<ServiceItem> serviceItems = serviceItemRepository.findAll();
        assertThat(serviceItems).hasSize(databaseSizeBeforeUpdate);
        ServiceItem testServiceItem = serviceItems.get(serviceItems.size() - 1);
        assertThat(testServiceItem.getItemTitle()).isEqualTo(UPDATED_ITEM_TITLE);
        assertThat(testServiceItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServiceItem.getMinPreReserveTime()).isEqualTo(UPDATED_MIN_PRE_RESERVE_TIME);
        assertThat(testServiceItem.getMaxPreReserveTime()).isEqualTo(UPDATED_MAX_PRE_RESERVE_TIME);
        assertThat(testServiceItem.isHasWaitingList()).isEqualTo(UPDATED_HAS_WAITING_LIST);
        assertThat(testServiceItem.isMustGetParticipantInfo()).isEqualTo(UPDATED_MUST_GET_PARTICIPANT_INFO);
        assertThat(testServiceItem.isCanBeCanceled()).isEqualTo(UPDATED_CAN_BE_CANCELED);
        assertThat(testServiceItem.getMinPreCancelTime()).isEqualTo(UPDATED_MIN_PRE_CANCEL_TIME);
        assertThat(testServiceItem.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testServiceItem.getServiceType()).isEqualTo(UPDATED_SERVICE_TYPE);

        // Validate the ServiceItem in ElasticSearch
        ServiceItem serviceItemEs = serviceItemSearchRepository.findOne(testServiceItem.getId());
        assertThat(serviceItemEs).isEqualToComparingFieldByField(testServiceItem);
    }

    @Test
    @Transactional
    public void deleteServiceItem() throws Exception {
        // Initialize the database
        serviceItemRepository.saveAndFlush(serviceItem);
        serviceItemSearchRepository.save(serviceItem);
        int databaseSizeBeforeDelete = serviceItemRepository.findAll().size();

        // Get the serviceItem
        restServiceItemMockMvc.perform(delete("/api/service-items/{id}", serviceItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean serviceItemExistsInEs = serviceItemSearchRepository.exists(serviceItem.getId());
        assertThat(serviceItemExistsInEs).isFalse();

        // Validate the database is empty
        List<ServiceItem> serviceItems = serviceItemRepository.findAll();
        assertThat(serviceItems).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServiceItem() throws Exception {
        // Initialize the database
        serviceItemRepository.saveAndFlush(serviceItem);
        serviceItemSearchRepository.save(serviceItem);

        // Search the serviceItem
        restServiceItemMockMvc.perform(get("/api/_search/service-items?query=id:" + serviceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemTitle").value(hasItem(DEFAULT_ITEM_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].minPreReserveTime").value(hasItem(DEFAULT_MIN_PRE_RESERVE_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].maxPreReserveTime").value(hasItem(DEFAULT_MAX_PRE_RESERVE_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].hasWaitingList").value(hasItem(DEFAULT_HAS_WAITING_LIST.booleanValue())))
            .andExpect(jsonPath("$.[*].mustGetParticipantInfo").value(hasItem(DEFAULT_MUST_GET_PARTICIPANT_INFO.booleanValue())))
            .andExpect(jsonPath("$.[*].canBeCanceled").value(hasItem(DEFAULT_CAN_BE_CANCELED.booleanValue())))
            .andExpect(jsonPath("$.[*].minPreCancelTime").value(hasItem(DEFAULT_MIN_PRE_CANCEL_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentType").value(hasItem(DEFAULT_PAYMENT_TYPE)))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())));
    }
}
