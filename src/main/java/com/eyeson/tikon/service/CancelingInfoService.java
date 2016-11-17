package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.CancelingInfo;
import com.eyeson.tikon.repository.CancelingInfoRepository;
import com.eyeson.tikon.repository.search.CancelingInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.CancelingInfoDTO;
import com.eyeson.tikon.web.rest.mapper.CancelingInfoMapper;
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
 * Service Implementation for managing CancelingInfo.
 */
@Service
@Transactional
public class CancelingInfoService {

    private final Logger log = LoggerFactory.getLogger(CancelingInfoService.class);
    
    @Inject
    private CancelingInfoRepository cancelingInfoRepository;
    
    @Inject
    private CancelingInfoMapper cancelingInfoMapper;
    
    @Inject
    private CancelingInfoSearchRepository cancelingInfoSearchRepository;
    
    /**
     * Save a cancelingInfo.
     * 
     * @param cancelingInfoDTO the entity to save
     * @return the persisted entity
     */
    public CancelingInfoDTO save(CancelingInfoDTO cancelingInfoDTO) {
        log.debug("Request to save CancelingInfo : {}", cancelingInfoDTO);
        CancelingInfo cancelingInfo = cancelingInfoMapper.cancelingInfoDTOToCancelingInfo(cancelingInfoDTO);
        cancelingInfo = cancelingInfoRepository.save(cancelingInfo);
        CancelingInfoDTO result = cancelingInfoMapper.cancelingInfoToCancelingInfoDTO(cancelingInfo);
        cancelingInfoSearchRepository.save(cancelingInfo);
        return result;
    }

    /**
     *  Get all the cancelingInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CancelingInfo> findAll(Pageable pageable) {
        log.debug("Request to get all CancelingInfos");
        Page<CancelingInfo> result = cancelingInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one cancelingInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CancelingInfoDTO findOne(Long id) {
        log.debug("Request to get CancelingInfo : {}", id);
        CancelingInfo cancelingInfo = cancelingInfoRepository.findOne(id);
        CancelingInfoDTO cancelingInfoDTO = cancelingInfoMapper.cancelingInfoToCancelingInfoDTO(cancelingInfo);
        return cancelingInfoDTO;
    }

    /**
     *  Delete the  cancelingInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CancelingInfo : {}", id);
        cancelingInfoRepository.delete(id);
        cancelingInfoSearchRepository.delete(id);
    }

    /**
     * Search for the cancelingInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CancelingInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CancelingInfos for query {}", query);
        return cancelingInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
