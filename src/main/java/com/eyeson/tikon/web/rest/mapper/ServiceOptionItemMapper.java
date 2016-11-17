package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ServiceOptionItemDTO;

import com.eyeson.tikon.web.rest.mapper.decorator.ServiceOptionItemMapperDecorator;
import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ServiceOptionItem and its DTO ServiceOptionItemDTO.
 */
//@Mapper(componentModel = "spring", uses = {})
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {})
@DecoratedWith(ServiceOptionItemMapperDecorator.class)
public interface ServiceOptionItemMapper {

    @Mapping(source = "optionInfo.id", target = "optionInfoId")
    @Mapping(source = "priceInfo.id", target = "priceInfoId")
    @Mapping(source = "images.id", target = "imagesId")
    @Mapping(target = "optionInfo",ignore = true)
//    @Mapping(target = "priceInfoDtail",ignore = true)
    ServiceOptionItemDTO serviceOptionItemToServiceOptionItemDTO(ServiceOptionItem serviceOptionItem);

    List<ServiceOptionItemDTO> serviceOptionItemsToServiceOptionItemDTOs(List<ServiceOptionItem> serviceOptionItems);

    @Mapping(source = "optionInfoId", target = "optionInfo")
    @Mapping(source = "priceInfoId", target = "priceInfo")
    @Mapping(source = "imagesId", target = "images")
//    @Mapping(target = "priceInfoDtail", ignore = true)
    ServiceOptionItem serviceOptionItemDTOToServiceOptionItem(ServiceOptionItemDTO serviceOptionItemDTO);

    List<ServiceOptionItem> serviceOptionItemDTOsToServiceOptionItems(List<ServiceOptionItemDTO> serviceOptionItemDTOs);

    default ServiceOptionInfo serviceOptionInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceOptionInfo serviceOptionInfo = new ServiceOptionInfo();
        serviceOptionInfo.setId(id);
        return serviceOptionInfo;
    }

    default PriceInfo priceInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setId(id);
        return priceInfo;
    }

    default AlbumInfo albumInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        AlbumInfo albumInfo = new AlbumInfo();
        albumInfo.setId(id);
        return albumInfo;
    }
}
