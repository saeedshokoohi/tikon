package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.CompanyManager;
import com.eyeson.tikon.repository.CompanyManagerRepository;
import com.eyeson.tikon.repository.search.CompanyManagerSearchRepository;
import com.eyeson.tikon.web.rest.dto.CompanyManagerDTO;
import com.eyeson.tikon.web.rest.mapper.CompanyManagerMapper;
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
 * Service Implementation for managing CompanyManager.
 */
@Service
@Transactional
public class CompanyManagerService {

    private final Logger log = LoggerFactory.getLogger(CompanyManagerService.class);
    
    @Inject
    private CompanyManagerRepository companyManagerRepository;
    
    @Inject
    private CompanyManagerMapper companyManagerMapper;
    
    @Inject
    private CompanyManagerSearchRepository companyManagerSearchRepository;
    
    /**
     * Save a companyManager.
     * 
     * @param companyManagerDTO the entity to save
     * @return the persisted entity
     */
    public CompanyManagerDTO save(CompanyManagerDTO companyManagerDTO) {
        log.debug("Request to save CompanyManager : {}", companyManagerDTO);
        CompanyManager companyManager = companyManagerMapper.companyManagerDTOToCompanyManager(companyManagerDTO);
        companyManager = companyManagerRepository.save(companyManager);
        CompanyManagerDTO result = companyManagerMapper.companyManagerToCompanyManagerDTO(companyManager);
        companyManagerSearchRepository.save(companyManager);
        return result;
    }

    /**
     *  Get all the companyManagers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CompanyManager> findAll(Pageable pageable) {
        log.debug("Request to get all CompanyManagers");
        Page<CompanyManager> result = companyManagerRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one companyManager by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CompanyManagerDTO findOne(Long id) {
        log.debug("Request to get CompanyManager : {}", id);
        CompanyManager companyManager = companyManagerRepository.findOne(id);
        CompanyManagerDTO companyManagerDTO = companyManagerMapper.companyManagerToCompanyManagerDTO(companyManager);
        return companyManagerDTO;
    }

    /**
     *  Delete the  companyManager by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyManager : {}", id);
        companyManagerRepository.delete(id);
        companyManagerSearchRepository.delete(id);
    }

    /**
     * Search for the companyManager corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CompanyManager> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CompanyManagers for query {}", query);
        return companyManagerSearchRepository.search(queryStringQuery(query), pageable);
    }
}
