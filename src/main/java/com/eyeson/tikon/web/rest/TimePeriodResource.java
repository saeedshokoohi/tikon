package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.TimePeriod;
import com.eyeson.tikon.service.TimePeriodService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.TimePeriodDTO;
import com.eyeson.tikon.web.rest.mapper.TimePeriodMapper;
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
 * REST controller for managing TimePeriod.
 */
@RestController
@RequestMapping("/api")
public class TimePeriodResource {

    private final Logger log = LoggerFactory.getLogger(TimePeriodResource.class);
        
    @Inject
    private TimePeriodService timePeriodService;
    
    @Inject
    private TimePeriodMapper timePeriodMapper;
    
    /**
     * POST  /time-periods : Create a new timePeriod.
     *
     * @param timePeriodDTO the timePeriodDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timePeriodDTO, or with status 400 (Bad Request) if the timePeriod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/time-periods",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TimePeriodDTO> createTimePeriod(@RequestBody TimePeriodDTO timePeriodDTO) throws URISyntaxException {
        log.debug("REST request to save TimePeriod : {}", timePeriodDTO);
        if (timePeriodDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("timePeriod", "idexists", "A new timePeriod cannot already have an ID")).body(null);
        }
        TimePeriodDTO result = timePeriodService.save(timePeriodDTO);
        return ResponseEntity.created(new URI("/api/time-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("timePeriod", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /time-periods : Updates an existing timePeriod.
     *
     * @param timePeriodDTO the timePeriodDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timePeriodDTO,
     * or with status 400 (Bad Request) if the timePeriodDTO is not valid,
     * or with status 500 (Internal Server Error) if the timePeriodDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/time-periods",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TimePeriodDTO> updateTimePeriod(@RequestBody TimePeriodDTO timePeriodDTO) throws URISyntaxException {
        log.debug("REST request to update TimePeriod : {}", timePeriodDTO);
        if (timePeriodDTO.getId() == null) {
            return createTimePeriod(timePeriodDTO);
        }
        TimePeriodDTO result = timePeriodService.save(timePeriodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("timePeriod", timePeriodDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /time-periods : get all the timePeriods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of timePeriods in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/time-periods",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<TimePeriodDTO>> getAllTimePeriods(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TimePeriods");
        Page<TimePeriod> page = timePeriodService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/time-periods");
        return new ResponseEntity<>(timePeriodMapper.timePeriodsToTimePeriodDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /time-periods/:id : get the "id" timePeriod.
     *
     * @param id the id of the timePeriodDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timePeriodDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/time-periods/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TimePeriodDTO> getTimePeriod(@PathVariable Long id) {
        log.debug("REST request to get TimePeriod : {}", id);
        TimePeriodDTO timePeriodDTO = timePeriodService.findOne(id);
        return Optional.ofNullable(timePeriodDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /time-periods/:id : delete the "id" timePeriod.
     *
     * @param id the id of the timePeriodDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/time-periods/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTimePeriod(@PathVariable Long id) {
        log.debug("REST request to delete TimePeriod : {}", id);
        timePeriodService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("timePeriod", id.toString())).build();
    }

    /**
     * SEARCH  /_search/time-periods?query=:query : search for the timePeriod corresponding
     * to the query.
     *
     * @param query the query of the timePeriod search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/time-periods",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<TimePeriodDTO>> searchTimePeriods(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of TimePeriods for query {}", query);
        Page<TimePeriod> page = timePeriodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/time-periods");
        return new ResponseEntity<>(timePeriodMapper.timePeriodsToTimePeriodDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
