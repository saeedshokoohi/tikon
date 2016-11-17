package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.SocialNetworkInfo;
import com.eyeson.tikon.repository.SocialNetworkInfoRepository;
import com.eyeson.tikon.repository.search.SocialNetworkInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.SocialNetworkInfoDTO;
import com.eyeson.tikon.web.rest.mapper.SocialNetworkInfoMapper;
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
 * Service Implementation for managing SocialNetworkInfo.
 */
@Service
@Transactional
public class SocialNetworkInfoService {

    private final Logger log = LoggerFactory.getLogger(SocialNetworkInfoService.class);
    
    @Inject
    private SocialNetworkInfoRepository socialNetworkInfoRepository;
    
    @Inject
    private SocialNetworkInfoMapper socialNetworkInfoMapper;
    
    @Inject
    private SocialNetworkInfoSearchRepository socialNetworkInfoSearchRepository;
    
    /**
     * Save a socialNetworkInfo.
     * 
     * @param socialNetworkInfoDTO the entity to save
     * @return the persisted entity
     */
    public SocialNetworkInfoDTO save(SocialNetworkInfoDTO socialNetworkInfoDTO) {
        log.debug("Request to save SocialNetworkInfo : {}", socialNetworkInfoDTO);
        SocialNetworkInfo socialNetworkInfo = socialNetworkInfoMapper.socialNetworkInfoDTOToSocialNetworkInfo(socialNetworkInfoDTO);
        socialNetworkInfo = socialNetworkInfoRepository.save(socialNetworkInfo);
        SocialNetworkInfoDTO result = socialNetworkInfoMapper.socialNetworkInfoToSocialNetworkInfoDTO(socialNetworkInfo);
        socialNetworkInfoSearchRepository.save(socialNetworkInfo);
        return result;
    }

    /**
     *  Get all the socialNetworkInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<SocialNetworkInfo> findAll(Pageable pageable) {
        log.debug("Request to get all SocialNetworkInfos");
        Page<SocialNetworkInfo> result = socialNetworkInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one socialNetworkInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SocialNetworkInfoDTO findOne(Long id) {
        log.debug("Request to get SocialNetworkInfo : {}", id);
        SocialNetworkInfo socialNetworkInfo = socialNetworkInfoRepository.findOne(id);
        SocialNetworkInfoDTO socialNetworkInfoDTO = socialNetworkInfoMapper.socialNetworkInfoToSocialNetworkInfoDTO(socialNetworkInfo);
        return socialNetworkInfoDTO;
    }

    /**
     *  Delete the  socialNetworkInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SocialNetworkInfo : {}", id);
        socialNetworkInfoRepository.delete(id);
        socialNetworkInfoSearchRepository.delete(id);
    }

    /**
     * Search for the socialNetworkInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SocialNetworkInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SocialNetworkInfos for query {}", query);
        return socialNetworkInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
