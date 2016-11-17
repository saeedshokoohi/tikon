package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.PersonInfo;
import com.eyeson.tikon.service.PersonInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.PersonInfoDTO;
import com.eyeson.tikon.web.rest.mapper.PersonInfoMapper;
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
 * REST controller for managing PersonInfo.
 */
@RestController
@RequestMapping("/api")
public class PersonInfoResource {

    private final Logger log = LoggerFactory.getLogger(PersonInfoResource.class);

    @Inject
    private PersonInfoService personInfoService;

    @Inject
    private PersonInfoMapper personInfoMapper;

    /**
     * POST  /person-infos : Create a new personInfo.
     *
     * @param personInfoDTO the personInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personInfoDTO, or with status 400 (Bad Request) if the personInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/person-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonInfoDTO> createPersonInfo(@RequestBody PersonInfoDTO personInfoDTO) throws URISyntaxException {
        log.debug("REST request to save PersonInfo : {}", personInfoDTO);
        if (personInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("personInfo", "idexists", "A new personInfo cannot already have an ID")).body(null);
        }
        PersonInfoDTO result = personInfoService.save(personInfoDTO);
        return ResponseEntity.created(new URI("/api/person-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("personInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /person-infos : Updates an existing personInfo.
     *
     * @param personInfoDTO the personInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personInfoDTO,
     * or with status 400 (Bad Request) if the personInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the personInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/person-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonInfoDTO> updatePersonInfo(@RequestBody PersonInfoDTO personInfoDTO) throws URISyntaxException {
        log.debug("REST request to update PersonInfo : {}", personInfoDTO);
        if (personInfoDTO.getId() == null) {
            return createPersonInfo(personInfoDTO);
        }
        PersonInfoDTO result = personInfoService.save(personInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("personInfo", personInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /person-infos : get all the personInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of personInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/person-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PersonInfoDTO>> getAllPersonInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PersonInfos");
        Page<PersonInfo> page = personInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/person-infos");
        return new ResponseEntity<>(personInfoMapper.personInfosToPersonInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /person-infos/:id : get the "id" personInfo.
     *
     * @param id the id of the personInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/person-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonInfoDTO> getPersonInfo(@PathVariable Long id) {
        log.debug("REST request to get PersonInfo : {}", id);
        PersonInfoDTO personInfoDTO = personInfoService.findOne(id);
        return Optional.ofNullable(personInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /person-infos/:id : delete the "id" personInfo.
     *
     * @param id the id of the personInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/person-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePersonInfo(@PathVariable Long id) {
        log.debug("REST request to delete PersonInfo : {}", id);
        personInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/person-infos?query=:query : search for the personInfo corresponding
     * to the query.
     *
     * @param query the query of the personInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/person-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PersonInfoDTO>> searchPersonInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of PersonInfos for query {}", query);
        Page<PersonInfo> page = personInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/person-infos");
        return new ResponseEntity<>(personInfoMapper.personInfosToPersonInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/getCurrentPersonInfo",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public PersonInfoDTO getCurrentPersonInfo()
    {
        PersonInfoDTO PersonInfoDTO = personInfoMapper.personInfoToPersonInfoDTO(personInfoService.getCurrentPersonInfo());
        return PersonInfoDTO;
    }



}
