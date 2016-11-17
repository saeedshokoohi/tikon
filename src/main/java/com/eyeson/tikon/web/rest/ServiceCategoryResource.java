package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ServiceCategory;
import com.eyeson.tikon.service.ServiceCategoryService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ServiceCategoryDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceCategoryMapper;
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
 * REST controller for managing ServiceCategory.
 */
@RestController
@RequestMapping("/api")
public class ServiceCategoryResource {

    private final Logger log = LoggerFactory.getLogger(ServiceCategoryResource.class);

    @Inject
    private ServiceCategoryService serviceCategoryService;

    @Inject
    private ServiceCategoryMapper serviceCategoryMapper;

    /**
     * POST  /service-categories : Create a new serviceCategory.
     *
     * @param serviceCategoryDTO the serviceCategoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serviceCategoryDTO, or with status 400 (Bad Request) if the serviceCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-categories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceCategoryDTO> createServiceCategory(@RequestBody ServiceCategoryDTO serviceCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save ServiceCategory : {}", serviceCategoryDTO);
        if (serviceCategoryDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceCategory", "idexists", "A new serviceCategory cannot already have an ID")).body(null);
        }
        ServiceCategoryDTO result = serviceCategoryService.save(serviceCategoryDTO);
        return ResponseEntity.created(new URI("/api/service-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceCategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-categories : Updates an existing serviceCategory.
     *
     * @param serviceCategoryDTO the serviceCategoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serviceCategoryDTO,
     * or with status 400 (Bad Request) if the serviceCategoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the serviceCategoryDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/service-categories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceCategoryDTO> updateServiceCategory(@RequestBody ServiceCategoryDTO serviceCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update ServiceCategory : {}", serviceCategoryDTO);
        if (serviceCategoryDTO.getId() == null) {
            return createServiceCategory(serviceCategoryDTO);
        }
        ServiceCategoryDTO result = serviceCategoryService.save(serviceCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceCategory", serviceCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /service-categories : get all the serviceCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of serviceCategories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/service-categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceCategoryDTO>> getAllServiceCategories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ServiceCategories");
        Page<ServiceCategory> page = serviceCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-categories");
        return new ResponseEntity<>(serviceCategoryMapper.serviceCategoriesToServiceCategoryDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /service-categories/:id : get the "id" serviceCategory.
     *
     * @param id the id of the serviceCategoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serviceCategoryDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/service-categories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceCategoryDTO> getServiceCategory(@PathVariable Long id) {
        log.debug("REST request to get ServiceCategory : {}", id);
        ServiceCategoryDTO serviceCategoryDTO = serviceCategoryService.findOne(id);
        return Optional.ofNullable(serviceCategoryDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-categories/:id : delete the "id" serviceCategory.
     *
     * @param id the id of the serviceCategoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/service-categories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServiceCategory(@PathVariable Long id) {
        log.debug("REST request to delete ServiceCategory : {}", id);
        serviceCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceCategory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/service-categories?query=:query : search for the serviceCategory corresponding
     * to the query.
     *
     * @param query the query of the serviceCategory search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/service-categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServiceCategoryDTO>> searchServiceCategories(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ServiceCategories for query {}", query);
        Page<ServiceCategory> page = serviceCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/service-categories");
        return new ResponseEntity<>(serviceCategoryMapper.serviceCategoriesToServiceCategoryDTOs(page.getContent()), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/service-categories-by-current-company",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ServiceCategoryDTO> getServiceCategoriesByCurrentCompany()
    {
        List<ServiceCategoryDTO> retList =new ArrayList<>();
        retList = serviceCategoryMapper.serviceCategoriesToServiceCategoryDTOs(serviceCategoryService.getServiceCategoriesByCurrentCompany());
        return  retList;
    }

}
