package com.eyeson.tikon.web.rest.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.OffDayType;

/**
 * A DTO for the OffDay entity.
 */
public class OffDayDTO implements Serializable {

    private Long id;

    private LocalDate offDate;

    private OffDayType offDayType;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OffDayDTO offDayDTO = (OffDayDTO) o;

        if ( ! Objects.equals(id, offDayDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(offDate);
    }

    @Override
    public String toString() {
        return "OffDayDTO{" +
            "id=" + id +
            ", offDate='" + offDate + "'" +
            ", offDayType='" + offDayType + "'" +
            '}';
    }
}
