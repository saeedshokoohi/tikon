package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ThemeSettingInfo entity.
 */
public class ThemeSettingInfoDTO implements Serializable {

    private Long id;

    private String name;


    private Long logoImageId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLogoImageId() {
        return logoImageId;
    }

    public void setLogoImageId(Long imageDataId) {
        this.logoImageId = imageDataId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ThemeSettingInfoDTO themeSettingInfoDTO = (ThemeSettingInfoDTO) o;

        if ( ! Objects.equals(id, themeSettingInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ThemeSettingInfoDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
