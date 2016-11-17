package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.WeeklyWorkDayDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity WeeklyWorkDay and its DTO WeeklyWorkDayDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WeeklyWorkDayMapper {

    WeeklyWorkDayDTO weeklyWorkDayToWeeklyWorkDayDTO(WeeklyWorkDay weeklyWorkDay);

    List<WeeklyWorkDayDTO> weeklyWorkDaysToWeeklyWorkDayDTOs(List<WeeklyWorkDay> weeklyWorkDays);

    @Mapping(target = "weeklyScheduleInfos", ignore = true)
    WeeklyWorkDay weeklyWorkDayDTOToWeeklyWorkDay(WeeklyWorkDayDTO weeklyWorkDayDTO);

    List<WeeklyWorkDay> weeklyWorkDayDTOsToWeeklyWorkDays(List<WeeklyWorkDayDTO> weeklyWorkDayDTOs);
}
