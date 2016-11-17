package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.CapacityException;
import com.eyeson.tikon.repository.CapacityExceptionRepository;
import com.eyeson.tikon.repository.search.CapacityExceptionSearchRepository;
import com.eyeson.tikon.web.rest.dto.CapacityExceptionDTO;
import com.eyeson.tikon.web.rest.mapper.CapacityExceptionMapper;
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
 * Service Implementation for managing CapacityException.
 */
@Service
@Transactional
public class CapacityExceptionService {

    private final Logger log = LoggerFactory.getLogger(CapacityExceptionService.class);
    
    @Inject
    private CapacityExceptionRepository capacityExceptionRepository;
    
    @Inject
    private CapacityExceptionMapper capacityExceptionMapper;
    
    @Inject
    private CapacityExceptionSearchRepository capacityExceptionSearchRepository;
    
    /**
     * Save a capacityException.
     * 
     * @param capacityExceptionDTO the entity to save
     * @return the persisted entity
     */
    public CapacityExceptionDTO save(CapacityExceptionDTO capacityExceptionDTO) {
        log.debug("Request to save CapacityException : {}", capacityExceptionDTO);
        CapacityException capacityException = capacityExceptionMapper.capacityExceptionDTOToCapacityException(capacityExceptionDTO);
        capacityException = capacityExceptionRepository.save(capacityException);
        CapacityExceptionDTO result = capacityExceptionMapper.capacityExceptionToCapacityExceptionDTO(capacityException);
        capacityExceptionSearchRepository.save(capacityException);
        return result;
    }

    /**
     *  Get all the capacityExceptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CapacityException> findAll(Pageable pageable) {
        log.debug("Request to get all CapacityExceptions");
        Page<CapacityException> result = capacityExceptionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one capacityException by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CapacityExceptionDTO findOne(Long id) {
        log.debug("Request to get CapacityException : {}", id);
        CapacityException capacityException = capacityExceptionRepository.findOne(id);
        CapacityExceptionDTO capacityExceptionDTO = capacityExceptionMapper.capacityExceptionToCapacityExceptionDTO(capacityException);
        return capacityExceptionDTO;
    }

    /**
     *  Delete the  capacityException by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CapacityException : {}", id);
        capacityExceptionRepository.delete(id);
        capacityExceptionSearchRepository.delete(id);
    }

    /**
     * Search for the capacityException corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CapacityException> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CapacityExceptions for query {}", query);
        return capacityExceptionSearchRepository.search(queryStringQuery(query), pageable);
    }
}
