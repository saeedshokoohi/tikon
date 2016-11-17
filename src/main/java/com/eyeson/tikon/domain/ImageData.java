package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ImageData.
 */
@Entity
@Table(name = "image_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "imagedata")
public class ImageData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "caption")
    private String caption;

    @Column(name = "file_type")
    private String fileType;

    @Lob
    @Column(name = "file_data")
    private byte[] fileData;

    @Column(name = "file_data_content_type")
    private String fileDataContentType;

    @Lob
    @Column(name = "thumbnail_data")
    private byte[] thumbnailData;

    @Column(name = "thumbnail_data_content_type")
    private String thumbnailDataContentType;

    @Column(name = "is_cover_image")
    private Boolean isCoverImage;

    @ManyToOne
    private AlbumInfo albumInfo;

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

    public Boolean isIsCoverImage() {
        return isCoverImage;
    }

    public void setIsCoverImage(Boolean isCoverImage) {
        this.isCoverImage = isCoverImage;
    }

    public AlbumInfo getAlbumInfo() {
        return albumInfo;
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        this.albumInfo = albumInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImageData imageData = (ImageData) o;
        if(imageData.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, imageData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ImageData{" +
            "id=" + id +
            ", fileName='" + fileName + "'" +
            ", caption='" + caption + "'" +
            ", fileType='" + fileType + "'" +
            ", fileData='" + fileData + "'" +
            ", fileDataContentType='" + fileDataContentType + "'" +
            ", thumbnailData='" + thumbnailData + "'" +
            ", thumbnailDataContentType='" + thumbnailDataContentType + "'" +
            ", isCoverImage='" + isCoverImage + "'" +
            '}';
    }
}
