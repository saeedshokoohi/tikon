package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.eyeson.tikon.domain.enumeration.PriceType;

/**
 * A PriceInfo.
 */
@Entity
@Table(name = "price_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "priceinfo")
public class PriceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "from_valid_date")
    private ZonedDateTime fromValidDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_type")
    private PriceType priceType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "price_info_servant",
               joinColumns = @JoinColumn(name="price_infos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="servants_id", referencedColumnName="ID"))
    private Set<Servant> servants = new HashSet<>();

    @ManyToMany(mappedBy = "servicePriceInfo")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceItem> serviceItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFromValidDate() {
        return fromValidDate;
    }

    public void setFromValidDate(ZonedDateTime fromValidDate) {
        this.fromValidDate = fromValidDate;
    }

    public PriceType getPriceType() {
        return priceType;
    }

    public void setPriceType(PriceType priceType) {
        this.priceType = priceType;
    }

    public Set<Servant> getServants() {
        return servants;
    }

    public void setServants(Set<Servant> servants) {
        this.servants = servants;
    }

    public Set<ServiceItem> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(Set<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PriceInfo priceInfo = (PriceInfo) o;
        if(priceInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, priceInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PriceInfo{" +
            "id=" + id +
            ", fromValidDate='" + fromValidDate + "'" +
            ", priceType='" + priceType + "'" +
            '}';
    }
}
