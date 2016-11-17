package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.FinancialSetting;
import com.eyeson.tikon.service.FinancialSettingService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.FinancialSettingDTO;
import com.eyeson.tikon.web.rest.mapper.FinancialSettingMapper;
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
 * REST controller for managing FinancialSetting.
 */
@RestController
@RequestMapping("/api")
public class FinancialSettingResource {

    private final Logger log = LoggerFactory.getLogger(FinancialSettingResource.class);
        
    @Inject
    private FinancialSettingService financialSettingService;
    
    @Inject
    private FinancialSettingMapper financialSettingMapper;
    
    /**
     * POST  /financial-settings : Create a new financialSetting.
     *
     * @param financialSettingDTO the financialSettingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new financialSettingDTO, or with status 400 (Bad Request) if the financialSetting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/financial-settings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FinancialSettingDTO> createFinancialSetting(@RequestBody FinancialSettingDTO financialSettingDTO) throws URISyntaxException {
        log.debug("REST request to save FinancialSetting : {}", financialSettingDTO);
        if (financialSettingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("financialSetting", "idexists", "A new financialSetting cannot already have an ID")).body(null);
        }
        FinancialSettingDTO result = financialSettingService.save(financialSettingDTO);
        return ResponseEntity.created(new URI("/api/financial-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("financialSetting", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /financial-settings : Updates an existing financialSetting.
     *
     * @param financialSettingDTO the financialSettingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated financialSettingDTO,
     * or with status 400 (Bad Request) if the financialSettingDTO is not valid,
     * or with status 500 (Internal Server Error) if the financialSettingDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/financial-settings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FinancialSettingDTO> updateFinancialSetting(@RequestBody FinancialSettingDTO financialSettingDTO) throws URISyntaxException {
        log.debug("REST request to update FinancialSetting : {}", financialSettingDTO);
        if (financialSettingDTO.getId() == null) {
            return createFinancialSetting(financialSettingDTO);
        }
        FinancialSettingDTO result = financialSettingService.save(financialSettingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("financialSetting", financialSettingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /financial-settings : get all the financialSettings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of financialSettings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/financial-settings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<FinancialSettingDTO>> getAllFinancialSettings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FinancialSettings");
        Page<FinancialSetting> page = financialSettingService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/financial-settings");
        return new ResponseEntity<>(financialSettingMapper.financialSettingsToFinancialSettingDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /financial-settings/:id : get the "id" financialSetting.
     *
     * @param id the id of the financialSettingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the financialSettingDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/financial-settings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FinancialSettingDTO> getFinancialSetting(@PathVariable Long id) {
        log.debug("REST request to get FinancialSetting : {}", id);
        FinancialSettingDTO financialSettingDTO = financialSettingService.findOne(id);
        return Optional.ofNullable(financialSettingDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /financial-settings/:id : delete the "id" financialSetting.
     *
     * @param id the id of the financialSettingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/financial-settings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFinancialSetting(@PathVariable Long id) {
        log.debug("REST request to delete FinancialSetting : {}", id);
        financialSettingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("financialSetting", id.toString())).build();
    }

    /**
     * SEARCH  /_search/financial-settings?query=:query : search for the financialSetting corresponding
     * to the query.
     *
     * @param query the query of the financialSetting search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/financial-settings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<FinancialSettingDTO>> searchFinancialSettings(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of FinancialSettings for query {}", query);
        Page<FinancialSetting> page = financialSettingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/financial-settings");
        return new ResponseEntity<>(financialSettingMapper.financialSettingsToFinancialSettingDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
