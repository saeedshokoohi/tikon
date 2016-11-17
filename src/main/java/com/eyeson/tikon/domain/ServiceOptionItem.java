package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ServiceOptionItem.
 */
@Entity
@Table(name = "service_option_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "serviceoptionitem")
public class ServiceOptionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "option_name")
    private String optionName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private ServiceOptionInfo optionInfo;

    @ManyToOne
    private PriceInfo priceInfo;

    @ManyToOne
    private AlbumInfo images;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceOptionInfo getOptionInfo() {
        return optionInfo;
    }

    public void setOptionInfo(ServiceOptionInfo serviceOptionInfo) {
        this.optionInfo = serviceOptionInfo;
    }

    public PriceInfo getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(PriceInfo priceInfo) {
        this.priceInfo = priceInfo;
    }

    public AlbumInfo getImages() {
        return images;
    }

    public void setImages(AlbumInfo albumInfo) {
        this.images = albumInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceOptionItem serviceOptionItem = (ServiceOptionItem) o;
        if(serviceOptionItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serviceOptionItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceOptionItem{" +
            "id=" + id +
            ", optionName='" + optionName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
