package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.WaitingList;
import com.eyeson.tikon.service.WaitingListService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.WaitingListDTO;
import com.eyeson.tikon.web.rest.mapper.WaitingListMapper;
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
 * REST controller for managing WaitingList.
 */
@RestController
@RequestMapping("/api")
public class WaitingListResource {

    private final Logger log = LoggerFactory.getLogger(WaitingListResource.class);
        
    @Inject
    private WaitingListService waitingListService;
    
    @Inject
    private WaitingListMapper waitingListMapper;
    
    /**
     * POST  /waiting-lists : Create a new waitingList.
     *
     * @param waitingListDTO the waitingListDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new waitingListDTO, or with status 400 (Bad Request) if the waitingList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/waiting-lists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WaitingListDTO> createWaitingList(@RequestBody WaitingListDTO waitingListDTO) throws URISyntaxException {
        log.debug("REST request to save WaitingList : {}", waitingListDTO);
        if (waitingListDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("waitingList", "idexists", "A new waitingList cannot already have an ID")).body(null);
        }
        WaitingListDTO result = waitingListService.save(waitingListDTO);
        return ResponseEntity.created(new URI("/api/waiting-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("waitingList", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /waiting-lists : Updates an existing waitingList.
     *
     * @param waitingListDTO the waitingListDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated waitingListDTO,
     * or with status 400 (Bad Request) if the waitingListDTO is not valid,
     * or with status 500 (Internal Server Error) if the waitingListDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/waiting-lists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WaitingListDTO> updateWaitingList(@RequestBody WaitingListDTO waitingListDTO) throws URISyntaxException {
        log.debug("REST request to update WaitingList : {}", waitingListDTO);
        if (waitingListDTO.getId() == null) {
            return createWaitingList(waitingListDTO);
        }
        WaitingListDTO result = waitingListService.save(waitingListDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("waitingList", waitingListDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /waiting-lists : get all the waitingLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of waitingLists in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/waiting-lists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<WaitingListDTO>> getAllWaitingLists(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WaitingLists");
        Page<WaitingList> page = waitingListService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/waiting-lists");
        return new ResponseEntity<>(waitingListMapper.waitingListsToWaitingListDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /waiting-lists/:id : get the "id" waitingList.
     *
     * @param id the id of the waitingListDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the waitingListDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/waiting-lists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WaitingListDTO> getWaitingList(@PathVariable Long id) {
        log.debug("REST request to get WaitingList : {}", id);
        WaitingListDTO waitingListDTO = waitingListService.findOne(id);
        return Optional.ofNullable(waitingListDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /waiting-lists/:id : delete the "id" waitingList.
     *
     * @param id the id of the waitingListDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/waiting-lists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWaitingList(@PathVariable Long id) {
        log.debug("REST request to delete WaitingList : {}", id);
        waitingListService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("waitingList", id.toString())).build();
    }

    /**
     * SEARCH  /_search/waiting-lists?query=:query : search for the waitingList corresponding
     * to the query.
     *
     * @param query the query of the waitingList search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/waiting-lists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<WaitingListDTO>> searchWaitingLists(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WaitingLists for query {}", query);
        Page<WaitingList> page = waitingListService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/waiting-lists");
        return new ResponseEntity<>(waitingListMapper.waitingListsToWaitingListDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
