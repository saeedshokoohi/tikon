package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.PaymentType;

/**
 * A DTO for the PaymentLog entity.
 */
public class PaymentLogDTO implements Serializable {

    private Long id;

    private String traceCode;

    private PaymentType paymentType;

    private ZonedDateTime createDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTraceCode() {
        return traceCode;
    }

    public void setTraceCode(String traceCode) {
        this.traceCode = traceCode;
    }
    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaymentLogDTO paymentLogDTO = (PaymentLogDTO) o;

        if ( ! Objects.equals(id, paymentLogDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PaymentLogDTO{" +
            "id=" + id +
            ", traceCode='" + traceCode + "'" +
            ", paymentType='" + paymentType + "'" +
            ", createDate='" + createDate + "'" +
            '}';
    }
}
