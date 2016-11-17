package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.ServiceOptionInfo;
import com.eyeson.tikon.repository.ServiceOptionInfoRepository;
import com.eyeson.tikon.repository.search.ServiceOptionInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServiceOptionInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceOptionInfoMapper;
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
 * Service Implementation for managing ServiceOptionInfo.
 */
@Service
@Transactional
public class ServiceOptionInfoService {

    private final Logger log = LoggerFactory.getLogger(ServiceOptionInfoService.class);
    
    @Inject
    private ServiceOptionInfoRepository serviceOptionInfoRepository;
    
    @Inject
    private ServiceOptionInfoMapper serviceOptionInfoMapper;
    
    @Inject
    private ServiceOptionInfoSearchRepository serviceOptionInfoSearchRepository;
    
    /**
     * Save a serviceOptionInfo.
     * 
     * @param serviceOptionInfoDTO the entity to save
     * @return the persisted entity
     */
    public ServiceOptionInfoDTO save(ServiceOptionInfoDTO serviceOptionInfoDTO) {
        log.debug("Request to save ServiceOptionInfo : {}", serviceOptionInfoDTO);
        ServiceOptionInfo serviceOptionInfo = serviceOptionInfoMapper.serviceOptionInfoDTOToServiceOptionInfo(serviceOptionInfoDTO);
        serviceOptionInfo = serviceOptionInfoRepository.save(serviceOptionInfo);
        ServiceOptionInfoDTO result = serviceOptionInfoMapper.serviceOptionInfoToServiceOptionInfoDTO(serviceOptionInfo);
        serviceOptionInfoSearchRepository.save(serviceOptionInfo);
        return result;
    }

    /**
     *  Get all the serviceOptionInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ServiceOptionInfo> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceOptionInfos");
        Page<ServiceOptionInfo> result = serviceOptionInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one serviceOptionInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ServiceOptionInfoDTO findOne(Long id) {
        log.debug("Request to get ServiceOptionInfo : {}", id);
        ServiceOptionInfo serviceOptionInfo = serviceOptionInfoRepository.findOne(id);
        ServiceOptionInfoDTO serviceOptionInfoDTO = serviceOptionInfoMapper.serviceOptionInfoToServiceOptionInfoDTO(serviceOptionInfo);
        return serviceOptionInfoDTO;
    }

    /**
     *  Delete the  serviceOptionInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceOptionInfo : {}", id);
        serviceOptionInfoRepository.delete(id);
        serviceOptionInfoSearchRepository.delete(id);
    }

    /**
     * Search for the serviceOptionInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceOptionInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceOptionInfos for query {}", query);
        return serviceOptionInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
