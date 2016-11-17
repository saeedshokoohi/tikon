package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.CustomerComment;
import com.eyeson.tikon.repository.CustomerCommentRepository;
import com.eyeson.tikon.repository.search.CustomerCommentSearchRepository;
import com.eyeson.tikon.web.rest.dto.CustomerCommentDTO;
import com.eyeson.tikon.web.rest.mapper.CustomerCommentMapper;
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
 * Service Implementation for managing CustomerComment.
 */
@Service
@Transactional
public class CustomerCommentService {

    private final Logger log = LoggerFactory.getLogger(CustomerCommentService.class);
    
    @Inject
    private CustomerCommentRepository customerCommentRepository;
    
    @Inject
    private CustomerCommentMapper customerCommentMapper;
    
    @Inject
    private CustomerCommentSearchRepository customerCommentSearchRepository;
    
    /**
     * Save a customerComment.
     * 
     * @param customerCommentDTO the entity to save
     * @return the persisted entity
     */
    public CustomerCommentDTO save(CustomerCommentDTO customerCommentDTO) {
        log.debug("Request to save CustomerComment : {}", customerCommentDTO);
        CustomerComment customerComment = customerCommentMapper.customerCommentDTOToCustomerComment(customerCommentDTO);
        customerComment = customerCommentRepository.save(customerComment);
        CustomerCommentDTO result = customerCommentMapper.customerCommentToCustomerCommentDTO(customerComment);
        customerCommentSearchRepository.save(customerComment);
        return result;
    }

    /**
     *  Get all the customerComments.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CustomerComment> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerComments");
        Page<CustomerComment> result = customerCommentRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one customerComment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CustomerCommentDTO findOne(Long id) {
        log.debug("Request to get CustomerComment : {}", id);
        CustomerComment customerComment = customerCommentRepository.findOne(id);
        CustomerCommentDTO customerCommentDTO = customerCommentMapper.customerCommentToCustomerCommentDTO(customerComment);
        return customerCommentDTO;
    }

    /**
     *  Delete the  customerComment by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerComment : {}", id);
        customerCommentRepository.delete(id);
        customerCommentSearchRepository.delete(id);
    }

    /**
     * Search for the customerComment corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomerComment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerComments for query {}", query);
        return customerCommentSearchRepository.search(queryStringQuery(query), pageable);
    }
}
