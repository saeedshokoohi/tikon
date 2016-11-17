package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ServiceOptionItem;
import com.eyeson.tikon.service.ServiceOptionItemService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ServiceOptionItemDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceOptionItemMapper;
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
 * REST controller for managing ServiceOptionItem.
 */
@RestController
@RequestMapping("/api")
public class ServiceOptionItemResource {

    private final Logger log = LoggerFactory.getLogger(ServiceOptionItemResource.class);

    @Inject
    private ServiceOptionItemService serviceOptionItemService;

    @Inject
    private ServiceOptionItemMapper serviceOptionItemMapper;


    /**
     * POST  /service-option-items : Create a new serviceOptionItem.
     *
     * @param serviceOptionItemDTO the serviceOptionItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceOptionItemDTO, or with status 400 (Bad Request) if the serviceOptionItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-option-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceOptionItemDTO> createServiceOptionItem(@RequestBody ServiceOptionItemDTO serviceOptionItemDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceOptionItem : {}", serviceOptionItemDTO);
        if (serviceOptionItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceOptionItem", "idexists", "A new serviceOptionItem cannot already have an ID")).body(null);
        }
        ServiceOptionItemDTO result = serviceOptionItemService.save(serviceOptionItemDTO);
        return ResponseEntity.created(new URI("/api/service-option-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceOptionItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-option-items : Updates an existing serviceOptionItem.
     *
     * @param serviceOptionItemDTO the serviceOptionItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceOptionItemDTO,
     * or with status 400 (Bad Request) if the serviceOptionItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceOptionItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-option-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceOptionItemDTO> updateServiceOptionItem(@RequestBody ServiceOptionItemDTO serviceOptionItemDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceOptionItem : {}", serviceOptionItemDTO);
        if (serviceOptionItemDTO.getId() == null) {
            return createServiceOptionItem(serviceOptionItemDTO);
        }
        ServiceOptionItemDTO result = serviceOptionItemService.save(serviceOptionItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceOptionItem", serviceOptionItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-option-items : get all the serviceOptionItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceOptionItems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/service-option-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceOptionItemDTO>> getAllServiceOptionItems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ServiceOptionItems");
        Page<ServiceOptionItem> page = serviceOptionItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-option-items");
        return new ResponseEntity<>(serviceOptionItemMapper.serviceOptionItemsToServiceOptionItemDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /service-option-items/:id : get the "id" serviceOptionItem.
     *
     * @param id the id of the serviceOptionItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceOptionItemDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/service-option-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceOptionItemDTO> getServiceOptionItem(@PathVariable Long id) {
        log.debug("REST request to get ServiceOptionItem : {}", id);
        ServiceOptionItemDTO serviceOptionItemDTO = serviceOptionItemService.findOne(id);
        return Optional.ofNullable(serviceOptionItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-option-items/:id : delete the "id" serviceOptionItem.
     *
     * @param id the id of the serviceOptionItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/service-option-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServiceOptionItem(@PathVariable Long id) {
        log.debug("REST request to delete ServiceOptionItem : {}", id);
        serviceOptionItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceOptionItem", id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-option-items?query=:query : search for the serviceOptionItem corresponding
     * to the query.
     *
     * @param query the query of the serviceOptionItem search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/service-option-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceOptionItemDTO>> searchServiceOptionItems(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ServiceOptionItems for query {}", query);
        Page<ServiceOptionItem> page = serviceOptionItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/service-option-items");
        return new ResponseEntity<>(serviceOptionItemMapper.serviceOptionItemsToServiceOptionItemDTOs(page.getContent()), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/service-option-items-by-service-item/{serviceItemId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public List<ServiceOptionItemDTO> getServiceOptionItemsByServiceItem(@PathVariable("serviceItemId") String serviceItemId)
    {
        List<ServiceOptionItemDTO> retList=new ArrayList<>();
        List<ServiceOptionItem> pldInfoList = serviceOptionItemService.getServiceOptionItemsByServiceItem(Long.parseLong(serviceItemId));
        for(ServiceOptionItem sch:pldInfoList)
        {
            retList.add(serviceOptionItemMapper.serviceOptionItemToServiceOptionItemDTO(sch));
        }

        return  retList;
    }

}
