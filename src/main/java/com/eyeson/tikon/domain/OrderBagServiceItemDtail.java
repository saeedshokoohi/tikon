package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A OrderBagServiceItemDtail.
 */
@Entity
@Table(name = "order_bag_service_item_dtail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orderbagserviceitemdtail")
public class OrderBagServiceItemDtail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "reserve_time")
    private ZonedDateTime reserveTime;

    @Column(name = "gty")
    private Double gty;

    @Column(name = "price")
    private Double price;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "vat")
    private Double vat;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne
    private OrderBagServiceItem orderBagItem;

    @ManyToOne
    private PriceInfoDtail priceInfoDtail;

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

    public OrderBagServiceItem getOrderBagItem() {
        return orderBagItem;
    }

    public void setOrderBagItem(OrderBagServiceItem orderBagServiceItem) {
        this.orderBagItem = orderBagServiceItem;
    }

    public PriceInfoDtail getPriceInfoDtail() {
        return priceInfoDtail;
    }

    public void setPriceInfoDtail(PriceInfoDtail priceInfoDtail) {
        this.priceInfoDtail = priceInfoDtail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderBagServiceItemDtail orderBagServiceItemDtail = (OrderBagServiceItemDtail) o;
        if(orderBagServiceItemDtail.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderBagServiceItemDtail.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderBagServiceItemDtail{" +
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
