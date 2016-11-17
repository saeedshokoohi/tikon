package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ExtraSetting entity.
 */
public class ExtraSettingDTO implements Serializable {

    private Long id;

    private String key;

    private String value;


    private Long settingInfoId;
    
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
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getSettingInfoId() {
        return settingInfoId;
    }

    public void setSettingInfoId(Long settingInfoId) {
        this.settingInfoId = settingInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExtraSettingDTO extraSettingDTO = (ExtraSettingDTO) o;

        if ( ! Objects.equals(id, extraSettingDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExtraSettingDTO{" +
            "id=" + id +
            ", key='" + key + "'" +
            ", value='" + value + "'" +
            '}';
    }
}
