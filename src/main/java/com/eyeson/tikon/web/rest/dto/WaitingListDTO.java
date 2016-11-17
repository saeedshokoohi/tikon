package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the WaitingList entity.
 */
public class WaitingListDTO implements Serializable {

    private Long id;

    private Integer qty;

    private ZonedDateTime reserveTime;

    private String description;


    private Long serviceIteId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public ZonedDateTime getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(ZonedDateTime reserveTime) {
        this.reserveTime = reserveTime;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getServiceIteId() {
        return serviceIteId;
    }

    public void setServiceIteId(Long serviceItemId) {
        this.serviceIteId = serviceItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WaitingListDTO waitingListDTO = (WaitingListDTO) o;

        if ( ! Objects.equals(id, waitingListDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WaitingListDTO{" +
            "id=" + id +
            ", qty='" + qty + "'" +
            ", reserveTime='" + reserveTime + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
