package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the InvoiceInfo entity.
 */
public class InvoiceInfoDTO implements Serializable {

    private Long id;

    private ZonedDateTime createDate;


    private Long orderBagId;
    
    private Set<PaymentLogDTO> paymentLogs = new HashSet<>();

    private Long customerId;
    
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

    public Long getOrderBagId() {
        return orderBagId;
    }

    public void setOrderBagId(Long orderBagId) {
        this.orderBagId = orderBagId;
    }

    public Set<PaymentLogDTO> getPaymentLogs() {
        return paymentLogs;
    }

    public void setPaymentLogs(Set<PaymentLogDTO> paymentLogs) {
        this.paymentLogs = paymentLogs;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceInfoDTO invoiceInfoDTO = (InvoiceInfoDTO) o;

        if ( ! Objects.equals(id, invoiceInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InvoiceInfoDTO{" +
            "id=" + id +
            ", createDate='" + createDate + "'" +
            '}';
    }
}
