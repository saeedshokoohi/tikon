package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.PriceInfo;
import com.eyeson.tikon.service.PriceInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.PriceInfoDTO;
import com.eyeson.tikon.web.rest.mapper.PriceInfoMapper;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PriceInfo.
 */
@RestController
@RequestMapping("/api")
public class PriceInfoResource {

    private final Logger log = LoggerFactory.getLogger(PriceInfoResource.class);

    @Inject
    private PriceInfoService priceInfoService;

    @Inject
    private PriceInfoMapper priceInfoMapper;

    /**
     * POST  /price-infos : Create a new priceInfo.
     *
     * @param priceInfoDTO the priceInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new priceInfoDTO, or with status 400 (Bad Request) if the priceInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/price-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceInfoDTO> createPriceInfo(@RequestBody PriceInfoDTO priceInfoDTO) throws URISyntaxException {
        log.debug("REST request to save PriceInfo : {}", priceInfoDTO);
        if (priceInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("priceInfo", "idexists", "A new priceInfo cannot already have an ID")).body(null);
        }
        PriceInfoDTO result = priceInfoService.save(priceInfoDTO);
        return ResponseEntity.created(new URI("/api/price-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("priceInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /price-infos : Updates an existing priceInfo.
     *
     * @param priceInfoDTO the priceInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated priceInfoDTO,
     * or with status 400 (Bad Request) if the priceInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the priceInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/price-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceInfoDTO> updatePriceInfo(@RequestBody PriceInfoDTO priceInfoDTO) throws URISyntaxException {
        log.debug("REST request to update PriceInfo : {}", priceInfoDTO);
        if (priceInfoDTO.getId() == null) {
            return createPriceInfo(priceInfoDTO);
        }
        PriceInfoDTO result = priceInfoService.save(priceInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("priceInfo", priceInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /price-infos : get all the priceInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of priceInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/price-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PriceInfoDTO>> getAllPriceInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PriceInfos");
        Page<PriceInfo> page = priceInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/price-infos");
        return new ResponseEntity<>(priceInfoMapper.priceInfosToPriceInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /price-infos/:id : get the "id" priceInfo.
     *
     * @param id the id of the priceInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the priceInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/price-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceInfoDTO> getPriceInfo(@PathVariable Long id) {
        log.debug("REST request to get PriceInfo : {}", id);
        PriceInfoDTO priceInfoDTO = priceInfoService.findOne(id);
        return Optional.ofNullable(priceInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /price-infos/:id : delete the "id" priceInfo.
     *
     * @param id the id of the priceInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/price-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePriceInfo(@PathVariable Long id) {
        log.debug("REST request to delete PriceInfo : {}", id);
        priceInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("priceInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/price-infos?query=:query : search for the priceInfo corresponding
     * to the query.
     *
     * @param query the query of the priceInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/price-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PriceInfoDTO>> searchPriceInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of PriceInfos for query {}", query);
        Page<PriceInfo> page = priceInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/price-infos");
        return new ResponseEntity<>(priceInfoMapper.priceInfosToPriceInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/price-infos-by-service-item/{serviceItemId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public List<PriceInfoDTO> getPriceInfosByServiceItem(@PathVariable("serviceItemId") String serviceItemId)
    {
        List<PriceInfoDTO> retList=new ArrayList<>();
        List<PriceInfo> pldInfoList = priceInfoService.getPriceInfosByServiceItem(Long.parseLong(serviceItemId));

       retList = priceInfoMapper.priceInfosToPriceInfoDTOs(pldInfoList);

        return  retList;
    }

}
