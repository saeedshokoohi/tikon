package com.eyeson.tikon.domain;

import com.eyeson.tikon.service.util.LocalDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DatePeriod.
 */
@Entity
@Table(name = "date_period")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "dateperiod")
public class DatePeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @ManyToMany(cascade = {CascadeType.ALL})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "date_period_offdays",
               joinColumns = @JoinColumn(name="date_periods_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="offdays_id", referencedColumnName="ID"))
    private Set<OffDay> offdays = new HashSet<>();

    @ManyToMany(mappedBy = "datePeriods")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WeeklyScheduleInfo> weeklyScheduleInfos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    public void setFromDateStr(String fromDate) {
        this.fromDate= LocalDateUtil.setLocalDateFromString(fromDate);
    }
    public void setToDateStr(String toDate) {
      this.toDate=LocalDateUtil.setLocalDateFromString(toDate);
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Set<OffDay> getOffdays() {
        return offdays;
    }

    public void setOffdays(Set<OffDay> offDays) {
        this.offdays = offDays;
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
        DatePeriod datePeriod = (DatePeriod) o;
        if(datePeriod.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, datePeriod.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DatePeriod{" +
            "id=" + id +
            ", fromDate='" + fromDate + "'" +
            ", toDate='" + toDate + "'" +
            '}';
    }
}
