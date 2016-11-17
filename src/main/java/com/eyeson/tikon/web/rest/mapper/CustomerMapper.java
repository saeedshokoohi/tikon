package com.eyeson.tikon.web.rest.mapper;

import com.eyeson.tikon.domain.*;
import com.eyeson.tikon.web.rest.dto.CustomerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerMapper {

    @Mapping(source = "personalInfo.id", target = "personalInfoId")
    CustomerDTO customerToCustomerDTO(Customer customer);

    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customers);

    @Mapping(source = "personalInfoId", target = "personalInfo")
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    List<Customer> customerDTOsToCustomers(List<CustomerDTO> customerDTOs);

    default PersonInfo personInfoFromId(Long id) {
        if (id == null) {
            return null;
        }
        PersonInfo personInfo = new PersonInfo();
        personInfo.setId(id);
        return personInfo;
    }
}
