package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OrderBagServiceItem.
 */
@Entity
@Table(name = "order_bag_service_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orderbagserviceitem")
public class OrderBagServiceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "status")
    private Integer status;

    @Column(name = "subtotal_service_price")
    private Double subtotalServicePrice;

    @Column(name = "subtotal_option_price")
    private Double subtotalOptionPrice;

    @Column(name = "subtotal_vat")
    private Double subtotalVAT;

    @Column(name = "subtotal_discount")
    private Double subtotalDiscount;

    @Column(name = "total_row_price")
    private Double totalRowPrice;

    @ManyToOne
    private OrderBag orderBag;

    @ManyToOne
    private ServiceItem serviceItem;

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

    public OrderBag getOrderBag() {
        return orderBag;
    }

    public void setOrderBag(OrderBag orderBag) {
        this.orderBag = orderBag;
    }

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderBagServiceItem orderBagServiceItem = (OrderBagServiceItem) o;
        if(orderBagServiceItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderBagServiceItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderBagServiceItem{" +
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
