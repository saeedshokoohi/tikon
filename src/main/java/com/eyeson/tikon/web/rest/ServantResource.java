package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.Servant;
import com.eyeson.tikon.repository.ServantRepository;
import com.eyeson.tikon.service.ServantService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ServantDTO;
import com.eyeson.tikon.web.rest.mapper.ServantMapper;
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
 * REST controller for managing Servant.
 */
@RestController
@RequestMapping("/api")
public class ServantResource {

    private final Logger log = LoggerFactory.getLogger(ServantResource.class);

    @Inject
    private ServantService servantService;

    @Inject
    private ServantMapper servantMapper;

    /**
     * POST  /servants : Create a new servant.
     *
     * @param servantDTO the servantDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new servantDTO, or with status 400 (Bad Request) if the servant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/servants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServantDTO> createServant(@RequestBody ServantDTO servantDTO) throws URISyntaxException {
        log.debug("REST request to save Servant : {}", servantDTO);
        if (servantDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("servant", "idexists", "A new servant cannot already have an ID")).body(null);
        }
        ServantDTO result = servantService.save(servantDTO);
        return ResponseEntity.created(new URI("/api/servants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("servant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /servants : Updates an existing servant.
     *
     * @param servantDTO the servantDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated servantDTO,
     * or with status 400 (Bad Request) if the servantDTO is not valid,
     * or with status 500 (Internal Server Error) if the servantDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/servants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServantDTO> updateServant(@RequestBody ServantDTO servantDTO) throws URISyntaxException {
        log.debug("REST request to update Servant : {}", servantDTO);
        if (servantDTO.getId() == null) {
            return createServant(servantDTO);
        }
        ServantDTO result = servantService.save(servantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("servant", servantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /servants : get all the servants.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of servants in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/servants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServantDTO>> getAllServants(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Servants");
        Page<Servant> page = servantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/servants");
        return new ResponseEntity<>(servantMapper.servantsToServantDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /servants/:id : get the "id" servant.
     *
     * @param id the id of the servantDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the servantDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/servants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServantDTO> getServant(@PathVariable Long id) {
        log.debug("REST request to get Servant : {}", id);
        ServantDTO servantDTO = servantService.findOne(id);
        return Optional.ofNullable(servantDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /servants/:id : delete the "id" servant.
     *
     * @param id the id of the servantDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/servants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServant(@PathVariable Long id) {
        log.debug("REST request to delete Servant : {}", id);
        servantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("servant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/servants?query=:query : search for the servant corresponding
     * to the query.
     *
     * @param query the query of the servant search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/servants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ServantDTO>> searchServants(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Servants for query {}", query);
        Page<Servant> page = servantService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/servants");
        return new ResponseEntity<>(servantMapper.servantsToServantDTOs(page.getContent()), headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/servants-by-company",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ServantDTO> findByCompany()
    {
        List<ServantDTO> retList =new ArrayList<>();
        retList = servantService.findByCompanyId();
        return  retList;
    }


    @Inject
    private ServantRepository servantRepository;

    @RequestMapping(value = "/servants-by-service-item/{serviceItemId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<ServantDTO> findByServiceItemsId(@PathVariable("serviceItemId") String serviceItemId)
    {
        List<ServantDTO> retList =new ArrayList<>();
        retList = servantRepository.findByServiceItemsId(Long.parseLong(serviceItemId));
        return  retList;
    }

}
