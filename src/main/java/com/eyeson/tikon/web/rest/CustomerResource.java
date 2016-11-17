package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.Customer;
import com.eyeson.tikon.service.CustomerService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.CustomerDTO;
import com.eyeson.tikon.web.rest.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);
        
    @Inject
    private CustomerService customerService;
    
    @Inject
    private CustomerMapper customerMapper;
    
    /**
     * POST  /customers : Create a new customer.
     *
     * @param customerDTO the customerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerDTO, or with status 400 (Bad Request) if the customer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customerDTO);
        if (customerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customer", "idexists", "A new customer cannot already have an ID")).body(null);
        }
        CustomerDTO result = customerService.save(customerDTO);
        return ResponseEntity.created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customers : Updates an existing customer.
     *
     * @param customerDTO the customerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerDTO,
     * or with status 400 (Bad Request) if the customerDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to update Customer : {}", customerDTO);
        if (customerDTO.getId() == null) {
            return createCustomer(customerDTO);
        }
        CustomerDTO result = customerService.save(customerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customer", customerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customers : get all the customers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/customers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Customers");
        Page<Customer> page = customerService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customers");
        return new ResponseEntity<>(customerMapper.customersToCustomerDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /customers/:id : get the "id" customer.
     *
     * @param id the id of the customerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/customers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);
        CustomerDTO customerDTO = customerService.findOne(id);
        return Optional.ofNullable(customerDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customers/:id : delete the "id" customer.
     *
     * @param id the id of the customerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/customers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.debug("REST request to delete Customer : {}", id);
        customerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customer", id.toString())).build();
    }

    /**
     * SEARCH  /_search/customers?query=:query : search for the customer corresponding
     * to the query.
     *
     * @param query the query of the customer search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/customers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Customers for query {}", query);
        Page<Customer> page = customerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customers");
        return new ResponseEntity<>(customerMapper.customersToCustomerDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
