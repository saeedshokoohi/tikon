package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.DiscountInfoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity DiscountInfo and its DTO DiscountInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DiscountInfoMapper {

    DiscountInfoDTO discountInfoToDiscountInfoDTO(DiscountInfo discountInfo);

    List<DiscountInfoDTO> discountInfosToDiscountInfoDTOs(List<DiscountInfo> discountInfos);

    DiscountInfo discountInfoDTOToDiscountInfo(DiscountInfoDTO discountInfoDTO);

    List<DiscountInfo> discountInfoDTOsToDiscountInfos(List<DiscountInfoDTO> discountInfoDTOs);
}
