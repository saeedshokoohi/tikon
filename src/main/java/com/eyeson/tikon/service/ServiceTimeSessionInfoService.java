package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.ServiceTimeSessionInfo;
import com.eyeson.tikon.repository.ServiceTimeSessionInfoRepository;
import com.eyeson.tikon.repository.search.ServiceTimeSessionInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServiceTimeSessionInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceTimeSessionInfoMapper;
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
 * Service Implementation for managing ServiceTimeSessionInfo.
 */
@Service
@Transactional
public class ServiceTimeSessionInfoService {

    private final Logger log = LoggerFactory.getLogger(ServiceTimeSessionInfoService.class);
    
    @Inject
    private ServiceTimeSessionInfoRepository serviceTimeSessionInfoRepository;
    
    @Inject
    private ServiceTimeSessionInfoMapper serviceTimeSessionInfoMapper;
    
    @Inject
    private ServiceTimeSessionInfoSearchRepository serviceTimeSessionInfoSearchRepository;
    
    /**
     * Save a serviceTimeSessionInfo.
     * 
     * @param serviceTimeSessionInfoDTO the entity to save
     * @return the persisted entity
     */
    public ServiceTimeSessionInfoDTO save(ServiceTimeSessionInfoDTO serviceTimeSessionInfoDTO) {
        log.debug("Request to save ServiceTimeSessionInfo : {}", serviceTimeSessionInfoDTO);
        ServiceTimeSessionInfo serviceTimeSessionInfo = serviceTimeSessionInfoMapper.serviceTimeSessionInfoDTOToServiceTimeSessionInfo(serviceTimeSessionInfoDTO);
        serviceTimeSessionInfo = serviceTimeSessionInfoRepository.save(serviceTimeSessionInfo);
        ServiceTimeSessionInfoDTO result = serviceTimeSessionInfoMapper.serviceTimeSessionInfoToServiceTimeSessionInfoDTO(serviceTimeSessionInfo);
        serviceTimeSessionInfoSearchRepository.save(serviceTimeSessionInfo);
        return result;
    }

    /**
     *  Get all the serviceTimeSessionInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ServiceTimeSessionInfo> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceTimeSessionInfos");
        Page<ServiceTimeSessionInfo> result = serviceTimeSessionInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one serviceTimeSessionInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ServiceTimeSessionInfoDTO findOne(Long id) {
        log.debug("Request to get ServiceTimeSessionInfo : {}", id);
        ServiceTimeSessionInfo serviceTimeSessionInfo = serviceTimeSessionInfoRepository.findOne(id);
        ServiceTimeSessionInfoDTO serviceTimeSessionInfoDTO = serviceTimeSessionInfoMapper.serviceTimeSessionInfoToServiceTimeSessionInfoDTO(serviceTimeSessionInfo);
        return serviceTimeSessionInfoDTO;
    }

    /**
     *  Delete the  serviceTimeSessionInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceTimeSessionInfo : {}", id);
        serviceTimeSessionInfoRepository.delete(id);
        serviceTimeSessionInfoSearchRepository.delete(id);
    }

    /**
     * Search for the serviceTimeSessionInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceTimeSessionInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceTimeSessionInfos for query {}", query);
        return serviceTimeSessionInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
