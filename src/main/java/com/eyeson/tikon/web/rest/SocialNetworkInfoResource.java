package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.SocialNetworkInfo;
import com.eyeson.tikon.service.SocialNetworkInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.SocialNetworkInfoDTO;
import com.eyeson.tikon.web.rest.mapper.SocialNetworkInfoMapper;
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
 * REST controller for managing SocialNetworkInfo.
 */
@RestController
@RequestMapping("/api")
public class SocialNetworkInfoResource {

    private final Logger log = LoggerFactory.getLogger(SocialNetworkInfoResource.class);
        
    @Inject
    private SocialNetworkInfoService socialNetworkInfoService;
    
    @Inject
    private SocialNetworkInfoMapper socialNetworkInfoMapper;
    
    /**
     * POST  /social-network-infos : Create a new socialNetworkInfo.
     *
     * @param socialNetworkInfoDTO the socialNetworkInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new socialNetworkInfoDTO, or with status 400 (Bad Request) if the socialNetworkInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/social-network-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SocialNetworkInfoDTO> createSocialNetworkInfo(@RequestBody SocialNetworkInfoDTO socialNetworkInfoDTO) throws URISyntaxException {
        log.debug("REST request to save SocialNetworkInfo : {}", socialNetworkInfoDTO);
        if (socialNetworkInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("socialNetworkInfo", "idexists", "A new socialNetworkInfo cannot already have an ID")).body(null);
        }
        SocialNetworkInfoDTO result = socialNetworkInfoService.save(socialNetworkInfoDTO);
        return ResponseEntity.created(new URI("/api/social-network-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("socialNetworkInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /social-network-infos : Updates an existing socialNetworkInfo.
     *
     * @param socialNetworkInfoDTO the socialNetworkInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated socialNetworkInfoDTO,
     * or with status 400 (Bad Request) if the socialNetworkInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the socialNetworkInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/social-network-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SocialNetworkInfoDTO> updateSocialNetworkInfo(@RequestBody SocialNetworkInfoDTO socialNetworkInfoDTO) throws URISyntaxException {
        log.debug("REST request to update SocialNetworkInfo : {}", socialNetworkInfoDTO);
        if (socialNetworkInfoDTO.getId() == null) {
            return createSocialNetworkInfo(socialNetworkInfoDTO);
        }
        SocialNetworkInfoDTO result = socialNetworkInfoService.save(socialNetworkInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("socialNetworkInfo", socialNetworkInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /social-network-infos : get all the socialNetworkInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of socialNetworkInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/social-network-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SocialNetworkInfoDTO>> getAllSocialNetworkInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of SocialNetworkInfos");
        Page<SocialNetworkInfo> page = socialNetworkInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/social-network-infos");
        return new ResponseEntity<>(socialNetworkInfoMapper.socialNetworkInfosToSocialNetworkInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /social-network-infos/:id : get the "id" socialNetworkInfo.
     *
     * @param id the id of the socialNetworkInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the socialNetworkInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/social-network-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SocialNetworkInfoDTO> getSocialNetworkInfo(@PathVariable Long id) {
        log.debug("REST request to get SocialNetworkInfo : {}", id);
        SocialNetworkInfoDTO socialNetworkInfoDTO = socialNetworkInfoService.findOne(id);
        return Optional.ofNullable(socialNetworkInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /social-network-infos/:id : delete the "id" socialNetworkInfo.
     *
     * @param id the id of the socialNetworkInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/social-network-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSocialNetworkInfo(@PathVariable Long id) {
        log.debug("REST request to delete SocialNetworkInfo : {}", id);
        socialNetworkInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("socialNetworkInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/social-network-infos?query=:query : search for the socialNetworkInfo corresponding
     * to the query.
     *
     * @param query the query of the socialNetworkInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/social-network-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<SocialNetworkInfoDTO>> searchSocialNetworkInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of SocialNetworkInfos for query {}", query);
        Page<SocialNetworkInfo> page = socialNetworkInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/social-network-infos");
        return new ResponseEntity<>(socialNetworkInfoMapper.socialNetworkInfosToSocialNetworkInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
