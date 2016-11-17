package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.PriceInfoDtailDTO;
import com.eyeson.tikon.web.rest.mapper.decorator.PriceInfoDtailMapperDecorator;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PriceInfoDtail and its DTO PriceInfoDtailDTO.
 */
//@Mapper(componentModel = "spring", uses = {})
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
@DecoratedWith(PriceInfoDtailMapperDecorator.class)
public interface PriceInfoDtailMapper {

    @Mapping(source = "priceInfo.id", target = "priceInfoId")
    @Mapping(target = "priceInfo",ignore = true)
    PriceInfoDtailDTO priceInfoDtailToPriceInfoDtailDTO(PriceInfoDtail priceInfoDtail);

    List<PriceInfoDtailDTO> priceInfoDtailsToPriceInfoDtailDTOs(List<PriceInfoDtail> priceInfoDtails);

    @Mapping(source = "priceInfoId", target = "priceInfo")
    PriceInfoDtail priceInfoDtailDTOToPriceInfoDtail(PriceInfoDtailDTO priceInfoDtailDTO);

    List<PriceInfoDtail> priceInfoDtailDTOsToPriceInfoDtails(List<PriceInfoDtailDTO> priceInfoDtailDTOs);


    default PriceInfo priceInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setId(id);
        return priceInfo;
    }

}
