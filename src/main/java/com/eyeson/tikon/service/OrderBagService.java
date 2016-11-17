package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.OrderBag;
import com.eyeson.tikon.repository.OrderBagRepository;
import com.eyeson.tikon.repository.search.OrderBagSearchRepository;
import com.eyeson.tikon.web.rest.dto.OrderBagDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagMapper;
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
 * Service Implementation for managing OrderBag.
 */
@Service
@Transactional
public class OrderBagService {

    private final Logger log = LoggerFactory.getLogger(OrderBagService.class);
    
    @Inject
    private OrderBagRepository orderBagRepository;
    
    @Inject
    private OrderBagMapper orderBagMapper;
    
    @Inject
    private OrderBagSearchRepository orderBagSearchRepository;
    
    /**
     * Save a orderBag.
     * 
     * @param orderBagDTO the entity to save
     * @return the persisted entity
     */
    public OrderBagDTO save(OrderBagDTO orderBagDTO) {
        log.debug("Request to save OrderBag : {}", orderBagDTO);
        OrderBag orderBag = orderBagMapper.orderBagDTOToOrderBag(orderBagDTO);
        orderBag = orderBagRepository.save(orderBag);
        OrderBagDTO result = orderBagMapper.orderBagToOrderBagDTO(orderBag);
        orderBagSearchRepository.save(orderBag);
        return result;
    }

    /**
     *  Get all the orderBags.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<OrderBag> findAll(Pageable pageable) {
        log.debug("Request to get all OrderBags");
        Page<OrderBag> result = orderBagRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one orderBag by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OrderBagDTO findOne(Long id) {
        log.debug("Request to get OrderBag : {}", id);
        OrderBag orderBag = orderBagRepository.findOne(id);
        OrderBagDTO orderBagDTO = orderBagMapper.orderBagToOrderBagDTO(orderBag);
        return orderBagDTO;
    }

    /**
     *  Delete the  orderBag by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderBag : {}", id);
        orderBagRepository.delete(id);
        orderBagSearchRepository.delete(id);
    }

    /**
     * Search for the orderBag corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderBag> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderBags for query {}", query);
        return orderBagSearchRepository.search(queryStringQuery(query), pageable);
    }
}
