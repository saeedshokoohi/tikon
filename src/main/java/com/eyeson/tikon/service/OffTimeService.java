package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.OffTime;
import com.eyeson.tikon.repository.OffTimeRepository;
import com.eyeson.tikon.repository.search.OffTimeSearchRepository;
import com.eyeson.tikon.web.rest.dto.OffTimeDTO;
import com.eyeson.tikon.web.rest.mapper.OffTimeMapper;
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
 * Service Implementation for managing OffTime.
 */
@Service
@Transactional
public class OffTimeService {

    private final Logger log = LoggerFactory.getLogger(OffTimeService.class);
    
    @Inject
    private OffTimeRepository offTimeRepository;
    
    @Inject
    private OffTimeMapper offTimeMapper;
    
    @Inject
    private OffTimeSearchRepository offTimeSearchRepository;
    
    /**
     * Save a offTime.
     * 
     * @param offTimeDTO the entity to save
     * @return the persisted entity
     */
    public OffTimeDTO save(OffTimeDTO offTimeDTO) {
        log.debug("Request to save OffTime : {}", offTimeDTO);
        OffTime offTime = offTimeMapper.offTimeDTOToOffTime(offTimeDTO);
        offTime = offTimeRepository.save(offTime);
        OffTimeDTO result = offTimeMapper.offTimeToOffTimeDTO(offTime);
        offTimeSearchRepository.save(offTime);
        return result;
    }

    /**
     *  Get all the offTimes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<OffTime> findAll(Pageable pageable) {
        log.debug("Request to get all OffTimes");
        Page<OffTime> result = offTimeRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one offTime by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OffTimeDTO findOne(Long id) {
        log.debug("Request to get OffTime : {}", id);
        OffTime offTime = offTimeRepository.findOne(id);
        OffTimeDTO offTimeDTO = offTimeMapper.offTimeToOffTimeDTO(offTime);
        return offTimeDTO;
    }

    /**
     *  Delete the  offTime by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OffTime : {}", id);
        offTimeRepository.delete(id);
        offTimeSearchRepository.delete(id);
    }

    /**
     * Search for the offTime corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OffTime> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OffTimes for query {}", query);
        return offTimeSearchRepository.search(queryStringQuery(query), pageable);
    }
}
