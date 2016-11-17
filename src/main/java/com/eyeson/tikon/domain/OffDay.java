package com.eyeson.tikon.domain;

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

import com.eyeson.tikon.domain.enumeration.OffDayType;

/**
 * A OffDay.
 */
@Entity
@Table(name = "off_day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "offday")
public class OffDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "off_date")
    private LocalDate offDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "off_day_type")
    private OffDayType offDayType;

    @ManyToMany(mappedBy = "offdays")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DatePeriod> datePeriods = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOffDate() {
        return offDate;
    }

    public void setOffDate(LocalDate offDate) {
        this.offDate = offDate;
    }

    public OffDayType getOffDayType() {
        return offDayType;
    }

    public void setOffDayType(OffDayType offDayType) {
        this.offDayType = offDayType;
    }

    public Set<DatePeriod> getDatePeriods() {
        return datePeriods;
    }

    public void setDatePeriods(Set<DatePeriod> datePeriods) {
        this.datePeriods = datePeriods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OffDay offDay = (OffDay) o;
        if(offDay.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, offDay.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OffDay{" +
            "id=" + id +
            ", offDate='" + offDate + "'" +
            ", offDayType='" + offDayType + "'" +
            '}';
    }
}
