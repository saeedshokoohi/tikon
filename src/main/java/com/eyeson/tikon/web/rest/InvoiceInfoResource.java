package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.InvoiceInfo;
import com.eyeson.tikon.service.InvoiceInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.InvoiceInfoDTO;
import com.eyeson.tikon.web.rest.mapper.InvoiceInfoMapper;
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
 * REST controller for managing InvoiceInfo.
 */
@RestController
@RequestMapping("/api")
public class InvoiceInfoResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceInfoResource.class);
        
    @Inject
    private InvoiceInfoService invoiceInfoService;
    
    @Inject
    private InvoiceInfoMapper invoiceInfoMapper;
    
    /**
     * POST  /invoice-infos : Create a new invoiceInfo.
     *
     * @param invoiceInfoDTO the invoiceInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceInfoDTO, or with status 400 (Bad Request) if the invoiceInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invoice-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InvoiceInfoDTO> createInvoiceInfo(@RequestBody InvoiceInfoDTO invoiceInfoDTO) throws URISyntaxException {
        log.debug("REST request to save InvoiceInfo : {}", invoiceInfoDTO);
        if (invoiceInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("invoiceInfo", "idexists", "A new invoiceInfo cannot already have an ID")).body(null);
        }
        InvoiceInfoDTO result = invoiceInfoService.save(invoiceInfoDTO);
        return ResponseEntity.created(new URI("/api/invoice-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("invoiceInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-infos : Updates an existing invoiceInfo.
     *
     * @param invoiceInfoDTO the invoiceInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceInfoDTO,
     * or with status 400 (Bad Request) if the invoiceInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the invoiceInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invoice-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InvoiceInfoDTO> updateInvoiceInfo(@RequestBody InvoiceInfoDTO invoiceInfoDTO) throws URISyntaxException {
        log.debug("REST request to update InvoiceInfo : {}", invoiceInfoDTO);
        if (invoiceInfoDTO.getId() == null) {
            return createInvoiceInfo(invoiceInfoDTO);
        }
        InvoiceInfoDTO result = invoiceInfoService.save(invoiceInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("invoiceInfo", invoiceInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-infos : get all the invoiceInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/invoice-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<InvoiceInfoDTO>> getAllInvoiceInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InvoiceInfos");
        Page<InvoiceInfo> page = invoiceInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoice-infos");
        return new ResponseEntity<>(invoiceInfoMapper.invoiceInfosToInvoiceInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoice-infos/:id : get the "id" invoiceInfo.
     *
     * @param id the id of the invoiceInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/invoice-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InvoiceInfoDTO> getInvoiceInfo(@PathVariable Long id) {
        log.debug("REST request to get InvoiceInfo : {}", id);
        InvoiceInfoDTO invoiceInfoDTO = invoiceInfoService.findOne(id);
        return Optional.ofNullable(invoiceInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /invoice-infos/:id : delete the "id" invoiceInfo.
     *
     * @param id the id of the invoiceInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/invoice-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInvoiceInfo(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceInfo : {}", id);
        invoiceInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("invoiceInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/invoice-infos?query=:query : search for the invoiceInfo corresponding
     * to the query.
     *
     * @param query the query of the invoiceInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/invoice-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<InvoiceInfoDTO>> searchInvoiceInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InvoiceInfos for query {}", query);
        Page<InvoiceInfo> page = invoiceInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/invoice-infos");
        return new ResponseEntity<>(invoiceInfoMapper.invoiceInfosToInvoiceInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
