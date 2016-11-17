package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the OrderBagServiceItemDtail entity.
 */
public class OrderBagServiceItemDtailDTO implements Serializable {

    private Long id;

    private ZonedDateTime reserveTime;

    private Double gty;

    private Double price;

    private Double discount;

    private Double vat;

    private Double totalPrice;


    private Long orderBagItemId;
    
    private Long priceInfoDtailId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(ZonedDateTime reserveTime) {
        this.reserveTime = reserveTime;
    }
    public Double getGty() {
        return gty;
    }

    public void setGty(Double gty) {
        this.gty = gty;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getOrderBagItemId() {
        return orderBagItemId;
    }

    public void setOrderBagItemId(Long orderBagServiceItemId) {
        this.orderBagItemId = orderBagServiceItemId;
    }

    public Long getPriceInfoDtailId() {
        return priceInfoDtailId;
    }

    public void setPriceInfoDtailId(Long priceInfoDtailId) {
        this.priceInfoDtailId = priceInfoDtailId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderBagServiceItemDtailDTO orderBagServiceItemDtailDTO = (OrderBagServiceItemDtailDTO) o;

        if ( ! Objects.equals(id, orderBagServiceItemDtailDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderBagServiceItemDtailDTO{" +
            "id=" + id +
            ", reserveTime='" + reserveTime + "'" +
            ", gty='" + gty + "'" +
            ", price='" + price + "'" +
            ", discount='" + discount + "'" +
            ", vat='" + vat + "'" +
            ", totalPrice='" + totalPrice + "'" +
            '}';
    }
}
