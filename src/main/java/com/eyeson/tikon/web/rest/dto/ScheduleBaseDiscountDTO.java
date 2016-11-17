package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ScheduleBaseDiscount entity.
 */
public class ScheduleBaseDiscountDTO implements Serializable {

    private Long id;

    private Double amount;


    private Long scheduleInfoId;
    
    private Long discountInfoId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getScheduleInfoId() {
        return scheduleInfoId;
    }

    public void setScheduleInfoId(Long scheduleInfoId) {
        this.scheduleInfoId = scheduleInfoId;
    }

    public Long getDiscountInfoId() {
        return discountInfoId;
    }

    public void setDiscountInfoId(Long discountInfoId) {
        this.discountInfoId = discountInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScheduleBaseDiscountDTO scheduleBaseDiscountDTO = (ScheduleBaseDiscountDTO) o;

        if ( ! Objects.equals(id, scheduleBaseDiscountDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ScheduleBaseDiscountDTO{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }
}
