package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the CustomerRank entity.
 */
public class CustomerRankDTO implements Serializable {

    private Long id;

    private ZonedDateTime createDate;

    private Double rankValue;


    private Long customerId;
    
    private Long serviceItemId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }
    public Double getRankValue() {
        return rankValue;
    }

    public void setRankValue(Double rankValue) {
        this.rankValue = rankValue;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Long serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerRankDTO customerRankDTO = (CustomerRankDTO) o;

        if ( ! Objects.equals(id, customerRankDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerRankDTO{" +
            "id=" + id +
            ", createDate='" + createDate + "'" +
            ", rankValue='" + rankValue + "'" +
            '}';
    }
}
