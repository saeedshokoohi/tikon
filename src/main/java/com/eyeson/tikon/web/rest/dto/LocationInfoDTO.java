package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the LocationInfo entity.
 */
public class LocationInfoDTO implements Serializable {

    private Long id;

    private String title;

    private String address;

    private String mapX;

    private String mapY;

    private Boolean isActive;


    private Long countryId;
    
    private Long stateId;
    
    private Long cityId;
    
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
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getMapX() {
        return mapX;
    }

    public void setMapX(String mapX) {
        this.mapX = mapX;
    }
    public String getMapY() {
        return mapY;
    }

    public void setMapY(String mapY) {
        this.mapY = mapY;
    }
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long selectorDataId) {
        this.countryId = selectorDataId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long selectorDataId) {
        this.stateId = selectorDataId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long selectorDataId) {
        this.cityId = selectorDataId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocationInfoDTO locationInfoDTO = (LocationInfoDTO) o;

        if ( ! Objects.equals(id, locationInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LocationInfoDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", address='" + address + "'" +
            ", mapX='" + mapX + "'" +
            ", mapY='" + mapY + "'" +
            ", isActive='" + isActive + "'" +
            '}';
    }
}
