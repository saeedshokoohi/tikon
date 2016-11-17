package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.RelatedServiceItem;
import com.eyeson.tikon.service.RelatedServiceItemService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.RelatedServiceItemDTO;
import com.eyeson.tikon.web.rest.mapper.RelatedServiceItemMapper;
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
 * REST controller for managing RelatedServiceItem.
 */
@RestController
@RequestMapping("/api")
public class RelatedServiceItemResource {

    private final Logger log = LoggerFactory.getLogger(RelatedServiceItemResource.class);
        
    @Inject
    private RelatedServiceItemService relatedServiceItemService;
    
    @Inject
    private RelatedServiceItemMapper relatedServiceItemMapper;
    
    /**
     * POST  /related-service-items : Create a new relatedServiceItem.
     *
     * @param relatedServiceItemDTO the relatedServiceItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new relatedServiceItemDTO, or with status 400 (Bad Request) if the relatedServiceItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/related-service-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RelatedServiceItemDTO> createRelatedServiceItem(@RequestBody RelatedServiceItemDTO relatedServiceItemDTO) throws URISyntaxException {
        log.debug("REST request to save RelatedServiceItem : {}", relatedServiceItemDTO);
        if (relatedServiceItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("relatedServiceItem", "idexists", "A new relatedServiceItem cannot already have an ID")).body(null);
        }
        RelatedServiceItemDTO result = relatedServiceItemService.save(relatedServiceItemDTO);
        return ResponseEntity.created(new URI("/api/related-service-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("relatedServiceItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /related-service-items : Updates an existing relatedServiceItem.
     *
     * @param relatedServiceItemDTO the relatedServiceItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated relatedServiceItemDTO,
     * or with status 400 (Bad Request) if the relatedServiceItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the relatedServiceItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/related-service-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RelatedServiceItemDTO> updateRelatedServiceItem(@RequestBody RelatedServiceItemDTO relatedServiceItemDTO) throws URISyntaxException {
        log.debug("REST request to update RelatedServiceItem : {}", relatedServiceItemDTO);
        if (relatedServiceItemDTO.getId() == null) {
            return createRelatedServiceItem(relatedServiceItemDTO);
        }
        RelatedServiceItemDTO result = relatedServiceItemService.save(relatedServiceItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("relatedServiceItem", relatedServiceItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /related-service-items : get all the relatedServiceItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of relatedServiceItems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/related-service-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<RelatedServiceItemDTO>> getAllRelatedServiceItems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RelatedServiceItems");
        Page<RelatedServiceItem> page = relatedServiceItemService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/related-service-items");
        return new ResponseEntity<>(relatedServiceItemMapper.relatedServiceItemsToRelatedServiceItemDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /related-service-items/:id : get the "id" relatedServiceItem.
     *
     * @param id the id of the relatedServiceItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the relatedServiceItemDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/related-service-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RelatedServiceItemDTO> getRelatedServiceItem(@PathVariable Long id) {
        log.debug("REST request to get RelatedServiceItem : {}", id);
        RelatedServiceItemDTO relatedServiceItemDTO = relatedServiceItemService.findOne(id);
        return Optional.ofNullable(relatedServiceItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /related-service-items/:id : delete the "id" relatedServiceItem.
     *
     * @param id the id of the relatedServiceItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/related-service-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRelatedServiceItem(@PathVariable Long id) {
        log.debug("REST request to delete RelatedServiceItem : {}", id);
        relatedServiceItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("relatedServiceItem", id.toString())).build();
    }

    /**
     * SEARCH  /_search/related-service-items?query=:query : search for the relatedServiceItem corresponding
     * to the query.
     *
     * @param query the query of the relatedServiceItem search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/related-service-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<RelatedServiceItemDTO>> searchRelatedServiceItems(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of RelatedServiceItems for query {}", query);
        Page<RelatedServiceItem> page = relatedServiceItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/related-service-items");
        return new ResponseEntity<>(relatedServiceItemMapper.relatedServiceItemsToRelatedServiceItemDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
