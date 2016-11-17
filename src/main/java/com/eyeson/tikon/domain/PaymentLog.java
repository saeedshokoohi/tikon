package com.eyeson.tikon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.PaymentType;

/**
 * A PaymentLog.
 */
@Entity
@Table(name = "payment_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "paymentlog")
public class PaymentLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "trace_code")
    private String traceCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @ManyToMany(mappedBy = "paymentLogs")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InvoiceInfo> invoiceInfos = new HashSet<>();

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

    public Set<InvoiceInfo> getInvoiceInfos() {
        return invoiceInfos;
    }

    public void setInvoiceInfos(Set<InvoiceInfo> invoiceInfos) {
        this.invoiceInfos = invoiceInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaymentLog paymentLog = (PaymentLog) o;
        if(paymentLog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, paymentLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PaymentLog{" +
            "id=" + id +
            ", traceCode='" + traceCode + "'" +
            ", paymentType='" + paymentType + "'" +
            ", createDate='" + createDate + "'" +
            '}';
    }
}
