package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ScheduleBaseDiscount;
import com.eyeson.tikon.service.ScheduleBaseDiscountService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ScheduleBaseDiscountDTO;
import com.eyeson.tikon.web.rest.mapper.ScheduleBaseDiscountMapper;
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
 * REST controller for managing ScheduleBaseDiscount.
 */
@RestController
@RequestMapping("/api")
public class ScheduleBaseDiscountResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleBaseDiscountResource.class);
        
    @Inject
    private ScheduleBaseDiscountService scheduleBaseDiscountService;
    
    @Inject
    private ScheduleBaseDiscountMapper scheduleBaseDiscountMapper;
    
    /**
     * POST  /schedule-base-discounts : Create a new scheduleBaseDiscount.
     *
     * @param scheduleBaseDiscountDTO the scheduleBaseDiscountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduleBaseDiscountDTO, or with status 400 (Bad Request) if the scheduleBaseDiscount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/schedule-base-discounts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduleBaseDiscountDTO> createScheduleBaseDiscount(@RequestBody ScheduleBaseDiscountDTO scheduleBaseDiscountDTO) throws URISyntaxException {
        log.debug("REST request to save ScheduleBaseDiscount : {}", scheduleBaseDiscountDTO);
        if (scheduleBaseDiscountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("scheduleBaseDiscount", "idexists", "A new scheduleBaseDiscount cannot already have an ID")).body(null);
        }
        ScheduleBaseDiscountDTO result = scheduleBaseDiscountService.save(scheduleBaseDiscountDTO);
        return ResponseEntity.created(new URI("/api/schedule-base-discounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scheduleBaseDiscount", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schedule-base-discounts : Updates an existing scheduleBaseDiscount.
     *
     * @param scheduleBaseDiscountDTO the scheduleBaseDiscountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scheduleBaseDiscountDTO,
     * or with status 400 (Bad Request) if the scheduleBaseDiscountDTO is not valid,
     * or with status 500 (Internal Server Error) if the scheduleBaseDiscountDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/schedule-base-discounts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduleBaseDiscountDTO> updateScheduleBaseDiscount(@RequestBody ScheduleBaseDiscountDTO scheduleBaseDiscountDTO) throws URISyntaxException {
        log.debug("REST request to update ScheduleBaseDiscount : {}", scheduleBaseDiscountDTO);
        if (scheduleBaseDiscountDTO.getId() == null) {
            return createScheduleBaseDiscount(scheduleBaseDiscountDTO);
        }
        ScheduleBaseDiscountDTO result = scheduleBaseDiscountService.save(scheduleBaseDiscountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scheduleBaseDiscount", scheduleBaseDiscountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schedule-base-discounts : get all the scheduleBaseDiscounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scheduleBaseDiscounts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/schedule-base-discounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ScheduleBaseDiscountDTO>> getAllScheduleBaseDiscounts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ScheduleBaseDiscounts");
        Page<ScheduleBaseDiscount> page = scheduleBaseDiscountService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schedule-base-discounts");
        return new ResponseEntity<>(scheduleBaseDiscountMapper.scheduleBaseDiscountsToScheduleBaseDiscountDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /schedule-base-discounts/:id : get the "id" scheduleBaseDiscount.
     *
     * @param id the id of the scheduleBaseDiscountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scheduleBaseDiscountDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/schedule-base-discounts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduleBaseDiscountDTO> getScheduleBaseDiscount(@PathVariable Long id) {
        log.debug("REST request to get ScheduleBaseDiscount : {}", id);
        ScheduleBaseDiscountDTO scheduleBaseDiscountDTO = scheduleBaseDiscountService.findOne(id);
        return Optional.ofNullable(scheduleBaseDiscountDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /schedule-base-discounts/:id : delete the "id" scheduleBaseDiscount.
     *
     * @param id the id of the scheduleBaseDiscountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/schedule-base-discounts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScheduleBaseDiscount(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleBaseDiscount : {}", id);
        scheduleBaseDiscountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scheduleBaseDiscount", id.toString())).build();
    }

    /**
     * SEARCH  /_search/schedule-base-discounts?query=:query : search for the scheduleBaseDiscount corresponding
     * to the query.
     *
     * @param query the query of the scheduleBaseDiscount search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/schedule-base-discounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ScheduleBaseDiscountDTO>> searchScheduleBaseDiscounts(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ScheduleBaseDiscounts for query {}", query);
        Page<ScheduleBaseDiscount> page = scheduleBaseDiscountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/schedule-base-discounts");
        return new ResponseEntity<>(scheduleBaseDiscountMapper.scheduleBaseDiscountsToScheduleBaseDiscountDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
