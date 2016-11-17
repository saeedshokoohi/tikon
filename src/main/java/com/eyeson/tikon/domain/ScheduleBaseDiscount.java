package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ScheduleBaseDiscount.
 */
@Entity
@Table(name = "schedule_base_discount")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "schedulebasediscount")
public class ScheduleBaseDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    private ScheduleInfo scheduleInfo;

    @ManyToOne
    private DiscountInfo discountInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ScheduleInfo getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(ScheduleInfo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    public DiscountInfo getDiscountInfo() {
        return discountInfo;
    }

    public void setDiscountInfo(DiscountInfo discountInfo) {
        this.discountInfo = discountInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScheduleBaseDiscount scheduleBaseDiscount = (ScheduleBaseDiscount) o;
        if(scheduleBaseDiscount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, scheduleBaseDiscount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ScheduleBaseDiscount{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }
}
