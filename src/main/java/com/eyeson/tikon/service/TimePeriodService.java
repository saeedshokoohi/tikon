package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.TimePeriod;
import com.eyeson.tikon.repository.TimePeriodRepository;
import com.eyeson.tikon.repository.search.TimePeriodSearchRepository;
import com.eyeson.tikon.web.rest.dto.TimePeriodDTO;
import com.eyeson.tikon.web.rest.mapper.TimePeriodMapper;
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
 * Service Implementation for managing TimePeriod.
 */
@Service
@Transactional
public class TimePeriodService {

    private final Logger log = LoggerFactory.getLogger(TimePeriodService.class);
    
    @Inject
    private TimePeriodRepository timePeriodRepository;
    
    @Inject
    private TimePeriodMapper timePeriodMapper;
    
    @Inject
    private TimePeriodSearchRepository timePeriodSearchRepository;
    
    /**
     * Save a timePeriod.
     * 
     * @param timePeriodDTO the entity to save
     * @return the persisted entity
     */
    public TimePeriodDTO save(TimePeriodDTO timePeriodDTO) {
        log.debug("Request to save TimePeriod : {}", timePeriodDTO);
        TimePeriod timePeriod = timePeriodMapper.timePeriodDTOToTimePeriod(timePeriodDTO);
        timePeriod = timePeriodRepository.save(timePeriod);
        TimePeriodDTO result = timePeriodMapper.timePeriodToTimePeriodDTO(timePeriod);
        timePeriodSearchRepository.save(timePeriod);
        return result;
    }

    /**
     *  Get all the timePeriods.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TimePeriod> findAll(Pageable pageable) {
        log.debug("Request to get all TimePeriods");
        Page<TimePeriod> result = timePeriodRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one timePeriod by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TimePeriodDTO findOne(Long id) {
        log.debug("Request to get TimePeriod : {}", id);
        TimePeriod timePeriod = timePeriodRepository.findOneWithEagerRelationships(id);
        TimePeriodDTO timePeriodDTO = timePeriodMapper.timePeriodToTimePeriodDTO(timePeriod);
        return timePeriodDTO;
    }

    /**
     *  Delete the  timePeriod by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TimePeriod : {}", id);
        timePeriodRepository.delete(id);
        timePeriodSearchRepository.delete(id);
    }

    /**
     * Search for the timePeriod corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TimePeriod> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TimePeriods for query {}", query);
        return timePeriodSearchRepository.search(queryStringQuery(query), pageable);
    }
}
