package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.OrderBagServiceItemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OrderBagServiceItem and its DTO OrderBagServiceItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderBagServiceItemMapper {

    @Mapping(source = "orderBag.id", target = "orderBagId")
    @Mapping(source = "serviceItem.id", target = "serviceItemId")
    OrderBagServiceItemDTO orderBagServiceItemToOrderBagServiceItemDTO(OrderBagServiceItem orderBagServiceItem);

    List<OrderBagServiceItemDTO> orderBagServiceItemsToOrderBagServiceItemDTOs(List<OrderBagServiceItem> orderBagServiceItems);

    @Mapping(source = "orderBagId", target = "orderBag")
    @Mapping(source = "serviceItemId", target = "serviceItem")
    OrderBagServiceItem orderBagServiceItemDTOToOrderBagServiceItem(OrderBagServiceItemDTO orderBagServiceItemDTO);

    List<OrderBagServiceItem> orderBagServiceItemDTOsToOrderBagServiceItems(List<OrderBagServiceItemDTO> orderBagServiceItemDTOs);

    default OrderBag orderBagFromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderBag orderBag = new OrderBag();
        orderBag.setId(id);
        return orderBag;
    }

    default ServiceItem serviceItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setId(id);
        return serviceItem;
    }
}
