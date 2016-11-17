package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.ParticipantPerson;
import com.eyeson.tikon.repository.ParticipantPersonRepository;
import com.eyeson.tikon.repository.search.ParticipantPersonSearchRepository;
import com.eyeson.tikon.web.rest.dto.ParticipantPersonDTO;
import com.eyeson.tikon.web.rest.mapper.ParticipantPersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ParticipantPerson.
 */
@Service
@Transactional
public class ParticipantPersonService {

    private final Logger log = LoggerFactory.getLogger(ParticipantPersonService.class);
    
    @Inject
    private ParticipantPersonRepository participantPersonRepository;
    
    @Inject
    private ParticipantPersonMapper participantPersonMapper;
    
    @Inject
    private ParticipantPersonSearchRepository participantPersonSearchRepository;
    
    /**
     * Save a participantPerson.
     * 
     * @param participantPersonDTO the entity to save
     * @return the persisted entity
     */
    public ParticipantPersonDTO save(ParticipantPersonDTO participantPersonDTO) {
        log.debug("Request to save ParticipantPerson : {}", participantPersonDTO);
        ParticipantPerson participantPerson = participantPersonMapper.participantPersonDTOToParticipantPerson(participantPersonDTO);
        participantPerson = participantPersonRepository.save(participantPerson);
        ParticipantPersonDTO result = participantPersonMapper.participantPersonToParticipantPersonDTO(participantPerson);
        participantPersonSearchRepository.save(participantPerson);
        return result;
    }

    /**
     *  Get all the participantPeople.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ParticipantPerson> findAll(Pageable pageable) {
        log.debug("Request to get all ParticipantPeople");
        Page<ParticipantPerson> result = participantPersonRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one participantPerson by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ParticipantPersonDTO findOne(Long id) {
        log.debug("Request to get ParticipantPerson : {}", id);
        ParticipantPerson participantPerson = participantPersonRepository.findOne(id);
        ParticipantPersonDTO participantPersonDTO = participantPersonMapper.participantPersonToParticipantPersonDTO(participantPerson);
        return participantPersonDTO;
    }

    /**
     *  Delete the  participantPerson by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ParticipantPerson : {}", id);
        participantPersonRepository.delete(id);
        participantPersonSearchRepository.delete(id);
    }

    /**
     * Search for the participantPerson corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ParticipantPerson> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ParticipantPeople for query {}", query);
        return participantPersonSearchRepository.search(queryStringQuery(query), pageable);
    }
}
