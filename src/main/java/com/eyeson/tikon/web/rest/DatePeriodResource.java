package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.DatePeriod;
import com.eyeson.tikon.service.DatePeriodService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.DatePeriodDTO;
import com.eyeson.tikon.web.rest.mapper.DatePeriodMapper;
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
 * REST controller for managing DatePeriod.
 */
@RestController
@RequestMapping("/api")
public class DatePeriodResource {

    private final Logger log = LoggerFactory.getLogger(DatePeriodResource.class);
        
    @Inject
    private DatePeriodService datePeriodService;
    
    @Inject
    private DatePeriodMapper datePeriodMapper;
    
    /**
     * POST  /date-periods : Create a new datePeriod.
     *
     * @param datePeriodDTO the datePeriodDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new datePeriodDTO, or with status 400 (Bad Request) if the datePeriod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/date-periods",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DatePeriodDTO> createDatePeriod(@RequestBody DatePeriodDTO datePeriodDTO) throws URISyntaxException {
        log.debug("REST request to save DatePeriod : {}", datePeriodDTO);
        if (datePeriodDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("datePeriod", "idexists", "A new datePeriod cannot already have an ID")).body(null);
        }
        DatePeriodDTO result = datePeriodService.save(datePeriodDTO);
        return ResponseEntity.created(new URI("/api/date-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("datePeriod", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /date-periods : Updates an existing datePeriod.
     *
     * @param datePeriodDTO the datePeriodDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated datePeriodDTO,
     * or with status 400 (Bad Request) if the datePeriodDTO is not valid,
     * or with status 500 (Internal Server Error) if the datePeriodDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/date-periods",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DatePeriodDTO> updateDatePeriod(@RequestBody DatePeriodDTO datePeriodDTO) throws URISyntaxException {
        log.debug("REST request to update DatePeriod : {}", datePeriodDTO);
        if (datePeriodDTO.getId() == null) {
            return createDatePeriod(datePeriodDTO);
        }
        DatePeriodDTO result = datePeriodService.save(datePeriodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("datePeriod", datePeriodDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /date-periods : get all the datePeriods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of datePeriods in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/date-periods",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<DatePeriodDTO>> getAllDatePeriods(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DatePeriods");
        Page<DatePeriod> page = datePeriodService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/date-periods");
        return new ResponseEntity<>(datePeriodMapper.datePeriodsToDatePeriodDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /date-periods/:id : get the "id" datePeriod.
     *
     * @param id the id of the datePeriodDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the datePeriodDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/date-periods/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DatePeriodDTO> getDatePeriod(@PathVariable Long id) {
        log.debug("REST request to get DatePeriod : {}", id);
        DatePeriodDTO datePeriodDTO = datePeriodService.findOne(id);
        return Optional.ofNullable(datePeriodDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /date-periods/:id : delete the "id" datePeriod.
     *
     * @param id the id of the datePeriodDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/date-periods/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDatePeriod(@PathVariable Long id) {
        log.debug("REST request to delete DatePeriod : {}", id);
        datePeriodService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("datePeriod", id.toString())).build();
    }

    /**
     * SEARCH  /_search/date-periods?query=:query : search for the datePeriod corresponding
     * to the query.
     *
     * @param query the query of the datePeriod search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/date-periods",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<DatePeriodDTO>> searchDatePeriods(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of DatePeriods for query {}", query);
        Page<DatePeriod> page = datePeriodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/date-periods");
        return new ResponseEntity<>(datePeriodMapper.datePeriodsToDatePeriodDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
