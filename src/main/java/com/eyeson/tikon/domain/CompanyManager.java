package com.eyeson.tikon.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CompanyManager.
 */
@Entity
@Table(name = "company_manager")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "companymanager")
public class CompanyManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "manager_type")
    private Integer managerType;

    @ManyToOne
    private Company company;

    @ManyToOne
    private PersonInfo personInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getManagerType() {
        return managerType;
    }

    public void setManagerType(Integer managerType) {
        this.managerType = managerType;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompanyManager companyManager = (CompanyManager) o;
        if(companyManager.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, companyManager.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanyManager{" +
            "id=" + id +
            ", managerType='" + managerType + "'" +
            '}';
    }
}
