package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CompanySocialNetworkInfo.
 */
@Entity
@Table(name = "company_social_network_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "companysocialnetworkinfo")
public class CompanySocialNetworkInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_number")
    private Integer orderNumber;

    @ManyToOne
    private Company company;

    @ManyToOne
    private SocialNetworkInfo socialNetworkInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public SocialNetworkInfo getSocialNetworkInfo() {
        return socialNetworkInfo;
    }

    public void setSocialNetworkInfo(SocialNetworkInfo socialNetworkInfo) {
        this.socialNetworkInfo = socialNetworkInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompanySocialNetworkInfo companySocialNetworkInfo = (CompanySocialNetworkInfo) o;
        if(companySocialNetworkInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, companySocialNetworkInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanySocialNetworkInfo{" +
            "id=" + id +
            ", orderNumber='" + orderNumber + "'" +
            '}';
    }
}
