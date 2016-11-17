package com.eyeson.tikon.web.rest.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the PersonInfo entity.
 */
public class PersonInfoDTO implements Serializable {

    private Long id;

    private String nationalCode;

    private String fisrtName;

    private String lastName;

    private Boolean gender;

    private String phoneNumber;

    private ZonedDateTime birthDate;


    private Long locationId;
    private LocationInfoDTO location = new LocationInfoDTO();


    private Set<SocialNetworkInfoDTO> socialNetworkInfos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }
    public String getFisrtName() {
        return fisrtName;
    }

    public void setFisrtName(String fisrtName) {
        this.fisrtName = fisrtName;
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public ZonedDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationInfoId) {
        this.locationId = locationInfoId;
    }

    public Set<SocialNetworkInfoDTO> getSocialNetworkInfos() {
        return socialNetworkInfos;
    }

    public void setSocialNetworkInfos(Set<SocialNetworkInfoDTO> socialNetworkInfos) {
        this.socialNetworkInfos = socialNetworkInfos;
    }

    public LocationInfoDTO getLocation() {
        return location;
    }

    public void setLocation(LocationInfoDTO locationInfo) {
        this.location = locationInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonInfoDTO personInfoDTO = (PersonInfoDTO) o;

        if ( ! Objects.equals(id, personInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PersonInfoDTO{" +
            "id=" + id +
            ", nationalCode='" + nationalCode + "'" +
            ", fisrtName='" + fisrtName + "'" +
            ", lastName='" + lastName + "'" +
            ", gender='" + gender + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", birthDate='" + birthDate + "'" +
            '}';
    }
}
