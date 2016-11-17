package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.LocationInfo;
import com.eyeson.tikon.repository.LocationInfoRepository;
import com.eyeson.tikon.repository.search.LocationInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.LocationInfoDTO;
import com.eyeson.tikon.web.rest.mapper.LocationInfoMapper;
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
 * Service Implementation for managing LocationInfo.
 */
@Service
@Transactional
public class LocationInfoService {

    private final Logger log = LoggerFactory.getLogger(LocationInfoService.class);
    
    @Inject
    private LocationInfoRepository locationInfoRepository;
    
    @Inject
    private LocationInfoMapper locationInfoMapper;
    
    @Inject
    private LocationInfoSearchRepository locationInfoSearchRepository;
    
    /**
     * Save a locationInfo.
     * 
     * @param locationInfoDTO the entity to save
     * @return the persisted entity
     */
    public LocationInfoDTO save(LocationInfoDTO locationInfoDTO) {
        log.debug("Request to save LocationInfo : {}", locationInfoDTO);
        LocationInfo locationInfo = locationInfoMapper.locationInfoDTOToLocationInfo(locationInfoDTO);
        locationInfo = locationInfoRepository.save(locationInfo);
        LocationInfoDTO result = locationInfoMapper.locationInfoToLocationInfoDTO(locationInfo);
        locationInfoSearchRepository.save(locationInfo);
        return result;
    }

    /**
     *  Get all the locationInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<LocationInfo> findAll(Pageable pageable) {
        log.debug("Request to get all LocationInfos");
        Page<LocationInfo> result = locationInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one locationInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public LocationInfoDTO findOne(Long id) {
        log.debug("Request to get LocationInfo : {}", id);
        LocationInfo locationInfo = locationInfoRepository.findOne(id);
        LocationInfoDTO locationInfoDTO = locationInfoMapper.locationInfoToLocationInfoDTO(locationInfo);
        return locationInfoDTO;
    }

    /**
     *  Delete the  locationInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LocationInfo : {}", id);
        locationInfoRepository.delete(id);
        locationInfoSearchRepository.delete(id);
    }

    /**
     * Search for the locationInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LocationInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LocationInfos for query {}", query);
        return locationInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
