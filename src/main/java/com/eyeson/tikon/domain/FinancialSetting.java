package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.Currency;

/**
 * A FinancialSetting.
 */
@Entity
@Table(name = "financial_setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "financialsetting")
public class FinancialSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tax_percentage")
    private Double taxPercentage;

    @Column(name = "has_discount")
    private Boolean hasDiscount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(Double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public Boolean isHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(Boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FinancialSetting financialSetting = (FinancialSetting) o;
        if(financialSetting.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, financialSetting.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FinancialSetting{" +
            "id=" + id +
            ", taxPercentage='" + taxPercentage + "'" +
            ", hasDiscount='" + hasDiscount + "'" +
            ", currency='" + currency + "'" +
            '}';
    }
}
