package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the OrderBagServiceItem entity.
 */
public class OrderBagServiceItemDTO implements Serializable {

    private Long id;

    private Integer status;

    private Double subtotalServicePrice;

    private Double subtotalOptionPrice;

    private Double subtotalVAT;

    private Double subtotalDiscount;

    private Double totalRowPrice;


    private Long orderBagId;
    
    private Long serviceItemId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Double getSubtotalServicePrice() {
        return subtotalServicePrice;
    }

    public void setSubtotalServicePrice(Double subtotalServicePrice) {
        this.subtotalServicePrice = subtotalServicePrice;
    }
    public Double getSubtotalOptionPrice() {
        return subtotalOptionPrice;
    }

    public void setSubtotalOptionPrice(Double subtotalOptionPrice) {
        this.subtotalOptionPrice = subtotalOptionPrice;
    }
    public Double getSubtotalVAT() {
        return subtotalVAT;
    }

    public void setSubtotalVAT(Double subtotalVAT) {
        this.subtotalVAT = subtotalVAT;
    }
    public Double getSubtotalDiscount() {
        return subtotalDiscount;
    }

    public void setSubtotalDiscount(Double subtotalDiscount) {
        this.subtotalDiscount = subtotalDiscount;
    }
    public Double getTotalRowPrice() {
        return totalRowPrice;
    }

    public void setTotalRowPrice(Double totalRowPrice) {
        this.totalRowPrice = totalRowPrice;
    }

    public Long getOrderBagId() {
        return orderBagId;
    }

    public void setOrderBagId(Long orderBagId) {
        this.orderBagId = orderBagId;
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

        OrderBagServiceItemDTO orderBagServiceItemDTO = (OrderBagServiceItemDTO) o;

        if ( ! Objects.equals(id, orderBagServiceItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderBagServiceItemDTO{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", subtotalServicePrice='" + subtotalServicePrice + "'" +
            ", subtotalOptionPrice='" + subtotalOptionPrice + "'" +
            ", subtotalVAT='" + subtotalVAT + "'" +
            ", subtotalDiscount='" + subtotalDiscount + "'" +
            ", totalRowPrice='" + totalRowPrice + "'" +
            '}';
    }
}
