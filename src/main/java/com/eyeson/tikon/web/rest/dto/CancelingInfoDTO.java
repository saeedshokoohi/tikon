package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the CancelingInfo entity.
 */
public class CancelingInfoDTO implements Serializable {

    private Long id;

    private String description;

    private Integer status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CancelingInfoDTO cancelingInfoDTO = (CancelingInfoDTO) o;

        if ( ! Objects.equals(id, cancelingInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CancelingInfoDTO{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
