package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.CapacityException;
import com.eyeson.tikon.service.CapacityExceptionService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.CapacityExceptionDTO;
import com.eyeson.tikon.web.rest.mapper.CapacityExceptionMapper;
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
 * REST controller for managing CapacityException.
 */
@RestController
@RequestMapping("/api")
public class CapacityExceptionResource {

    private final Logger log = LoggerFactory.getLogger(CapacityExceptionResource.class);
        
    @Inject
    private CapacityExceptionService capacityExceptionService;
    
    @Inject
    private CapacityExceptionMapper capacityExceptionMapper;
    
    /**
     * POST  /capacity-exceptions : Create a new capacityException.
     *
     * @param capacityExceptionDTO the capacityExceptionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new capacityExceptionDTO, or with status 400 (Bad Request) if the capacityException has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/capacity-exceptions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CapacityExceptionDTO> createCapacityException(@RequestBody CapacityExceptionDTO capacityExceptionDTO) throws URISyntaxException {
        log.debug("REST request to save CapacityException : {}", capacityExceptionDTO);
        if (capacityExceptionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("capacityException", "idexists", "A new capacityException cannot already have an ID")).body(null);
        }
        CapacityExceptionDTO result = capacityExceptionService.save(capacityExceptionDTO);
        return ResponseEntity.created(new URI("/api/capacity-exceptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("capacityException", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /capacity-exceptions : Updates an existing capacityException.
     *
     * @param capacityExceptionDTO the capacityExceptionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated capacityExceptionDTO,
     * or with status 400 (Bad Request) if the capacityExceptionDTO is not valid,
     * or with status 500 (Internal Server Error) if the capacityExceptionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/capacity-exceptions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CapacityExceptionDTO> updateCapacityException(@RequestBody CapacityExceptionDTO capacityExceptionDTO) throws URISyntaxException {
        log.debug("REST request to update CapacityException : {}", capacityExceptionDTO);
        if (capacityExceptionDTO.getId() == null) {
            return createCapacityException(capacityExceptionDTO);
        }
        CapacityExceptionDTO result = capacityExceptionService.save(capacityExceptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("capacityException", capacityExceptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /capacity-exceptions : get all the capacityExceptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of capacityExceptions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/capacity-exceptions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CapacityExceptionDTO>> getAllCapacityExceptions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CapacityExceptions");
        Page<CapacityException> page = capacityExceptionService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/capacity-exceptions");
        return new ResponseEntity<>(capacityExceptionMapper.capacityExceptionsToCapacityExceptionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /capacity-exceptions/:id : get the "id" capacityException.
     *
     * @param id the id of the capacityExceptionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the capacityExceptionDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/capacity-exceptions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CapacityExceptionDTO> getCapacityException(@PathVariable Long id) {
        log.debug("REST request to get CapacityException : {}", id);
        CapacityExceptionDTO capacityExceptionDTO = capacityExceptionService.findOne(id);
        return Optional.ofNullable(capacityExceptionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /capacity-exceptions/:id : delete the "id" capacityException.
     *
     * @param id the id of the capacityExceptionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/capacity-exceptions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCapacityException(@PathVariable Long id) {
        log.debug("REST request to delete CapacityException : {}", id);
        capacityExceptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("capacityException", id.toString())).build();
    }

    /**
     * SEARCH  /_search/capacity-exceptions?query=:query : search for the capacityException corresponding
     * to the query.
     *
     * @param query the query of the capacityException search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/capacity-exceptions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CapacityExceptionDTO>> searchCapacityExceptions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CapacityExceptions for query {}", query);
        Page<CapacityException> page = capacityExceptionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/capacity-exceptions");
        return new ResponseEntity<>(capacityExceptionMapper.capacityExceptionsToCapacityExceptionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
