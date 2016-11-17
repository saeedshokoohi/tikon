package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.DatePeriod;
import com.eyeson.tikon.repository.DatePeriodRepository;
import com.eyeson.tikon.repository.search.DatePeriodSearchRepository;
import com.eyeson.tikon.web.rest.dto.DatePeriodDTO;
import com.eyeson.tikon.web.rest.mapper.DatePeriodMapper;
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
 * Service Implementation for managing DatePeriod.
 */
@Service
@Transactional
public class DatePeriodService {

    private final Logger log = LoggerFactory.getLogger(DatePeriodService.class);
    
    @Inject
    private DatePeriodRepository datePeriodRepository;
    
    @Inject
    private DatePeriodMapper datePeriodMapper;
    
    @Inject
    private DatePeriodSearchRepository datePeriodSearchRepository;
    
    /**
     * Save a datePeriod.
     * 
     * @param datePeriodDTO the entity to save
     * @return the persisted entity
     */
    public DatePeriodDTO save(DatePeriodDTO datePeriodDTO) {
        log.debug("Request to save DatePeriod : {}", datePeriodDTO);
        DatePeriod datePeriod = datePeriodMapper.datePeriodDTOToDatePeriod(datePeriodDTO);
        datePeriod = datePeriodRepository.save(datePeriod);
        DatePeriodDTO result = datePeriodMapper.datePeriodToDatePeriodDTO(datePeriod);
        datePeriodSearchRepository.save(datePeriod);
        return result;
    }

    /**
     *  Get all the datePeriods.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DatePeriod> findAll(Pageable pageable) {
        log.debug("Request to get all DatePeriods");
        Page<DatePeriod> result = datePeriodRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one datePeriod by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DatePeriodDTO findOne(Long id) {
        log.debug("Request to get DatePeriod : {}", id);
        DatePeriod datePeriod = datePeriodRepository.findOneWithEagerRelationships(id);
        DatePeriodDTO datePeriodDTO = datePeriodMapper.datePeriodToDatePeriodDTO(datePeriod);
        return datePeriodDTO;
    }

    /**
     *  Delete the  datePeriod by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DatePeriod : {}", id);
        datePeriodRepository.delete(id);
        datePeriodSearchRepository.delete(id);
    }

    /**
     * Search for the datePeriod corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DatePeriod> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DatePeriods for query {}", query);
        return datePeriodSearchRepository.search(queryStringQuery(query), pageable);
    }
}
