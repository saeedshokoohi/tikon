package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ServiceItem;
import com.eyeson.tikon.service.ScheduleInfoService;
import com.eyeson.tikon.service.ServiceItemService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ServiceItemDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceItemMapper;
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
 * REST controller for managing ServiceItem.
 */
@RestController
@RequestMapping("/api")
public class ServiceItemResource {

    private final Logger log = LoggerFactory.getLogger(ServiceItemResource.class);

    @Inject
    private ServiceItemService serviceItemService;

    @Inject
    private ServiceItemMapper serviceItemMapper;
    @Inject
    private ScheduleInfoService scheduleInfoService;

    /**
     * POST  /service-items : Create a new serviceItem.
     *
     * @param serviceItemDTO the serviceItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceItemDTO, or with status 400 (Bad Request) if the serviceItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceItemDTO> createServiceItem(@RequestBody ServiceItemDTO serviceItemDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceItem : {}", serviceItemDTO);
        if (serviceItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceItem", "idexists", "A new serviceItem cannot already have an ID")).body(null);
        }
        ServiceItemDTO result = serviceItemService.saveWithRelatedEntities(serviceItemDTO);
        return ResponseEntity.created(new URI("/api/service-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-items : Updates an existing serviceItem.
     *
     * @param serviceItemDTO the serviceItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceItemDTO,
     * or with status 400 (Bad Request) if the serviceItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceItemDTO> updateServiceItem(@RequestBody ServiceItemDTO serviceItemDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceItem : {}", serviceItemDTO);
        if (serviceItemDTO.getId() == null) {
            return createServiceItem(serviceItemDTO);
        }
        ServiceItemDTO result = serviceItemService.saveWithRelatedEntities(serviceItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceItem", serviceItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-items : get all the serviceItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceItems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/service-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceItemDTO>> getAllServiceItems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ServiceItems");
        Page<ServiceItem> page = serviceItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-items");
        return new ResponseEntity<>(serviceItemMapper.serviceItemsToServiceItemDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /service-items/:id : get the "id" serviceItem.
     *
     * @param id the id of the serviceItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceItemDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/service-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceItemDTO> getServiceItem(@PathVariable Long id) {
        log.debug("REST request to get ServiceItem : {}", id);
        ServiceItemDTO serviceItemDTO = serviceItemService.findOne(id);
        serviceItemDTO.getScheduleInfo().setTimingInfo(scheduleInfoService.fillCurrentTimingInfo(serviceItemDTO.getScheduleInfo()));
        return Optional.ofNullable(serviceItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-items/:id : delete the "id" serviceItem.
     *
     * @param id the id of the serviceItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/service-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServiceItem(@PathVariable Long id) {
        log.debug("REST request to delete ServiceItem : {}", id);
        serviceItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceItem", id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-items?query=:query : search for the serviceItem corresponding
     * to the query.
     *
     * @param query the query of the serviceItem search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/service-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceItemDTO>> searchServiceItems(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ServiceItems for query {}", query);
        Page<ServiceItem> page = serviceItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/service-items");
        return new ResponseEntity<>(serviceItemMapper.serviceItemsToServiceItemDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/service-items-by-company",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ServiceItemDTO> getServiceItemsByCurrentCompany()
    {
        List<ServiceItemDTO> retList =new ArrayList<>();
        retList = serviceItemMapper.serviceItemsToServiceItemDTOs(serviceItemService.getServiceItemsByCurrentCompany());
        return  retList;
    }

}
