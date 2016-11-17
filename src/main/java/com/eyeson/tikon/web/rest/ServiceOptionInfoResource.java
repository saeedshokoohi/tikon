package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ServiceOptionInfo;
import com.eyeson.tikon.service.ServiceOptionInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ServiceOptionInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceOptionInfoMapper;
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
 * REST controller for managing ServiceOptionInfo.
 */
@RestController
@RequestMapping("/api")
public class ServiceOptionInfoResource {

    private final Logger log = LoggerFactory.getLogger(ServiceOptionInfoResource.class);
        
    @Inject
    private ServiceOptionInfoService serviceOptionInfoService;
    
    @Inject
    private ServiceOptionInfoMapper serviceOptionInfoMapper;
    
    /**
     * POST  /service-option-infos : Create a new serviceOptionInfo.
     *
     * @param serviceOptionInfoDTO the serviceOptionInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceOptionInfoDTO, or with status 400 (Bad Request) if the serviceOptionInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-option-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceOptionInfoDTO> createServiceOptionInfo(@RequestBody ServiceOptionInfoDTO serviceOptionInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceOptionInfo : {}", serviceOptionInfoDTO);
        if (serviceOptionInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceOptionInfo", "idexists", "A new serviceOptionInfo cannot already have an ID")).body(null);
        }
        ServiceOptionInfoDTO result = serviceOptionInfoService.save(serviceOptionInfoDTO);
        return ResponseEntity.created(new URI("/api/service-option-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceOptionInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-option-infos : Updates an existing serviceOptionInfo.
     *
     * @param serviceOptionInfoDTO the serviceOptionInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceOptionInfoDTO,
     * or with status 400 (Bad Request) if the serviceOptionInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceOptionInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-option-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceOptionInfoDTO> updateServiceOptionInfo(@RequestBody ServiceOptionInfoDTO serviceOptionInfoDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceOptionInfo : {}", serviceOptionInfoDTO);
        if (serviceOptionInfoDTO.getId() == null) {
            return createServiceOptionInfo(serviceOptionInfoDTO);
        }
        ServiceOptionInfoDTO result = serviceOptionInfoService.save(serviceOptionInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceOptionInfo", serviceOptionInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-option-infos : get all the serviceOptionInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceOptionInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/service-option-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceOptionInfoDTO>> getAllServiceOptionInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ServiceOptionInfos");
        Page<ServiceOptionInfo> page = serviceOptionInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-option-infos");
        return new ResponseEntity<>(serviceOptionInfoMapper.serviceOptionInfosToServiceOptionInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /service-option-infos/:id : get the "id" serviceOptionInfo.
     *
     * @param id the id of the serviceOptionInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceOptionInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/service-option-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceOptionInfoDTO> getServiceOptionInfo(@PathVariable Long id) {
        log.debug("REST request to get ServiceOptionInfo : {}", id);
        ServiceOptionInfoDTO serviceOptionInfoDTO = serviceOptionInfoService.findOne(id);
        return Optional.ofNullable(serviceOptionInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-option-infos/:id : delete the "id" serviceOptionInfo.
     *
     * @param id the id of the serviceOptionInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/service-option-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServiceOptionInfo(@PathVariable Long id) {
        log.debug("REST request to delete ServiceOptionInfo : {}", id);
        serviceOptionInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceOptionInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-option-infos?query=:query : search for the serviceOptionInfo corresponding
     * to the query.
     *
     * @param query the query of the serviceOptionInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/service-option-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceOptionInfoDTO>> searchServiceOptionInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ServiceOptionInfos for query {}", query);
        Page<ServiceOptionInfo> page = serviceOptionInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/service-option-infos");
        return new ResponseEntity<>(serviceOptionInfoMapper.serviceOptionInfosToServiceOptionInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
