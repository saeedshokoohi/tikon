package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the MetaTag entity.
 */
public class MetaTagDTO implements Serializable {

    private Long id;

    private String tag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MetaTagDTO metaTagDTO = (MetaTagDTO) o;

        if ( ! Objects.equals(id, metaTagDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MetaTagDTO{" +
            "id=" + id +
            ", tag='" + tag + "'" +
            '}';
    }
}
