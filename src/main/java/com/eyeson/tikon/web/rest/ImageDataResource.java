package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ImageData;
import com.eyeson.tikon.service.ImageDataService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ImageDataDTO;
import com.eyeson.tikon.web.rest.mapper.ImageDataMapper;
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
 * REST controller for managing ImageData.
 */
@RestController
@RequestMapping("/api")
public class ImageDataResource {

    private final Logger log = LoggerFactory.getLogger(ImageDataResource.class);

    @Inject
    private ImageDataService imageDataService;

    @Inject
    private ImageDataMapper imageDataMapper;

    /**
     * POST  /image-data : Create a new imageData.
     *
     * @param imageDataDTO the imageDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new imageDataDTO, or with status 400 (Bad Request) if the imageData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/image-data",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageDataDTO> createImageData(@RequestBody ImageDataDTO imageDataDTO) throws URISyntaxException {
        log.debug("REST request to save ImageData : {}", imageDataDTO);
        if (imageDataDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("imageData", "idexists", "A new imageData cannot already have an ID")).body(null);
        }
        ImageDataDTO result = imageDataService.save(imageDataDTO);
        return ResponseEntity.created(new URI("/api/image-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("imageData", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /image-data : Updates an existing imageData.
     *
     * @param imageDataDTO the imageDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated imageDataDTO,
     * or with status 400 (Bad Request) if the imageDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the imageDataDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/image-data",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageDataDTO> updateImageData(@RequestBody ImageDataDTO imageDataDTO) throws URISyntaxException {
        log.debug("REST request to update ImageData : {}", imageDataDTO);
        if (imageDataDTO.getId() == null) {
            return createImageData(imageDataDTO);
        }
        ImageDataDTO result = imageDataService.save(imageDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("imageData", imageDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /image-data : get all the imageData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of imageData in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/image-data",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ImageDataDTO>> getAllImageData(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ImageData");
        Page<ImageData> page = imageDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/image-data");
        return new ResponseEntity<>(imageDataMapper.imageDataToImageDataDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /image-data/:id : get the "id" imageData.
     *
     * @param id the id of the imageDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the imageDataDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/image-data/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageDataDTO> getImageData(@PathVariable Long id) {
        log.debug("REST request to get ImageData : {}", id);
        ImageDataDTO imageDataDTO = imageDataService.findOne(id);
        return Optional.ofNullable(imageDataDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /image-data/:id : delete the "id" imageData.
     *
     * @param id the id of the imageDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/image-data/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteImageData(@PathVariable Long id) {
        log.debug("REST request to delete ImageData : {}", id);
        imageDataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("imageData", id.toString())).build();
    }

    /**
     * SEARCH  /_search/image-data?query=:query : search for the imageData corresponding
     * to the query.
     *
     * @param query the query of the imageData search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/image-data",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ImageDataDTO>> searchImageData(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ImageData for query {}", query);
        Page<ImageData> page = imageDataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/image-data");
        return new ResponseEntity<>(imageDataMapper.imageDataToImageDataDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     *  url : /image-data-by-albumid/{albumid}
     *  return the list of given albumid as parameter
     * @param albumid
     * @return
     */
    @RequestMapping(value = "/image-data-by-albumid/{albumid}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ImageDataDTO>  getImageDataByAlbumId(@PathVariable("albumid") Long albumid) {

        List<ImageDataDTO> list =imageDataMapper.imageDataToImageDataDTOs(imageDataService.findByAlbumId(albumid));
        return list;


    }

}
