package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.OffDayDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OffDay and its DTO OffDayDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OffDayMapper {

    OffDayDTO offDayToOffDayDTO(OffDay offDay);

    List<OffDayDTO> offDaysToOffDayDTOs(List<OffDay> offDays);

    @Mapping(target = "datePeriods", ignore = true)
    OffDay offDayDTOToOffDay(OffDayDTO offDayDTO);

    List<OffDay> offDayDTOsToOffDays(List<OffDayDTO> offDayDTOs);
}
