package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.CancelingInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CancelingInfo and its DTO CancelingInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CancelingInfoMapper {

    CancelingInfoDTO cancelingInfoToCancelingInfoDTO(CancelingInfo cancelingInfo);

    List<CancelingInfoDTO> cancelingInfosToCancelingInfoDTOs(List<CancelingInfo> cancelingInfos);

    CancelingInfo cancelingInfoDTOToCancelingInfo(CancelingInfoDTO cancelingInfoDTO);

    List<CancelingInfo> cancelingInfoDTOsToCancelingInfos(List<CancelingInfoDTO> cancelingInfoDTOs);
}
