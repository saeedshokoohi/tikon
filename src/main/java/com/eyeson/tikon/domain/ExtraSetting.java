package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ExtraSetting.
 */
@Entity
@Table(name = "extra_setting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "extrasetting")
public class ExtraSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @ManyToOne
    private SettingInfo settingInfo;

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

    public SettingInfo getSettingInfo() {
        return settingInfo;
    }

    public void setSettingInfo(SettingInfo settingInfo) {
        this.settingInfo = settingInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExtraSetting extraSetting = (ExtraSetting) o;
        if(extraSetting.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, extraSetting.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExtraSetting{" +
            "id=" + id +
            ", key='" + key + "'" +
            ", value='" + value + "'" +
            '}';
    }
}
