package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.OrderBagItemOption;
import com.eyeson.tikon.service.OrderBagItemOptionService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.OrderBagItemOptionDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagItemOptionMapper;
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
 * REST controller for managing OrderBagItemOption.
 */
@RestController
@RequestMapping("/api")
public class OrderBagItemOptionResource {

    private final Logger log = LoggerFactory.getLogger(OrderBagItemOptionResource.class);
        
    @Inject
    private OrderBagItemOptionService orderBagItemOptionService;
    
    @Inject
    private OrderBagItemOptionMapper orderBagItemOptionMapper;
    
    /**
     * POST  /order-bag-item-options : Create a new orderBagItemOption.
     *
     * @param orderBagItemOptionDTO the orderBagItemOptionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderBagItemOptionDTO, or with status 400 (Bad Request) if the orderBagItemOption has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-bag-item-options",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagItemOptionDTO> createOrderBagItemOption(@RequestBody OrderBagItemOptionDTO orderBagItemOptionDTO) throws URISyntaxException {
        log.debug("REST request to save OrderBagItemOption : {}", orderBagItemOptionDTO);
        if (orderBagItemOptionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderBagItemOption", "idexists", "A new orderBagItemOption cannot already have an ID")).body(null);
        }
        OrderBagItemOptionDTO result = orderBagItemOptionService.save(orderBagItemOptionDTO);
        return ResponseEntity.created(new URI("/api/order-bag-item-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderBagItemOption", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-bag-item-options : Updates an existing orderBagItemOption.
     *
     * @param orderBagItemOptionDTO the orderBagItemOptionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderBagItemOptionDTO,
     * or with status 400 (Bad Request) if the orderBagItemOptionDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderBagItemOptionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-bag-item-options",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagItemOptionDTO> updateOrderBagItemOption(@RequestBody OrderBagItemOptionDTO orderBagItemOptionDTO) throws URISyntaxException {
        log.debug("REST request to update OrderBagItemOption : {}", orderBagItemOptionDTO);
        if (orderBagItemOptionDTO.getId() == null) {
            return createOrderBagItemOption(orderBagItemOptionDTO);
        }
        OrderBagItemOptionDTO result = orderBagItemOptionService.save(orderBagItemOptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderBagItemOption", orderBagItemOptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-bag-item-options : get all the orderBagItemOptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderBagItemOptions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/order-bag-item-options",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderBagItemOptionDTO>> getAllOrderBagItemOptions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OrderBagItemOptions");
        Page<OrderBagItemOption> page = orderBagItemOptionService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-bag-item-options");
        return new ResponseEntity<>(orderBagItemOptionMapper.orderBagItemOptionsToOrderBagItemOptionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-bag-item-options/:id : get the "id" orderBagItemOption.
     *
     * @param id the id of the orderBagItemOptionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderBagItemOptionDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/order-bag-item-options/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagItemOptionDTO> getOrderBagItemOption(@PathVariable Long id) {
        log.debug("REST request to get OrderBagItemOption : {}", id);
        OrderBagItemOptionDTO orderBagItemOptionDTO = orderBagItemOptionService.findOne(id);
        return Optional.ofNullable(orderBagItemOptionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-bag-item-options/:id : delete the "id" orderBagItemOption.
     *
     * @param id the id of the orderBagItemOptionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/order-bag-item-options/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderBagItemOption(@PathVariable Long id) {
        log.debug("REST request to delete OrderBagItemOption : {}", id);
        orderBagItemOptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderBagItemOption", id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-bag-item-options?query=:query : search for the orderBagItemOption corresponding
     * to the query.
     *
     * @param query the query of the orderBagItemOption search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/order-bag-item-options",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderBagItemOptionDTO>> searchOrderBagItemOptions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of OrderBagItemOptions for query {}", query);
        Page<OrderBagItemOption> page = orderBagItemOptionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-bag-item-options");
        return new ResponseEntity<>(orderBagItemOptionMapper.orderBagItemOptionsToOrderBagItemOptionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
