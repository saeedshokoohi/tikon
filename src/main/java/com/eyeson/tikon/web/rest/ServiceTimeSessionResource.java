package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ServiceTimeSession;
import com.eyeson.tikon.service.ServiceTimeSessionService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ServiceTimeSessionDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceTimeSessionMapper;
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
 * REST controller for managing ServiceTimeSession.
 */
@RestController
@RequestMapping("/api")
public class ServiceTimeSessionResource {

    private final Logger log = LoggerFactory.getLogger(ServiceTimeSessionResource.class);
        
    @Inject
    private ServiceTimeSessionService serviceTimeSessionService;
    
    @Inject
    private ServiceTimeSessionMapper serviceTimeSessionMapper;
    
    /**
     * POST  /service-time-sessions : Create a new serviceTimeSession.
     *
     * @param serviceTimeSessionDTO the serviceTimeSessionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceTimeSessionDTO, or with status 400 (Bad Request) if the serviceTimeSession has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-time-sessions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceTimeSessionDTO> createServiceTimeSession(@RequestBody ServiceTimeSessionDTO serviceTimeSessionDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceTimeSession : {}", serviceTimeSessionDTO);
        if (serviceTimeSessionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceTimeSession", "idexists", "A new serviceTimeSession cannot already have an ID")).body(null);
        }
        ServiceTimeSessionDTO result = serviceTimeSessionService.save(serviceTimeSessionDTO);
        return ResponseEntity.created(new URI("/api/service-time-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceTimeSession", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-time-sessions : Updates an existing serviceTimeSession.
     *
     * @param serviceTimeSessionDTO the serviceTimeSessionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceTimeSessionDTO,
     * or with status 400 (Bad Request) if the serviceTimeSessionDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceTimeSessionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-time-sessions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceTimeSessionDTO> updateServiceTimeSession(@RequestBody ServiceTimeSessionDTO serviceTimeSessionDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceTimeSession : {}", serviceTimeSessionDTO);
        if (serviceTimeSessionDTO.getId() == null) {
            return createServiceTimeSession(serviceTimeSessionDTO);
        }
        ServiceTimeSessionDTO result = serviceTimeSessionService.save(serviceTimeSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceTimeSession", serviceTimeSessionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-time-sessions : get all the serviceTimeSessions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceTimeSessions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/service-time-sessions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceTimeSessionDTO>> getAllServiceTimeSessions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ServiceTimeSessions");
        Page<ServiceTimeSession> page = serviceTimeSessionService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-time-sessions");
        return new ResponseEntity<>(serviceTimeSessionMapper.serviceTimeSessionsToServiceTimeSessionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /service-time-sessions/:id : get the "id" serviceTimeSession.
     *
     * @param id the id of the serviceTimeSessionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceTimeSessionDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/service-time-sessions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceTimeSessionDTO> getServiceTimeSession(@PathVariable Long id) {
        log.debug("REST request to get ServiceTimeSession : {}", id);
        ServiceTimeSessionDTO serviceTimeSessionDTO = serviceTimeSessionService.findOne(id);
        return Optional.ofNullable(serviceTimeSessionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-time-sessions/:id : delete the "id" serviceTimeSession.
     *
     * @param id the id of the serviceTimeSessionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/service-time-sessions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServiceTimeSession(@PathVariable Long id) {
        log.debug("REST request to delete ServiceTimeSession : {}", id);
        serviceTimeSessionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceTimeSession", id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-time-sessions?query=:query : search for the serviceTimeSession corresponding
     * to the query.
     *
     * @param query the query of the serviceTimeSession search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/service-time-sessions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceTimeSessionDTO>> searchServiceTimeSessions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ServiceTimeSessions for query {}", query);
        Page<ServiceTimeSession> page = serviceTimeSessionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/service-time-sessions");
        return new ResponseEntity<>(serviceTimeSessionMapper.serviceTimeSessionsToServiceTimeSessionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
