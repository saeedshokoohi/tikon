package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.ExtraSetting;
import com.eyeson.tikon.repository.ExtraSettingRepository;
import com.eyeson.tikon.repository.search.ExtraSettingSearchRepository;
import com.eyeson.tikon.web.rest.dto.ExtraSettingDTO;
import com.eyeson.tikon.web.rest.mapper.ExtraSettingMapper;
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
 * Service Implementation for managing ExtraSetting.
 */
@Service
@Transactional
public class ExtraSettingService {

    private final Logger log = LoggerFactory.getLogger(ExtraSettingService.class);
    
    @Inject
    private ExtraSettingRepository extraSettingRepository;
    
    @Inject
    private ExtraSettingMapper extraSettingMapper;
    
    @Inject
    private ExtraSettingSearchRepository extraSettingSearchRepository;
    
    /**
     * Save a extraSetting.
     * 
     * @param extraSettingDTO the entity to save
     * @return the persisted entity
     */
    public ExtraSettingDTO save(ExtraSettingDTO extraSettingDTO) {
        log.debug("Request to save ExtraSetting : {}", extraSettingDTO);
        ExtraSetting extraSetting = extraSettingMapper.extraSettingDTOToExtraSetting(extraSettingDTO);
        extraSetting = extraSettingRepository.save(extraSetting);
        ExtraSettingDTO result = extraSettingMapper.extraSettingToExtraSettingDTO(extraSetting);
        extraSettingSearchRepository.save(extraSetting);
        return result;
    }

    /**
     *  Get all the extraSettings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ExtraSetting> findAll(Pageable pageable) {
        log.debug("Request to get all ExtraSettings");
        Page<ExtraSetting> result = extraSettingRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one extraSetting by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ExtraSettingDTO findOne(Long id) {
        log.debug("Request to get ExtraSetting : {}", id);
        ExtraSetting extraSetting = extraSettingRepository.findOne(id);
        ExtraSettingDTO extraSettingDTO = extraSettingMapper.extraSettingToExtraSettingDTO(extraSetting);
        return extraSettingDTO;
    }

    /**
     *  Delete the  extraSetting by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExtraSetting : {}", id);
        extraSettingRepository.delete(id);
        extraSettingSearchRepository.delete(id);
    }

    /**
     * Search for the extraSetting corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ExtraSetting> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ExtraSettings for query {}", query);
        return extraSettingSearchRepository.search(queryStringQuery(query), pageable);
    }
}
