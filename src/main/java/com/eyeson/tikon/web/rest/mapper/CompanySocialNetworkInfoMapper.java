package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.CompanySocialNetworkInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CompanySocialNetworkInfo and its DTO CompanySocialNetworkInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanySocialNetworkInfoMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "socialNetworkInfo.id", target = "socialNetworkInfoId")
    CompanySocialNetworkInfoDTO companySocialNetworkInfoToCompanySocialNetworkInfoDTO(CompanySocialNetworkInfo companySocialNetworkInfo);

    List<CompanySocialNetworkInfoDTO> companySocialNetworkInfosToCompanySocialNetworkInfoDTOs(List<CompanySocialNetworkInfo> companySocialNetworkInfos);

    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "socialNetworkInfoId", target = "socialNetworkInfo")
    CompanySocialNetworkInfo companySocialNetworkInfoDTOToCompanySocialNetworkInfo(CompanySocialNetworkInfoDTO companySocialNetworkInfoDTO);

    List<CompanySocialNetworkInfo> companySocialNetworkInfoDTOsToCompanySocialNetworkInfos(List<CompanySocialNetworkInfoDTO> companySocialNetworkInfoDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default SocialNetworkInfo socialNetworkInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        SocialNetworkInfo socialNetworkInfo = new SocialNetworkInfo();
        socialNetworkInfo.setId(id);
        return socialNetworkInfo;
    }
}
