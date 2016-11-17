package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;


/**
 * A DTO for the ImageData entity.
 */
public class ImageDataDTO implements Serializable {

    private Long id;

    private String fileName;

    private String caption;

    private String fileType;

    @Lob
    private byte[] fileData;

    private String fileDataContentType;
    @Lob
    private byte[] thumbnailData;

    private String thumbnailDataContentType;
    private Boolean isCoverImage;


    private Long albumInfoId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileDataContentType() {
        return fileDataContentType;
    }

    public void setFileDataContentType(String fileDataContentType) {
        this.fileDataContentType = fileDataContentType;
    }
    public byte[] getThumbnailData() {
        return thumbnailData;
    }

    public void setThumbnailData(byte[] thumbnailData) {
        this.thumbnailData = thumbnailData;
    }

    public String getThumbnailDataContentType() {
        return thumbnailDataContentType;
    }

    public void setThumbnailDataContentType(String thumbnailDataContentType) {
        this.thumbnailDataContentType = thumbnailDataContentType;
    }
    public Boolean getIsCoverImage() {
        return isCoverImage;
    }

    public void setIsCoverImage(Boolean isCoverImage) {
        this.isCoverImage = isCoverImage;
    }

    public Long getAlbumInfoId() {
        return albumInfoId;
    }

    public void setAlbumInfoId(Long albumInfoId) {
        this.albumInfoId = albumInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ImageDataDTO imageDataDTO = (ImageDataDTO) o;

        if ( ! Objects.equals(id, imageDataDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ImageDataDTO{" +
            "id=" + id +
            ", fileName='" + fileName + "'" +
            ", caption='" + caption + "'" +
            ", fileType='" + fileType + "'" +
            ", fileData='" + fileData + "'" +
            ", thumbnailData='" + thumbnailData + "'" +
            ", isCoverImage='" + isCoverImage + "'" +
            '}';
    }
}
