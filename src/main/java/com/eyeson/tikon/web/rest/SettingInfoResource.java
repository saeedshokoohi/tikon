package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.SettingInfo;
import com.eyeson.tikon.service.SettingInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.SettingInfoDTO;
import com.eyeson.tikon.web.rest.mapper.SettingInfoMapper;
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
 * REST controller for managing SettingInfo.
 */
@RestController
@RequestMapping("/api")
public class SettingInfoResource {

    private final Logger log = LoggerFactory.getLogger(SettingInfoResource.class);

    @Inject
    private SettingInfoService settingInfoService;

    @Inject
    private SettingInfoMapper settingInfoMapper;

    /**
     * POST  /setting-infos : Create a new settingInfo.
     *
     * @param settingInfoDTO the settingInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new settingInfoDTO, or with status 400 (Bad Request) if the settingInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/setting-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SettingInfoDTO> createSettingInfo(@RequestBody SettingInfoDTO settingInfoDTO) throws URISyntaxException {
        log.debug("REST request to save SettingInfo : {}", settingInfoDTO);
        if (settingInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("settingInfo", "idexists", "A new settingInfo cannot already have an ID")).body(null);
        }
        SettingInfoDTO result = settingInfoService.save(settingInfoDTO);
        return ResponseEntity.created(new URI("/api/setting-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("settingInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /setting-infos : Updates an existing settingInfo.
     *
     * @param settingInfoDTO the settingInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated settingInfoDTO,
     * or with status 400 (Bad Request) if the settingInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the settingInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/setting-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SettingInfoDTO> updateSettingInfo(@RequestBody SettingInfoDTO settingInfoDTO) throws URISyntaxException {
        log.debug("REST request to update SettingInfo : {}", settingInfoDTO);
        if (settingInfoDTO.getId() == null) {
            return createSettingInfo(settingInfoDTO);
        }
        SettingInfoDTO result = settingInfoService.save(settingInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("settingInfo", settingInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /setting-infos : get all the settingInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of settingInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/setting-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SettingInfoDTO>> getAllSettingInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SettingInfos");
        Page<SettingInfo> page = settingInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/setting-infos");
        return new ResponseEntity<>(settingInfoMapper.settingInfosToSettingInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /setting-infos/:id : get the "id" settingInfo.
     *
     * @param id the id of the settingInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the settingInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/setting-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SettingInfoDTO> getSettingInfo(@PathVariable Long id) {
        log.debug("REST request to get SettingInfo : {}", id);
        SettingInfoDTO settingInfoDTO = settingInfoService.findOne(id);
        return Optional.ofNullable(settingInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /setting-infos/:id : delete the "id" settingInfo.
     *
     * @param id the id of the settingInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/setting-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSettingInfo(@PathVariable Long id) {
        log.debug("REST request to delete SettingInfo : {}", id);
        settingInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("settingInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/setting-infos?query=:query : search for the settingInfo corresponding
     * to the query.
     *
     * @param query the query of the settingInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/setting-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SettingInfoDTO>> searchSettingInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SettingInfos for query {}", query);
        Page<SettingInfo> page = settingInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/setting-infos");
        return new ResponseEntity<>(settingInfoMapper.settingInfosToSettingInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/setting-info-by-current-company",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public SettingInfoDTO getSettingInfoByCurrentCompany()
    {
        SettingInfoDTO settingInfoDTO = settingInfoMapper.settingInfoToSettingInfoDTO(settingInfoService.getSettingInfoByCurrentCompany());


        return settingInfoDTO;

    }

    @RequestMapping(value = "/setting-info-with-company",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SettingInfoDTO> createSettingInfoWithCompany(@RequestBody SettingInfoDTO settingInfoDTO) throws URISyntaxException {
        log.debug("REST request to save SettingInfo : {}", settingInfoDTO);
        if (settingInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("settingInfo", "idexists", "A new settingInfo cannot already have an ID")).body(null);
        }
        SettingInfoDTO result = settingInfoService.saveWithCompany(settingInfoDTO);

        return ResponseEntity.created(new URI("/api/setting-info-with-company/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("settingInfo", result.getId().toString()))
            .body(result);
    }


}
