package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ServiceTimeSessionInfo entity.
 */
public class ServiceTimeSessionInfoDTO implements Serializable {

    private Long id;

    private Integer type;


    private Long serviceItemId;
    
    private Long scheduleInfoId;
    
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

    public Long getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Long serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    public Long getScheduleInfoId() {
        return scheduleInfoId;
    }

    public void setScheduleInfoId(Long scheduleInfoId) {
        this.scheduleInfoId = scheduleInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceTimeSessionInfoDTO serviceTimeSessionInfoDTO = (ServiceTimeSessionInfoDTO) o;

        if ( ! Objects.equals(id, serviceTimeSessionInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceTimeSessionInfoDTO{" +
            "id=" + id +
            ", type='" + type + "'" +
            '}';
    }
}
