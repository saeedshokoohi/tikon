package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.ScheduleBaseDiscount;
import com.eyeson.tikon.repository.ScheduleBaseDiscountRepository;
import com.eyeson.tikon.repository.search.ScheduleBaseDiscountSearchRepository;
import com.eyeson.tikon.web.rest.dto.ScheduleBaseDiscountDTO;
import com.eyeson.tikon.web.rest.mapper.ScheduleBaseDiscountMapper;
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
 * Service Implementation for managing ScheduleBaseDiscount.
 */
@Service
@Transactional
public class ScheduleBaseDiscountService {

    private final Logger log = LoggerFactory.getLogger(ScheduleBaseDiscountService.class);
    
    @Inject
    private ScheduleBaseDiscountRepository scheduleBaseDiscountRepository;
    
    @Inject
    private ScheduleBaseDiscountMapper scheduleBaseDiscountMapper;
    
    @Inject
    private ScheduleBaseDiscountSearchRepository scheduleBaseDiscountSearchRepository;
    
    /**
     * Save a scheduleBaseDiscount.
     * 
     * @param scheduleBaseDiscountDTO the entity to save
     * @return the persisted entity
     */
    public ScheduleBaseDiscountDTO save(ScheduleBaseDiscountDTO scheduleBaseDiscountDTO) {
        log.debug("Request to save ScheduleBaseDiscount : {}", scheduleBaseDiscountDTO);
        ScheduleBaseDiscount scheduleBaseDiscount = scheduleBaseDiscountMapper.scheduleBaseDiscountDTOToScheduleBaseDiscount(scheduleBaseDiscountDTO);
        scheduleBaseDiscount = scheduleBaseDiscountRepository.save(scheduleBaseDiscount);
        ScheduleBaseDiscountDTO result = scheduleBaseDiscountMapper.scheduleBaseDiscountToScheduleBaseDiscountDTO(scheduleBaseDiscount);
        scheduleBaseDiscountSearchRepository.save(scheduleBaseDiscount);
        return result;
    }

    /**
     *  Get all the scheduleBaseDiscounts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ScheduleBaseDiscount> findAll(Pageable pageable) {
        log.debug("Request to get all ScheduleBaseDiscounts");
        Page<ScheduleBaseDiscount> result = scheduleBaseDiscountRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one scheduleBaseDiscount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ScheduleBaseDiscountDTO findOne(Long id) {
        log.debug("Request to get ScheduleBaseDiscount : {}", id);
        ScheduleBaseDiscount scheduleBaseDiscount = scheduleBaseDiscountRepository.findOne(id);
        ScheduleBaseDiscountDTO scheduleBaseDiscountDTO = scheduleBaseDiscountMapper.scheduleBaseDiscountToScheduleBaseDiscountDTO(scheduleBaseDiscount);
        return scheduleBaseDiscountDTO;
    }

    /**
     *  Delete the  scheduleBaseDiscount by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ScheduleBaseDiscount : {}", id);
        scheduleBaseDiscountRepository.delete(id);
        scheduleBaseDiscountSearchRepository.delete(id);
    }

    /**
     * Search for the scheduleBaseDiscount corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ScheduleBaseDiscount> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ScheduleBaseDiscounts for query {}", query);
        return scheduleBaseDiscountSearchRepository.search(queryStringQuery(query), pageable);
    }
}
