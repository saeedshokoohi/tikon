package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.PersonInfoDTO;
//import com.eyeson.tikon.web.rest.mapper.decorator.PersonInfoMapperDecorator;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PersonInfo and its DTO PersonInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {SocialNetworkInfoMapper.class,LocationInfoMapper.class })
//@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
//@DecoratedWith(PersonInfoMapperDecorator.class)
public interface PersonInfoMapper {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(target = "socialNetworkInfos",ignore = true)
    PersonInfoDTO personInfoToPersonInfoDTO(PersonInfo personInfo);

    List<PersonInfoDTO> personInfosToPersonInfoDTOs(List<PersonInfo> personInfos);

    @Mapping(target = "socialNetworkInfos",ignore = true)
    PersonInfo personInfoDTOToPersonInfo(PersonInfoDTO personInfoDTO);

    List<PersonInfo> personInfoDTOsToPersonInfos(List<PersonInfoDTO> personInfoDTOs);

    default LocationInfo locationInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setId(id);
        return locationInfo;
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
