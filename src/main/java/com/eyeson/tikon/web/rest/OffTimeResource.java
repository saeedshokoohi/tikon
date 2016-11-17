package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.OffTime;
import com.eyeson.tikon.service.OffTimeService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.OffTimeDTO;
import com.eyeson.tikon.web.rest.mapper.OffTimeMapper;
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
 * REST controller for managing OffTime.
 */
@RestController
@RequestMapping("/api")
public class OffTimeResource {

    private final Logger log = LoggerFactory.getLogger(OffTimeResource.class);
        
    @Inject
    private OffTimeService offTimeService;
    
    @Inject
    private OffTimeMapper offTimeMapper;
    
    /**
     * POST  /off-times : Create a new offTime.
     *
     * @param offTimeDTO the offTimeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new offTimeDTO, or with status 400 (Bad Request) if the offTime has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/off-times",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OffTimeDTO> createOffTime(@RequestBody OffTimeDTO offTimeDTO) throws URISyntaxException {
        log.debug("REST request to save OffTime : {}", offTimeDTO);
        if (offTimeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("offTime", "idexists", "A new offTime cannot already have an ID")).body(null);
        }
        OffTimeDTO result = offTimeService.save(offTimeDTO);
        return ResponseEntity.created(new URI("/api/off-times/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("offTime", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /off-times : Updates an existing offTime.
     *
     * @param offTimeDTO the offTimeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated offTimeDTO,
     * or with status 400 (Bad Request) if the offTimeDTO is not valid,
     * or with status 500 (Internal Server Error) if the offTimeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/off-times",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OffTimeDTO> updateOffTime(@RequestBody OffTimeDTO offTimeDTO) throws URISyntaxException {
        log.debug("REST request to update OffTime : {}", offTimeDTO);
        if (offTimeDTO.getId() == null) {
            return createOffTime(offTimeDTO);
        }
        OffTimeDTO result = offTimeService.save(offTimeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("offTime", offTimeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /off-times : get all the offTimes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of offTimes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/off-times",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OffTimeDTO>> getAllOffTimes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OffTimes");
        Page<OffTime> page = offTimeService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/off-times");
        return new ResponseEntity<>(offTimeMapper.offTimesToOffTimeDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /off-times/:id : get the "id" offTime.
     *
     * @param id the id of the offTimeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the offTimeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/off-times/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OffTimeDTO> getOffTime(@PathVariable Long id) {
        log.debug("REST request to get OffTime : {}", id);
        OffTimeDTO offTimeDTO = offTimeService.findOne(id);
        return Optional.ofNullable(offTimeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /off-times/:id : delete the "id" offTime.
     *
     * @param id the id of the offTimeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/off-times/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOffTime(@PathVariable Long id) {
        log.debug("REST request to delete OffTime : {}", id);
        offTimeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("offTime", id.toString())).build();
    }

    /**
     * SEARCH  /_search/off-times?query=:query : search for the offTime corresponding
     * to the query.
     *
     * @param query the query of the offTime search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/off-times",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OffTimeDTO>> searchOffTimes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of OffTimes for query {}", query);
        Page<OffTime> page = offTimeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/off-times");
        return new ResponseEntity<>(offTimeMapper.offTimesToOffTimeDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
