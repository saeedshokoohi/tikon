package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ServiceCapacityInfo;
import com.eyeson.tikon.service.ServiceCapacityInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ServiceCapacityInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceCapacityInfoMapper;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ServiceCapacityInfo.
 */
@RestController
@RequestMapping("/api")
public class ServiceCapacityInfoResource {

    private final Logger log = LoggerFactory.getLogger(ServiceCapacityInfoResource.class);

    @Inject
    private ServiceCapacityInfoService serviceCapacityInfoService;

    @Inject
    private ServiceCapacityInfoMapper serviceCapacityInfoMapper;

    /**
     * POST  /service-capacity-infos : Create a new serviceCapacityInfo.
     *
     * @param serviceCapacityInfoDTO the serviceCapacityInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceCapacityInfoDTO, or with status 400 (Bad Request) if the serviceCapacityInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-capacity-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceCapacityInfoDTO> createServiceCapacityInfo(@RequestBody ServiceCapacityInfoDTO serviceCapacityInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceCapacityInfo : {}", serviceCapacityInfoDTO);
        if (serviceCapacityInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceCapacityInfo", "idexists", "A new serviceCapacityInfo cannot already have an ID")).body(null);
        }
        ServiceCapacityInfoDTO result = serviceCapacityInfoService.save(serviceCapacityInfoDTO);
        return ResponseEntity.created(new URI("/api/service-capacity-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceCapacityInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-capacity-infos : Updates an existing serviceCapacityInfo.
     *
     * @param serviceCapacityInfoDTO the serviceCapacityInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceCapacityInfoDTO,
     * or with status 400 (Bad Request) if the serviceCapacityInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceCapacityInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-capacity-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceCapacityInfoDTO> updateServiceCapacityInfo(@RequestBody ServiceCapacityInfoDTO serviceCapacityInfoDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceCapacityInfo : {}", serviceCapacityInfoDTO);
        if (serviceCapacityInfoDTO.getId() == null) {
            return createServiceCapacityInfo(serviceCapacityInfoDTO);
        }
        ServiceCapacityInfoDTO result = serviceCapacityInfoService.save(serviceCapacityInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceCapacityInfo", serviceCapacityInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-capacity-infos : get all the serviceCapacityInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceCapacityInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/service-capacity-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceCapacityInfoDTO>> getAllServiceCapacityInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ServiceCapacityInfos");
        Page<ServiceCapacityInfo> page = serviceCapacityInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-capacity-infos");
        return new ResponseEntity<>(serviceCapacityInfoMapper.serviceCapacityInfosToServiceCapacityInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /service-capacity-infos/:id : get the "id" serviceCapacityInfo.
     *
     * @param id the id of the serviceCapacityInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceCapacityInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/service-capacity-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceCapacityInfoDTO> getServiceCapacityInfo(@PathVariable Long id) {
        log.debug("REST request to get ServiceCapacityInfo : {}", id);
        ServiceCapacityInfoDTO serviceCapacityInfoDTO = serviceCapacityInfoService.findOne(id);
        return Optional.ofNullable(serviceCapacityInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-capacity-infos/:id : delete the "id" serviceCapacityInfo.
     *
     * @param id the id of the serviceCapacityInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/service-capacity-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServiceCapacityInfo(@PathVariable Long id) {
        log.debug("REST request to delete ServiceCapacityInfo : {}", id);
        serviceCapacityInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceCapacityInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-capacity-infos?query=:query : search for the serviceCapacityInfo corresponding
     * to the query.
     *
     * @param query the query of the serviceCapacityInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/service-capacity-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceCapacityInfoDTO>> searchServiceCapacityInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ServiceCapacityInfos for query {}", query);
        Page<ServiceCapacityInfo> page = serviceCapacityInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/service-capacity-infos");
        return new ResponseEntity<>(serviceCapacityInfoMapper.serviceCapacityInfosToServiceCapacityInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/service-capacity-info-by-service-item/{serviceItemId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ServiceCapacityInfo getServiceCapacityInfoByServiceItem(@PathVariable("serviceItemId") String serviceItemId)
    {
        return serviceCapacityInfoService.getServiceCapacityInfoByServiceItem(Long.parseLong(serviceItemId));
    }

}
