package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.NotificationType;
import com.eyeson.tikon.domain.enumeration.NotificationType;
import com.eyeson.tikon.domain.enumeration.NotificationType;
import com.eyeson.tikon.domain.enumeration.NotificationType;

/**
 * A DTO for the NotificationSetting entity.
 */
public class NotificationSettingDTO implements Serializable {

    private Long id;

    private NotificationType sendBookingConfirmation;

    private NotificationType sendOnCancelBooking;

    private NotificationType sendOnMoveBooking;

    private NotificationType sendCustomerReminder;

    private Double reminderTimeInAdvanced;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public NotificationType getSendBookingConfirmation() {
        return sendBookingConfirmation;
    }

    public void setSendBookingConfirmation(NotificationType sendBookingConfirmation) {
        this.sendBookingConfirmation = sendBookingConfirmation;
    }
    public NotificationType getSendOnCancelBooking() {
        return sendOnCancelBooking;
    }

    public void setSendOnCancelBooking(NotificationType sendOnCancelBooking) {
        this.sendOnCancelBooking = sendOnCancelBooking;
    }
    public NotificationType getSendOnMoveBooking() {
        return sendOnMoveBooking;
    }

    public void setSendOnMoveBooking(NotificationType sendOnMoveBooking) {
        this.sendOnMoveBooking = sendOnMoveBooking;
    }
    public NotificationType getSendCustomerReminder() {
        return sendCustomerReminder;
    }

    public void setSendCustomerReminder(NotificationType sendCustomerReminder) {
        this.sendCustomerReminder = sendCustomerReminder;
    }
    public Double getReminderTimeInAdvanced() {
        return reminderTimeInAdvanced;
    }

    public void setReminderTimeInAdvanced(Double reminderTimeInAdvanced) {
        this.reminderTimeInAdvanced = reminderTimeInAdvanced;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationSettingDTO notificationSettingDTO = (NotificationSettingDTO) o;

        if ( ! Objects.equals(id, notificationSettingDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NotificationSettingDTO{" +
            "id=" + id +
            ", sendBookingConfirmation='" + sendBookingConfirmation + "'" +
            ", sendOnCancelBooking='" + sendOnCancelBooking + "'" +
            ", sendOnMoveBooking='" + sendOnMoveBooking + "'" +
            ", sendCustomerReminder='" + sendCustomerReminder + "'" +
            ", reminderTimeInAdvanced='" + reminderTimeInAdvanced + "'" +
            '}';
    }
}
