package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.OrderBagItemOptionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OrderBagItemOption and its DTO OrderBagItemOptionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderBagItemOptionMapper {

    @Mapping(source = "orderbagItemDtail.id", target = "orderbagItemDtailId")
    @Mapping(source = "orderbagItem.id", target = "orderbagItemId")
    OrderBagItemOptionDTO orderBagItemOptionToOrderBagItemOptionDTO(OrderBagItemOption orderBagItemOption);

    List<OrderBagItemOptionDTO> orderBagItemOptionsToOrderBagItemOptionDTOs(List<OrderBagItemOption> orderBagItemOptions);

    @Mapping(source = "orderbagItemDtailId", target = "orderbagItemDtail")
    @Mapping(source = "orderbagItemId", target = "orderbagItem")
    OrderBagItemOption orderBagItemOptionDTOToOrderBagItemOption(OrderBagItemOptionDTO orderBagItemOptionDTO);

    List<OrderBagItemOption> orderBagItemOptionDTOsToOrderBagItemOptions(List<OrderBagItemOptionDTO> orderBagItemOptionDTOs);

    default OrderBagServiceItemDtail orderBagServiceItemDtailFromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderBagServiceItemDtail orderBagServiceItemDtail = new OrderBagServiceItemDtail();
        orderBagServiceItemDtail.setId(id);
        return orderBagServiceItemDtail;
    }

    default ServiceOptionItem serviceOptionItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceOptionItem serviceOptionItem = new ServiceOptionItem();
        serviceOptionItem.setId(id);
        return serviceOptionItem;
    }
}
