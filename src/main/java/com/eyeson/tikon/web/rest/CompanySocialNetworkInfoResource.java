package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.CompanySocialNetworkInfo;
import com.eyeson.tikon.service.CompanySocialNetworkInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.CompanySocialNetworkInfoDTO;
import com.eyeson.tikon.web.rest.mapper.CompanySocialNetworkInfoMapper;
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
 * REST controller for managing CompanySocialNetworkInfo.
 */
@RestController
@RequestMapping("/api")
public class CompanySocialNetworkInfoResource {

    private final Logger log = LoggerFactory.getLogger(CompanySocialNetworkInfoResource.class);
        
    @Inject
    private CompanySocialNetworkInfoService companySocialNetworkInfoService;
    
    @Inject
    private CompanySocialNetworkInfoMapper companySocialNetworkInfoMapper;
    
    /**
     * POST  /company-social-network-infos : Create a new companySocialNetworkInfo.
     *
     * @param companySocialNetworkInfoDTO the companySocialNetworkInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companySocialNetworkInfoDTO, or with status 400 (Bad Request) if the companySocialNetworkInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/company-social-network-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanySocialNetworkInfoDTO> createCompanySocialNetworkInfo(@RequestBody CompanySocialNetworkInfoDTO companySocialNetworkInfoDTO) throws URISyntaxException {
        log.debug("REST request to save CompanySocialNetworkInfo : {}", companySocialNetworkInfoDTO);
        if (companySocialNetworkInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("companySocialNetworkInfo", "idexists", "A new companySocialNetworkInfo cannot already have an ID")).body(null);
        }
        CompanySocialNetworkInfoDTO result = companySocialNetworkInfoService.save(companySocialNetworkInfoDTO);
        return ResponseEntity.created(new URI("/api/company-social-network-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("companySocialNetworkInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-social-network-infos : Updates an existing companySocialNetworkInfo.
     *
     * @param companySocialNetworkInfoDTO the companySocialNetworkInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companySocialNetworkInfoDTO,
     * or with status 400 (Bad Request) if the companySocialNetworkInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the companySocialNetworkInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/company-social-network-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanySocialNetworkInfoDTO> updateCompanySocialNetworkInfo(@RequestBody CompanySocialNetworkInfoDTO companySocialNetworkInfoDTO) throws URISyntaxException {
        log.debug("REST request to update CompanySocialNetworkInfo : {}", companySocialNetworkInfoDTO);
        if (companySocialNetworkInfoDTO.getId() == null) {
            return createCompanySocialNetworkInfo(companySocialNetworkInfoDTO);
        }
        CompanySocialNetworkInfoDTO result = companySocialNetworkInfoService.save(companySocialNetworkInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("companySocialNetworkInfo", companySocialNetworkInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-social-network-infos : get all the companySocialNetworkInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of companySocialNetworkInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/company-social-network-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CompanySocialNetworkInfoDTO>> getAllCompanySocialNetworkInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CompanySocialNetworkInfos");
        Page<CompanySocialNetworkInfo> page = companySocialNetworkInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/company-social-network-infos");
        return new ResponseEntity<>(companySocialNetworkInfoMapper.companySocialNetworkInfosToCompanySocialNetworkInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /company-social-network-infos/:id : get the "id" companySocialNetworkInfo.
     *
     * @param id the id of the companySocialNetworkInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companySocialNetworkInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/company-social-network-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompanySocialNetworkInfoDTO> getCompanySocialNetworkInfo(@PathVariable Long id) {
        log.debug("REST request to get CompanySocialNetworkInfo : {}", id);
        CompanySocialNetworkInfoDTO companySocialNetworkInfoDTO = companySocialNetworkInfoService.findOne(id);
        return Optional.ofNullable(companySocialNetworkInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /company-social-network-infos/:id : delete the "id" companySocialNetworkInfo.
     *
     * @param id the id of the companySocialNetworkInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/company-social-network-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCompanySocialNetworkInfo(@PathVariable Long id) {
        log.debug("REST request to delete CompanySocialNetworkInfo : {}", id);
        companySocialNetworkInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("companySocialNetworkInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-social-network-infos?query=:query : search for the companySocialNetworkInfo corresponding
     * to the query.
     *
     * @param query the query of the companySocialNetworkInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/company-social-network-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CompanySocialNetworkInfoDTO>> searchCompanySocialNetworkInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CompanySocialNetworkInfos for query {}", query);
        Page<CompanySocialNetworkInfo> page = companySocialNetworkInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/company-social-network-infos");
        return new ResponseEntity<>(companySocialNetworkInfoMapper.companySocialNetworkInfosToCompanySocialNetworkInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
