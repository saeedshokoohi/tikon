package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.OrderBagServiceItemDtail;
import com.eyeson.tikon.service.OrderBagServiceItemDtailService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.OrderBagServiceItemDtailDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagServiceItemDtailMapper;
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
 * REST controller for managing OrderBagServiceItemDtail.
 */
@RestController
@RequestMapping("/api")
public class OrderBagServiceItemDtailResource {

    private final Logger log = LoggerFactory.getLogger(OrderBagServiceItemDtailResource.class);
        
    @Inject
    private OrderBagServiceItemDtailService orderBagServiceItemDtailService;
    
    @Inject
    private OrderBagServiceItemDtailMapper orderBagServiceItemDtailMapper;
    
    /**
     * POST  /order-bag-service-item-dtails : Create a new orderBagServiceItemDtail.
     *
     * @param orderBagServiceItemDtailDTO the orderBagServiceItemDtailDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderBagServiceItemDtailDTO, or with status 400 (Bad Request) if the orderBagServiceItemDtail has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-bag-service-item-dtails",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagServiceItemDtailDTO> createOrderBagServiceItemDtail(@RequestBody OrderBagServiceItemDtailDTO orderBagServiceItemDtailDTO) throws URISyntaxException {
        log.debug("REST request to save OrderBagServiceItemDtail : {}", orderBagServiceItemDtailDTO);
        if (orderBagServiceItemDtailDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderBagServiceItemDtail", "idexists", "A new orderBagServiceItemDtail cannot already have an ID")).body(null);
        }
        OrderBagServiceItemDtailDTO result = orderBagServiceItemDtailService.save(orderBagServiceItemDtailDTO);
        return ResponseEntity.created(new URI("/api/order-bag-service-item-dtails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderBagServiceItemDtail", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-bag-service-item-dtails : Updates an existing orderBagServiceItemDtail.
     *
     * @param orderBagServiceItemDtailDTO the orderBagServiceItemDtailDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderBagServiceItemDtailDTO,
     * or with status 400 (Bad Request) if the orderBagServiceItemDtailDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderBagServiceItemDtailDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-bag-service-item-dtails",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagServiceItemDtailDTO> updateOrderBagServiceItemDtail(@RequestBody OrderBagServiceItemDtailDTO orderBagServiceItemDtailDTO) throws URISyntaxException {
        log.debug("REST request to update OrderBagServiceItemDtail : {}", orderBagServiceItemDtailDTO);
        if (orderBagServiceItemDtailDTO.getId() == null) {
            return createOrderBagServiceItemDtail(orderBagServiceItemDtailDTO);
        }
        OrderBagServiceItemDtailDTO result = orderBagServiceItemDtailService.save(orderBagServiceItemDtailDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderBagServiceItemDtail", orderBagServiceItemDtailDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-bag-service-item-dtails : get all the orderBagServiceItemDtails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderBagServiceItemDtails in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/order-bag-service-item-dtails",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderBagServiceItemDtailDTO>> getAllOrderBagServiceItemDtails(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OrderBagServiceItemDtails");
        Page<OrderBagServiceItemDtail> page = orderBagServiceItemDtailService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-bag-service-item-dtails");
        return new ResponseEntity<>(orderBagServiceItemDtailMapper.orderBagServiceItemDtailsToOrderBagServiceItemDtailDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-bag-service-item-dtails/:id : get the "id" orderBagServiceItemDtail.
     *
     * @param id the id of the orderBagServiceItemDtailDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderBagServiceItemDtailDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/order-bag-service-item-dtails/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagServiceItemDtailDTO> getOrderBagServiceItemDtail(@PathVariable Long id) {
        log.debug("REST request to get OrderBagServiceItemDtail : {}", id);
        OrderBagServiceItemDtailDTO orderBagServiceItemDtailDTO = orderBagServiceItemDtailService.findOne(id);
        return Optional.ofNullable(orderBagServiceItemDtailDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-bag-service-item-dtails/:id : delete the "id" orderBagServiceItemDtail.
     *
     * @param id the id of the orderBagServiceItemDtailDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/order-bag-service-item-dtails/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderBagServiceItemDtail(@PathVariable Long id) {
        log.debug("REST request to delete OrderBagServiceItemDtail : {}", id);
        orderBagServiceItemDtailService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderBagServiceItemDtail", id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-bag-service-item-dtails?query=:query : search for the orderBagServiceItemDtail corresponding
     * to the query.
     *
     * @param query the query of the orderBagServiceItemDtail search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/order-bag-service-item-dtails",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderBagServiceItemDtailDTO>> searchOrderBagServiceItemDtails(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of OrderBagServiceItemDtails for query {}", query);
        Page<OrderBagServiceItemDtail> page = orderBagServiceItemDtailService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-bag-service-item-dtails");
        return new ResponseEntity<>(orderBagServiceItemDtailMapper.orderBagServiceItemDtailsToOrderBagServiceItemDtailDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
