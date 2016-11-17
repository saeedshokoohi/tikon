package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A InvoiceInfo.
 */
@Entity
@Table(name = "invoice_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "invoiceinfo")
public class InvoiceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @ManyToOne
    private OrderBag orderBag;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "invoice_info_payment_log",
               joinColumns = @JoinColumn(name="invoice_infos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="payment_logs_id", referencedColumnName="ID"))
    private Set<PaymentLog> paymentLogs = new HashSet<>();

    @ManyToOne
    private Customer customer;

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

    public OrderBag getOrderBag() {
        return orderBag;
    }

    public void setOrderBag(OrderBag orderBag) {
        this.orderBag = orderBag;
    }

    public Set<PaymentLog> getPaymentLogs() {
        return paymentLogs;
    }

    public void setPaymentLogs(Set<PaymentLog> paymentLogs) {
        this.paymentLogs = paymentLogs;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvoiceInfo invoiceInfo = (InvoiceInfo) o;
        if(invoiceInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, invoiceInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InvoiceInfo{" +
            "id=" + id +
            ", createDate='" + createDate + "'" +
            '}';
    }
}
