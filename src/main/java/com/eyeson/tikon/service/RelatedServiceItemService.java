package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.RelatedServiceItem;
import com.eyeson.tikon.repository.RelatedServiceItemRepository;
import com.eyeson.tikon.repository.search.RelatedServiceItemSearchRepository;
import com.eyeson.tikon.web.rest.dto.RelatedServiceItemDTO;
import com.eyeson.tikon.web.rest.mapper.RelatedServiceItemMapper;
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
 * Service Implementation for managing RelatedServiceItem.
 */
@Service
@Transactional
public class RelatedServiceItemService {

    private final Logger log = LoggerFactory.getLogger(RelatedServiceItemService.class);
    
    @Inject
    private RelatedServiceItemRepository relatedServiceItemRepository;
    
    @Inject
    private RelatedServiceItemMapper relatedServiceItemMapper;
    
    @Inject
    private RelatedServiceItemSearchRepository relatedServiceItemSearchRepository;
    
    /**
     * Save a relatedServiceItem.
     * 
     * @param relatedServiceItemDTO the entity to save
     * @return the persisted entity
     */
    public RelatedServiceItemDTO save(RelatedServiceItemDTO relatedServiceItemDTO) {
        log.debug("Request to save RelatedServiceItem : {}", relatedServiceItemDTO);
        RelatedServiceItem relatedServiceItem = relatedServiceItemMapper.relatedServiceItemDTOToRelatedServiceItem(relatedServiceItemDTO);
        relatedServiceItem = relatedServiceItemRepository.save(relatedServiceItem);
        RelatedServiceItemDTO result = relatedServiceItemMapper.relatedServiceItemToRelatedServiceItemDTO(relatedServiceItem);
        relatedServiceItemSearchRepository.save(relatedServiceItem);
        return result;
    }

    /**
     *  Get all the relatedServiceItems.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<RelatedServiceItem> findAll(Pageable pageable) {
        log.debug("Request to get all RelatedServiceItems");
        Page<RelatedServiceItem> result = relatedServiceItemRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one relatedServiceItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RelatedServiceItemDTO findOne(Long id) {
        log.debug("Request to get RelatedServiceItem : {}", id);
        RelatedServiceItem relatedServiceItem = relatedServiceItemRepository.findOne(id);
        RelatedServiceItemDTO relatedServiceItemDTO = relatedServiceItemMapper.relatedServiceItemToRelatedServiceItemDTO(relatedServiceItem);
        return relatedServiceItemDTO;
    }

    /**
     *  Delete the  relatedServiceItem by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete RelatedServiceItem : {}", id);
        relatedServiceItemRepository.delete(id);
        relatedServiceItemSearchRepository.delete(id);
    }

    /**
     * Search for the relatedServiceItem corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RelatedServiceItem> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of RelatedServiceItems for query {}", query);
        return relatedServiceItemSearchRepository.search(queryStringQuery(query), pageable);
    }
}
