package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the SelectorDataType entity.
 */
public class SelectorDataTypeDTO implements Serializable {

    private Long id;

    private String key;

    private String typeName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SelectorDataTypeDTO selectorDataTypeDTO = (SelectorDataTypeDTO) o;

        if ( ! Objects.equals(id, selectorDataTypeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SelectorDataTypeDTO{" +
            "id=" + id +
            ", key='" + key + "'" +
            ", typeName='" + typeName + "'" +
            '}';
    }
}
