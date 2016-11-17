package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the AlbumInfo entity.
 */
public class AlbumInfoDTO implements Serializable {

    private Long id;

    private Boolean isSingleImage;

    private String caption;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Boolean getIsSingleImage() {
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

        AlbumInfoDTO albumInfoDTO = (AlbumInfoDTO) o;

        if ( ! Objects.equals(id, albumInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AlbumInfoDTO{" +
            "id=" + id +
            ", isSingleImage='" + isSingleImage + "'" +
            ", caption='" + caption + "'" +
            '}';
    }
}
