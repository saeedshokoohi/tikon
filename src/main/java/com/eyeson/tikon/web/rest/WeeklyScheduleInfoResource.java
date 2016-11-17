package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.WeeklyScheduleInfo;
import com.eyeson.tikon.service.WeeklyScheduleInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.WeeklyScheduleInfoDTO;
import com.eyeson.tikon.web.rest.mapper.WeeklyScheduleInfoMapper;
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
 * REST controller for managing WeeklyScheduleInfo.
 */
@RestController
@RequestMapping("/api")
public class WeeklyScheduleInfoResource {

    private final Logger log = LoggerFactory.getLogger(WeeklyScheduleInfoResource.class);
        
    @Inject
    private WeeklyScheduleInfoService weeklyScheduleInfoService;
    
    @Inject
    private WeeklyScheduleInfoMapper weeklyScheduleInfoMapper;
    
    /**
     * POST  /weekly-schedule-infos : Create a new weeklyScheduleInfo.
     *
     * @param weeklyScheduleInfoDTO the weeklyScheduleInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new weeklyScheduleInfoDTO, or with status 400 (Bad Request) if the weeklyScheduleInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/weekly-schedule-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeeklyScheduleInfoDTO> createWeeklyScheduleInfo(@RequestBody WeeklyScheduleInfoDTO weeklyScheduleInfoDTO) throws URISyntaxException {
        log.debug("REST request to save WeeklyScheduleInfo : {}", weeklyScheduleInfoDTO);
        if (weeklyScheduleInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("weeklyScheduleInfo", "idexists", "A new weeklyScheduleInfo cannot already have an ID")).body(null);
        }
        WeeklyScheduleInfoDTO result = weeklyScheduleInfoService.save(weeklyScheduleInfoDTO);
        return ResponseEntity.created(new URI("/api/weekly-schedule-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("weeklyScheduleInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weekly-schedule-infos : Updates an existing weeklyScheduleInfo.
     *
     * @param weeklyScheduleInfoDTO the weeklyScheduleInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated weeklyScheduleInfoDTO,
     * or with status 400 (Bad Request) if the weeklyScheduleInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the weeklyScheduleInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/weekly-schedule-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeeklyScheduleInfoDTO> updateWeeklyScheduleInfo(@RequestBody WeeklyScheduleInfoDTO weeklyScheduleInfoDTO) throws URISyntaxException {
        log.debug("REST request to update WeeklyScheduleInfo : {}", weeklyScheduleInfoDTO);
        if (weeklyScheduleInfoDTO.getId() == null) {
            return createWeeklyScheduleInfo(weeklyScheduleInfoDTO);
        }
        WeeklyScheduleInfoDTO result = weeklyScheduleInfoService.save(weeklyScheduleInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("weeklyScheduleInfo", weeklyScheduleInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /weekly-schedule-infos : get all the weeklyScheduleInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of weeklyScheduleInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/weekly-schedule-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<WeeklyScheduleInfoDTO>> getAllWeeklyScheduleInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of WeeklyScheduleInfos");
        Page<WeeklyScheduleInfo> page = weeklyScheduleInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/weekly-schedule-infos");
        return new ResponseEntity<>(weeklyScheduleInfoMapper.weeklyScheduleInfosToWeeklyScheduleInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /weekly-schedule-infos/:id : get the "id" weeklyScheduleInfo.
     *
     * @param id the id of the weeklyScheduleInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the weeklyScheduleInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/weekly-schedule-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeeklyScheduleInfoDTO> getWeeklyScheduleInfo(@PathVariable Long id) {
        log.debug("REST request to get WeeklyScheduleInfo : {}", id);
        WeeklyScheduleInfoDTO weeklyScheduleInfoDTO = weeklyScheduleInfoService.findOne(id);
        return Optional.ofNullable(weeklyScheduleInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /weekly-schedule-infos/:id : delete the "id" weeklyScheduleInfo.
     *
     * @param id the id of the weeklyScheduleInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/weekly-schedule-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWeeklyScheduleInfo(@PathVariable Long id) {
        log.debug("REST request to delete WeeklyScheduleInfo : {}", id);
        weeklyScheduleInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("weeklyScheduleInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/weekly-schedule-infos?query=:query : search for the weeklyScheduleInfo corresponding
     * to the query.
     *
     * @param query the query of the weeklyScheduleInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/weekly-schedule-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<WeeklyScheduleInfoDTO>> searchWeeklyScheduleInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of WeeklyScheduleInfos for query {}", query);
        Page<WeeklyScheduleInfo> page = weeklyScheduleInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/weekly-schedule-infos");
        return new ResponseEntity<>(weeklyScheduleInfoMapper.weeklyScheduleInfosToWeeklyScheduleInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
