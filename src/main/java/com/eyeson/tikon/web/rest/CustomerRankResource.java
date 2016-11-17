package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.CustomerRank;
import com.eyeson.tikon.service.CustomerRankService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.CustomerRankDTO;
import com.eyeson.tikon.web.rest.mapper.CustomerRankMapper;
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
 * REST controller for managing CustomerRank.
 */
@RestController
@RequestMapping("/api")
public class CustomerRankResource {

    private final Logger log = LoggerFactory.getLogger(CustomerRankResource.class);
        
    @Inject
    private CustomerRankService customerRankService;
    
    @Inject
    private CustomerRankMapper customerRankMapper;
    
    /**
     * POST  /customer-ranks : Create a new customerRank.
     *
     * @param customerRankDTO the customerRankDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerRankDTO, or with status 400 (Bad Request) if the customerRank has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-ranks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerRankDTO> createCustomerRank(@RequestBody CustomerRankDTO customerRankDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerRank : {}", customerRankDTO);
        if (customerRankDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customerRank", "idexists", "A new customerRank cannot already have an ID")).body(null);
        }
        CustomerRankDTO result = customerRankService.save(customerRankDTO);
        return ResponseEntity.created(new URI("/api/customer-ranks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customerRank", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-ranks : Updates an existing customerRank.
     *
     * @param customerRankDTO the customerRankDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerRankDTO,
     * or with status 400 (Bad Request) if the customerRankDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerRankDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-ranks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerRankDTO> updateCustomerRank(@RequestBody CustomerRankDTO customerRankDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerRank : {}", customerRankDTO);
        if (customerRankDTO.getId() == null) {
            return createCustomerRank(customerRankDTO);
        }
        CustomerRankDTO result = customerRankService.save(customerRankDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customerRank", customerRankDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-ranks : get all the customerRanks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customerRanks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/customer-ranks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerRankDTO>> getAllCustomerRanks(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CustomerRanks");
        Page<CustomerRank> page = customerRankService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-ranks");
        return new ResponseEntity<>(customerRankMapper.customerRanksToCustomerRankDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /customer-ranks/:id : get the "id" customerRank.
     *
     * @param id the id of the customerRankDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerRankDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/customer-ranks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerRankDTO> getCustomerRank(@PathVariable Long id) {
        log.debug("REST request to get CustomerRank : {}", id);
        CustomerRankDTO customerRankDTO = customerRankService.findOne(id);
        return Optional.ofNullable(customerRankDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customer-ranks/:id : delete the "id" customerRank.
     *
     * @param id the id of the customerRankDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/customer-ranks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCustomerRank(@PathVariable Long id) {
        log.debug("REST request to delete CustomerRank : {}", id);
        customerRankService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerRank", id.toString())).build();
    }

    /**
     * SEARCH  /_search/customer-ranks?query=:query : search for the customerRank corresponding
     * to the query.
     *
     * @param query the query of the customerRank search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/customer-ranks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerRankDTO>> searchCustomerRanks(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CustomerRanks for query {}", query);
        Page<CustomerRank> page = customerRankService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customer-ranks");
        return new ResponseEntity<>(customerRankMapper.customerRanksToCustomerRankDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
