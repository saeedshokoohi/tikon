package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RelatedServiceItem.
 */
@Entity
@Table(name = "related_service_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "relatedserviceitem")
public class RelatedServiceItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type")
    private Integer type;

    @ManyToOne
    private ServiceItem firstItem;

    @ManyToOne
    private ServiceItem secondItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ServiceItem getFirstItem() {
        return firstItem;
    }

    public void setFirstItem(ServiceItem serviceItem) {
        this.firstItem = serviceItem;
    }

    public ServiceItem getSecondItem() {
        return secondItem;
    }

    public void setSecondItem(ServiceItem serviceItem) {
        this.secondItem = serviceItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RelatedServiceItem relatedServiceItem = (RelatedServiceItem) o;
        if(relatedServiceItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, relatedServiceItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RelatedServiceItem{" +
            "id=" + id +
            ", type='" + type + "'" +
            '}';
    }
}
