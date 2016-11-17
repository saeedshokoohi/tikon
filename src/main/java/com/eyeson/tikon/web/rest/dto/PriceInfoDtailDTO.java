package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the PriceInfoDtail entity.
 */
public class PriceInfoDtailDTO implements Serializable {

    private Long id;

    private String title;

    private Integer capacityRatio;

    private Double price;

    private Double coworkerPrice;

    private Long priceInfoId;

    private PriceInfoDTO priceInfo ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getCapacityRatio() {
        return capacityRatio;
    }

    public void setCapacityRatio(Integer capacityRatio) {
        this.capacityRatio = capacityRatio;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public Double getCoworkerPrice() {
        return coworkerPrice;
    }

    public void setCoworkerPrice(Double coworkerPrice) {
        this.coworkerPrice = coworkerPrice;
    }

    public Long getPriceInfoId() {
        return priceInfoId;
    }

    public void setPriceInfoId(Long priceInfoId) {
        this.priceInfoId = priceInfoId;
    }

    public PriceInfoDTO getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(PriceInfoDTO priceInfo) {
        this.priceInfo = priceInfo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PriceInfoDtailDTO priceInfoDtailDTO = (PriceInfoDtailDTO) o;

        if ( ! Objects.equals(id, priceInfoDtailDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PriceInfoDtailDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", capacityRatio='" + capacityRatio + "'" +
            ", price='" + price + "'" +
            ", coworkerPrice='" + coworkerPrice + "'" +
            '}';
    }
}
