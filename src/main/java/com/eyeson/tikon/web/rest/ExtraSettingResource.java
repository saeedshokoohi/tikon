package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ExtraSetting;
import com.eyeson.tikon.service.ExtraSettingService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ExtraSettingDTO;
import com.eyeson.tikon.web.rest.mapper.ExtraSettingMapper;
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
 * REST controller for managing ExtraSetting.
 */
@RestController
@RequestMapping("/api")
public class ExtraSettingResource {

    private final Logger log = LoggerFactory.getLogger(ExtraSettingResource.class);
        
    @Inject
    private ExtraSettingService extraSettingService;
    
    @Inject
    private ExtraSettingMapper extraSettingMapper;
    
    /**
     * POST  /extra-settings : Create a new extraSetting.
     *
     * @param extraSettingDTO the extraSettingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extraSettingDTO, or with status 400 (Bad Request) if the extraSetting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/extra-settings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtraSettingDTO> createExtraSetting(@RequestBody ExtraSettingDTO extraSettingDTO) throws URISyntaxException {
        log.debug("REST request to save ExtraSetting : {}", extraSettingDTO);
        if (extraSettingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("extraSetting", "idexists", "A new extraSetting cannot already have an ID")).body(null);
        }
        ExtraSettingDTO result = extraSettingService.save(extraSettingDTO);
        return ResponseEntity.created(new URI("/api/extra-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("extraSetting", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /extra-settings : Updates an existing extraSetting.
     *
     * @param extraSettingDTO the extraSettingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated extraSettingDTO,
     * or with status 400 (Bad Request) if the extraSettingDTO is not valid,
     * or with status 500 (Internal Server Error) if the extraSettingDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/extra-settings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtraSettingDTO> updateExtraSetting(@RequestBody ExtraSettingDTO extraSettingDTO) throws URISyntaxException {
        log.debug("REST request to update ExtraSetting : {}", extraSettingDTO);
        if (extraSettingDTO.getId() == null) {
            return createExtraSetting(extraSettingDTO);
        }
        ExtraSettingDTO result = extraSettingService.save(extraSettingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("extraSetting", extraSettingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /extra-settings : get all the extraSettings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of extraSettings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/extra-settings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ExtraSettingDTO>> getAllExtraSettings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ExtraSettings");
        Page<ExtraSetting> page = extraSettingService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/extra-settings");
        return new ResponseEntity<>(extraSettingMapper.extraSettingsToExtraSettingDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /extra-settings/:id : get the "id" extraSetting.
     *
     * @param id the id of the extraSettingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extraSettingDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/extra-settings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExtraSettingDTO> getExtraSetting(@PathVariable Long id) {
        log.debug("REST request to get ExtraSetting : {}", id);
        ExtraSettingDTO extraSettingDTO = extraSettingService.findOne(id);
        return Optional.ofNullable(extraSettingDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /extra-settings/:id : delete the "id" extraSetting.
     *
     * @param id the id of the extraSettingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/extra-settings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExtraSetting(@PathVariable Long id) {
        log.debug("REST request to delete ExtraSetting : {}", id);
        extraSettingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("extraSetting", id.toString())).build();
    }

    /**
     * SEARCH  /_search/extra-settings?query=:query : search for the extraSetting corresponding
     * to the query.
     *
     * @param query the query of the extraSetting search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/extra-settings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ExtraSettingDTO>> searchExtraSettings(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ExtraSettings for query {}", query);
        Page<ExtraSetting> page = extraSettingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/extra-settings");
        return new ResponseEntity<>(extraSettingMapper.extraSettingsToExtraSettingDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
