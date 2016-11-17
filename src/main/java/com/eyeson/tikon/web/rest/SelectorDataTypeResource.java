package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.SelectorDataType;
import com.eyeson.tikon.service.SelectorDataTypeService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.SelectorDataTypeDTO;
import com.eyeson.tikon.web.rest.mapper.SelectorDataTypeMapper;
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
 * REST controller for managing SelectorDataType.
 */
@RestController
@RequestMapping("/api")
public class SelectorDataTypeResource {

    private final Logger log = LoggerFactory.getLogger(SelectorDataTypeResource.class);
        
    @Inject
    private SelectorDataTypeService selectorDataTypeService;
    
    @Inject
    private SelectorDataTypeMapper selectorDataTypeMapper;
    
    /**
     * POST  /selector-data-types : Create a new selectorDataType.
     *
     * @param selectorDataTypeDTO the selectorDataTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new selectorDataTypeDTO, or with status 400 (Bad Request) if the selectorDataType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/selector-data-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SelectorDataTypeDTO> createSelectorDataType(@RequestBody SelectorDataTypeDTO selectorDataTypeDTO) throws URISyntaxException {
        log.debug("REST request to save SelectorDataType : {}", selectorDataTypeDTO);
        if (selectorDataTypeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("selectorDataType", "idexists", "A new selectorDataType cannot already have an ID")).body(null);
        }
        SelectorDataTypeDTO result = selectorDataTypeService.save(selectorDataTypeDTO);
        return ResponseEntity.created(new URI("/api/selector-data-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("selectorDataType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /selector-data-types : Updates an existing selectorDataType.
     *
     * @param selectorDataTypeDTO the selectorDataTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated selectorDataTypeDTO,
     * or with status 400 (Bad Request) if the selectorDataTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the selectorDataTypeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/selector-data-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SelectorDataTypeDTO> updateSelectorDataType(@RequestBody SelectorDataTypeDTO selectorDataTypeDTO) throws URISyntaxException {
        log.debug("REST request to update SelectorDataType : {}", selectorDataTypeDTO);
        if (selectorDataTypeDTO.getId() == null) {
            return createSelectorDataType(selectorDataTypeDTO);
        }
        SelectorDataTypeDTO result = selectorDataTypeService.save(selectorDataTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("selectorDataType", selectorDataTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /selector-data-types : get all the selectorDataTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of selectorDataTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/selector-data-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SelectorDataTypeDTO>> getAllSelectorDataTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SelectorDataTypes");
        Page<SelectorDataType> page = selectorDataTypeService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/selector-data-types");
        return new ResponseEntity<>(selectorDataTypeMapper.selectorDataTypesToSelectorDataTypeDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /selector-data-types/:id : get the "id" selectorDataType.
     *
     * @param id the id of the selectorDataTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the selectorDataTypeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/selector-data-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SelectorDataTypeDTO> getSelectorDataType(@PathVariable Long id) {
        log.debug("REST request to get SelectorDataType : {}", id);
        SelectorDataTypeDTO selectorDataTypeDTO = selectorDataTypeService.findOne(id);
        return Optional.ofNullable(selectorDataTypeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /selector-data-types/:id : delete the "id" selectorDataType.
     *
     * @param id the id of the selectorDataTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/selector-data-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSelectorDataType(@PathVariable Long id) {
        log.debug("REST request to delete SelectorDataType : {}", id);
        selectorDataTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("selectorDataType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/selector-data-types?query=:query : search for the selectorDataType corresponding
     * to the query.
     *
     * @param query the query of the selectorDataType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/selector-data-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SelectorDataTypeDTO>> searchSelectorDataTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SelectorDataTypes for query {}", query);
        Page<SelectorDataType> page = selectorDataTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/selector-data-types");
        return new ResponseEntity<>(selectorDataTypeMapper.selectorDataTypesToSelectorDataTypeDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
