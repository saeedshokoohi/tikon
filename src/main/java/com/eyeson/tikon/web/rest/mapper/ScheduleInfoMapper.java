package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ScheduleInfoDTO;

import com.eyeson.tikon.web.rest.mapper.decorator.ScheduleInfoMapperDecorator;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ScheduleInfo and its DTO ScheduleInfoDTO.
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
@DecoratedWith(ScheduleInfoMapperDecorator.class)
public interface ScheduleInfoMapper {

    @Mapping(source = "weeklyScheduleInfo.id", target = "weeklyScheduleInfoId")
    @Mapping(target = "weeklyScheduleInfo",ignore = true)
    ScheduleInfoDTO scheduleInfoToScheduleInfoDTO(ScheduleInfo scheduleInfo);

    List<ScheduleInfoDTO> scheduleInfosToScheduleInfoDTOs(List<ScheduleInfo> scheduleInfos);

    @Mapping(source = "weeklyScheduleInfoId", target = "weeklyScheduleInfo")
    @Mapping(target = "serviceItems", ignore = true)

    ScheduleInfo scheduleInfoDTOToScheduleInfo(ScheduleInfoDTO scheduleInfoDTO);

    List<ScheduleInfo> scheduleInfoDTOsToScheduleInfos(List<ScheduleInfoDTO> scheduleInfoDTOs);

    default WeeklyScheduleInfo weeklyScheduleInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        WeeklyScheduleInfo weeklyScheduleInfo = new WeeklyScheduleInfo();
        weeklyScheduleInfo.setId(id);
        return weeklyScheduleInfo;
    }
}
