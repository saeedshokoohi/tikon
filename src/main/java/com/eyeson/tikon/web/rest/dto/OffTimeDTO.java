package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.OffTimeType;

/**
 * A DTO for the OffTime entity.
 */
public class OffTimeDTO implements Serializable {

    private Long id;

    private Long i;

    private ZonedDateTime fromTime;

    private ZonedDateTime toTime;

    private OffTimeType offTimeType;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OffTimeDTO offTimeDTO = (OffTimeDTO) o;

        if ( ! Objects.equals(id, offTimeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fromTime)+Objects.hashCode(toTime);
    }

    @Override
    public String toString() {
        return "OffTimeDTO{" +
            "id=" + id +
            ", i='" + i + "'" +
            ", fromTime='" + fromTime + "'" +
            ", toTime='" + toTime + "'" +
            ", offTimeType='" + offTimeType + "'" +
            '}';
    }
}
