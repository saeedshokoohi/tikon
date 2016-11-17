package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the CapacityException entity.
 */
public class CapacityExceptionDTO implements Serializable {

    private Long id;

    private Integer exceptionType;

    private ZonedDateTime reserveTime;

    private Integer newQty;


    private Long serviceCapacityInfoId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(Integer exceptionType) {
        this.exceptionType = exceptionType;
    }
    public ZonedDateTime getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(ZonedDateTime reserveTime) {
        this.reserveTime = reserveTime;
    }
    public Integer getNewQty() {
        return newQty;
    }

    public void setNewQty(Integer newQty) {
        this.newQty = newQty;
    }

    public Long getServiceCapacityInfoId() {
        return serviceCapacityInfoId;
    }

    public void setServiceCapacityInfoId(Long serviceCapacityInfoId) {
        this.serviceCapacityInfoId = serviceCapacityInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CapacityExceptionDTO capacityExceptionDTO = (CapacityExceptionDTO) o;

        if ( ! Objects.equals(id, capacityExceptionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CapacityExceptionDTO{" +
            "id=" + id +
            ", exceptionType='" + exceptionType + "'" +
            ", reserveTime='" + reserveTime + "'" +
            ", newQty='" + newQty + "'" +
            '}';
    }
}
