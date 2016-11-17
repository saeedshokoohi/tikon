package com.eyeson.tikon.web.rest.dto;

import com.eyeson.tikon.domain.PriceInfoDtail;
import com.eyeson.tikon.domain.ServiceOptionInfo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ServiceOptionItem entity.
 */
public class ServiceOptionItemDTO implements Serializable {

    private Long id;

    private String optionName;

    private String description;


    private Long optionInfoId;

    private Long priceInfoId;

    private Long imagesId;

    private ServiceOptionInfoDTO OptionInfo ;

    private PriceInfoDtailDTO priceInfoDtail ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOptionInfoId() {
        return optionInfoId;
    }

    public void setOptionInfoId(Long serviceOptionInfoId) {
        this.optionInfoId = serviceOptionInfoId;
    }

    public Long getPriceInfoId() {
        return priceInfoId;
    }

    public void setPriceInfoId(Long priceInfoId) {
        this.priceInfoId = priceInfoId;
    }

    public Long getImagesId() {
        return imagesId;
    }

    public void setImagesId(Long albumInfoId) {
        this.imagesId = albumInfoId;
    }

    public ServiceOptionInfoDTO getOptionInfo() {
        return OptionInfo;
    }

    public void setOptionInfo(ServiceOptionInfoDTO serviceOptionInfo) {
        this.OptionInfo = serviceOptionInfo;
    }

    public PriceInfoDtailDTO getPriceInfoDtail() {
        return priceInfoDtail;
    }

    public void setPriceInfoDtail(PriceInfoDtailDTO priceInfoDtail) {
        this.priceInfoDtail = priceInfoDtail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceOptionItemDTO serviceOptionItemDTO = (ServiceOptionItemDTO) o;

        if ( ! Objects.equals(id, serviceOptionItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceOptionItemDTO{" +
            "id=" + id +
            ", optionName='" + optionName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
