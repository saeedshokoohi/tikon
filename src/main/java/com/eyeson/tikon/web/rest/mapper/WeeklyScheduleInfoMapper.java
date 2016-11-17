package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.WeeklyScheduleInfoDTO;

import com.eyeson.tikon.web.rest.mapper.decorator.WeeklyScheduleInfoMapperDecorator;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity WeeklyScheduleInfo and its DTO WeeklyScheduleInfoDTO.
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE,uses = {DatePeriodMapper.class, TimePeriodMapper.class, WeeklyWorkDayMapper.class, })
@DecoratedWith(WeeklyScheduleInfoMapperDecorator.class)
public interface WeeklyScheduleInfoMapper {

    WeeklyScheduleInfoDTO weeklyScheduleInfoToWeeklyScheduleInfoDTO(WeeklyScheduleInfo weeklyScheduleInfo);

    List<WeeklyScheduleInfoDTO> weeklyScheduleInfosToWeeklyScheduleInfoDTOs(List<WeeklyScheduleInfo> weeklyScheduleInfos);

    WeeklyScheduleInfo weeklyScheduleInfoDTOToWeeklyScheduleInfo(WeeklyScheduleInfoDTO weeklyScheduleInfoDTO);

    List<WeeklyScheduleInfo> weeklyScheduleInfoDTOsToWeeklyScheduleInfos(List<WeeklyScheduleInfoDTO> weeklyScheduleInfoDTOs);

    default DatePeriod datePeriodFromId(Long id) {
        if (id == null) {
            return null;
        }
        DatePeriod datePeriod = new DatePeriod();
        datePeriod.setId(id);
        return datePeriod;
    }

    default TimePeriod timePeriodFromId(Long id) {
        if (id == null) {
            return null;
        }
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setId(id);
        return timePeriod;
    }

    default WeeklyWorkDay weeklyWorkDayFromId(Long id) {
        if (id == null) {
            return null;
        }
        WeeklyWorkDay weeklyWorkDay = new WeeklyWorkDay();
        weeklyWorkDay.setId(id);
        return weeklyWorkDay;
    }
}
