package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.WeeklyScheduleInfo;
import com.eyeson.tikon.repository.WeeklyScheduleInfoRepository;
import com.eyeson.tikon.repository.search.WeeklyScheduleInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.WeeklyScheduleInfoDTO;
import com.eyeson.tikon.web.rest.mapper.WeeklyScheduleInfoMapper;
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
 * Service Implementation for managing WeeklyScheduleInfo.
 */
@Service
@Transactional
public class WeeklyScheduleInfoService {

    private final Logger log = LoggerFactory.getLogger(WeeklyScheduleInfoService.class);
    
    @Inject
    private WeeklyScheduleInfoRepository weeklyScheduleInfoRepository;
    
    @Inject
    private WeeklyScheduleInfoMapper weeklyScheduleInfoMapper;
    
    @Inject
    private WeeklyScheduleInfoSearchRepository weeklyScheduleInfoSearchRepository;
    
    /**
     * Save a weeklyScheduleInfo.
     * 
     * @param weeklyScheduleInfoDTO the entity to save
     * @return the persisted entity
     */
    public WeeklyScheduleInfoDTO save(WeeklyScheduleInfoDTO weeklyScheduleInfoDTO) {
        log.debug("Request to save WeeklyScheduleInfo : {}", weeklyScheduleInfoDTO);
        WeeklyScheduleInfo weeklyScheduleInfo = weeklyScheduleInfoMapper.weeklyScheduleInfoDTOToWeeklyScheduleInfo(weeklyScheduleInfoDTO);
        weeklyScheduleInfo = weeklyScheduleInfoRepository.save(weeklyScheduleInfo);
        WeeklyScheduleInfoDTO result = weeklyScheduleInfoMapper.weeklyScheduleInfoToWeeklyScheduleInfoDTO(weeklyScheduleInfo);
        weeklyScheduleInfoSearchRepository.save(weeklyScheduleInfo);
        return result;
    }

    /**
     *  Get all the weeklyScheduleInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<WeeklyScheduleInfo> findAll(Pageable pageable) {
        log.debug("Request to get all WeeklyScheduleInfos");
        Page<WeeklyScheduleInfo> result = weeklyScheduleInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one weeklyScheduleInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public WeeklyScheduleInfoDTO findOne(Long id) {
        log.debug("Request to get WeeklyScheduleInfo : {}", id);
        WeeklyScheduleInfo weeklyScheduleInfo = weeklyScheduleInfoRepository.findOneWithEagerRelationships(id);
        WeeklyScheduleInfoDTO weeklyScheduleInfoDTO = weeklyScheduleInfoMapper.weeklyScheduleInfoToWeeklyScheduleInfoDTO(weeklyScheduleInfo);
        return weeklyScheduleInfoDTO;
    }

    /**
     *  Delete the  weeklyScheduleInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WeeklyScheduleInfo : {}", id);
        weeklyScheduleInfoRepository.delete(id);
        weeklyScheduleInfoSearchRepository.delete(id);
    }

    /**
     * Search for the weeklyScheduleInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WeeklyScheduleInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WeeklyScheduleInfos for query {}", query);
        return weeklyScheduleInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
