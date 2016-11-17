package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the OrderBagItemOption entity.
 */
public class OrderBagItemOptionDTO implements Serializable {

    private Long id;

    private ZonedDateTime reserveTime;

    private Double qty;

    private Double price;

    private Double discount;

    private Double vat;

    private Double totalPrice;


    private Long orderbagItemDtailId;
    
    private Long orderbagItemId;
    
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
    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
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

    public Long getOrderbagItemDtailId() {
        return orderbagItemDtailId;
    }

    public void setOrderbagItemDtailId(Long orderBagServiceItemDtailId) {
        this.orderbagItemDtailId = orderBagServiceItemDtailId;
    }

    public Long getOrderbagItemId() {
        return orderbagItemId;
    }

    public void setOrderbagItemId(Long serviceOptionItemId) {
        this.orderbagItemId = serviceOptionItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderBagItemOptionDTO orderBagItemOptionDTO = (OrderBagItemOptionDTO) o;

        if ( ! Objects.equals(id, orderBagItemOptionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderBagItemOptionDTO{" +
            "id=" + id +
            ", reserveTime='" + reserveTime + "'" +
            ", qty='" + qty + "'" +
            ", price='" + price + "'" +
            ", discount='" + discount + "'" +
            ", vat='" + vat + "'" +
            ", totalPrice='" + totalPrice + "'" +
            '}';
    }
}
