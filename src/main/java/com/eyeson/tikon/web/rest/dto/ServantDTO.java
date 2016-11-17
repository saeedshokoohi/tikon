package com.eyeson.tikon.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Servant entity.
 */
public class ServantDTO implements Serializable {

    private Long id;

    private String title;

    private Integer level;


    private Long personInfoId;

    private CompanyDTO company;

    private PersonInfoDTO personInfo;

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
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getPersonInfoId() {
        return personInfoId;
    }

    public void setPersonInfoId(Long personInfoId) {
        this.personInfoId = personInfoId;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public PersonInfoDTO getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfoDTO personInfo) {
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

        ServantDTO servantDTO = (ServantDTO) o;

        if ( ! Objects.equals(id, servantDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServantDTO{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", level='" + level + "'" +
            '}';
    }
}
