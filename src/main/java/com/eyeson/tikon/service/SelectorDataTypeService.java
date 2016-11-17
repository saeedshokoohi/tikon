package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.SelectorDataType;
import com.eyeson.tikon.repository.SelectorDataTypeRepository;
import com.eyeson.tikon.repository.search.SelectorDataTypeSearchRepository;
import com.eyeson.tikon.web.rest.dto.SelectorDataTypeDTO;
import com.eyeson.tikon.web.rest.mapper.SelectorDataTypeMapper;
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
 * Service Implementation for managing SelectorDataType.
 */
@Service
@Transactional
public class SelectorDataTypeService {

    private final Logger log = LoggerFactory.getLogger(SelectorDataTypeService.class);
    
    @Inject
    private SelectorDataTypeRepository selectorDataTypeRepository;
    
    @Inject
    private SelectorDataTypeMapper selectorDataTypeMapper;
    
    @Inject
    private SelectorDataTypeSearchRepository selectorDataTypeSearchRepository;
    
    /**
     * Save a selectorDataType.
     * 
     * @param selectorDataTypeDTO the entity to save
     * @return the persisted entity
     */
    public SelectorDataTypeDTO save(SelectorDataTypeDTO selectorDataTypeDTO) {
        log.debug("Request to save SelectorDataType : {}", selectorDataTypeDTO);
        SelectorDataType selectorDataType = selectorDataTypeMapper.selectorDataTypeDTOToSelectorDataType(selectorDataTypeDTO);
        selectorDataType = selectorDataTypeRepository.save(selectorDataType);
        SelectorDataTypeDTO result = selectorDataTypeMapper.selectorDataTypeToSelectorDataTypeDTO(selectorDataType);
        selectorDataTypeSearchRepository.save(selectorDataType);
        return result;
    }

    /**
     *  Get all the selectorDataTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<SelectorDataType> findAll(Pageable pageable) {
        log.debug("Request to get all SelectorDataTypes");
        Page<SelectorDataType> result = selectorDataTypeRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one selectorDataType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SelectorDataTypeDTO findOne(Long id) {
        log.debug("Request to get SelectorDataType : {}", id);
        SelectorDataType selectorDataType = selectorDataTypeRepository.findOne(id);
        SelectorDataTypeDTO selectorDataTypeDTO = selectorDataTypeMapper.selectorDataTypeToSelectorDataTypeDTO(selectorDataType);
        return selectorDataTypeDTO;
    }

    /**
     *  Delete the  selectorDataType by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SelectorDataType : {}", id);
        selectorDataTypeRepository.delete(id);
        selectorDataTypeSearchRepository.delete(id);
    }

    /**
     * Search for the selectorDataType corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SelectorDataType> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SelectorDataTypes for query {}", query);
        return selectorDataTypeSearchRepository.search(queryStringQuery(query), pageable);
    }
}
