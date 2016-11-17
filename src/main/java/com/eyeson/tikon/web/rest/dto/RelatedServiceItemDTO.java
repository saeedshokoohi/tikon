package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the RelatedServiceItem entity.
 */
public class RelatedServiceItemDTO implements Serializable {

    private Long id;

    private Integer type;


    private Long firstItemId;
    
    private Long secondItemId;
    
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

    public Long getFirstItemId() {
        return firstItemId;
    }

    public void setFirstItemId(Long serviceItemId) {
        this.firstItemId = serviceItemId;
    }

    public Long getSecondItemId() {
        return secondItemId;
    }

    public void setSecondItemId(Long serviceItemId) {
        this.secondItemId = serviceItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RelatedServiceItemDTO relatedServiceItemDTO = (RelatedServiceItemDTO) o;

        if ( ! Objects.equals(id, relatedServiceItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RelatedServiceItemDTO{" +
            "id=" + id +
            ", type='" + type + "'" +
            '}';
    }
}
