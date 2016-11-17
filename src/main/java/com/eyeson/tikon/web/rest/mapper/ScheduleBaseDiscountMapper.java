package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ScheduleBaseDiscountDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ScheduleBaseDiscount and its DTO ScheduleBaseDiscountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ScheduleBaseDiscountMapper {

    @Mapping(source = "scheduleInfo.id", target = "scheduleInfoId")
    @Mapping(source = "discountInfo.id", target = "discountInfoId")
    ScheduleBaseDiscountDTO scheduleBaseDiscountToScheduleBaseDiscountDTO(ScheduleBaseDiscount scheduleBaseDiscount);

    List<ScheduleBaseDiscountDTO> scheduleBaseDiscountsToScheduleBaseDiscountDTOs(List<ScheduleBaseDiscount> scheduleBaseDiscounts);

    @Mapping(source = "scheduleInfoId", target = "scheduleInfo")
    @Mapping(source = "discountInfoId", target = "discountInfo")
    ScheduleBaseDiscount scheduleBaseDiscountDTOToScheduleBaseDiscount(ScheduleBaseDiscountDTO scheduleBaseDiscountDTO);

    List<ScheduleBaseDiscount> scheduleBaseDiscountDTOsToScheduleBaseDiscounts(List<ScheduleBaseDiscountDTO> scheduleBaseDiscountDTOs);

    default ScheduleInfo scheduleInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        ScheduleInfo scheduleInfo = new ScheduleInfo();
        scheduleInfo.setId(id);
        return scheduleInfo;
    }

    default DiscountInfo discountInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        DiscountInfo discountInfo = new DiscountInfo();
        discountInfo.setId(id);
        return discountInfo;
    }
}
