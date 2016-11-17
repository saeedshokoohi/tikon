package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.PaymentLog;
import com.eyeson.tikon.service.PaymentLogService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.PaymentLogDTO;
import com.eyeson.tikon.web.rest.mapper.PaymentLogMapper;
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
 * REST controller for managing PaymentLog.
 */
@RestController
@RequestMapping("/api")
public class PaymentLogResource {

    private final Logger log = LoggerFactory.getLogger(PaymentLogResource.class);
        
    @Inject
    private PaymentLogService paymentLogService;
    
    @Inject
    private PaymentLogMapper paymentLogMapper;
    
    /**
     * POST  /payment-logs : Create a new paymentLog.
     *
     * @param paymentLogDTO the paymentLogDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentLogDTO, or with status 400 (Bad Request) if the paymentLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/payment-logs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaymentLogDTO> createPaymentLog(@RequestBody PaymentLogDTO paymentLogDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentLog : {}", paymentLogDTO);
        if (paymentLogDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("paymentLog", "idexists", "A new paymentLog cannot already have an ID")).body(null);
        }
        PaymentLogDTO result = paymentLogService.save(paymentLogDTO);
        return ResponseEntity.created(new URI("/api/payment-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("paymentLog", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-logs : Updates an existing paymentLog.
     *
     * @param paymentLogDTO the paymentLogDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentLogDTO,
     * or with status 400 (Bad Request) if the paymentLogDTO is not valid,
     * or with status 500 (Internal Server Error) if the paymentLogDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/payment-logs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaymentLogDTO> updatePaymentLog(@RequestBody PaymentLogDTO paymentLogDTO) throws URISyntaxException {
        log.debug("REST request to update PaymentLog : {}", paymentLogDTO);
        if (paymentLogDTO.getId() == null) {
            return createPaymentLog(paymentLogDTO);
        }
        PaymentLogDTO result = paymentLogService.save(paymentLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("paymentLog", paymentLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-logs : get all the paymentLogs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paymentLogs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/payment-logs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PaymentLogDTO>> getAllPaymentLogs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PaymentLogs");
        Page<PaymentLog> page = paymentLogService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-logs");
        return new ResponseEntity<>(paymentLogMapper.paymentLogsToPaymentLogDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /payment-logs/:id : get the "id" paymentLog.
     *
     * @param id the id of the paymentLogDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentLogDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/payment-logs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaymentLogDTO> getPaymentLog(@PathVariable Long id) {
        log.debug("REST request to get PaymentLog : {}", id);
        PaymentLogDTO paymentLogDTO = paymentLogService.findOne(id);
        return Optional.ofNullable(paymentLogDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /payment-logs/:id : delete the "id" paymentLog.
     *
     * @param id the id of the paymentLogDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/payment-logs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePaymentLog(@PathVariable Long id) {
        log.debug("REST request to delete PaymentLog : {}", id);
        paymentLogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("paymentLog", id.toString())).build();
    }

    /**
     * SEARCH  /_search/payment-logs?query=:query : search for the paymentLog corresponding
     * to the query.
     *
     * @param query the query of the paymentLog search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/payment-logs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PaymentLogDTO>> searchPaymentLogs(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of PaymentLogs for query {}", query);
        Page<PaymentLog> page = paymentLogService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/payment-logs");
        return new ResponseEntity<>(paymentLogMapper.paymentLogsToPaymentLogDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
