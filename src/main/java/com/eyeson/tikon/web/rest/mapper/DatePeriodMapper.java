package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.DatePeriodDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity DatePeriod and its DTO DatePeriodDTO.
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {OffDayMapper.class, })
public interface DatePeriodMapper {

    DatePeriodDTO datePeriodToDatePeriodDTO(DatePeriod datePeriod);

    List<DatePeriodDTO> datePeriodsToDatePeriodDTOs(List<DatePeriod> datePeriods);

    @Mapping(target = "weeklyScheduleInfos", ignore = true)
    DatePeriod datePeriodDTOToDatePeriod(DatePeriodDTO datePeriodDTO);

    List<DatePeriod> datePeriodDTOsToDatePeriods(List<DatePeriodDTO> datePeriodDTOs);

    default OffDay offDayFromId(Long id) {
        if (id == null) {
            return null;
        }
        OffDay offDay = new OffDay();
        offDay.setId(id);
        return offDay;
    }
}
