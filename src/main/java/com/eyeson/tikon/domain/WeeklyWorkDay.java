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

import com.eyeson.tikon.domain.enumeration.WeekDay;

/**
 * A WeeklyWorkDay.
 */
@Entity
@Table(name = "weekly_work_day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "weeklyworkday")
public class WeeklyWorkDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "weekday")
    private WeekDay weekday;

    @ManyToMany(mappedBy = "weekdays")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WeeklyScheduleInfo> weeklyScheduleInfos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WeekDay getWeekday() {
        return weekday;
    }

    public void setWeekday(WeekDay weekday) {
        this.weekday = weekday;
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
        WeeklyWorkDay weeklyWorkDay = (WeeklyWorkDay) o;
        if(weeklyWorkDay.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, weeklyWorkDay.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WeeklyWorkDay{" +
            "id=" + id +
            ", weekday='" + weekday + "'" +
            '}';
    }
}
