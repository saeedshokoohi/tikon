package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.OrderBag;
import com.eyeson.tikon.service.OrderBagService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.OrderBagDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagMapper;
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
 * REST controller for managing OrderBag.
 */
@RestController
@RequestMapping("/api")
public class OrderBagResource {

    private final Logger log = LoggerFactory.getLogger(OrderBagResource.class);
        
    @Inject
    private OrderBagService orderBagService;
    
    @Inject
    private OrderBagMapper orderBagMapper;
    
    /**
     * POST  /order-bags : Create a new orderBag.
     *
     * @param orderBagDTO the orderBagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderBagDTO, or with status 400 (Bad Request) if the orderBag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-bags",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagDTO> createOrderBag(@RequestBody OrderBagDTO orderBagDTO) throws URISyntaxException {
        log.debug("REST request to save OrderBag : {}", orderBagDTO);
        if (orderBagDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderBag", "idexists", "A new orderBag cannot already have an ID")).body(null);
        }
        OrderBagDTO result = orderBagService.save(orderBagDTO);
        return ResponseEntity.created(new URI("/api/order-bags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderBag", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-bags : Updates an existing orderBag.
     *
     * @param orderBagDTO the orderBagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderBagDTO,
     * or with status 400 (Bad Request) if the orderBagDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderBagDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-bags",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagDTO> updateOrderBag(@RequestBody OrderBagDTO orderBagDTO) throws URISyntaxException {
        log.debug("REST request to update OrderBag : {}", orderBagDTO);
        if (orderBagDTO.getId() == null) {
            return createOrderBag(orderBagDTO);
        }
        OrderBagDTO result = orderBagService.save(orderBagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderBag", orderBagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-bags : get all the orderBags.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderBags in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/order-bags",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderBagDTO>> getAllOrderBags(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OrderBags");
        Page<OrderBag> page = orderBagService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-bags");
        return new ResponseEntity<>(orderBagMapper.orderBagsToOrderBagDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-bags/:id : get the "id" orderBag.
     *
     * @param id the id of the orderBagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderBagDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/order-bags/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagDTO> getOrderBag(@PathVariable Long id) {
        log.debug("REST request to get OrderBag : {}", id);
        OrderBagDTO orderBagDTO = orderBagService.findOne(id);
        return Optional.ofNullable(orderBagDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-bags/:id : delete the "id" orderBag.
     *
     * @param id the id of the orderBagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/order-bags/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderBag(@PathVariable Long id) {
        log.debug("REST request to delete OrderBag : {}", id);
        orderBagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderBag", id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-bags?query=:query : search for the orderBag corresponding
     * to the query.
     *
     * @param query the query of the orderBag search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/order-bags",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderBagDTO>> searchOrderBags(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of OrderBags for query {}", query);
        Page<OrderBag> page = orderBagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-bags");
        return new ResponseEntity<>(orderBagMapper.orderBagsToOrderBagDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
