package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ServiceTimeSessionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ServiceTimeSession and its DTO ServiceTimeSessionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceTimeSessionMapper {

    @Mapping(source = "serviceTimeSessionInfo.id", target = "serviceTimeSessionInfoId")

    ServiceTimeSessionDTO serviceTimeSessionToServiceTimeSessionDTO(ServiceTimeSession serviceTimeSession);

    List<ServiceTimeSessionDTO> serviceTimeSessionsToServiceTimeSessionDTOs(List<ServiceTimeSession> serviceTimeSessions);

    @Mapping(source = "serviceTimeSessionInfoId", target = "serviceTimeSessionInfo")

    ServiceTimeSession serviceTimeSessionDTOToServiceTimeSession(ServiceTimeSessionDTO serviceTimeSessionDTO);

    List<ServiceTimeSession> serviceTimeSessionDTOsToServiceTimeSessions(List<ServiceTimeSessionDTO> serviceTimeSessionDTOs);

    default ServiceTimeSessionInfo serviceTimeSessionInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceTimeSessionInfo serviceTimeSessionInfo = new ServiceTimeSessionInfo();
        serviceTimeSessionInfo.setId(id);
        return serviceTimeSessionInfo;
    }
}
