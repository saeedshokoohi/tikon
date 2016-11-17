package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.LocationInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity LocationInfo and its DTO LocationInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LocationInfoMapper {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "city.id", target = "cityId")
    LocationInfoDTO locationInfoToLocationInfoDTO(LocationInfo locationInfo);

    List<LocationInfoDTO> locationInfosToLocationInfoDTOs(List<LocationInfo> locationInfos);

    @Mapping(source = "countryId", target = "country")
    @Mapping(source = "stateId", target = "state")
    @Mapping(source = "cityId", target = "city")
    LocationInfo locationInfoDTOToLocationInfo(LocationInfoDTO locationInfoDTO);

    List<LocationInfo> locationInfoDTOsToLocationInfos(List<LocationInfoDTO> locationInfoDTOs);

    default SelectorData selectorDataFromId(Long id) {
        if (id == null) {
            return null;
        }
        SelectorData selectorData = new SelectorData();
        selectorData.setId(id);
        return selectorData;
    }
}
