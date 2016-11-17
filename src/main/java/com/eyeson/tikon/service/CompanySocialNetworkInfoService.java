package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.CompanySocialNetworkInfo;
import com.eyeson.tikon.repository.CompanySocialNetworkInfoRepository;
import com.eyeson.tikon.repository.search.CompanySocialNetworkInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.CompanySocialNetworkInfoDTO;
import com.eyeson.tikon.web.rest.mapper.CompanySocialNetworkInfoMapper;
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
 * Service Implementation for managing CompanySocialNetworkInfo.
 */
@Service
@Transactional
public class CompanySocialNetworkInfoService {

    private final Logger log = LoggerFactory.getLogger(CompanySocialNetworkInfoService.class);
    
    @Inject
    private CompanySocialNetworkInfoRepository companySocialNetworkInfoRepository;
    
    @Inject
    private CompanySocialNetworkInfoMapper companySocialNetworkInfoMapper;
    
    @Inject
    private CompanySocialNetworkInfoSearchRepository companySocialNetworkInfoSearchRepository;
    
    /**
     * Save a companySocialNetworkInfo.
     * 
     * @param companySocialNetworkInfoDTO the entity to save
     * @return the persisted entity
     */
    public CompanySocialNetworkInfoDTO save(CompanySocialNetworkInfoDTO companySocialNetworkInfoDTO) {
        log.debug("Request to save CompanySocialNetworkInfo : {}", companySocialNetworkInfoDTO);
        CompanySocialNetworkInfo companySocialNetworkInfo = companySocialNetworkInfoMapper.companySocialNetworkInfoDTOToCompanySocialNetworkInfo(companySocialNetworkInfoDTO);
        companySocialNetworkInfo = companySocialNetworkInfoRepository.save(companySocialNetworkInfo);
        CompanySocialNetworkInfoDTO result = companySocialNetworkInfoMapper.companySocialNetworkInfoToCompanySocialNetworkInfoDTO(companySocialNetworkInfo);
        companySocialNetworkInfoSearchRepository.save(companySocialNetworkInfo);
        return result;
    }

    /**
     *  Get all the companySocialNetworkInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CompanySocialNetworkInfo> findAll(Pageable pageable) {
        log.debug("Request to get all CompanySocialNetworkInfos");
        Page<CompanySocialNetworkInfo> result = companySocialNetworkInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one companySocialNetworkInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CompanySocialNetworkInfoDTO findOne(Long id) {
        log.debug("Request to get CompanySocialNetworkInfo : {}", id);
        CompanySocialNetworkInfo companySocialNetworkInfo = companySocialNetworkInfoRepository.findOne(id);
        CompanySocialNetworkInfoDTO companySocialNetworkInfoDTO = companySocialNetworkInfoMapper.companySocialNetworkInfoToCompanySocialNetworkInfoDTO(companySocialNetworkInfo);
        return companySocialNetworkInfoDTO;
    }

    /**
     *  Delete the  companySocialNetworkInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanySocialNetworkInfo : {}", id);
        companySocialNetworkInfoRepository.delete(id);
        companySocialNetworkInfoSearchRepository.delete(id);
    }

    /**
     * Search for the companySocialNetworkInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CompanySocialNetworkInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CompanySocialNetworkInfos for query {}", query);
        return companySocialNetworkInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
