package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.CustomerComment;
import com.eyeson.tikon.service.CustomerCommentService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.CustomerCommentDTO;
import com.eyeson.tikon.web.rest.mapper.CustomerCommentMapper;
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
 * REST controller for managing CustomerComment.
 */
@RestController
@RequestMapping("/api")
public class CustomerCommentResource {

    private final Logger log = LoggerFactory.getLogger(CustomerCommentResource.class);
        
    @Inject
    private CustomerCommentService customerCommentService;
    
    @Inject
    private CustomerCommentMapper customerCommentMapper;
    
    /**
     * POST  /customer-comments : Create a new customerComment.
     *
     * @param customerCommentDTO the customerCommentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerCommentDTO, or with status 400 (Bad Request) if the customerComment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-comments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerCommentDTO> createCustomerComment(@RequestBody CustomerCommentDTO customerCommentDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerComment : {}", customerCommentDTO);
        if (customerCommentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customerComment", "idexists", "A new customerComment cannot already have an ID")).body(null);
        }
        CustomerCommentDTO result = customerCommentService.save(customerCommentDTO);
        return ResponseEntity.created(new URI("/api/customer-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customerComment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-comments : Updates an existing customerComment.
     *
     * @param customerCommentDTO the customerCommentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerCommentDTO,
     * or with status 400 (Bad Request) if the customerCommentDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerCommentDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-comments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerCommentDTO> updateCustomerComment(@RequestBody CustomerCommentDTO customerCommentDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerComment : {}", customerCommentDTO);
        if (customerCommentDTO.getId() == null) {
            return createCustomerComment(customerCommentDTO);
        }
        CustomerCommentDTO result = customerCommentService.save(customerCommentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customerComment", customerCommentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-comments : get all the customerComments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customerComments in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/customer-comments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerCommentDTO>> getAllCustomerComments(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CustomerComments");
        Page<CustomerComment> page = customerCommentService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-comments");
        return new ResponseEntity<>(customerCommentMapper.customerCommentsToCustomerCommentDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /customer-comments/:id : get the "id" customerComment.
     *
     * @param id the id of the customerCommentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerCommentDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/customer-comments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerCommentDTO> getCustomerComment(@PathVariable Long id) {
        log.debug("REST request to get CustomerComment : {}", id);
        CustomerCommentDTO customerCommentDTO = customerCommentService.findOne(id);
        return Optional.ofNullable(customerCommentDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customer-comments/:id : delete the "id" customerComment.
     *
     * @param id the id of the customerCommentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/customer-comments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCustomerComment(@PathVariable Long id) {
        log.debug("REST request to delete CustomerComment : {}", id);
        customerCommentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerComment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/customer-comments?query=:query : search for the customerComment corresponding
     * to the query.
     *
     * @param query the query of the customerComment search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/customer-comments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CustomerCommentDTO>> searchCustomerComments(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CustomerComments for query {}", query);
        Page<CustomerComment> page = customerCommentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customer-comments");
        return new ResponseEntity<>(customerCommentMapper.customerCommentsToCustomerCommentDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
