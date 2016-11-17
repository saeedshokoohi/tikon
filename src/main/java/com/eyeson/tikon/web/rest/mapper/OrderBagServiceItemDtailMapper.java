package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.OrderBagServiceItemDtailDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OrderBagServiceItemDtail and its DTO OrderBagServiceItemDtailDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderBagServiceItemDtailMapper {

    @Mapping(source = "orderBagItem.id", target = "orderBagItemId")
    @Mapping(source = "priceInfoDtail.id", target = "priceInfoDtailId")
    OrderBagServiceItemDtailDTO orderBagServiceItemDtailToOrderBagServiceItemDtailDTO(OrderBagServiceItemDtail orderBagServiceItemDtail);

    List<OrderBagServiceItemDtailDTO> orderBagServiceItemDtailsToOrderBagServiceItemDtailDTOs(List<OrderBagServiceItemDtail> orderBagServiceItemDtails);

    @Mapping(source = "orderBagItemId", target = "orderBagItem")
    @Mapping(source = "priceInfoDtailId", target = "priceInfoDtail")
    OrderBagServiceItemDtail orderBagServiceItemDtailDTOToOrderBagServiceItemDtail(OrderBagServiceItemDtailDTO orderBagServiceItemDtailDTO);

    List<OrderBagServiceItemDtail> orderBagServiceItemDtailDTOsToOrderBagServiceItemDtails(List<OrderBagServiceItemDtailDTO> orderBagServiceItemDtailDTOs);

    default OrderBagServiceItem orderBagServiceItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderBagServiceItem orderBagServiceItem = new OrderBagServiceItem();
        orderBagServiceItem.setId(id);
        return orderBagServiceItem;
    }

    default PriceInfoDtail priceInfoDtailFromId(Long id) {
        if (id == null) {
            return null;
        }
        PriceInfoDtail priceInfoDtail = new PriceInfoDtail();
        priceInfoDtail.setId(id);
        return priceInfoDtail;
    }
}
