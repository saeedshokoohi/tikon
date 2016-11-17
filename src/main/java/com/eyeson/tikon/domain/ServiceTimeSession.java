package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ServiceTimeSession.
 */
@Entity
@Table(name = "service_time_session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "servicetimesession")
public class ServiceTimeSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "comment")
    private String comment;

    @Column(name = "price")
    private Double price;

    @Column(name = "discount")
    private Double discount;

    @ManyToOne
    @JoinColumn(name = "service_time_session_info_id")
    private ServiceTimeSessionInfo serviceTimeSessionInfo;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public ServiceTimeSessionInfo getServiceTimeSessionInfo() {
        return serviceTimeSessionInfo;
    }

    public void setServiceTimeSessionInfo(ServiceTimeSessionInfo serviceTimeSessionInfo) {
        this.serviceTimeSessionInfo = serviceTimeSessionInfo;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceTimeSession serviceTimeSession = (ServiceTimeSession) o;
        if(serviceTimeSession.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviceTimeSession.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceTimeSession{" +
            "id=" + id +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", capacity='" + capacity + "'" +
            ", comment='" + comment + "'" +
            ", price='" + price + "'" +
            ", discount='" + discount + "'" +
            '}';
    }
}
