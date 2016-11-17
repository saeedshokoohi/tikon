package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.NotificationSetting;
import com.eyeson.tikon.repository.NotificationSettingRepository;
import com.eyeson.tikon.service.NotificationSettingService;
import com.eyeson.tikon.repository.search.NotificationSettingSearchRepository;
import com.eyeson.tikon.web.rest.dto.NotificationSettingDTO;
import com.eyeson.tikon.web.rest.mapper.NotificationSettingMapper;

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

import com.eyeson.tikon.domain.enumeration.NotificationType;
import com.eyeson.tikon.domain.enumeration.NotificationType;
import com.eyeson.tikon.domain.enumeration.NotificationType;
import com.eyeson.tikon.domain.enumeration.NotificationType;

/**
 * Test class for the NotificationSettingResource REST controller.
 *
 * @see NotificationSettingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class NotificationSettingResourceIntTest {


    private static final NotificationType DEFAULT_SEND_BOOKING_CONFIRMATION = NotificationType.NONE;
    private static final NotificationType UPDATED_SEND_BOOKING_CONFIRMATION = NotificationType.SMS;

    private static final NotificationType DEFAULT_SEND_ON_CANCEL_BOOKING = NotificationType.NONE;
    private static final NotificationType UPDATED_SEND_ON_CANCEL_BOOKING = NotificationType.SMS;

    private static final NotificationType DEFAULT_SEND_ON_MOVE_BOOKING = NotificationType.NONE;
    private static final NotificationType UPDATED_SEND_ON_MOVE_BOOKING = NotificationType.SMS;

    private static final NotificationType DEFAULT_SEND_CUSTOMER_REMINDER = NotificationType.NONE;
    private static final NotificationType UPDATED_SEND_CUSTOMER_REMINDER = NotificationType.SMS;

    private static final Double DEFAULT_REMINDER_TIME_IN_ADVANCED = 1D;
    private static final Double UPDATED_REMINDER_TIME_IN_ADVANCED = 2D;

    @Inject
    private NotificationSettingRepository notificationSettingRepository;

    @Inject
    private NotificationSettingMapper notificationSettingMapper;

    @Inject
    private NotificationSettingService notificationSettingService;

    @Inject
    private NotificationSettingSearchRepository notificationSettingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNotificationSettingMockMvc;

    private NotificationSetting notificationSetting;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationSettingResource notificationSettingResource = new NotificationSettingResource();
        ReflectionTestUtils.setField(notificationSettingResource, "notificationSettingService", notificationSettingService);
        ReflectionTestUtils.setField(notificationSettingResource, "notificationSettingMapper", notificationSettingMapper);
        this.restNotificationSettingMockMvc = MockMvcBuilders.standaloneSetup(notificationSettingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        notificationSettingSearchRepository.deleteAll();
        notificationSetting = new NotificationSetting();
        notificationSetting.setSendBookingConfirmation(DEFAULT_SEND_BOOKING_CONFIRMATION);
        notificationSetting.setSendOnCancelBooking(DEFAULT_SEND_ON_CANCEL_BOOKING);
        notificationSetting.setSendOnMoveBooking(DEFAULT_SEND_ON_MOVE_BOOKING);
        notificationSetting.setSendCustomerReminder(DEFAULT_SEND_CUSTOMER_REMINDER);
        notificationSetting.setReminderTimeInAdvanced(DEFAULT_REMINDER_TIME_IN_ADVANCED);
    }

    @Test
    @Transactional
    public void createNotificationSetting() throws Exception {
        int databaseSizeBeforeCreate = notificationSettingRepository.findAll().size();

        // Create the NotificationSetting
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.notificationSettingToNotificationSettingDTO(notificationSetting);

        restNotificationSettingMockMvc.perform(post("/api/notification-settings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationSettingDTO)))
                .andExpect(status().isCreated());

        // Validate the NotificationSetting in the database
        List<NotificationSetting> notificationSettings = notificationSettingRepository.findAll();
        assertThat(notificationSettings).hasSize(databaseSizeBeforeCreate + 1);
        NotificationSetting testNotificationSetting = notificationSettings.get(notificationSettings.size() - 1);
        assertThat(testNotificationSetting.getSendBookingConfirmation()).isEqualTo(DEFAULT_SEND_BOOKING_CONFIRMATION);
        assertThat(testNotificationSetting.getSendOnCancelBooking()).isEqualTo(DEFAULT_SEND_ON_CANCEL_BOOKING);
        assertThat(testNotificationSetting.getSendOnMoveBooking()).isEqualTo(DEFAULT_SEND_ON_MOVE_BOOKING);
        assertThat(testNotificationSetting.getSendCustomerReminder()).isEqualTo(DEFAULT_SEND_CUSTOMER_REMINDER);
        assertThat(testNotificationSetting.getReminderTimeInAdvanced()).isEqualTo(DEFAULT_REMINDER_TIME_IN_ADVANCED);

        // Validate the NotificationSetting in ElasticSearch
        NotificationSetting notificationSettingEs = notificationSettingSearchRepository.findOne(testNotificationSetting.getId());
        assertThat(notificationSettingEs).isEqualToComparingFieldByField(testNotificationSetting);
    }

    @Test
    @Transactional
    public void getAllNotificationSettings() throws Exception {
        // Initialize the database
        notificationSettingRepository.saveAndFlush(notificationSetting);

        // Get all the notificationSettings
        restNotificationSettingMockMvc.perform(get("/api/notification-settings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notificationSetting.getId().intValue())))
                .andExpect(jsonPath("$.[*].sendBookingConfirmation").value(hasItem(DEFAULT_SEND_BOOKING_CONFIRMATION.toString())))
                .andExpect(jsonPath("$.[*].sendOnCancelBooking").value(hasItem(DEFAULT_SEND_ON_CANCEL_BOOKING.toString())))
                .andExpect(jsonPath("$.[*].sendOnMoveBooking").value(hasItem(DEFAULT_SEND_ON_MOVE_BOOKING.toString())))
                .andExpect(jsonPath("$.[*].sendCustomerReminder").value(hasItem(DEFAULT_SEND_CUSTOMER_REMINDER.toString())))
                .andExpect(jsonPath("$.[*].reminderTimeInAdvanced").value(hasItem(DEFAULT_REMINDER_TIME_IN_ADVANCED.doubleValue())));
    }

    @Test
    @Transactional
    public void getNotificationSetting() throws Exception {
        // Initialize the database
        notificationSettingRepository.saveAndFlush(notificationSetting);

        // Get the notificationSetting
        restNotificationSettingMockMvc.perform(get("/api/notification-settings/{id}", notificationSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(notificationSetting.getId().intValue()))
            .andExpect(jsonPath("$.sendBookingConfirmation").value(DEFAULT_SEND_BOOKING_CONFIRMATION.toString()))
            .andExpect(jsonPath("$.sendOnCancelBooking").value(DEFAULT_SEND_ON_CANCEL_BOOKING.toString()))
            .andExpect(jsonPath("$.sendOnMoveBooking").value(DEFAULT_SEND_ON_MOVE_BOOKING.toString()))
            .andExpect(jsonPath("$.sendCustomerReminder").value(DEFAULT_SEND_CUSTOMER_REMINDER.toString()))
            .andExpect(jsonPath("$.reminderTimeInAdvanced").value(DEFAULT_REMINDER_TIME_IN_ADVANCED.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationSetting() throws Exception {
        // Get the notificationSetting
        restNotificationSettingMockMvc.perform(get("/api/notification-settings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationSetting() throws Exception {
        // Initialize the database
        notificationSettingRepository.saveAndFlush(notificationSetting);
        notificationSettingSearchRepository.save(notificationSetting);
        int databaseSizeBeforeUpdate = notificationSettingRepository.findAll().size();

        // Update the notificationSetting
        NotificationSetting updatedNotificationSetting = new NotificationSetting();
        updatedNotificationSetting.setId(notificationSetting.getId());
        updatedNotificationSetting.setSendBookingConfirmation(UPDATED_SEND_BOOKING_CONFIRMATION);
        updatedNotificationSetting.setSendOnCancelBooking(UPDATED_SEND_ON_CANCEL_BOOKING);
        updatedNotificationSetting.setSendOnMoveBooking(UPDATED_SEND_ON_MOVE_BOOKING);
        updatedNotificationSetting.setSendCustomerReminder(UPDATED_SEND_CUSTOMER_REMINDER);
        updatedNotificationSetting.setReminderTimeInAdvanced(UPDATED_REMINDER_TIME_IN_ADVANCED);
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.notificationSettingToNotificationSettingDTO(updatedNotificationSetting);

        restNotificationSettingMockMvc.perform(put("/api/notification-settings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationSettingDTO)))
                .andExpect(status().isOk());

        // Validate the NotificationSetting in the database
        List<NotificationSetting> notificationSettings = notificationSettingRepository.findAll();
        assertThat(notificationSettings).hasSize(databaseSizeBeforeUpdate);
        NotificationSetting testNotificationSetting = notificationSettings.get(notificationSettings.size() - 1);
        assertThat(testNotificationSetting.getSendBookingConfirmation()).isEqualTo(UPDATED_SEND_BOOKING_CONFIRMATION);
        assertThat(testNotificationSetting.getSendOnCancelBooking()).isEqualTo(UPDATED_SEND_ON_CANCEL_BOOKING);
        assertThat(testNotificationSetting.getSendOnMoveBooking()).isEqualTo(UPDATED_SEND_ON_MOVE_BOOKING);
        assertThat(testNotificationSetting.getSendCustomerReminder()).isEqualTo(UPDATED_SEND_CUSTOMER_REMINDER);
        assertThat(testNotificationSetting.getReminderTimeInAdvanced()).isEqualTo(UPDATED_REMINDER_TIME_IN_ADVANCED);

        // Validate the NotificationSetting in ElasticSearch
        NotificationSetting notificationSettingEs = notificationSettingSearchRepository.findOne(testNotificationSetting.getId());
        assertThat(notificationSettingEs).isEqualToComparingFieldByField(testNotificationSetting);
    }

    @Test
    @Transactional
    public void deleteNotificationSetting() throws Exception {
        // Initialize the database
        notificationSettingRepository.saveAndFlush(notificationSetting);
        notificationSettingSearchRepository.save(notificationSetting);
        int databaseSizeBeforeDelete = notificationSettingRepository.findAll().size();

        // Get the notificationSetting
        restNotificationSettingMockMvc.perform(delete("/api/notification-settings/{id}", notificationSetting.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean notificationSettingExistsInEs = notificationSettingSearchRepository.exists(notificationSetting.getId());
        assertThat(notificationSettingExistsInEs).isFalse();

        // Validate the database is empty
        List<NotificationSetting> notificationSettings = notificationSettingRepository.findAll();
        assertThat(notificationSettings).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNotificationSetting() throws Exception {
        // Initialize the database
        notificationSettingRepository.saveAndFlush(notificationSetting);
        notificationSettingSearchRepository.save(notificationSetting);

        // Search the notificationSetting
        restNotificationSettingMockMvc.perform(get("/api/_search/notification-settings?query=id:" + notificationSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].sendBookingConfirmation").value(hasItem(DEFAULT_SEND_BOOKING_CONFIRMATION.toString())))
            .andExpect(jsonPath("$.[*].sendOnCancelBooking").value(hasItem(DEFAULT_SEND_ON_CANCEL_BOOKING.toString())))
            .andExpect(jsonPath("$.[*].sendOnMoveBooking").value(hasItem(DEFAULT_SEND_ON_MOVE_BOOKING.toString())))
            .andExpect(jsonPath("$.[*].sendCustomerReminder").value(hasItem(DEFAULT_SEND_CUSTOMER_REMINDER.toString())))
            .andExpect(jsonPath("$.[*].reminderTimeInAdvanced").value(hasItem(DEFAULT_REMINDER_TIME_IN_ADVANCED.doubleValue())));
    }
}
