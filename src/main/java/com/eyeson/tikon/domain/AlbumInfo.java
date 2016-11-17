package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AlbumInfo.
 */
@Entity
@Table(name = "album_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "albuminfo")
public class AlbumInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "is_single_image")
    private Boolean isSingleImage;

    @Column(name = "caption")
    private String caption;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsSingleImage() {
        return isSingleImage;
    }

    public void setIsSingleImage(Boolean isSingleImage) {
        this.isSingleImage = isSingleImage;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlbumInfo albumInfo = (AlbumInfo) o;
        if(albumInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, albumInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AlbumInfo{" +
            "id=" + id +
            ", isSingleImage='" + isSingleImage + "'" +
            ", caption='" + caption + "'" +
            '}';
    }
}
