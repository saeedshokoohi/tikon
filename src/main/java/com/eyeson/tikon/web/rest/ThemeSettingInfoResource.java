package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ThemeSettingInfo;
import com.eyeson.tikon.service.ThemeSettingInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ThemeSettingInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ThemeSettingInfoMapper;
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
 * REST controller for managing ThemeSettingInfo.
 */
@RestController
@RequestMapping("/api")
public class ThemeSettingInfoResource {

    private final Logger log = LoggerFactory.getLogger(ThemeSettingInfoResource.class);
        
    @Inject
    private ThemeSettingInfoService themeSettingInfoService;
    
    @Inject
    private ThemeSettingInfoMapper themeSettingInfoMapper;
    
    /**
     * POST  /theme-setting-infos : Create a new themeSettingInfo.
     *
     * @param themeSettingInfoDTO the themeSettingInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new themeSettingInfoDTO, or with status 400 (Bad Request) if the themeSettingInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/theme-setting-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ThemeSettingInfoDTO> createThemeSettingInfo(@RequestBody ThemeSettingInfoDTO themeSettingInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ThemeSettingInfo : {}", themeSettingInfoDTO);
        if (themeSettingInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("themeSettingInfo", "idexists", "A new themeSettingInfo cannot already have an ID")).body(null);
        }
        ThemeSettingInfoDTO result = themeSettingInfoService.save(themeSettingInfoDTO);
        return ResponseEntity.created(new URI("/api/theme-setting-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("themeSettingInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /theme-setting-infos : Updates an existing themeSettingInfo.
     *
     * @param themeSettingInfoDTO the themeSettingInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated themeSettingInfoDTO,
     * or with status 400 (Bad Request) if the themeSettingInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the themeSettingInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/theme-setting-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ThemeSettingInfoDTO> updateThemeSettingInfo(@RequestBody ThemeSettingInfoDTO themeSettingInfoDTO) throws URISyntaxException {
        log.debug("REST request to update ThemeSettingInfo : {}", themeSettingInfoDTO);
        if (themeSettingInfoDTO.getId() == null) {
            return createThemeSettingInfo(themeSettingInfoDTO);
        }
        ThemeSettingInfoDTO result = themeSettingInfoService.save(themeSettingInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("themeSettingInfo", themeSettingInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /theme-setting-infos : get all the themeSettingInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of themeSettingInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/theme-setting-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ThemeSettingInfoDTO>> getAllThemeSettingInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ThemeSettingInfos");
        Page<ThemeSettingInfo> page = themeSettingInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/theme-setting-infos");
        return new ResponseEntity<>(themeSettingInfoMapper.themeSettingInfosToThemeSettingInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /theme-setting-infos/:id : get the "id" themeSettingInfo.
     *
     * @param id the id of the themeSettingInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the themeSettingInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/theme-setting-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ThemeSettingInfoDTO> getThemeSettingInfo(@PathVariable Long id) {
        log.debug("REST request to get ThemeSettingInfo : {}", id);
        ThemeSettingInfoDTO themeSettingInfoDTO = themeSettingInfoService.findOne(id);
        return Optional.ofNullable(themeSettingInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /theme-setting-infos/:id : delete the "id" themeSettingInfo.
     *
     * @param id the id of the themeSettingInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/theme-setting-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteThemeSettingInfo(@PathVariable Long id) {
        log.debug("REST request to delete ThemeSettingInfo : {}", id);
        themeSettingInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("themeSettingInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/theme-setting-infos?query=:query : search for the themeSettingInfo corresponding
     * to the query.
     *
     * @param query the query of the themeSettingInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/theme-setting-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ThemeSettingInfoDTO>> searchThemeSettingInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ThemeSettingInfos for query {}", query);
        Page<ThemeSettingInfo> page = themeSettingInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/theme-setting-infos");
        return new ResponseEntity<>(themeSettingInfoMapper.themeSettingInfosToThemeSettingInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
