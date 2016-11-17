package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ServiceCapacityInfo entity.
 */
public class ServiceCapacityInfoDTO implements Serializable {

    private Long id;

    private Integer qty;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceCapacityInfoDTO serviceCapacityInfoDTO = (ServiceCapacityInfoDTO) o;

        if ( ! Objects.equals(id, serviceCapacityInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceCapacityInfoDTO{" +
            "id=" + id +
            ", qty='" + qty + "'" +
            '}';
    }
}
