package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ServiceCapacityInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ServiceCapacityInfo and its DTO ServiceCapacityInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceCapacityInfoMapper {

    ServiceCapacityInfoDTO serviceCapacityInfoToServiceCapacityInfoDTO(ServiceCapacityInfo serviceCapacityInfo);

    List<ServiceCapacityInfoDTO> serviceCapacityInfosToServiceCapacityInfoDTOs(List<ServiceCapacityInfo> serviceCapacityInfos);

    ServiceCapacityInfo serviceCapacityInfoDTOToServiceCapacityInfo(ServiceCapacityInfoDTO serviceCapacityInfoDTO);

    List<ServiceCapacityInfo> serviceCapacityInfoDTOsToServiceCapacityInfos(List<ServiceCapacityInfoDTO> serviceCapacityInfoDTOs);
}
