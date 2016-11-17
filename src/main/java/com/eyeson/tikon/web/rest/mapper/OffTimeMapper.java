package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.OffTimeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OffTime and its DTO OffTimeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OffTimeMapper {

    OffTimeDTO offTimeToOffTimeDTO(OffTime offTime);

    List<OffTimeDTO> offTimesToOffTimeDTOs(List<OffTime> offTimes);

    @Mapping(target = "durationSlice", ignore = true)
    OffTime offTimeDTOToOffTime(OffTimeDTO offTimeDTO);

    List<OffTime> offTimeDTOsToOffTimes(List<OffTimeDTO> offTimeDTOs);
}
