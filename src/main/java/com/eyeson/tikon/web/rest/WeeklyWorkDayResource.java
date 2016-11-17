package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.WeeklyWorkDay;
import com.eyeson.tikon.service.WeeklyWorkDayService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.WeeklyWorkDayDTO;
import com.eyeson.tikon.web.rest.mapper.WeeklyWorkDayMapper;
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
 * REST controller for managing WeeklyWorkDay.
 */
@RestController
@RequestMapping("/api")
public class WeeklyWorkDayResource {

    private final Logger log = LoggerFactory.getLogger(WeeklyWorkDayResource.class);
        
    @Inject
    private WeeklyWorkDayService weeklyWorkDayService;
    
    @Inject
    private WeeklyWorkDayMapper weeklyWorkDayMapper;
    
    /**
     * POST  /weekly-work-days : Create a new weeklyWorkDay.
     *
     * @param weeklyWorkDayDTO the weeklyWorkDayDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new weeklyWorkDayDTO, or with status 400 (Bad Request) if the weeklyWorkDay has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/weekly-work-days",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeeklyWorkDayDTO> createWeeklyWorkDay(@RequestBody WeeklyWorkDayDTO weeklyWorkDayDTO) throws URISyntaxException {
        log.debug("REST request to save WeeklyWorkDay : {}", weeklyWorkDayDTO);
        if (weeklyWorkDayDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("weeklyWorkDay", "idexists", "A new weeklyWorkDay cannot already have an ID")).body(null);
        }
        WeeklyWorkDayDTO result = weeklyWorkDayService.save(weeklyWorkDayDTO);
        return ResponseEntity.created(new URI("/api/weekly-work-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("weeklyWorkDay", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weekly-work-days : Updates an existing weeklyWorkDay.
     *
     * @param weeklyWorkDayDTO the weeklyWorkDayDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated weeklyWorkDayDTO,
     * or with status 400 (Bad Request) if the weeklyWorkDayDTO is not valid,
     * or with status 500 (Internal Server Error) if the weeklyWorkDayDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/weekly-work-days",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeeklyWorkDayDTO> updateWeeklyWorkDay(@RequestBody WeeklyWorkDayDTO weeklyWorkDayDTO) throws URISyntaxException {
        log.debug("REST request to update WeeklyWorkDay : {}", weeklyWorkDayDTO);
        if (weeklyWorkDayDTO.getId() == null) {
            return createWeeklyWorkDay(weeklyWorkDayDTO);
        }
        WeeklyWorkDayDTO result = weeklyWorkDayService.save(weeklyWorkDayDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("weeklyWorkDay", weeklyWorkDayDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /weekly-work-days : get all the weeklyWorkDays.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of weeklyWorkDays in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/weekly-work-days",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<WeeklyWorkDayDTO>> getAllWeeklyWorkDays(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WeeklyWorkDays");
        Page<WeeklyWorkDay> page = weeklyWorkDayService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/weekly-work-days");
        return new ResponseEntity<>(weeklyWorkDayMapper.weeklyWorkDaysToWeeklyWorkDayDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /weekly-work-days/:id : get the "id" weeklyWorkDay.
     *
     * @param id the id of the weeklyWorkDayDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the weeklyWorkDayDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/weekly-work-days/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeeklyWorkDayDTO> getWeeklyWorkDay(@PathVariable Long id) {
        log.debug("REST request to get WeeklyWorkDay : {}", id);
        WeeklyWorkDayDTO weeklyWorkDayDTO = weeklyWorkDayService.findOne(id);
        return Optional.ofNullable(weeklyWorkDayDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /weekly-work-days/:id : delete the "id" weeklyWorkDay.
     *
     * @param id the id of the weeklyWorkDayDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/weekly-work-days/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWeeklyWorkDay(@PathVariable Long id) {
        log.debug("REST request to delete WeeklyWorkDay : {}", id);
        weeklyWorkDayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("weeklyWorkDay", id.toString())).build();
    }

    /**
     * SEARCH  /_search/weekly-work-days?query=:query : search for the weeklyWorkDay corresponding
     * to the query.
     *
     * @param query the query of the weeklyWorkDay search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/weekly-work-days",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<WeeklyWorkDayDTO>> searchWeeklyWorkDays(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WeeklyWorkDays for query {}", query);
        Page<WeeklyWorkDay> page = weeklyWorkDayService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/weekly-work-days");
        return new ResponseEntity<>(weeklyWorkDayMapper.weeklyWorkDaysToWeeklyWorkDayDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
