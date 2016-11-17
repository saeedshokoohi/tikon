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

import com.eyeson.tikon.domain.enumeration.OffTimeType;

/**
 * A OffTime.
 */
@Entity
@Table(name = "off_time")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "offtime")
public class OffTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "i")
    private Long i;

    @Column(name = "from_time")
    private ZonedDateTime fromTime;

    @Column(name = "to_time")
    private ZonedDateTime toTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "off_time_type")
    private OffTimeType offTimeType;

    @ManyToMany(mappedBy = "offtimes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TimePeriod> durationSlice = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getI() {
        return i;
    }

    public void setI(Long i) {
        this.i = i;
    }

    public ZonedDateTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(ZonedDateTime fromTime) {
        this.fromTime = fromTime;
    }

    public ZonedDateTime getToTime() {
        return toTime;
    }

    public void setToTime(ZonedDateTime toTime) {
        this.toTime = toTime;
    }

    public OffTimeType getOffTimeType() {
        return offTimeType;
    }

    public void setOffTimeType(OffTimeType offTimeType) {
        this.offTimeType = offTimeType;
    }

    public Set<TimePeriod> getDurationSlice() {
        return durationSlice;
    }

    public void setDurationSlice(Set<TimePeriod> timePeriods) {
        this.durationSlice = timePeriods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OffTime offTime = (OffTime) o;
        if(offTime.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, offTime.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OffTime{" +
            "id=" + id +
            ", i='" + i + "'" +
            ", fromTime='" + fromTime + "'" +
            ", toTime='" + toTime + "'" +
            ", offTimeType='" + offTimeType + "'" +
            '}';
    }
}
