package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ThemeSettingInfo.
 */
@Entity
@Table(name = "theme_setting_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "themesettinginfo")
public class ThemeSettingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private ImageData logoImage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageData getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(ImageData imageData) {
        this.logoImage = imageData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ThemeSettingInfo themeSettingInfo = (ThemeSettingInfo) o;
        if(themeSettingInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, themeSettingInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ThemeSettingInfo{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
