package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.LocationInfo;
import com.eyeson.tikon.service.LocationInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.LocationInfoDTO;
import com.eyeson.tikon.web.rest.mapper.LocationInfoMapper;
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
 * REST controller for managing LocationInfo.
 */
@RestController
@RequestMapping("/api")
public class LocationInfoResource {

    private final Logger log = LoggerFactory.getLogger(LocationInfoResource.class);
        
    @Inject
    private LocationInfoService locationInfoService;
    
    @Inject
    private LocationInfoMapper locationInfoMapper;
    
    /**
     * POST  /location-infos : Create a new locationInfo.
     *
     * @param locationInfoDTO the locationInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new locationInfoDTO, or with status 400 (Bad Request) if the locationInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/location-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocationInfoDTO> createLocationInfo(@RequestBody LocationInfoDTO locationInfoDTO) throws URISyntaxException {
        log.debug("REST request to save LocationInfo : {}", locationInfoDTO);
        if (locationInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("locationInfo", "idexists", "A new locationInfo cannot already have an ID")).body(null);
        }
        LocationInfoDTO result = locationInfoService.save(locationInfoDTO);
        return ResponseEntity.created(new URI("/api/location-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("locationInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /location-infos : Updates an existing locationInfo.
     *
     * @param locationInfoDTO the locationInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated locationInfoDTO,
     * or with status 400 (Bad Request) if the locationInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the locationInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/location-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocationInfoDTO> updateLocationInfo(@RequestBody LocationInfoDTO locationInfoDTO) throws URISyntaxException {
        log.debug("REST request to update LocationInfo : {}", locationInfoDTO);
        if (locationInfoDTO.getId() == null) {
            return createLocationInfo(locationInfoDTO);
        }
        LocationInfoDTO result = locationInfoService.save(locationInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("locationInfo", locationInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /location-infos : get all the locationInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of locationInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/location-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<LocationInfoDTO>> getAllLocationInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LocationInfos");
        Page<LocationInfo> page = locationInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/location-infos");
        return new ResponseEntity<>(locationInfoMapper.locationInfosToLocationInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /location-infos/:id : get the "id" locationInfo.
     *
     * @param id the id of the locationInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the locationInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/location-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LocationInfoDTO> getLocationInfo(@PathVariable Long id) {
        log.debug("REST request to get LocationInfo : {}", id);
        LocationInfoDTO locationInfoDTO = locationInfoService.findOne(id);
        return Optional.ofNullable(locationInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /location-infos/:id : delete the "id" locationInfo.
     *
     * @param id the id of the locationInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/location-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLocationInfo(@PathVariable Long id) {
        log.debug("REST request to delete LocationInfo : {}", id);
        locationInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("locationInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/location-infos?query=:query : search for the locationInfo corresponding
     * to the query.
     *
     * @param query the query of the locationInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/location-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<LocationInfoDTO>> searchLocationInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of LocationInfos for query {}", query);
        Page<LocationInfo> page = locationInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/location-infos");
        return new ResponseEntity<>(locationInfoMapper.locationInfosToLocationInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
