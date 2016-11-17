package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.OrderBagItemOption;
import com.eyeson.tikon.repository.OrderBagItemOptionRepository;
import com.eyeson.tikon.repository.search.OrderBagItemOptionSearchRepository;
import com.eyeson.tikon.web.rest.dto.OrderBagItemOptionDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagItemOptionMapper;
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
 * Service Implementation for managing OrderBagItemOption.
 */
@Service
@Transactional
public class OrderBagItemOptionService {

    private final Logger log = LoggerFactory.getLogger(OrderBagItemOptionService.class);
    
    @Inject
    private OrderBagItemOptionRepository orderBagItemOptionRepository;
    
    @Inject
    private OrderBagItemOptionMapper orderBagItemOptionMapper;
    
    @Inject
    private OrderBagItemOptionSearchRepository orderBagItemOptionSearchRepository;
    
    /**
     * Save a orderBagItemOption.
     * 
     * @param orderBagItemOptionDTO the entity to save
     * @return the persisted entity
     */
    public OrderBagItemOptionDTO save(OrderBagItemOptionDTO orderBagItemOptionDTO) {
        log.debug("Request to save OrderBagItemOption : {}", orderBagItemOptionDTO);
        OrderBagItemOption orderBagItemOption = orderBagItemOptionMapper.orderBagItemOptionDTOToOrderBagItemOption(orderBagItemOptionDTO);
        orderBagItemOption = orderBagItemOptionRepository.save(orderBagItemOption);
        OrderBagItemOptionDTO result = orderBagItemOptionMapper.orderBagItemOptionToOrderBagItemOptionDTO(orderBagItemOption);
        orderBagItemOptionSearchRepository.save(orderBagItemOption);
        return result;
    }

    /**
     *  Get all the orderBagItemOptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<OrderBagItemOption> findAll(Pageable pageable) {
        log.debug("Request to get all OrderBagItemOptions");
        Page<OrderBagItemOption> result = orderBagItemOptionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one orderBagItemOption by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OrderBagItemOptionDTO findOne(Long id) {
        log.debug("Request to get OrderBagItemOption : {}", id);
        OrderBagItemOption orderBagItemOption = orderBagItemOptionRepository.findOne(id);
        OrderBagItemOptionDTO orderBagItemOptionDTO = orderBagItemOptionMapper.orderBagItemOptionToOrderBagItemOptionDTO(orderBagItemOption);
        return orderBagItemOptionDTO;
    }

    /**
     *  Delete the  orderBagItemOption by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderBagItemOption : {}", id);
        orderBagItemOptionRepository.delete(id);
        orderBagItemOptionSearchRepository.delete(id);
    }

    /**
     * Search for the orderBagItemOption corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderBagItemOption> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderBagItemOptions for query {}", query);
        return orderBagItemOptionSearchRepository.search(queryStringQuery(query), pageable);
    }
}
