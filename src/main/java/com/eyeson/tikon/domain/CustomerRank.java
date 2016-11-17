package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CustomerRank.
 */
@Entity
@Table(name = "customer_rank")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customerrank")
public class CustomerRank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "rank_value")
    private Double rankValue;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private ServiceItem serviceItem;

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

    public Double getRankValue() {
        return rankValue;
    }

    public void setRankValue(Double rankValue) {
        this.rankValue = rankValue;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        CustomerRank customerRank = (CustomerRank) o;
        if(customerRank.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, customerRank.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerRank{" +
            "id=" + id +
            ", createDate='" + createDate + "'" +
            ", rankValue='" + rankValue + "'" +
            '}';
    }
}
