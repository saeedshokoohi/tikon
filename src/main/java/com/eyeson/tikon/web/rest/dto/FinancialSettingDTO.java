package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.Currency;

/**
 * A DTO for the FinancialSetting entity.
 */
public class FinancialSettingDTO implements Serializable {

    private Long id;

    private Double taxPercentage;

    private Boolean hasDiscount;

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
    public Boolean getHasDiscount() {
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

        FinancialSettingDTO financialSettingDTO = (FinancialSettingDTO) o;

        if ( ! Objects.equals(id, financialSettingDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FinancialSettingDTO{" +
            "id=" + id +
            ", taxPercentage='" + taxPercentage + "'" +
            ", hasDiscount='" + hasDiscount + "'" +
            ", currency='" + currency + "'" +
            '}';
    }
}
