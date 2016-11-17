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
 * A ServiceOptionInfo.
 */
@Entity
@Table(name = "service_option_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "serviceoptioninfo")
public class ServiceOptionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToMany(mappedBy = "options")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ServiceItem> serviceItems = new HashSet<>();

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
        ServiceOptionInfo serviceOptionInfo = (ServiceOptionInfo) o;
        if(serviceOptionInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviceOptionInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceOptionInfo{" +
            "id=" + id +
            ", title='" + title + "'" +
            '}';
    }
}
