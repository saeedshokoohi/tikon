package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.FinancialSetting;
import com.eyeson.tikon.repository.FinancialSettingRepository;
import com.eyeson.tikon.repository.search.FinancialSettingSearchRepository;
import com.eyeson.tikon.web.rest.dto.FinancialSettingDTO;
import com.eyeson.tikon.web.rest.mapper.FinancialSettingMapper;
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
 * Service Implementation for managing FinancialSetting.
 */
@Service
@Transactional
public class FinancialSettingService {

    private final Logger log = LoggerFactory.getLogger(FinancialSettingService.class);
    
    @Inject
    private FinancialSettingRepository financialSettingRepository;
    
    @Inject
    private FinancialSettingMapper financialSettingMapper;
    
    @Inject
    private FinancialSettingSearchRepository financialSettingSearchRepository;
    
    /**
     * Save a financialSetting.
     * 
     * @param financialSettingDTO the entity to save
     * @return the persisted entity
     */
    public FinancialSettingDTO save(FinancialSettingDTO financialSettingDTO) {
        log.debug("Request to save FinancialSetting : {}", financialSettingDTO);
        FinancialSetting financialSetting = financialSettingMapper.financialSettingDTOToFinancialSetting(financialSettingDTO);
        financialSetting = financialSettingRepository.save(financialSetting);
        FinancialSettingDTO result = financialSettingMapper.financialSettingToFinancialSettingDTO(financialSetting);
        financialSettingSearchRepository.save(financialSetting);
        return result;
    }

    /**
     *  Get all the financialSettings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<FinancialSetting> findAll(Pageable pageable) {
        log.debug("Request to get all FinancialSettings");
        Page<FinancialSetting> result = financialSettingRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one financialSetting by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public FinancialSettingDTO findOne(Long id) {
        log.debug("Request to get FinancialSetting : {}", id);
        FinancialSetting financialSetting = financialSettingRepository.findOne(id);
        FinancialSettingDTO financialSettingDTO = financialSettingMapper.financialSettingToFinancialSettingDTO(financialSetting);
        return financialSettingDTO;
    }

    /**
     *  Delete the  financialSetting by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FinancialSetting : {}", id);
        financialSettingRepository.delete(id);
        financialSettingSearchRepository.delete(id);
    }

    /**
     * Search for the financialSetting corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FinancialSetting> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FinancialSettings for query {}", query);
        return financialSettingSearchRepository.search(queryStringQuery(query), pageable);
    }
}
