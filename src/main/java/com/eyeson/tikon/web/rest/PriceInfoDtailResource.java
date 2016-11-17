package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.PriceInfoDtail;
import com.eyeson.tikon.domain.ScheduleInfo;
import com.eyeson.tikon.service.PriceInfoDtailService;
import com.eyeson.tikon.web.rest.dto.PriceInfoDTO;
import com.eyeson.tikon.web.rest.dto.ScheduleInfoDTO;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.PriceInfoDtailDTO;
import com.eyeson.tikon.web.rest.mapper.PriceInfoDtailMapper;
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
 * REST controller for managing PriceInfoDtail.
 */
@RestController
@RequestMapping("/api")
public class PriceInfoDtailResource {

    private final Logger log = LoggerFactory.getLogger(PriceInfoDtailResource.class);

    @Inject
    private PriceInfoDtailService priceInfoDtailService;

    @Inject
    private PriceInfoDtailMapper priceInfoDtailMapper;

    /**
     * POST  /price-info-dtails : Create a new priceInfoDtail.
     *
     * @param priceInfoDtailDTO the priceInfoDtailDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new priceInfoDtailDTO, or with status 400 (Bad Request) if the priceInfoDtail has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/price-info-dtails",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceInfoDtailDTO> createPriceInfoDtail(@RequestBody PriceInfoDtailDTO priceInfoDtailDTO) throws URISyntaxException {
        log.debug("REST request to save PriceInfoDtail : {}", priceInfoDtailDTO);
        if (priceInfoDtailDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("priceInfoDtail", "idexists", "A new priceInfoDtail cannot already have an ID")).body(null);
        }
        PriceInfoDtailDTO result = priceInfoDtailService.save(priceInfoDtailDTO);
        return ResponseEntity.created(new URI("/api/price-info-dtails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("priceInfoDtail", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /price-info-dtails : Updates an existing priceInfoDtail.
     *
     * @param priceInfoDtailDTO the priceInfoDtailDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated priceInfoDtailDTO,
     * or with status 400 (Bad Request) if the priceInfoDtailDTO is not valid,
     * or with status 500 (Internal Server Error) if the priceInfoDtailDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/price-info-dtails",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceInfoDtailDTO> updatePriceInfoDtail(@RequestBody PriceInfoDtailDTO priceInfoDtailDTO) throws URISyntaxException {
        log.debug("REST request to update PriceInfoDtail : {}", priceInfoDtailDTO);
        if (priceInfoDtailDTO.getId() == null) {
            return createPriceInfoDtail(priceInfoDtailDTO);
        }
        PriceInfoDtailDTO result = priceInfoDtailService.save(priceInfoDtailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("priceInfoDtail", priceInfoDtailDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /price-info-dtails : get all the priceInfoDtails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of priceInfoDtails in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/price-info-dtails",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PriceInfoDtailDTO>> getAllPriceInfoDtails(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PriceInfoDtails");
        Page<PriceInfoDtail> page = priceInfoDtailService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/price-info-dtails");
        return new ResponseEntity<>(priceInfoDtailMapper.priceInfoDtailsToPriceInfoDtailDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /price-info-dtails/:id : get the "id" priceInfoDtail.
     *
     * @param id the id of the priceInfoDtailDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the priceInfoDtailDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/price-info-dtails/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PriceInfoDtailDTO> getPriceInfoDtail(@PathVariable Long id) {
        log.debug("REST request to get PriceInfoDtail : {}", id);
        PriceInfoDtailDTO priceInfoDtailDTO = priceInfoDtailService.findOne(id);
        return Optional.ofNullable(priceInfoDtailDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /price-info-dtails/:id : delete the "id" priceInfoDtail.
     *
     * @param id the id of the priceInfoDtailDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/price-info-dtails/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePriceInfoDtail(@PathVariable Long id) {
        log.debug("REST request to delete PriceInfoDtail : {}", id);
        priceInfoDtailService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("priceInfoDtail", id.toString())).build();
    }

    /**
     * SEARCH  /_search/price-info-dtails?query=:query : search for the priceInfoDtail corresponding
     * to the query.
     *
     * @param query the query of the priceInfoDtail search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/price-info-dtails",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PriceInfoDtailDTO>> searchPriceInfoDtails(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of PriceInfoDtails for query {}", query);
        Page<PriceInfoDtail> page = priceInfoDtailService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/price-info-dtails");
        return new ResponseEntity<>(priceInfoDtailMapper.priceInfoDtailsToPriceInfoDtailDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/price-info-dtails-by-service-item/{serviceItemId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public List<PriceInfoDtailDTO> getPriceInfoDtailsByServiceItem(@PathVariable("serviceItemId") String serviceItemId)
    {
        List<PriceInfoDtailDTO> retList=new ArrayList<>();
        List<PriceInfoDtail> pldInfoList = priceInfoDtailService.getPriceInfoDtailsByServiceItem(Long.parseLong(serviceItemId));
        for(PriceInfoDtail sch:pldInfoList)
        {
            retList.add(priceInfoDtailMapper.priceInfoDtailToPriceInfoDtailDTO(sch));
        }

        return  retList;
    }

}
