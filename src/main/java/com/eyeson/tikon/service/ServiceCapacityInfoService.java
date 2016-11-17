package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.ServiceCapacityInfo;
import com.eyeson.tikon.repository.ServiceCapacityInfoRepository;
import com.eyeson.tikon.repository.extended.ServiceCapacityInfoExtendedRepository;
import com.eyeson.tikon.repository.search.ServiceCapacityInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServiceCapacityInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceCapacityInfoMapper;
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
 * Service Implementation for managing ServiceCapacityInfo.
 */
@Service
@Transactional
public class ServiceCapacityInfoService {

    private final Logger log = LoggerFactory.getLogger(ServiceCapacityInfoService.class);

    @Inject
    private ServiceCapacityInfoRepository serviceCapacityInfoRepository;

    @Inject
    private ServiceCapacityInfoMapper serviceCapacityInfoMapper;

    @Inject
    private ServiceCapacityInfoSearchRepository serviceCapacityInfoSearchRepository;

    /**
     * Save a serviceCapacityInfo.
     *
     * @param serviceCapacityInfoDTO the entity to save
     * @return the persisted entity
     */
    public ServiceCapacityInfoDTO save(ServiceCapacityInfoDTO serviceCapacityInfoDTO) {
        log.debug("Request to save ServiceCapacityInfo : {}", serviceCapacityInfoDTO);
        ServiceCapacityInfo serviceCapacityInfo = serviceCapacityInfoMapper.serviceCapacityInfoDTOToServiceCapacityInfo(serviceCapacityInfoDTO);
        serviceCapacityInfo = serviceCapacityInfoRepository.save(serviceCapacityInfo);
        ServiceCapacityInfoDTO result = serviceCapacityInfoMapper.serviceCapacityInfoToServiceCapacityInfoDTO(serviceCapacityInfo);
        serviceCapacityInfoSearchRepository.save(serviceCapacityInfo);
        return result;
    }

    /**
     *  Get all the serviceCapacityInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceCapacityInfo> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceCapacityInfos");
        Page<ServiceCapacityInfo> result = serviceCapacityInfoRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one serviceCapacityInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ServiceCapacityInfoDTO findOne(Long id) {
        log.debug("Request to get ServiceCapacityInfo : {}", id);
        ServiceCapacityInfo serviceCapacityInfo = serviceCapacityInfoRepository.findOne(id);
        ServiceCapacityInfoDTO serviceCapacityInfoDTO = serviceCapacityInfoMapper.serviceCapacityInfoToServiceCapacityInfoDTO(serviceCapacityInfo);
        return serviceCapacityInfoDTO;
    }

    /**
     *  Delete the  serviceCapacityInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceCapacityInfo : {}", id);
        serviceCapacityInfoRepository.delete(id);
        serviceCapacityInfoSearchRepository.delete(id);
    }

    /**
     * Search for the serviceCapacityInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceCapacityInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceCapacityInfos for query {}", query);
        return serviceCapacityInfoSearchRepository.search(queryStringQuery(query), pageable);
    }


    @Inject
    ServiceCapacityInfoExtendedRepository serviceCapacityInfoExtendedRepository;
    public ServiceCapacityInfo getServiceCapacityInfoByServiceItem(Long serviceItemId) {
        return serviceCapacityInfoExtendedRepository.getServiceCapacityInfoByServiceItem(serviceItemId) ;
    }
}
