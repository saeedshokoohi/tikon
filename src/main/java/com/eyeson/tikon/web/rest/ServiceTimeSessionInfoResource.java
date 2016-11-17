package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ServiceTimeSessionInfo;
import com.eyeson.tikon.service.ServiceTimeSessionInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ServiceTimeSessionInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceTimeSessionInfoMapper;
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
 * REST controller for managing ServiceTimeSessionInfo.
 */
@RestController
@RequestMapping("/api")
public class ServiceTimeSessionInfoResource {

    private final Logger log = LoggerFactory.getLogger(ServiceTimeSessionInfoResource.class);
        
    @Inject
    private ServiceTimeSessionInfoService serviceTimeSessionInfoService;
    
    @Inject
    private ServiceTimeSessionInfoMapper serviceTimeSessionInfoMapper;
    
    /**
     * POST  /service-time-session-infos : Create a new serviceTimeSessionInfo.
     *
     * @param serviceTimeSessionInfoDTO the serviceTimeSessionInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceTimeSessionInfoDTO, or with status 400 (Bad Request) if the serviceTimeSessionInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-time-session-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceTimeSessionInfoDTO> createServiceTimeSessionInfo(@RequestBody ServiceTimeSessionInfoDTO serviceTimeSessionInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceTimeSessionInfo : {}", serviceTimeSessionInfoDTO);
        if (serviceTimeSessionInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceTimeSessionInfo", "idexists", "A new serviceTimeSessionInfo cannot already have an ID")).body(null);
        }
        ServiceTimeSessionInfoDTO result = serviceTimeSessionInfoService.save(serviceTimeSessionInfoDTO);
        return ResponseEntity.created(new URI("/api/service-time-session-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceTimeSessionInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-time-session-infos : Updates an existing serviceTimeSessionInfo.
     *
     * @param serviceTimeSessionInfoDTO the serviceTimeSessionInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceTimeSessionInfoDTO,
     * or with status 400 (Bad Request) if the serviceTimeSessionInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceTimeSessionInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-time-session-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceTimeSessionInfoDTO> updateServiceTimeSessionInfo(@RequestBody ServiceTimeSessionInfoDTO serviceTimeSessionInfoDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceTimeSessionInfo : {}", serviceTimeSessionInfoDTO);
        if (serviceTimeSessionInfoDTO.getId() == null) {
            return createServiceTimeSessionInfo(serviceTimeSessionInfoDTO);
        }
        ServiceTimeSessionInfoDTO result = serviceTimeSessionInfoService.save(serviceTimeSessionInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceTimeSessionInfo", serviceTimeSessionInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-time-session-infos : get all the serviceTimeSessionInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceTimeSessionInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/service-time-session-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceTimeSessionInfoDTO>> getAllServiceTimeSessionInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ServiceTimeSessionInfos");
        Page<ServiceTimeSessionInfo> page = serviceTimeSessionInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-time-session-infos");
        return new ResponseEntity<>(serviceTimeSessionInfoMapper.serviceTimeSessionInfosToServiceTimeSessionInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /service-time-session-infos/:id : get the "id" serviceTimeSessionInfo.
     *
     * @param id the id of the serviceTimeSessionInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceTimeSessionInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/service-time-session-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceTimeSessionInfoDTO> getServiceTimeSessionInfo(@PathVariable Long id) {
        log.debug("REST request to get ServiceTimeSessionInfo : {}", id);
        ServiceTimeSessionInfoDTO serviceTimeSessionInfoDTO = serviceTimeSessionInfoService.findOne(id);
        return Optional.ofNullable(serviceTimeSessionInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-time-session-infos/:id : delete the "id" serviceTimeSessionInfo.
     *
     * @param id the id of the serviceTimeSessionInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/service-time-session-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServiceTimeSessionInfo(@PathVariable Long id) {
        log.debug("REST request to delete ServiceTimeSessionInfo : {}", id);
        serviceTimeSessionInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceTimeSessionInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-time-session-infos?query=:query : search for the serviceTimeSessionInfo corresponding
     * to the query.
     *
     * @param query the query of the serviceTimeSessionInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/service-time-session-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceTimeSessionInfoDTO>> searchServiceTimeSessionInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ServiceTimeSessionInfos for query {}", query);
        Page<ServiceTimeSessionInfo> page = serviceTimeSessionInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/service-time-session-infos");
        return new ResponseEntity<>(serviceTimeSessionInfoMapper.serviceTimeSessionInfosToServiceTimeSessionInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
