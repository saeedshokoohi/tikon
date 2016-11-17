package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ParticipantPerson;
import com.eyeson.tikon.service.ParticipantPersonService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ParticipantPersonDTO;
import com.eyeson.tikon.web.rest.mapper.ParticipantPersonMapper;
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
 * REST controller for managing ParticipantPerson.
 */
@RestController
@RequestMapping("/api")
public class ParticipantPersonResource {

    private final Logger log = LoggerFactory.getLogger(ParticipantPersonResource.class);
        
    @Inject
    private ParticipantPersonService participantPersonService;
    
    @Inject
    private ParticipantPersonMapper participantPersonMapper;
    
    /**
     * POST  /participant-people : Create a new participantPerson.
     *
     * @param participantPersonDTO the participantPersonDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new participantPersonDTO, or with status 400 (Bad Request) if the participantPerson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/participant-people",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParticipantPersonDTO> createParticipantPerson(@RequestBody ParticipantPersonDTO participantPersonDTO) throws URISyntaxException {
        log.debug("REST request to save ParticipantPerson : {}", participantPersonDTO);
        if (participantPersonDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("participantPerson", "idexists", "A new participantPerson cannot already have an ID")).body(null);
        }
        ParticipantPersonDTO result = participantPersonService.save(participantPersonDTO);
        return ResponseEntity.created(new URI("/api/participant-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("participantPerson", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /participant-people : Updates an existing participantPerson.
     *
     * @param participantPersonDTO the participantPersonDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated participantPersonDTO,
     * or with status 400 (Bad Request) if the participantPersonDTO is not valid,
     * or with status 500 (Internal Server Error) if the participantPersonDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/participant-people",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParticipantPersonDTO> updateParticipantPerson(@RequestBody ParticipantPersonDTO participantPersonDTO) throws URISyntaxException {
        log.debug("REST request to update ParticipantPerson : {}", participantPersonDTO);
        if (participantPersonDTO.getId() == null) {
            return createParticipantPerson(participantPersonDTO);
        }
        ParticipantPersonDTO result = participantPersonService.save(participantPersonDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("participantPerson", participantPersonDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /participant-people : get all the participantPeople.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of participantPeople in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/participant-people",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ParticipantPersonDTO>> getAllParticipantPeople(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ParticipantPeople");
        Page<ParticipantPerson> page = participantPersonService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/participant-people");
        return new ResponseEntity<>(participantPersonMapper.participantPeopleToParticipantPersonDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /participant-people/:id : get the "id" participantPerson.
     *
     * @param id the id of the participantPersonDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the participantPersonDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/participant-people/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ParticipantPersonDTO> getParticipantPerson(@PathVariable Long id) {
        log.debug("REST request to get ParticipantPerson : {}", id);
        ParticipantPersonDTO participantPersonDTO = participantPersonService.findOne(id);
        return Optional.ofNullable(participantPersonDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /participant-people/:id : delete the "id" participantPerson.
     *
     * @param id the id of the participantPersonDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/participant-people/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteParticipantPerson(@PathVariable Long id) {
        log.debug("REST request to delete ParticipantPerson : {}", id);
        participantPersonService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("participantPerson", id.toString())).build();
    }

    /**
     * SEARCH  /_search/participant-people?query=:query : search for the participantPerson corresponding
     * to the query.
     *
     * @param query the query of the participantPerson search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/participant-people",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ParticipantPersonDTO>> searchParticipantPeople(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ParticipantPeople for query {}", query);
        Page<ParticipantPerson> page = participantPersonService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/participant-people");
        return new ResponseEntity<>(participantPersonMapper.participantPeopleToParticipantPersonDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
