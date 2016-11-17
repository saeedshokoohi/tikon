package com.eyeson.tikon.web.rest.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ServiceTimeSession entity.
 */
public class ServiceTimeSessionDTO implements Serializable {

    private Long id;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer capacity;

    private String comment;

    private Double price;

    private Double discount;


    private Long serviceTimeSessionInfoId;
    
    private Long sessionTimesId;
    
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

    public Long getServiceTimeSessionInfoId() {
        return serviceTimeSessionInfoId;
    }

    public void setServiceTimeSessionInfoId(Long serviceTimeSessionInfoId) {
        this.serviceTimeSessionInfoId = serviceTimeSessionInfoId;
    }

    public Long getSessionTimesId() {
        return sessionTimesId;
    }

    public void setSessionTimesId(Long serviceTimeSessionInfoId) {
        this.sessionTimesId = serviceTimeSessionInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceTimeSessionDTO serviceTimeSessionDTO = (ServiceTimeSessionDTO) o;

        if ( ! Objects.equals(id, serviceTimeSessionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceTimeSessionDTO{" +
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
