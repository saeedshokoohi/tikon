package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.CancelingInfo;
import com.eyeson.tikon.service.CancelingInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.CancelingInfoDTO;
import com.eyeson.tikon.web.rest.mapper.CancelingInfoMapper;
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
 * REST controller for managing CancelingInfo.
 */
@RestController
@RequestMapping("/api")
public class CancelingInfoResource {

    private final Logger log = LoggerFactory.getLogger(CancelingInfoResource.class);
        
    @Inject
    private CancelingInfoService cancelingInfoService;
    
    @Inject
    private CancelingInfoMapper cancelingInfoMapper;
    
    /**
     * POST  /canceling-infos : Create a new cancelingInfo.
     *
     * @param cancelingInfoDTO the cancelingInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cancelingInfoDTO, or with status 400 (Bad Request) if the cancelingInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/canceling-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CancelingInfoDTO> createCancelingInfo(@RequestBody CancelingInfoDTO cancelingInfoDTO) throws URISyntaxException {
        log.debug("REST request to save CancelingInfo : {}", cancelingInfoDTO);
        if (cancelingInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cancelingInfo", "idexists", "A new cancelingInfo cannot already have an ID")).body(null);
        }
        CancelingInfoDTO result = cancelingInfoService.save(cancelingInfoDTO);
        return ResponseEntity.created(new URI("/api/canceling-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cancelingInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /canceling-infos : Updates an existing cancelingInfo.
     *
     * @param cancelingInfoDTO the cancelingInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cancelingInfoDTO,
     * or with status 400 (Bad Request) if the cancelingInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the cancelingInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/canceling-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CancelingInfoDTO> updateCancelingInfo(@RequestBody CancelingInfoDTO cancelingInfoDTO) throws URISyntaxException {
        log.debug("REST request to update CancelingInfo : {}", cancelingInfoDTO);
        if (cancelingInfoDTO.getId() == null) {
            return createCancelingInfo(cancelingInfoDTO);
        }
        CancelingInfoDTO result = cancelingInfoService.save(cancelingInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cancelingInfo", cancelingInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /canceling-infos : get all the cancelingInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cancelingInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/canceling-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CancelingInfoDTO>> getAllCancelingInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CancelingInfos");
        Page<CancelingInfo> page = cancelingInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/canceling-infos");
        return new ResponseEntity<>(cancelingInfoMapper.cancelingInfosToCancelingInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /canceling-infos/:id : get the "id" cancelingInfo.
     *
     * @param id the id of the cancelingInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cancelingInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/canceling-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CancelingInfoDTO> getCancelingInfo(@PathVariable Long id) {
        log.debug("REST request to get CancelingInfo : {}", id);
        CancelingInfoDTO cancelingInfoDTO = cancelingInfoService.findOne(id);
        return Optional.ofNullable(cancelingInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /canceling-infos/:id : delete the "id" cancelingInfo.
     *
     * @param id the id of the cancelingInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/canceling-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCancelingInfo(@PathVariable Long id) {
        log.debug("REST request to delete CancelingInfo : {}", id);
        cancelingInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cancelingInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/canceling-infos?query=:query : search for the cancelingInfo corresponding
     * to the query.
     *
     * @param query the query of the cancelingInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/canceling-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<CancelingInfoDTO>> searchCancelingInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CancelingInfos for query {}", query);
        Page<CancelingInfo> page = cancelingInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/canceling-infos");
        return new ResponseEntity<>(cancelingInfoMapper.cancelingInfosToCancelingInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
