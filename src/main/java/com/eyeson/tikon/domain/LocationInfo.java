package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LocationInfo.
 */
@Entity
@Table(name = "location_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "locationinfo")
public class LocationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "address")
    private String address;

    @Column(name = "map_x")
    private String mapX;

    @Column(name = "map_y")
    private String mapY;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    private SelectorData country;

    @ManyToOne
    private SelectorData state;

    @ManyToOne
    private SelectorData city;

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

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public SelectorData getCountry() {
        return country;
    }

    public void setCountry(SelectorData selectorData) {
        this.country = selectorData;
    }

    public SelectorData getState() {
        return state;
    }

    public void setState(SelectorData selectorData) {
        this.state = selectorData;
    }

    public SelectorData getCity() {
        return city;
    }

    public void setCity(SelectorData selectorData) {
        this.city = selectorData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationInfo locationInfo = (LocationInfo) o;
        if(locationInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, locationInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", address='" + address + "'" +
            ", mapX='" + mapX + "'" +
            ", mapY='" + mapY + "'" +
            ", isActive='" + isActive + "'" +
            '}';
    }
}
