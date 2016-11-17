package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.SocialNetworkInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SocialNetworkInfo and its DTO SocialNetworkInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SocialNetworkInfoMapper {

    SocialNetworkInfoDTO socialNetworkInfoToSocialNetworkInfoDTO(SocialNetworkInfo socialNetworkInfo);

    List<SocialNetworkInfoDTO> socialNetworkInfosToSocialNetworkInfoDTOs(List<SocialNetworkInfo> socialNetworkInfos);

    @Mapping(target = "personInfos", ignore = true)
    SocialNetworkInfo socialNetworkInfoDTOToSocialNetworkInfo(SocialNetworkInfoDTO socialNetworkInfoDTO);

    List<SocialNetworkInfo> socialNetworkInfoDTOsToSocialNetworkInfos(List<SocialNetworkInfoDTO> socialNetworkInfoDTOs);
}
