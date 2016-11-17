package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.OrderBagServiceItem;
import com.eyeson.tikon.repository.OrderBagServiceItemRepository;
import com.eyeson.tikon.repository.search.OrderBagServiceItemSearchRepository;
import com.eyeson.tikon.web.rest.dto.OrderBagServiceItemDTO;
import com.eyeson.tikon.web.rest.mapper.OrderBagServiceItemMapper;
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
 * Service Implementation for managing OrderBagServiceItem.
 */
@Service
@Transactional
public class OrderBagServiceItemService {

    private final Logger log = LoggerFactory.getLogger(OrderBagServiceItemService.class);
    
    @Inject
    private OrderBagServiceItemRepository orderBagServiceItemRepository;
    
    @Inject
    private OrderBagServiceItemMapper orderBagServiceItemMapper;
    
    @Inject
    private OrderBagServiceItemSearchRepository orderBagServiceItemSearchRepository;
    
    /**
     * Save a orderBagServiceItem.
     * 
     * @param orderBagServiceItemDTO the entity to save
     * @return the persisted entity
     */
    public OrderBagServiceItemDTO save(OrderBagServiceItemDTO orderBagServiceItemDTO) {
        log.debug("Request to save OrderBagServiceItem : {}", orderBagServiceItemDTO);
        OrderBagServiceItem orderBagServiceItem = orderBagServiceItemMapper.orderBagServiceItemDTOToOrderBagServiceItem(orderBagServiceItemDTO);
        orderBagServiceItem = orderBagServiceItemRepository.save(orderBagServiceItem);
        OrderBagServiceItemDTO result = orderBagServiceItemMapper.orderBagServiceItemToOrderBagServiceItemDTO(orderBagServiceItem);
        orderBagServiceItemSearchRepository.save(orderBagServiceItem);
        return result;
    }

    /**
     *  Get all the orderBagServiceItems.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<OrderBagServiceItem> findAll(Pageable pageable) {
        log.debug("Request to get all OrderBagServiceItems");
        Page<OrderBagServiceItem> result = orderBagServiceItemRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one orderBagServiceItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OrderBagServiceItemDTO findOne(Long id) {
        log.debug("Request to get OrderBagServiceItem : {}", id);
        OrderBagServiceItem orderBagServiceItem = orderBagServiceItemRepository.findOne(id);
        OrderBagServiceItemDTO orderBagServiceItemDTO = orderBagServiceItemMapper.orderBagServiceItemToOrderBagServiceItemDTO(orderBagServiceItem);
        return orderBagServiceItemDTO;
    }

    /**
     *  Delete the  orderBagServiceItem by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderBagServiceItem : {}", id);
        orderBagServiceItemRepository.delete(id);
        orderBagServiceItemSearchRepository.delete(id);
    }

    /**
     * Search for the orderBagServiceItem corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrderBagServiceItem> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderBagServiceItems for query {}", query);
        return orderBagServiceItemSearchRepository.search(queryStringQuery(query), pageable);
    }
}
