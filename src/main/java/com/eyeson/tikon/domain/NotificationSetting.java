package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.NotificationType;

/**
 * A NotificationSetting.
 */
@Entity
@Table(name = "notification_setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notificationsetting")
public class NotificationSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "send_booking_confirmation")
    private NotificationType sendBookingConfirmation;

    @Enumerated(EnumType.STRING)
    @Column(name = "send_on_cancel_booking")
    private NotificationType sendOnCancelBooking;

    @Enumerated(EnumType.STRING)
    @Column(name = "send_on_move_booking")
    private NotificationType sendOnMoveBooking;

    @Enumerated(EnumType.STRING)
    @Column(name = "send_customer_reminder")
    private NotificationType sendCustomerReminder;

    @Column(name = "reminder_time_in_advanced")
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
        NotificationSetting notificationSetting = (NotificationSetting) o;
        if(notificationSetting.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notificationSetting.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NotificationSetting{" +
            "id=" + id +
            ", sendBookingConfirmation='" + sendBookingConfirmation + "'" +
            ", sendOnCancelBooking='" + sendOnCancelBooking + "'" +
            ", sendOnMoveBooking='" + sendOnMoveBooking + "'" +
            ", sendCustomerReminder='" + sendCustomerReminder + "'" +
            ", reminderTimeInAdvanced='" + reminderTimeInAdvanced + "'" +
            '}';
    }
}
