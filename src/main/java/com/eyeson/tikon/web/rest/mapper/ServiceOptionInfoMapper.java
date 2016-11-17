package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ServiceOptionInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ServiceOptionInfo and its DTO ServiceOptionInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceOptionInfoMapper {

    ServiceOptionInfoDTO serviceOptionInfoToServiceOptionInfoDTO(ServiceOptionInfo serviceOptionInfo);

    List<ServiceOptionInfoDTO> serviceOptionInfosToServiceOptionInfoDTOs(List<ServiceOptionInfo> serviceOptionInfos);

    @Mapping(target = "serviceItems", ignore = true)
    ServiceOptionInfo serviceOptionInfoDTOToServiceOptionInfo(ServiceOptionInfoDTO serviceOptionInfoDTO);

    List<ServiceOptionInfo> serviceOptionInfoDTOsToServiceOptionInfos(List<ServiceOptionInfoDTO> serviceOptionInfoDTOs);
}
