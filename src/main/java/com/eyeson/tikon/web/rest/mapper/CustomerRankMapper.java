package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.CustomerRankDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CustomerRank and its DTO CustomerRankDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerRankMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "serviceItem.id", target = "serviceItemId")
    CustomerRankDTO customerRankToCustomerRankDTO(CustomerRank customerRank);

    List<CustomerRankDTO> customerRanksToCustomerRankDTOs(List<CustomerRank> customerRanks);

    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "serviceItemId", target = "serviceItem")
    CustomerRank customerRankDTOToCustomerRank(CustomerRankDTO customerRankDTO);

    List<CustomerRank> customerRankDTOsToCustomerRanks(List<CustomerRankDTO> customerRankDTOs);

    default Customer customerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
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
