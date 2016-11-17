package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.RelatedServiceItemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity RelatedServiceItem and its DTO RelatedServiceItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RelatedServiceItemMapper {

    @Mapping(source = "firstItem.id", target = "firstItemId")
    @Mapping(source = "secondItem.id", target = "secondItemId")
    RelatedServiceItemDTO relatedServiceItemToRelatedServiceItemDTO(RelatedServiceItem relatedServiceItem);

    List<RelatedServiceItemDTO> relatedServiceItemsToRelatedServiceItemDTOs(List<RelatedServiceItem> relatedServiceItems);

    @Mapping(source = "firstItemId", target = "firstItem")
    @Mapping(source = "secondItemId", target = "secondItem")
    RelatedServiceItem relatedServiceItemDTOToRelatedServiceItem(RelatedServiceItemDTO relatedServiceItemDTO);

    List<RelatedServiceItem> relatedServiceItemDTOsToRelatedServiceItems(List<RelatedServiceItemDTO> relatedServiceItemDTOs);

    default ServiceItem serviceItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setId(id);
        return serviceItem;
    }
}
