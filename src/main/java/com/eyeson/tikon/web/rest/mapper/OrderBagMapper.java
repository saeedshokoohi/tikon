package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.OrderBagDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OrderBag and its DTO OrderBagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderBagMapper {

    OrderBagDTO orderBagToOrderBagDTO(OrderBag orderBag);

    List<OrderBagDTO> orderBagsToOrderBagDTOs(List<OrderBag> orderBags);

    OrderBag orderBagDTOToOrderBag(OrderBagDTO orderBagDTO);

    List<OrderBag> orderBagDTOsToOrderBags(List<OrderBagDTO> orderBagDTOs);
}
