package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.ServiceItemDTO;

import com.eyeson.tikon.web.rest.mapper.decorator.ServiceItemMapperDecorator;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper for the entity ServiceItem and its DTO ServiceItemDTO.
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ServiceOptionInfoMapper.class, ScheduleInfoMapper.class, ServantMapper.class, MetaTagMapper.class,AgreementInfoMapper.class })
@DecoratedWith(ServiceItemMapperDecorator.class)
public interface ServiceItemMapper {

    ServiceItemMapper INSTANCE = Mappers.getMapper( ServiceItemMapper.class );
    @Mapping(source = "discountInfo.id", target = "discountInfoId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "imageAlbum.id", target = "imageAlbumId")
    @Mapping(source = "capacityInfo.id", target = "capacityInfoId")
    @Mapping(source = "category.id", target = "categoryId")
//    @Mapping(source = "priceinfo.id", target = "priceinfoId")
    @Mapping(source = "agreement.id", target = "agreementId")
    @Mapping(target = "servicePriceInfo", ignore = true)
    @Mapping(target = "capacityInfo", ignore = true)
//    @Mapping(target = "agreementInfo", ignore = true)
    ServiceItemDTO serviceItemToServiceItemDTO(ServiceItem serviceItem);

    List<ServiceItemDTO> serviceItemsToServiceItemDTOs(List<ServiceItem> serviceItems);

    @Mapping(source = "discountInfoId", target = "discountInfo")
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "imageAlbumId", target = "imageAlbum")
//    @Mapping(source = "capacityInfoId", target = "capacityInfo")
    @Mapping(source = "categoryId", target = "category")
//    @Mapping(source = "priceinfoId", target = "priceinfo")
//    @Mapping(source = "agreementId", target = "agreement")
    @Mapping(target = "servicePriceInfo", ignore = true)
    @Mapping(target = "capacityInfo", ignore = true)
    ServiceItem serviceItemDTOToServiceItem(ServiceItemDTO serviceItemDTO);

    List<ServiceItem> serviceItemDTOsToServiceItems(List<ServiceItemDTO> serviceItemDTOs);

    default ServiceOptionInfo serviceOptionInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceOptionInfo serviceOptionInfo = new ServiceOptionInfo();
        serviceOptionInfo.setId(id);
        return serviceOptionInfo;
    }

    default DiscountInfo discountInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        DiscountInfo discountInfo = new DiscountInfo();
        discountInfo.setId(id);
        return discountInfo;
    }

    default LocationInfo locationInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setId(id);
        return locationInfo;
    }

    default AlbumInfo albumInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        AlbumInfo albumInfo = new AlbumInfo();
        albumInfo.setId(id);
        return albumInfo;
    }

    default ServiceCapacityInfo serviceCapacityInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceCapacityInfo serviceCapacityInfo = new ServiceCapacityInfo();
        serviceCapacityInfo.setId(id);
        return serviceCapacityInfo;
    }

    default ServiceCategory serviceCategoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceCategory serviceCategory = new ServiceCategory();
        serviceCategory.setId(id);
        return serviceCategory;
    }

    default PriceInfo priceInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setId(id);
        return priceInfo;
    }

    default ScheduleInfo scheduleInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        ScheduleInfo scheduleInfo = new ScheduleInfo();
        scheduleInfo.setId(id);
        return scheduleInfo;
    }

    default Servant servantFromId(Long id) {
        if (id == null) {
            return null;
        }
        Servant servant = new Servant();
        servant.setId(id);
        return servant;
    }

    default AgreementInfo agreementInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        AgreementInfo agreementInfo = new AgreementInfo();
        agreementInfo.setId(id);
        return agreementInfo;
    }

    default MetaTag metaTagFromId(Long id) {
        if (id == null) {
            return null;
        }
        MetaTag metaTag = new MetaTag();
        metaTag.setId(id);
        return metaTag;
    }
}
