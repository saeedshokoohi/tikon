package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ServiceCategory entity.
 */
public class ServiceCategoryDTO implements Serializable {

    private Long id;

    private String categoryName;


    private Long settingId;

    private Long companyId;

    private Long parentId;

    private Set<ServantDTO> servants = new HashSet<>();

    private Long imagesId;

    private SettingInfoDTO setting;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingInfoId) {
        this.settingId = settingInfoId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long serviceCategoryId) {
        this.parentId = serviceCategoryId;
    }

    public Set<ServantDTO> getServants() {
        return servants;
    }

    public void setServants(Set<ServantDTO> servants) {
        this.servants = servants;
    }

    public Long getImagesId() {
        return imagesId;
    }

    public void setImagesId(Long albumInfoId) {
        this.imagesId = albumInfoId;
    }

    public SettingInfoDTO getSetting() {
        return setting;
    }

    public void setSetting(SettingInfoDTO setting) {
        this.setting = setting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceCategoryDTO serviceCategoryDTO = (ServiceCategoryDTO) o;

        if ( ! Objects.equals(id, serviceCategoryDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceCategoryDTO{" +
            "id=" + id +
            ", categoryName='" + categoryName + "'" +
            '}';
    }
}
