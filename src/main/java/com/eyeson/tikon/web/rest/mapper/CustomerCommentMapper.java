package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.CustomerCommentDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CustomerComment and its DTO CustomerCommentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerCommentMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "serviceItem.id", target = "serviceItemId")
    CustomerCommentDTO customerCommentToCustomerCommentDTO(CustomerComment customerComment);

    List<CustomerCommentDTO> customerCommentsToCustomerCommentDTOs(List<CustomerComment> customerComments);

    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "serviceItemId", target = "serviceItem")
    CustomerComment customerCommentDTOToCustomerComment(CustomerCommentDTO customerCommentDTO);

    List<CustomerComment> customerCommentDTOsToCustomerComments(List<CustomerCommentDTO> customerCommentDTOs);

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
