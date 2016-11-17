package com.eyeson.tikon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Servant.
 */
@Entity
@Table(name = "servant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "servant")
public class Servant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "level")
    private Integer level;

    @ManyToOne
    private PersonInfo personInfo;

    @ManyToOne
    private Company company;



    @ManyToMany(mappedBy = "servants")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceCategory> serviceCategories = new HashSet<>();

    @ManyToMany(mappedBy = "servants")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceItem> serviceItems = new HashSet<>();

    @ManyToMany(mappedBy = "servants")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PriceInfo> priceInfos = new HashSet<>();

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public Set<ServiceCategory> getServiceCategories() {
        return serviceCategories;
    }

    public void setServiceCategories(Set<ServiceCategory> serviceCategories) {
        this.serviceCategories = serviceCategories;
    }

    public Set<ServiceItem> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(Set<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }

    public Set<PriceInfo> getPriceInfos() {
        return priceInfos;
    }

    public void setPriceInfos(Set<PriceInfo> priceInfos) {
        this.priceInfos = priceInfos;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Servant servant = (Servant) o;
        if(servant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, servant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Servant{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", level='" + level + "'" +
            '}';
    }
}
