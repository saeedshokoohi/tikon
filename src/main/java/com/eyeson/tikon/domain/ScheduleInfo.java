package com.eyeson.tikon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.ScheduleType;

/**
 * A ScheduleInfo.
 */
@Entity
@Table(name = "schedule_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "scheduleinfo")
public class ScheduleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type")
    private ScheduleType scheduleType;

    @ManyToOne
    private WeeklyScheduleInfo weeklyScheduleInfo;

    @ManyToMany(mappedBy = "serviceTimes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceItem> serviceItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public WeeklyScheduleInfo getWeeklyScheduleInfo() {
        return weeklyScheduleInfo;
    }

    public void setWeeklyScheduleInfo(WeeklyScheduleInfo weeklyScheduleInfo) {
        this.weeklyScheduleInfo = weeklyScheduleInfo;
    }

    public Set<ServiceItem> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(Set<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScheduleInfo scheduleInfo = (ScheduleInfo) o;
        if(scheduleInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, scheduleInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ScheduleInfo{" +
            "id=" + id +
            ", scheduleType='" + scheduleType + "'" +
            '}';
    }
}
