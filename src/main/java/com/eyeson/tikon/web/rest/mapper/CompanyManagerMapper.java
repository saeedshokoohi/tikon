package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.CompanyManagerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CompanyManager and its DTO CompanyManagerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyManagerMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "personInfo.id", target = "personInfoId")
    CompanyManagerDTO companyManagerToCompanyManagerDTO(CompanyManager companyManager);

    List<CompanyManagerDTO> companyManagersToCompanyManagerDTOs(List<CompanyManager> companyManagers);

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "personInfoId", target = "personInfo")
    CompanyManager companyManagerDTOToCompanyManager(CompanyManagerDTO companyManagerDTO);

    List<CompanyManager> companyManagerDTOsToCompanyManagers(List<CompanyManagerDTO> companyManagerDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default PersonInfo personInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        PersonInfo personInfo = new PersonInfo();
        personInfo.setId(id);
        return personInfo;
    }
}
