package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.ActivityType;

/**
 * A DTO for the Company entity.
 */
public class CompanyDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private String phoneNumber;

    private ActivityType activityType;

    private String webSiteUrl;

    private String keyUrl;


    private Long settingId;

    private Long agreementId;

    private Long locationId;

    private LocationInfoDTO locationInfo = new LocationInfoDTO();

    private Set<MetaTagDTO> tags = new HashSet<>();

    private AgreementInfoDTO agreementInfo  = new AgreementInfoDTO() ;


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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl;
    }
    public String getKeyUrl() {
        return keyUrl;
    }

    public void setKeyUrl(String keyUrl) {
        this.keyUrl = keyUrl;
    }

    public Long getSettingId() {
        return settingId;
    }

    public void setSettingId(Long settingInfoId) {
        this.settingId = settingInfoId;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementInfoId) {
        this.agreementId = agreementInfoId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationInfoId) {
        this.locationId = locationInfoId;
    }

    public Set<MetaTagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<MetaTagDTO> metaTags) {
        this.tags = metaTags;
    }

    public LocationInfoDTO getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfoDTO locationInfo) {
        this.locationInfo = locationInfo;
    }

    public AgreementInfoDTO getAgreementInfo() {
        return agreementInfo;
    }

    public void setAgreementInfo(AgreementInfoDTO agreementInfo) {
        this.agreementInfo= agreementInfo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompanyDTO companyDTO = (CompanyDTO) o;

        if ( ! Objects.equals(id, companyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", activityType='" + activityType + "'" +
            ", webSiteUrl='" + webSiteUrl + "'" +
            ", keyUrl='" + keyUrl + "'" +
            '}';
    }
}
