package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A WaitingList.
 */
@Entity
@Table(name = "waiting_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "waitinglist")
public class WaitingList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "reserve_time")
    private ZonedDateTime reserveTime;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private ServiceItem serviceIte;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public ZonedDateTime getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(ZonedDateTime reserveTime) {
        this.reserveTime = reserveTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceItem getServiceIte() {
        return serviceIte;
    }

    public void setServiceIte(ServiceItem serviceItem) {
        this.serviceIte = serviceItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WaitingList waitingList = (WaitingList) o;
        if(waitingList.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, waitingList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WaitingList{" +
            "id=" + id +
            ", qty='" + qty + "'" +
            ", reserveTime='" + reserveTime + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
