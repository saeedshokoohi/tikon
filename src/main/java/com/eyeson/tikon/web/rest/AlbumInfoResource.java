package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.AlbumInfo;
import com.eyeson.tikon.service.AlbumInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.AlbumInfoDTO;
import com.eyeson.tikon.web.rest.mapper.AlbumInfoMapper;
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
 * REST controller for managing AlbumInfo.
 */
@RestController
@RequestMapping("/api")
public class AlbumInfoResource {

    private final Logger log = LoggerFactory.getLogger(AlbumInfoResource.class);
        
    @Inject
    private AlbumInfoService albumInfoService;
    
    @Inject
    private AlbumInfoMapper albumInfoMapper;
    
    /**
     * POST  /album-infos : Create a new albumInfo.
     *
     * @param albumInfoDTO the albumInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new albumInfoDTO, or with status 400 (Bad Request) if the albumInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/album-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AlbumInfoDTO> createAlbumInfo(@RequestBody AlbumInfoDTO albumInfoDTO) throws URISyntaxException {
        log.debug("REST request to save AlbumInfo : {}", albumInfoDTO);
        if (albumInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("albumInfo", "idexists", "A new albumInfo cannot already have an ID")).body(null);
        }
        AlbumInfoDTO result = albumInfoService.save(albumInfoDTO);
        return ResponseEntity.created(new URI("/api/album-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("albumInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /album-infos : Updates an existing albumInfo.
     *
     * @param albumInfoDTO the albumInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated albumInfoDTO,
     * or with status 400 (Bad Request) if the albumInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the albumInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/album-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AlbumInfoDTO> updateAlbumInfo(@RequestBody AlbumInfoDTO albumInfoDTO) throws URISyntaxException {
        log.debug("REST request to update AlbumInfo : {}", albumInfoDTO);
        if (albumInfoDTO.getId() == null) {
            return createAlbumInfo(albumInfoDTO);
        }
        AlbumInfoDTO result = albumInfoService.save(albumInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("albumInfo", albumInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /album-infos : get all the albumInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of albumInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/album-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AlbumInfoDTO>> getAllAlbumInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AlbumInfos");
        Page<AlbumInfo> page = albumInfoService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/album-infos");
        return new ResponseEntity<>(albumInfoMapper.albumInfosToAlbumInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /album-infos/:id : get the "id" albumInfo.
     *
     * @param id the id of the albumInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the albumInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/album-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AlbumInfoDTO> getAlbumInfo(@PathVariable Long id) {
        log.debug("REST request to get AlbumInfo : {}", id);
        AlbumInfoDTO albumInfoDTO = albumInfoService.findOne(id);
        return Optional.ofNullable(albumInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /album-infos/:id : delete the "id" albumInfo.
     *
     * @param id the id of the albumInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/album-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAlbumInfo(@PathVariable Long id) {
        log.debug("REST request to delete AlbumInfo : {}", id);
        albumInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("albumInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/album-infos?query=:query : search for the albumInfo corresponding
     * to the query.
     *
     * @param query the query of the albumInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/album-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AlbumInfoDTO>> searchAlbumInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AlbumInfos for query {}", query);
        Page<AlbumInfo> page = albumInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/album-infos");
        return new ResponseEntity<>(albumInfoMapper.albumInfosToAlbumInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
