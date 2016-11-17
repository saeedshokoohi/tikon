package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.CompanyManager;
import com.eyeson.tikon.service.CompanyManagerService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.CompanyManagerDTO;
import com.eyeson.tikon.web.rest.mapper.CompanyManagerMapper;
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
 * REST controller for managing CompanyManager.
 */
@RestController
@RequestMapping("/api")
public class CompanyManagerResource {

    private final Logger log = LoggerFactory.getLogger(CompanyManagerResource.class);
        
    @Inject
    private CompanyManagerService companyManagerService;
    
    @Inject
    private CompanyManagerMapper companyManagerMapper;
    
    /**
     * POST  /company-managers : Create a new companyManager.
     *
     * @param companyManagerDTO the companyManagerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyManagerDTO, or with status 400 (Bad Request) if the companyManager has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/company-managers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyManagerDTO> createCompanyManager(@RequestBody CompanyManagerDTO companyManagerDTO) throws URISyntaxException {
        log.debug("REST request to save CompanyManager : {}", companyManagerDTO);
        if (companyManagerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("companyManager", "idexists", "A new companyManager cannot already have an ID")).body(null);
        }
        CompanyManagerDTO result = companyManagerService.save(companyManagerDTO);
        return ResponseEntity.created(new URI("/api/company-managers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("companyManager", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-managers : Updates an existing companyManager.
     *
     * @param companyManagerDTO the companyManagerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyManagerDTO,
     * or with status 400 (Bad Request) if the companyManagerDTO is not valid,
     * or with status 500 (Internal Server Error) if the companyManagerDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/company-managers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyManagerDTO> updateCompanyManager(@RequestBody CompanyManagerDTO companyManagerDTO) throws URISyntaxException {
        log.debug("REST request to update CompanyManager : {}", companyManagerDTO);
        if (companyManagerDTO.getId() == null) {
            return createCompanyManager(companyManagerDTO);
        }
        CompanyManagerDTO result = companyManagerService.save(companyManagerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("companyManager", companyManagerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-managers : get all the companyManagers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companyManagers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/company-managers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CompanyManagerDTO>> getAllCompanyManagers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CompanyManagers");
        Page<CompanyManager> page = companyManagerService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/company-managers");
        return new ResponseEntity<>(companyManagerMapper.companyManagersToCompanyManagerDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /company-managers/:id : get the "id" companyManager.
     *
     * @param id the id of the companyManagerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyManagerDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/company-managers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanyManagerDTO> getCompanyManager(@PathVariable Long id) {
        log.debug("REST request to get CompanyManager : {}", id);
        CompanyManagerDTO companyManagerDTO = companyManagerService.findOne(id);
        return Optional.ofNullable(companyManagerDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /company-managers/:id : delete the "id" companyManager.
     *
     * @param id the id of the companyManagerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/company-managers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCompanyManager(@PathVariable Long id) {
        log.debug("REST request to delete CompanyManager : {}", id);
        companyManagerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("companyManager", id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-managers?query=:query : search for the companyManager corresponding
     * to the query.
     *
     * @param query the query of the companyManager search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/company-managers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CompanyManagerDTO>> searchCompanyManagers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CompanyManagers for query {}", query);
        Page<CompanyManager> page = companyManagerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/company-managers");
        return new ResponseEntity<>(companyManagerMapper.companyManagersToCompanyManagerDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
