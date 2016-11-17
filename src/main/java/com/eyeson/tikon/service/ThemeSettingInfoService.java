package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.ThemeSettingInfo;
import com.eyeson.tikon.repository.ThemeSettingInfoRepository;
import com.eyeson.tikon.repository.search.ThemeSettingInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.ThemeSettingInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ThemeSettingInfoMapper;
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
 * Service Implementation for managing ThemeSettingInfo.
 */
@Service
@Transactional
public class ThemeSettingInfoService {

    private final Logger log = LoggerFactory.getLogger(ThemeSettingInfoService.class);
    
    @Inject
    private ThemeSettingInfoRepository themeSettingInfoRepository;
    
    @Inject
    private ThemeSettingInfoMapper themeSettingInfoMapper;
    
    @Inject
    private ThemeSettingInfoSearchRepository themeSettingInfoSearchRepository;
    
    /**
     * Save a themeSettingInfo.
     * 
     * @param themeSettingInfoDTO the entity to save
     * @return the persisted entity
     */
    public ThemeSettingInfoDTO save(ThemeSettingInfoDTO themeSettingInfoDTO) {
        log.debug("Request to save ThemeSettingInfo : {}", themeSettingInfoDTO);
        ThemeSettingInfo themeSettingInfo = themeSettingInfoMapper.themeSettingInfoDTOToThemeSettingInfo(themeSettingInfoDTO);
        themeSettingInfo = themeSettingInfoRepository.save(themeSettingInfo);
        ThemeSettingInfoDTO result = themeSettingInfoMapper.themeSettingInfoToThemeSettingInfoDTO(themeSettingInfo);
        themeSettingInfoSearchRepository.save(themeSettingInfo);
        return result;
    }

    /**
     *  Get all the themeSettingInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ThemeSettingInfo> findAll(Pageable pageable) {
        log.debug("Request to get all ThemeSettingInfos");
        Page<ThemeSettingInfo> result = themeSettingInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one themeSettingInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ThemeSettingInfoDTO findOne(Long id) {
        log.debug("Request to get ThemeSettingInfo : {}", id);
        ThemeSettingInfo themeSettingInfo = themeSettingInfoRepository.findOne(id);
        ThemeSettingInfoDTO themeSettingInfoDTO = themeSettingInfoMapper.themeSettingInfoToThemeSettingInfoDTO(themeSettingInfo);
        return themeSettingInfoDTO;
    }

    /**
     *  Delete the  themeSettingInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ThemeSettingInfo : {}", id);
        themeSettingInfoRepository.delete(id);
        themeSettingInfoSearchRepository.delete(id);
    }

    /**
     * Search for the themeSettingInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ThemeSettingInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ThemeSettingInfos for query {}", query);
        return themeSettingInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
