package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.ServiceTimeSession;
import com.eyeson.tikon.repository.ServiceTimeSessionRepository;
import com.eyeson.tikon.repository.search.ServiceTimeSessionSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServiceTimeSessionDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceTimeSessionMapper;
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
 * Service Implementation for managing ServiceTimeSession.
 */
@Service
@Transactional
public class ServiceTimeSessionService {

    private final Logger log = LoggerFactory.getLogger(ServiceTimeSessionService.class);
    
    @Inject
    private ServiceTimeSessionRepository serviceTimeSessionRepository;
    
    @Inject
    private ServiceTimeSessionMapper serviceTimeSessionMapper;
    
    @Inject
    private ServiceTimeSessionSearchRepository serviceTimeSessionSearchRepository;
    
    /**
     * Save a serviceTimeSession.
     * 
     * @param serviceTimeSessionDTO the entity to save
     * @return the persisted entity
     */
    public ServiceTimeSessionDTO save(ServiceTimeSessionDTO serviceTimeSessionDTO) {
        log.debug("Request to save ServiceTimeSession : {}", serviceTimeSessionDTO);
        ServiceTimeSession serviceTimeSession = serviceTimeSessionMapper.serviceTimeSessionDTOToServiceTimeSession(serviceTimeSessionDTO);
        serviceTimeSession = serviceTimeSessionRepository.save(serviceTimeSession);
        ServiceTimeSessionDTO result = serviceTimeSessionMapper.serviceTimeSessionToServiceTimeSessionDTO(serviceTimeSession);
        serviceTimeSessionSearchRepository.save(serviceTimeSession);
        return result;
    }

    /**
     *  Get all the serviceTimeSessions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ServiceTimeSession> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceTimeSessions");
        Page<ServiceTimeSession> result = serviceTimeSessionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one serviceTimeSession by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ServiceTimeSessionDTO findOne(Long id) {
        log.debug("Request to get ServiceTimeSession : {}", id);
        ServiceTimeSession serviceTimeSession = serviceTimeSessionRepository.findOne(id);
        ServiceTimeSessionDTO serviceTimeSessionDTO = serviceTimeSessionMapper.serviceTimeSessionToServiceTimeSessionDTO(serviceTimeSession);
        return serviceTimeSessionDTO;
    }

    /**
     *  Delete the  serviceTimeSession by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceTimeSession : {}", id);
        serviceTimeSessionRepository.delete(id);
        serviceTimeSessionSearchRepository.delete(id);
    }

    /**
     * Search for the serviceTimeSession corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceTimeSession> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceTimeSessions for query {}", query);
        return serviceTimeSessionSearchRepository.search(queryStringQuery(query), pageable);
    }
}
