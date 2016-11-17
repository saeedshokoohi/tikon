package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A WeeklyScheduleInfo.
 */
@Entity
@Table(name = "weekly_schedule_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "weeklyscheduleinfo")
public class WeeklyScheduleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "weekly_schedule_info_date_period",
               joinColumns = @JoinColumn(name="weekly_schedule_infos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="date_periods_id", referencedColumnName="ID"))
    private Set<DatePeriod> datePeriods = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "weekly_schedule_info_daily_duration",
               joinColumns = @JoinColumn(name="weekly_schedule_infos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="daily_durations_id", referencedColumnName="ID"))
    private Set<TimePeriod> dailyDurations = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "weekly_schedule_info_weekdays",
               joinColumns = @JoinColumn(name="weekly_schedule_infos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="weekdays_id", referencedColumnName="ID"))
    private Set<WeeklyWorkDay> weekdays = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<DatePeriod> getDatePeriods() {
        return datePeriods;
    }

    public void setDatePeriods(Set<DatePeriod> datePeriods) {
        this.datePeriods = datePeriods;
    }

    public Set<TimePeriod> getDailyDurations() {
        return dailyDurations;
    }

    public void setDailyDurations(Set<TimePeriod> timePeriods) {
        this.dailyDurations = timePeriods;
    }

    public Set<WeeklyWorkDay> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(Set<WeeklyWorkDay> weeklyWorkDays) {
        this.weekdays = weeklyWorkDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WeeklyScheduleInfo weeklyScheduleInfo = (WeeklyScheduleInfo) o;
        if(weeklyScheduleInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, weeklyScheduleInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WeeklyScheduleInfo{" +
            "id=" + id +
            '}';
    }
}
