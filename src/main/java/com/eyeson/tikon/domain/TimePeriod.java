package com.eyeson.tikon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TimePeriod.
 */
@Entity
@Table(name = "time_period")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "timeperiod")
public class TimePeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "gaptime")
    private Integer gaptime;

    @ManyToMany(cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "time_period_offtimes",
               joinColumns = @JoinColumn(name="time_periods_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="offtimes_id", referencedColumnName="ID"))
    private Set<OffTime> offtimes = new HashSet<>();

    @ManyToMany(mappedBy = "dailyDurations")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WeeklyScheduleInfo> weeklyScheduleInfos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getGaptime() {
        return gaptime;
    }

    public void setGaptime(Integer gaptime) {
        this.gaptime = gaptime;
    }

    public Set<OffTime> getOfftimes() {
        return offtimes;
    }

    public void setOfftimes(Set<OffTime> offTimes) {
        this.offtimes = offTimes;
    }

    public Set<WeeklyScheduleInfo> getWeeklyScheduleInfos() {
        return weeklyScheduleInfos;
    }

    public void setWeeklyScheduleInfos(Set<WeeklyScheduleInfo> weeklyScheduleInfos) {
        this.weeklyScheduleInfos = weeklyScheduleInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimePeriod timePeriod = (TimePeriod) o;
        if(timePeriod.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, timePeriod.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
            "id=" + id +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", duration='" + duration + "'" +
            ", gaptime='" + gaptime + "'" +
            '}';
    }
}
