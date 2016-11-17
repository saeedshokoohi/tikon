package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PriceInfoDtail.
 */
@Entity
@Table(name = "price_info_dtail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "priceinfodtail")
public class PriceInfoDtail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "capacity_ratio")
    private Integer capacityRatio;

    @Column(name = "price")
    private Double price;

    @Column(name = "coworker_price")
    private Double coworkerPrice;

    @ManyToOne
    private PriceInfo priceInfo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCapacityRatio() {
        return capacityRatio;
    }

    public void setCapacityRatio(Integer capacityRatio) {
        this.capacityRatio = capacityRatio;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCoworkerPrice() {
        return coworkerPrice;
    }

    public void setCoworkerPrice(Double coworkerPrice) {
        this.coworkerPrice = coworkerPrice;
    }

    public PriceInfo getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(PriceInfo priceInfoSet) {
        this.priceInfo = priceInfoSet;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PriceInfoDtail priceInfoDtail = (PriceInfoDtail) o;
        if(priceInfoDtail.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, priceInfoDtail.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PriceInfoDtail{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", capacityRatio='" + capacityRatio + "'" +
            ", price='" + price + "'" +
            ", coworkerPrice='" + coworkerPrice + "'" +
            '}';
    }
}
