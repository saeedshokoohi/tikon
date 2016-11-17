package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ServiceTimeSessionInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ServiceTimeSessionInfo and its DTO ServiceTimeSessionInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceTimeSessionInfoMapper {

    @Mapping(source = "serviceItem.id", target = "serviceItemId")
    @Mapping(source = "scheduleInfo.id", target = "scheduleInfoId")
    ServiceTimeSessionInfoDTO serviceTimeSessionInfoToServiceTimeSessionInfoDTO(ServiceTimeSessionInfo serviceTimeSessionInfo);

    List<ServiceTimeSessionInfoDTO> serviceTimeSessionInfosToServiceTimeSessionInfoDTOs(List<ServiceTimeSessionInfo> serviceTimeSessionInfos);

    @Mapping(source = "serviceItemId", target = "serviceItem")
    @Mapping(source = "scheduleInfoId", target = "scheduleInfo")
    @Mapping(target = "serviceTimeSessions", ignore = true)
    ServiceTimeSessionInfo serviceTimeSessionInfoDTOToServiceTimeSessionInfo(ServiceTimeSessionInfoDTO serviceTimeSessionInfoDTO);

    List<ServiceTimeSessionInfo> serviceTimeSessionInfoDTOsToServiceTimeSessionInfos(List<ServiceTimeSessionInfoDTO> serviceTimeSessionInfoDTOs);

    default ServiceItem serviceItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setId(id);
        return serviceItem;
    }

    default ScheduleInfo scheduleInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        ScheduleInfo scheduleInfo = new ScheduleInfo();
        scheduleInfo.setId(id);
        return scheduleInfo;
    }
}
