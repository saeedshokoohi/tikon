package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.ServiceOptionItem;
import com.eyeson.tikon.repository.ServiceOptionItemRepository;
import com.eyeson.tikon.repository.extended.ServiceOptionItemExtendedRepository;
import com.eyeson.tikon.repository.search.ServiceOptionItemSearchRepository;
import com.eyeson.tikon.web.rest.dto.ServiceOptionItemDTO;
import com.eyeson.tikon.web.rest.mapper.ServiceOptionItemMapper;
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
 * Service Implementation for managing ServiceOptionItem.
 */
@Service
@Transactional
public class ServiceOptionItemService {

    private final Logger log = LoggerFactory.getLogger(ServiceOptionItemService.class);

    @Inject
    private ServiceOptionItemRepository serviceOptionItemRepository;

    @Inject
    private ServiceOptionItemMapper serviceOptionItemMapper;

    @Inject
    private ServiceOptionItemSearchRepository serviceOptionItemSearchRepository;

    /**
     * Save a serviceOptionItem.
     *
     * @param serviceOptionItemDTO the entity to save
     * @return the persisted entity
     */
    public ServiceOptionItemDTO save(ServiceOptionItemDTO serviceOptionItemDTO) {
        log.debug("Request to save ServiceOptionItem : {}", serviceOptionItemDTO);
        ServiceOptionItem serviceOptionItem = serviceOptionItemMapper.serviceOptionItemDTOToServiceOptionItem(serviceOptionItemDTO);
        serviceOptionItem = serviceOptionItemRepository.save(serviceOptionItem);
        ServiceOptionItemDTO result = serviceOptionItemMapper.serviceOptionItemToServiceOptionItemDTO(serviceOptionItem);
        serviceOptionItemSearchRepository.save(serviceOptionItem);
        return result;
    }

    /**
     *  Get all the serviceOptionItems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceOptionItem> findAll(Pageable pageable) {
        log.debug("Request to get all ServiceOptionItems");
        Page<ServiceOptionItem> result = serviceOptionItemRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one serviceOptionItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ServiceOptionItemDTO findOne(Long id) {
        log.debug("Request to get ServiceOptionItem : {}", id);
        ServiceOptionItem serviceOptionItem = serviceOptionItemRepository.findOne(id);
        ServiceOptionItemDTO serviceOptionItemDTO = serviceOptionItemMapper.serviceOptionItemToServiceOptionItemDTO(serviceOptionItem);
        return serviceOptionItemDTO;
    }

    /**
     *  Delete the  serviceOptionItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ServiceOptionItem : {}", id);
        serviceOptionItemRepository.delete(id);
        serviceOptionItemSearchRepository.delete(id);
    }

    /**
     * Search for the serviceOptionItem corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ServiceOptionItem> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ServiceOptionItems for query {}", query);
        return serviceOptionItemSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Inject
    ServiceOptionItemExtendedRepository serviceOptionItemExtendedRepository;
    public List<ServiceOptionItem> getServiceOptionItemsByServiceItem(Long serviceItemId) {
        return serviceOptionItemExtendedRepository.getServiceOptionItemsByServiceItem(serviceItemId) ;
    }
}
