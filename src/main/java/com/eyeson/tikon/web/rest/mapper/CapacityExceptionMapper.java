package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.CapacityExceptionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CapacityException and its DTO CapacityExceptionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CapacityExceptionMapper {

    @Mapping(source = "serviceCapacityInfo.id", target = "serviceCapacityInfoId")
    CapacityExceptionDTO capacityExceptionToCapacityExceptionDTO(CapacityException capacityException);

    List<CapacityExceptionDTO> capacityExceptionsToCapacityExceptionDTOs(List<CapacityException> capacityExceptions);

    @Mapping(source = "serviceCapacityInfoId", target = "serviceCapacityInfo")
    CapacityException capacityExceptionDTOToCapacityException(CapacityExceptionDTO capacityExceptionDTO);

    List<CapacityException> capacityExceptionDTOsToCapacityExceptions(List<CapacityExceptionDTO> capacityExceptionDTOs);

    default ServiceCapacityInfo serviceCapacityInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceCapacityInfo serviceCapacityInfo = new ServiceCapacityInfo();
        serviceCapacityInfo.setId(id);
        return serviceCapacityInfo;
    }
}
