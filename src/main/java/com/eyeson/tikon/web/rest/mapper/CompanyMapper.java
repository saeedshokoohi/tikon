package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.CompanyDTO;
import com.eyeson.tikon.web.rest.mapper.decorator.CompanyMapperDecorator;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Company and its DTO CompanyDTO.
 */
//@Mapper(componentModel = "spring", uses = {MetaTagMapper.class, })

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MetaTagMapper.class})
@DecoratedWith(CompanyMapperDecorator.class)
public interface CompanyMapper {

    @Mapping(source = "setting.id", target = "settingId")
    @Mapping(source = "agreement.id", target = "agreementId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(target = "locationInfo",ignore = true)
    @Mapping(target = "agreementInfo",ignore = true)
    CompanyDTO companyToCompanyDTO(Company company);

    List<CompanyDTO> companiesToCompanyDTOs(List<Company> companies);

    @Mapping(source = "settingId", target = "setting")
//    @Mapping(source = "agreementId", target = "agreement")
//    @Mapping(source = "locationId", target = "location")
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    Company companyDTOToCompany(CompanyDTO companyDTO);

    List<Company> companyDTOsToCompanies(List<CompanyDTO> companyDTOs);

    default SettingInfo settingInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        SettingInfo settingInfo = new SettingInfo();
        settingInfo.setId(id);
        return settingInfo;
    }

    default AgreementInfo agreementInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        AgreementInfo agreementInfo = new AgreementInfo();
        agreementInfo.setId(id);
        return agreementInfo;
    }

    default LocationInfo locationInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setId(id);
        return locationInfo;
    }

    default MetaTag metaTagFromId(Long id) {
        if (id == null) {
            return null;
        }
        MetaTag metaTag = new MetaTag();
        metaTag.setId(id);
        return metaTag;
    }
}
