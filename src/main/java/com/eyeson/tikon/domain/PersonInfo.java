package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PersonInfo.
 */
@Entity
@Table(name = "person_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "personinfo")
public class PersonInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "national_code")
    private String nationalCode;

    @Column(name = "fisrt_name")
    private String fisrtName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private ZonedDateTime birthDate;

    @ManyToOne(cascade = {CascadeType.ALL})
    private LocationInfo location;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "person_info_social_network_info",
               joinColumns = @JoinColumn(name="person_infos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="social_network_infos_id", referencedColumnName="ID"))
    private Set<SocialNetworkInfo> socialNetworkInfos = new HashSet<>();

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

    public Boolean isGender() {
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

    public LocationInfo getLocation() {
        return location;
    }

    public void setLocation(LocationInfo locationInfo) {
        this.location = locationInfo;
    }



    public Set<SocialNetworkInfo> getSocialNetworkInfos() {
        return socialNetworkInfos;
    }

    public void setSocialNetworkInfos(Set<SocialNetworkInfo> socialNetworkInfos) {
        this.socialNetworkInfos = socialNetworkInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonInfo personInfo = (PersonInfo) o;
        if(personInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, personInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PersonInfo{" +
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
