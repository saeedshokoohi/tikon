package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.MetaTag;
import com.eyeson.tikon.service.MetaTagService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.MetaTagDTO;
import com.eyeson.tikon.web.rest.mapper.MetaTagMapper;
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
 * REST controller for managing MetaTag.
 */
@RestController
@RequestMapping("/api")
public class MetaTagResource {

    private final Logger log = LoggerFactory.getLogger(MetaTagResource.class);
        
    @Inject
    private MetaTagService metaTagService;
    
    @Inject
    private MetaTagMapper metaTagMapper;
    
    /**
     * POST  /meta-tags : Create a new metaTag.
     *
     * @param metaTagDTO the metaTagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new metaTagDTO, or with status 400 (Bad Request) if the metaTag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/meta-tags",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MetaTagDTO> createMetaTag(@RequestBody MetaTagDTO metaTagDTO) throws URISyntaxException {
        log.debug("REST request to save MetaTag : {}", metaTagDTO);
        if (metaTagDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("metaTag", "idexists", "A new metaTag cannot already have an ID")).body(null);
        }
        MetaTagDTO result = metaTagService.save(metaTagDTO);
        return ResponseEntity.created(new URI("/api/meta-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("metaTag", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /meta-tags : Updates an existing metaTag.
     *
     * @param metaTagDTO the metaTagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated metaTagDTO,
     * or with status 400 (Bad Request) if the metaTagDTO is not valid,
     * or with status 500 (Internal Server Error) if the metaTagDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/meta-tags",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MetaTagDTO> updateMetaTag(@RequestBody MetaTagDTO metaTagDTO) throws URISyntaxException {
        log.debug("REST request to update MetaTag : {}", metaTagDTO);
        if (metaTagDTO.getId() == null) {
            return createMetaTag(metaTagDTO);
        }
        MetaTagDTO result = metaTagService.save(metaTagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("metaTag", metaTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /meta-tags : get all the metaTags.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of metaTags in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/meta-tags",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MetaTagDTO>> getAllMetaTags(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MetaTags");
        Page<MetaTag> page = metaTagService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/meta-tags");
        return new ResponseEntity<>(metaTagMapper.metaTagsToMetaTagDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /meta-tags/:id : get the "id" metaTag.
     *
     * @param id the id of the metaTagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the metaTagDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/meta-tags/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MetaTagDTO> getMetaTag(@PathVariable Long id) {
        log.debug("REST request to get MetaTag : {}", id);
        MetaTagDTO metaTagDTO = metaTagService.findOne(id);
        return Optional.ofNullable(metaTagDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /meta-tags/:id : delete the "id" metaTag.
     *
     * @param id the id of the metaTagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/meta-tags/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMetaTag(@PathVariable Long id) {
        log.debug("REST request to delete MetaTag : {}", id);
        metaTagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("metaTag", id.toString())).build();
    }

    /**
     * SEARCH  /_search/meta-tags?query=:query : search for the metaTag corresponding
     * to the query.
     *
     * @param query the query of the metaTag search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/meta-tags",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<MetaTagDTO>> searchMetaTags(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of MetaTags for query {}", query);
        Page<MetaTag> page = metaTagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/meta-tags");
        return new ResponseEntity<>(metaTagMapper.metaTagsToMetaTagDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
