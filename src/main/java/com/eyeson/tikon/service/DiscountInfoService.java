package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.DiscountInfo;
import com.eyeson.tikon.repository.DiscountInfoRepository;
import com.eyeson.tikon.repository.search.DiscountInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.DiscountInfoDTO;
import com.eyeson.tikon.web.rest.mapper.DiscountInfoMapper;
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
 * Service Implementation for managing DiscountInfo.
 */
@Service
@Transactional
public class DiscountInfoService {

    private final Logger log = LoggerFactory.getLogger(DiscountInfoService.class);
    
    @Inject
    private DiscountInfoRepository discountInfoRepository;
    
    @Inject
    private DiscountInfoMapper discountInfoMapper;
    
    @Inject
    private DiscountInfoSearchRepository discountInfoSearchRepository;
    
    /**
     * Save a discountInfo.
     * 
     * @param discountInfoDTO the entity to save
     * @return the persisted entity
     */
    public DiscountInfoDTO save(DiscountInfoDTO discountInfoDTO) {
        log.debug("Request to save DiscountInfo : {}", discountInfoDTO);
        DiscountInfo discountInfo = discountInfoMapper.discountInfoDTOToDiscountInfo(discountInfoDTO);
        discountInfo = discountInfoRepository.save(discountInfo);
        DiscountInfoDTO result = discountInfoMapper.discountInfoToDiscountInfoDTO(discountInfo);
        discountInfoSearchRepository.save(discountInfo);
        return result;
    }

    /**
     *  Get all the discountInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<DiscountInfo> findAll(Pageable pageable) {
        log.debug("Request to get all DiscountInfos");
        Page<DiscountInfo> result = discountInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one discountInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DiscountInfoDTO findOne(Long id) {
        log.debug("Request to get DiscountInfo : {}", id);
        DiscountInfo discountInfo = discountInfoRepository.findOne(id);
        DiscountInfoDTO discountInfoDTO = discountInfoMapper.discountInfoToDiscountInfoDTO(discountInfo);
        return discountInfoDTO;
    }

    /**
     *  Delete the  discountInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DiscountInfo : {}", id);
        discountInfoRepository.delete(id);
        discountInfoSearchRepository.delete(id);
    }

    /**
     * Search for the discountInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DiscountInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DiscountInfos for query {}", query);
        return discountInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
