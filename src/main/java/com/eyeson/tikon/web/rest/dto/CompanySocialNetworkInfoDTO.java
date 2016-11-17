package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the CompanySocialNetworkInfo entity.
 */
public class CompanySocialNetworkInfoDTO implements Serializable {

    private Long id;

    private Integer orderNumber;


    private Long companyId;
    
    private Long socialNetworkInfoId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getSocialNetworkInfoId() {
        return socialNetworkInfoId;
    }

    public void setSocialNetworkInfoId(Long socialNetworkInfoId) {
        this.socialNetworkInfoId = socialNetworkInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompanySocialNetworkInfoDTO companySocialNetworkInfoDTO = (CompanySocialNetworkInfoDTO) o;

        if ( ! Objects.equals(id, companySocialNetworkInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanySocialNetworkInfoDTO{" +
            "id=" + id +
            ", orderNumber='" + orderNumber + "'" +
            '}';
    }
}
