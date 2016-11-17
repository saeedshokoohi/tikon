package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.OrderBagServiceItem;
import com.eyeson.tikon.service.OrderBagServiceItemService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.OrderBagServiceItemDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagServiceItemMapper;
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
 * REST controller for managing OrderBagServiceItem.
 */
@RestController
@RequestMapping("/api")
public class OrderBagServiceItemResource {

    private final Logger log = LoggerFactory.getLogger(OrderBagServiceItemResource.class);
        
    @Inject
    private OrderBagServiceItemService orderBagServiceItemService;
    
    @Inject
    private OrderBagServiceItemMapper orderBagServiceItemMapper;
    
    /**
     * POST  /order-bag-service-items : Create a new orderBagServiceItem.
     *
     * @param orderBagServiceItemDTO the orderBagServiceItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderBagServiceItemDTO, or with status 400 (Bad Request) if the orderBagServiceItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-bag-service-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagServiceItemDTO> createOrderBagServiceItem(@RequestBody OrderBagServiceItemDTO orderBagServiceItemDTO) throws URISyntaxException {
        log.debug("REST request to save OrderBagServiceItem : {}", orderBagServiceItemDTO);
        if (orderBagServiceItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderBagServiceItem", "idexists", "A new orderBagServiceItem cannot already have an ID")).body(null);
        }
        OrderBagServiceItemDTO result = orderBagServiceItemService.save(orderBagServiceItemDTO);
        return ResponseEntity.created(new URI("/api/order-bag-service-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderBagServiceItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-bag-service-items : Updates an existing orderBagServiceItem.
     *
     * @param orderBagServiceItemDTO the orderBagServiceItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderBagServiceItemDTO,
     * or with status 400 (Bad Request) if the orderBagServiceItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderBagServiceItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/order-bag-service-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagServiceItemDTO> updateOrderBagServiceItem(@RequestBody OrderBagServiceItemDTO orderBagServiceItemDTO) throws URISyntaxException {
        log.debug("REST request to update OrderBagServiceItem : {}", orderBagServiceItemDTO);
        if (orderBagServiceItemDTO.getId() == null) {
            return createOrderBagServiceItem(orderBagServiceItemDTO);
        }
        OrderBagServiceItemDTO result = orderBagServiceItemService.save(orderBagServiceItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderBagServiceItem", orderBagServiceItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-bag-service-items : get all the orderBagServiceItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderBagServiceItems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/order-bag-service-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderBagServiceItemDTO>> getAllOrderBagServiceItems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of OrderBagServiceItems");
        Page<OrderBagServiceItem> page = orderBagServiceItemService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-bag-service-items");
        return new ResponseEntity<>(orderBagServiceItemMapper.orderBagServiceItemsToOrderBagServiceItemDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-bag-service-items/:id : get the "id" orderBagServiceItem.
     *
     * @param id the id of the orderBagServiceItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderBagServiceItemDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/order-bag-service-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrderBagServiceItemDTO> getOrderBagServiceItem(@PathVariable Long id) {
        log.debug("REST request to get OrderBagServiceItem : {}", id);
        OrderBagServiceItemDTO orderBagServiceItemDTO = orderBagServiceItemService.findOne(id);
        return Optional.ofNullable(orderBagServiceItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-bag-service-items/:id : delete the "id" orderBagServiceItem.
     *
     * @param id the id of the orderBagServiceItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/order-bag-service-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrderBagServiceItem(@PathVariable Long id) {
        log.debug("REST request to delete OrderBagServiceItem : {}", id);
        orderBagServiceItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderBagServiceItem", id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-bag-service-items?query=:query : search for the orderBagServiceItem corresponding
     * to the query.
     *
     * @param query the query of the orderBagServiceItem search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/order-bag-service-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderBagServiceItemDTO>> searchOrderBagServiceItems(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of OrderBagServiceItems for query {}", query);
        Page<OrderBagServiceItem> page = orderBagServiceItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-bag-service-items");
        return new ResponseEntity<>(orderBagServiceItemMapper.orderBagServiceItemsToOrderBagServiceItemDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
