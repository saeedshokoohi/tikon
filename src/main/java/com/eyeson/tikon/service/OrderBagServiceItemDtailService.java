package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.OrderBagServiceItemDtail;
import com.eyeson.tikon.repository.OrderBagServiceItemDtailRepository;
import com.eyeson.tikon.repository.search.OrderBagServiceItemDtailSearchRepository;
import com.eyeson.tikon.web.rest.dto.OrderBagServiceItemDtailDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagServiceItemDtailMapper;
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
 * Service Implementation for managing OrderBagServiceItemDtail.
 */
@Service
@Transactional
public class OrderBagServiceItemDtailService {

    private final Logger log = LoggerFactory.getLogger(OrderBagServiceItemDtailService.class);
    
    @Inject
    private OrderBagServiceItemDtailRepository orderBagServiceItemDtailRepository;
    
    @Inject
    private OrderBagServiceItemDtailMapper orderBagServiceItemDtailMapper;
    
    @Inject
    private OrderBagServiceItemDtailSearchRepository orderBagServiceItemDtailSearchRepository;
    
    /**
     * Save a orderBagServiceItemDtail.
     * 
     * @param orderBagServiceItemDtailDTO the entity to save
     * @return the persisted entity
     */
    public OrderBagServiceItemDtailDTO save(OrderBagServiceItemDtailDTO orderBagServiceItemDtailDTO) {
        log.debug("Request to save OrderBagServiceItemDtail : {}", orderBagServiceItemDtailDTO);
        OrderBagServiceItemDtail orderBagServiceItemDtail = orderBagServiceItemDtailMapper.orderBagServiceItemDtailDTOToOrderBagServiceItemDtail(orderBagServiceItemDtailDTO);
        orderBagServiceItemDtail = orderBagServiceItemDtailRepository.save(orderBagServiceItemDtail);
        OrderBagServiceItemDtailDTO result = orderBagServiceItemDtailMapper.orderBagServiceItemDtailToOrderBagServiceItemDtailDTO(orderBagServiceItemDtail);
        orderBagServiceItemDtailSearchRepository.save(orderBagServiceItemDtail);
        return result;
    }

    /**
     *  Get all the orderBagServiceItemDtails.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<OrderBagServiceItemDtail> findAll(Pageable pageable) {
        log.debug("Request to get all OrderBagServiceItemDtails");
        Page<OrderBagServiceItemDtail> result = orderBagServiceItemDtailRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one orderBagServiceItemDtail by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OrderBagServiceItemDtailDTO findOne(Long id) {
        log.debug("Request to get OrderBagServiceItemDtail : {}", id);
        OrderBagServiceItemDtail orderBagServiceItemDtail = orderBagServiceItemDtailRepository.findOne(id);
        OrderBagServiceItemDtailDTO orderBagServiceItemDtailDTO = orderBagServiceItemDtailMapper.orderBagServiceItemDtailToOrderBagServiceItemDtailDTO(orderBagServiceItemDtail);
        return orderBagServiceItemDtailDTO;
    }

    /**
     *  Delete the  orderBagServiceItemDtail by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderBagServiceItemDtail : {}", id);
        orderBagServiceItemDtailRepository.delete(id);
        orderBagServiceItemDtailSearchRepository.delete(id);
    }

    /**
     * Search for the orderBagServiceItemDtail corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderBagServiceItemDtail> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderBagServiceItemDtails for query {}", query);
        return orderBagServiceItemDtailSearchRepository.search(queryStringQuery(query), pageable);
    }
}
