package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.SelectorData;
import com.eyeson.tikon.service.SelectorDataService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.SelectorDataDTO;
import com.eyeson.tikon.web.rest.mapper.SelectorDataMapper;
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
 * REST controller for managing SelectorData.
 */
@RestController
@RequestMapping("/api")
public class SelectorDataResource {

    private final Logger log = LoggerFactory.getLogger(SelectorDataResource.class);

    @Inject
    private SelectorDataService selectorDataService;

    @Inject
    private SelectorDataMapper selectorDataMapper;

    /**
     * POST  /selector-data : Create a new selectorData.
     *
     * @param selectorDataDTO the selectorDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new selectorDataDTO, or with status 400 (Bad Request) if the selectorData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/selector-data",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SelectorDataDTO> createSelectorData(@RequestBody SelectorDataDTO selectorDataDTO) throws URISyntaxException {
        log.debug("REST request to save SelectorData : {}", selectorDataDTO);
        if (selectorDataDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("selectorData", "idexists", "A new selectorData cannot already have an ID")).body(null);
        }
        SelectorDataDTO result = selectorDataService.save(selectorDataDTO);
        return ResponseEntity.created(new URI("/api/selector-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("selectorData", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /selector-data : Updates an existing selectorData.
     *
     * @param selectorDataDTO the selectorDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated selectorDataDTO,
     * or with status 400 (Bad Request) if the selectorDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the selectorDataDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/selector-data",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SelectorDataDTO> updateSelectorData(@RequestBody SelectorDataDTO selectorDataDTO) throws URISyntaxException {
        log.debug("REST request to update SelectorData : {}", selectorDataDTO);
        if (selectorDataDTO.getId() == null) {
            return createSelectorData(selectorDataDTO);
        }
        SelectorDataDTO result = selectorDataService.save(selectorDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("selectorData", selectorDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /selector-data : get all the selectorData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of selectorData in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/selector-data",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SelectorDataDTO>> getAllSelectorData(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SelectorData");
        Page<SelectorData> page = selectorDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/selector-data");
        return new ResponseEntity<>(selectorDataMapper.selectorDataToSelectorDataDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /selector-data/:id : get the "id" selectorData.
     *
     * @param id the id of the selectorDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the selectorDataDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/selector-data/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SelectorDataDTO> getSelectorData(@PathVariable Long id) {
        log.debug("REST request to get SelectorData : {}", id);
        SelectorDataDTO selectorDataDTO = selectorDataService.findOne(id);
        return Optional.ofNullable(selectorDataDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /selector-data/:id : delete the "id" selectorData.
     *
     * @param id the id of the selectorDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/selector-data/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSelectorData(@PathVariable Long id) {
        log.debug("REST request to delete SelectorData : {}", id);
        selectorDataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("selectorData", id.toString())).build();
    }

    /**
     * SEARCH  /_search/selector-data?query=:query : search for the selectorData corresponding
     * to the query.
     *
     * @param query the query of the selectorData search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/selector-data",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SelectorDataDTO>> searchSelectorData(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SelectorData for query {}", query);
        Page<SelectorData> page = selectorDataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/selector-data");
        return new ResponseEntity<>(selectorDataMapper.selectorDataToSelectorDataDTOs(page.getContent()), headers, HttpStatus.OK);
    }


//    new service

    /** url : /selector-data-by-key/{key}/{parentid:[\d]+}
     * return selector data by parent id and data-type key
     *
     *
     * @param key
     * @param parentid
     * @return
     */
    @RequestMapping(value = "/selector-data-by-key/{key}/{parentid:[\\d]+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SelectorDataDTO>  getSelectorsByKey(@PathVariable("key") String key,@PathVariable("parentid") Long parentid) {

        List<SelectorDataDTO> selectorDataDTOList =selectorDataMapper.selectorDataToSelectorDataDTOs(selectorDataService.findByTypeKeyAndParentId(key,parentid));
        return selectorDataDTOList;


    }

    /**
     *
     * url : /selector-data-by-key/{key}
     * return list os selector-data by its data-type key name
     *
     * @param key
     * @return
     */
    @RequestMapping(value = "/selector-data-by-key/{key}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SelectorDataDTO>  getSelectorsByKey(@PathVariable("key") String key) {

        List<SelectorDataDTO> selectorDataDTOList =selectorDataMapper.selectorDataToSelectorDataDTOs(selectorDataService.findByTypeKeyAndParentId(key,null));
        return selectorDataDTOList;


    }
}
