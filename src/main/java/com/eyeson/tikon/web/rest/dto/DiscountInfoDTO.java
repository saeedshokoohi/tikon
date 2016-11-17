package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.DiscountType;

/**
 * A DTO for the DiscountInfo entity.
 */
public class DiscountInfoDTO implements Serializable {

    private Long id;

    private Double fixedDiscount;

    private DiscountType discountType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getFixedDiscount() {
        return fixedDiscount;
    }

    public void setFixedDiscount(Double fixedDiscount) {
        this.fixedDiscount = fixedDiscount;
    }
    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DiscountInfoDTO discountInfoDTO = (DiscountInfoDTO) o;

        if ( ! Objects.equals(id, discountInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DiscountInfoDTO{" +
            "id=" + id +
            ", fixedDiscount='" + fixedDiscount + "'" +
            ", discountType='" + discountType + "'" +
            '}';
    }
}
