package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ServiceCategory.
 */
@Entity
@Table(name = "service_category")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "servicecategory")
public class ServiceCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne(cascade = {CascadeType.ALL})
    private SettingInfo setting;

    @ManyToOne
    private Company company;

    @ManyToOne
    private ServiceCategory parent;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "service_category_servants",
               joinColumns = @JoinColumn(name="service_categories_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="servants_id", referencedColumnName="ID"))
    private Set<Servant> servants = new HashSet<>();

    @ManyToOne
    private AlbumInfo images;

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

    public SettingInfo getSetting() {
        return setting;
    }

    public void setSetting(SettingInfo settingInfo) {
        this.setting = settingInfo;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ServiceCategory getParent() {
        return parent;
    }

    public void setParent(ServiceCategory serviceCategory) {
        this.parent = serviceCategory;
    }

    public Set<Servant> getServants() {
        return servants;
    }

    public void setServants(Set<Servant> servants) {
        this.servants = servants;
    }

    public AlbumInfo getImages() {
        return images;
    }

    public void setImages(AlbumInfo albumInfo) {
        this.images = albumInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceCategory serviceCategory = (ServiceCategory) o;
        if(serviceCategory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviceCategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceCategory{" +
            "id=" + id +
            ", categoryName='" + categoryName + "'" +
            '}';
    }
}
