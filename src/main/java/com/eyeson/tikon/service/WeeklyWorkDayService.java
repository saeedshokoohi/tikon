package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.WeeklyWorkDay;
import com.eyeson.tikon.repository.WeeklyWorkDayRepository;
import com.eyeson.tikon.repository.search.WeeklyWorkDaySearchRepository;
import com.eyeson.tikon.web.rest.dto.WeeklyWorkDayDTO;
import com.eyeson.tikon.web.rest.mapper.WeeklyWorkDayMapper;
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
 * Service Implementation for managing WeeklyWorkDay.
 */
@Service
@Transactional
public class WeeklyWorkDayService {

    private final Logger log = LoggerFactory.getLogger(WeeklyWorkDayService.class);
    
    @Inject
    private WeeklyWorkDayRepository weeklyWorkDayRepository;
    
    @Inject
    private WeeklyWorkDayMapper weeklyWorkDayMapper;
    
    @Inject
    private WeeklyWorkDaySearchRepository weeklyWorkDaySearchRepository;
    
    /**
     * Save a weeklyWorkDay.
     * 
     * @param weeklyWorkDayDTO the entity to save
     * @return the persisted entity
     */
    public WeeklyWorkDayDTO save(WeeklyWorkDayDTO weeklyWorkDayDTO) {
        log.debug("Request to save WeeklyWorkDay : {}", weeklyWorkDayDTO);
        WeeklyWorkDay weeklyWorkDay = weeklyWorkDayMapper.weeklyWorkDayDTOToWeeklyWorkDay(weeklyWorkDayDTO);
        weeklyWorkDay = weeklyWorkDayRepository.save(weeklyWorkDay);
        WeeklyWorkDayDTO result = weeklyWorkDayMapper.weeklyWorkDayToWeeklyWorkDayDTO(weeklyWorkDay);
        weeklyWorkDaySearchRepository.save(weeklyWorkDay);
        return result;
    }

    /**
     *  Get all the weeklyWorkDays.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<WeeklyWorkDay> findAll(Pageable pageable) {
        log.debug("Request to get all WeeklyWorkDays");
        Page<WeeklyWorkDay> result = weeklyWorkDayRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one weeklyWorkDay by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public WeeklyWorkDayDTO findOne(Long id) {
        log.debug("Request to get WeeklyWorkDay : {}", id);
        WeeklyWorkDay weeklyWorkDay = weeklyWorkDayRepository.findOne(id);
        WeeklyWorkDayDTO weeklyWorkDayDTO = weeklyWorkDayMapper.weeklyWorkDayToWeeklyWorkDayDTO(weeklyWorkDay);
        return weeklyWorkDayDTO;
    }

    /**
     *  Delete the  weeklyWorkDay by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WeeklyWorkDay : {}", id);
        weeklyWorkDayRepository.delete(id);
        weeklyWorkDaySearchRepository.delete(id);
    }

    /**
     * Search for the weeklyWorkDay corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WeeklyWorkDay> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WeeklyWorkDays for query {}", query);
        return weeklyWorkDaySearchRepository.search(queryStringQuery(query), pageable);
    }
}
