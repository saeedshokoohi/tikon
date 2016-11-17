package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.AgreementInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AgreementInfo and its DTO AgreementInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AgreementInfoMapper {

    AgreementInfoDTO agreementInfoToAgreementInfoDTO(AgreementInfo agreementInfo);

    List<AgreementInfoDTO> agreementInfosToAgreementInfoDTOs(List<AgreementInfo> agreementInfos);

    AgreementInfo agreementInfoDTOToAgreementInfo(AgreementInfoDTO agreementInfoDTO);

    List<AgreementInfo> agreementInfoDTOsToAgreementInfos(List<AgreementInfoDTO> agreementInfoDTOs);
}
