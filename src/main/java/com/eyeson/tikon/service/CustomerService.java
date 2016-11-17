package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.Customer;
import com.eyeson.tikon.repository.CustomerRepository;
import com.eyeson.tikon.repository.search.CustomerSearchRepository;
import com.eyeson.tikon.web.rest.dto.CustomerDTO;
import com.eyeson.tikon.web.rest.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Customer.
 */
@Service
@Transactional
public class CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerService.class);
    
    @Inject
    private CustomerRepository customerRepository;
    
    @Inject
    private CustomerMapper customerMapper;
    
    @Inject
    private CustomerSearchRepository customerSearchRepository;
    
    /**
     * Save a customer.
     * 
     * @param customerDTO the entity to save
     * @return the persisted entity
     */
    public CustomerDTO save(CustomerDTO customerDTO) {
        log.debug("Request to save Customer : {}", customerDTO);
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        customer = customerRepository.save(customer);
        CustomerDTO result = customerMapper.customerToCustomerDTO(customer);
        customerSearchRepository.save(customer);
        return result;
    }

    /**
     *  Get all the customers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Customer> findAll(Pageable pageable) {
        log.debug("Request to get all Customers");
        Page<Customer> result = customerRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one customer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CustomerDTO findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        Customer customer = customerRepository.findOne(id);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        return customerDTO;
    }

    /**
     *  Delete the  customer by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Customer : {}", id);
        customerRepository.delete(id);
        customerSearchRepository.delete(id);
    }

    /**
     * Search for the customer corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Customer> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Customers for query {}", query);
        return customerSearchRepository.search(queryStringQuery(query), pageable);
    }
}
