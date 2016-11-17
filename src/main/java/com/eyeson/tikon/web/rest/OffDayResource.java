package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.OffDay;
import com.eyeson.tikon.service.OffDayService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.OffDayDTO;
import com.eyeson.tikon.web.rest.mapper.OffDayMapper;
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
 * REST controller for managing OffDay.
 */
@RestController
@RequestMapping("/api")
public class OffDayResource {

    private final Logger log = LoggerFactory.getLogger(OffDayResource.class);
        
    @Inject
    private OffDayService offDayService;
    
    @Inject
    private OffDayMapper offDayMapper;
    
    /**
     * POST  /off-days : Create a new offDay.
     *
     * @param offDayDTO the offDayDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new offDayDTO, or with status 400 (Bad Request) if the offDay has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/off-days",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OffDayDTO> createOffDay(@RequestBody OffDayDTO offDayDTO) throws URISyntaxException {
        log.debug("REST request to save OffDay : {}", offDayDTO);
        if (offDayDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("offDay", "idexists", "A new offDay cannot already have an ID")).body(null);
        }
        OffDayDTO result = offDayService.save(offDayDTO);
        return ResponseEntity.created(new URI("/api/off-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("offDay", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /off-days : Updates an existing offDay.
     *
     * @param offDayDTO the offDayDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated offDayDTO,
     * or with status 400 (Bad Request) if the offDayDTO is not valid,
     * or with status 500 (Internal Server Error) if the offDayDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/off-days",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OffDayDTO> updateOffDay(@RequestBody OffDayDTO offDayDTO) throws URISyntaxException {
        log.debug("REST request to update OffDay : {}", offDayDTO);
        if (offDayDTO.getId() == null) {
            return createOffDay(offDayDTO);
        }
        OffDayDTO result = offDayService.save(offDayDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("offDay", offDayDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /off-days : get all the offDays.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of offDays in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/off-days",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OffDayDTO>> getAllOffDays(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OffDays");
        Page<OffDay> page = offDayService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/off-days");
        return new ResponseEntity<>(offDayMapper.offDaysToOffDayDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /off-days/:id : get the "id" offDay.
     *
     * @param id the id of the offDayDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the offDayDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/off-days/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OffDayDTO> getOffDay(@PathVariable Long id) {
        log.debug("REST request to get OffDay : {}", id);
        OffDayDTO offDayDTO = offDayService.findOne(id);
        return Optional.ofNullable(offDayDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /off-days/:id : delete the "id" offDay.
     *
     * @param id the id of the offDayDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/off-days/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOffDay(@PathVariable Long id) {
        log.debug("REST request to delete OffDay : {}", id);
        offDayService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("offDay", id.toString())).build();
    }

    /**
     * SEARCH  /_search/off-days?query=:query : search for the offDay corresponding
     * to the query.
     *
     * @param query the query of the offDay search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/off-days",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OffDayDTO>> searchOffDays(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of OffDays for query {}", query);
        Page<OffDay> page = offDayService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/off-days");
        return new ResponseEntity<>(offDayMapper.offDaysToOffDayDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
