package com.eyeson.tikon.web.rest.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the DatePeriod entity.
 */
public class DatePeriodDTO implements Serializable {

    private Long id;

    private LocalDate fromDate;

    private LocalDate toDate;


    private Set<OffDayDTO> offdays = new HashSet<>();

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
    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Set<OffDayDTO> getOffdays() {
        return offdays;
    }

    public void setOffdays(Set<OffDayDTO> offDays) {
        this.offdays = offDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DatePeriodDTO datePeriodDTO = (DatePeriodDTO) o;

        if ( ! Objects.equals(id, datePeriodDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DatePeriodDTO{" +
            "id=" + id +
            ", fromDate='" + fromDate + "'" +
            ", toDate='" + toDate + "'" +
            '}';
    }
}
