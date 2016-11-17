package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the CompanyManager entity.
 */
public class CompanyManagerDTO implements Serializable {

    private Long id;

    private Integer managerType;


    private Long companyId;
    
    private Long personInfoId;
    
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getPersonInfoId() {
        return personInfoId;
    }

    public void setPersonInfoId(Long personInfoId) {
        this.personInfoId = personInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompanyManagerDTO companyManagerDTO = (CompanyManagerDTO) o;

        if ( ! Objects.equals(id, companyManagerDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanyManagerDTO{" +
            "id=" + id +
            ", managerType='" + managerType + "'" +
            '}';
    }
}
