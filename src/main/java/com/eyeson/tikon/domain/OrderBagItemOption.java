package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A OrderBagItemOption.
 */
@Entity
@Table(name = "order_bag_item_option")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orderbagitemoption")
public class OrderBagItemOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "reserve_time")
    private ZonedDateTime reserveTime;

    @Column(name = "qty")
    private Double qty;

    @Column(name = "price")
    private Double price;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "vat")
    private Double vat;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne
    private OrderBagServiceItemDtail orderbagItemDtail;

    @ManyToOne
    private ServiceOptionItem orderbagItem;

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

    public OrderBagServiceItemDtail getOrderbagItemDtail() {
        return orderbagItemDtail;
    }

    public void setOrderbagItemDtail(OrderBagServiceItemDtail orderBagServiceItemDtail) {
        this.orderbagItemDtail = orderBagServiceItemDtail;
    }

    public ServiceOptionItem getOrderbagItem() {
        return orderbagItem;
    }

    public void setOrderbagItem(ServiceOptionItem serviceOptionItem) {
        this.orderbagItem = serviceOptionItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderBagItemOption orderBagItemOption = (OrderBagItemOption) o;
        if(orderBagItemOption.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderBagItemOption.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderBagItemOption{" +
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
