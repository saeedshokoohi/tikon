package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CapacityException.
 */
@Entity
@Table(name = "capacity_exception")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "capacityexception")
public class CapacityException implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "exception_type")
    private Integer exceptionType;

    @Column(name = "reserve_time")
    private ZonedDateTime reserveTime;

    @Column(name = "new_qty")
    private Integer newQty;

    @ManyToOne
    private ServiceCapacityInfo serviceCapacityInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(Integer exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ZonedDateTime getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(ZonedDateTime reserveTime) {
        this.reserveTime = reserveTime;
    }

    public Integer getNewQty() {
        return newQty;
    }

    public void setNewQty(Integer newQty) {
        this.newQty = newQty;
    }

    public ServiceCapacityInfo getServiceCapacityInfo() {
        return serviceCapacityInfo;
    }

    public void setServiceCapacityInfo(ServiceCapacityInfo serviceCapacityInfo) {
        this.serviceCapacityInfo = serviceCapacityInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CapacityException capacityException = (CapacityException) o;
        if(capacityException.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, capacityException.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CapacityException{" +
            "id=" + id +
            ", exceptionType='" + exceptionType + "'" +
            ", reserveTime='" + reserveTime + "'" +
            ", newQty='" + newQty + "'" +
            '}';
    }
}
