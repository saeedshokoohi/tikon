package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the SelectorData entity.
 */
public class SelectorDataDTO implements Serializable {

    private Long id;

    private String key;

    private String text;

    private Integer orderNo;


    private Long typeId;
    
    private Long parentId;
    
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
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long selectorDataTypeId) {
        this.typeId = selectorDataTypeId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long selectorDataId) {
        this.parentId = selectorDataId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SelectorDataDTO selectorDataDTO = (SelectorDataDTO) o;

        if ( ! Objects.equals(id, selectorDataDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SelectorDataDTO{" +
            "id=" + id +
            ", key='" + key + "'" +
            ", text='" + text + "'" +
            ", orderNo='" + orderNo + "'" +
            '}';
    }
}
