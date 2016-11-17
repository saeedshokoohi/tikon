package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.TimePeriodDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity TimePeriod and its DTO TimePeriodDTO.
 */
@Mapper(componentModel = "spring", uses = {OffTimeMapper.class, })
public interface TimePeriodMapper {

    TimePeriodDTO timePeriodToTimePeriodDTO(TimePeriod timePeriod);

    List<TimePeriodDTO> timePeriodsToTimePeriodDTOs(List<TimePeriod> timePeriods);

    @Mapping(target = "weeklyScheduleInfos", ignore = true)
    TimePeriod timePeriodDTOToTimePeriod(TimePeriodDTO timePeriodDTO);

    List<TimePeriod> timePeriodDTOsToTimePeriods(List<TimePeriodDTO> timePeriodDTOs);

    default OffTime offTimeFromId(Long id) {
        if (id == null) {
            return null;
        }
        OffTime offTime = new OffTime();
        offTime.setId(id);
        return offTime;
    }
}
