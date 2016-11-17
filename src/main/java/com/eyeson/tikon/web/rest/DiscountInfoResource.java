package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.DiscountInfo;
import com.eyeson.tikon.service.DiscountInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.DiscountInfoDTO;
import com.eyeson.tikon.web.rest.mapper.DiscountInfoMapper;
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
 * REST controller for managing DiscountInfo.
 */
@RestController
@RequestMapping("/api")
public class DiscountInfoResource {

    private final Logger log = LoggerFactory.getLogger(DiscountInfoResource.class);
        
    @Inject
    private DiscountInfoService discountInfoService;
    
    @Inject
    private DiscountInfoMapper discountInfoMapper;
    
    /**
     * POST  /discount-infos : Create a new discountInfo.
     *
     * @param discountInfoDTO the discountInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new discountInfoDTO, or with status 400 (Bad Request) if the discountInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/discount-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountInfoDTO> createDiscountInfo(@RequestBody DiscountInfoDTO discountInfoDTO) throws URISyntaxException {
        log.debug("REST request to save DiscountInfo : {}", discountInfoDTO);
        if (discountInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("discountInfo", "idexists", "A new discountInfo cannot already have an ID")).body(null);
        }
        DiscountInfoDTO result = discountInfoService.save(discountInfoDTO);
        return ResponseEntity.created(new URI("/api/discount-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("discountInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /discount-infos : Updates an existing discountInfo.
     *
     * @param discountInfoDTO the discountInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated discountInfoDTO,
     * or with status 400 (Bad Request) if the discountInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the discountInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/discount-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountInfoDTO> updateDiscountInfo(@RequestBody DiscountInfoDTO discountInfoDTO) throws URISyntaxException {
        log.debug("REST request to update DiscountInfo : {}", discountInfoDTO);
        if (discountInfoDTO.getId() == null) {
            return createDiscountInfo(discountInfoDTO);
        }
        DiscountInfoDTO result = discountInfoService.save(discountInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("discountInfo", discountInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /discount-infos : get all the discountInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of discountInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/discount-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<DiscountInfoDTO>> getAllDiscountInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DiscountInfos");
        Page<DiscountInfo> page = discountInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/discount-infos");
        return new ResponseEntity<>(discountInfoMapper.discountInfosToDiscountInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /discount-infos/:id : get the "id" discountInfo.
     *
     * @param id the id of the discountInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the discountInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/discount-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiscountInfoDTO> getDiscountInfo(@PathVariable Long id) {
        log.debug("REST request to get DiscountInfo : {}", id);
        DiscountInfoDTO discountInfoDTO = discountInfoService.findOne(id);
        return Optional.ofNullable(discountInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /discount-infos/:id : delete the "id" discountInfo.
     *
     * @param id the id of the discountInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/discount-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiscountInfo(@PathVariable Long id) {
        log.debug("REST request to delete DiscountInfo : {}", id);
        discountInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("discountInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/discount-infos?query=:query : search for the discountInfo corresponding
     * to the query.
     *
     * @param query the query of the discountInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/discount-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<DiscountInfoDTO>> searchDiscountInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of DiscountInfos for query {}", query);
        Page<DiscountInfo> page = discountInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/discount-infos");
        return new ResponseEntity<>(discountInfoMapper.discountInfosToDiscountInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
