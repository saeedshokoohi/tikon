package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.DiscountType;

/**
 * A DiscountInfo.
 */
@Entity
@Table(name = "discount_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "discountinfo")
public class DiscountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fixed_discount")
    private Double fixedDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFixedDiscount() {
        return fixedDiscount;
    }

    public void setFixedDiscount(Double fixedDiscount) {
        this.fixedDiscount = fixedDiscount;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiscountInfo discountInfo = (DiscountInfo) o;
        if(discountInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, discountInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DiscountInfo{" +
            "id=" + id +
            ", fixedDiscount='" + fixedDiscount + "'" +
            ", discountType='" + discountType + "'" +
            '}';
    }
}
