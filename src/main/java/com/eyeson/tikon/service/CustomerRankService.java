package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.CustomerRank;
import com.eyeson.tikon.repository.CustomerRankRepository;
import com.eyeson.tikon.repository.search.CustomerRankSearchRepository;
import com.eyeson.tikon.web.rest.dto.CustomerRankDTO;
import com.eyeson.tikon.web.rest.mapper.CustomerRankMapper;
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
 * Service Implementation for managing CustomerRank.
 */
@Service
@Transactional
public class CustomerRankService {

    private final Logger log = LoggerFactory.getLogger(CustomerRankService.class);
    
    @Inject
    private CustomerRankRepository customerRankRepository;
    
    @Inject
    private CustomerRankMapper customerRankMapper;
    
    @Inject
    private CustomerRankSearchRepository customerRankSearchRepository;
    
    /**
     * Save a customerRank.
     * 
     * @param customerRankDTO the entity to save
     * @return the persisted entity
     */
    public CustomerRankDTO save(CustomerRankDTO customerRankDTO) {
        log.debug("Request to save CustomerRank : {}", customerRankDTO);
        CustomerRank customerRank = customerRankMapper.customerRankDTOToCustomerRank(customerRankDTO);
        customerRank = customerRankRepository.save(customerRank);
        CustomerRankDTO result = customerRankMapper.customerRankToCustomerRankDTO(customerRank);
        customerRankSearchRepository.save(customerRank);
        return result;
    }

    /**
     *  Get all the customerRanks.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CustomerRank> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerRanks");
        Page<CustomerRank> result = customerRankRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one customerRank by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CustomerRankDTO findOne(Long id) {
        log.debug("Request to get CustomerRank : {}", id);
        CustomerRank customerRank = customerRankRepository.findOne(id);
        CustomerRankDTO customerRankDTO = customerRankMapper.customerRankToCustomerRankDTO(customerRank);
        return customerRankDTO;
    }

    /**
     *  Delete the  customerRank by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerRank : {}", id);
        customerRankRepository.delete(id);
        customerRankSearchRepository.delete(id);
    }

    /**
     * Search for the customerRank corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomerRank> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerRanks for query {}", query);
        return customerRankSearchRepository.search(queryStringQuery(query), pageable);
    }
}
