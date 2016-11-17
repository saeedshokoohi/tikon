package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.ActivityType;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type")
    private ActivityType activityType;

    @Column(name = "web_site_url")
    private String webSiteUrl;

    @Column(name = "key_url")
    private String keyUrl;

    @ManyToOne
    private SettingInfo setting;

    @ManyToOne
    private AgreementInfo agreement;

    @ManyToOne
    private LocationInfo location;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "company_tags",
               joinColumns = @JoinColumn(name="companies_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="tags_id", referencedColumnName="ID"))
    private Set<MetaTag> tags = new HashSet<>();

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

    public SettingInfo getSetting() {
        return setting;
    }

    public void setSetting(SettingInfo settingInfo) {
        this.setting = settingInfo;
    }

    public AgreementInfo getAgreement() {
        return agreement;
    }

    public void setAgreement(AgreementInfo agreementInfo) {
        this.agreement = agreementInfo;
    }

    public LocationInfo getLocation() {
        return location;
    }

    public void setLocation(LocationInfo locationInfo) {
        this.location = locationInfo;
    }

    public Set<MetaTag> getTags() {
        return tags;
    }

    public void setTags(Set<MetaTag> metaTags) {
        this.tags = metaTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company = (Company) o;
        if(company.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Company{" +
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
