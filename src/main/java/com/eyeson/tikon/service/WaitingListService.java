package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.WaitingList;
import com.eyeson.tikon.repository.WaitingListRepository;
import com.eyeson.tikon.repository.search.WaitingListSearchRepository;
import com.eyeson.tikon.web.rest.dto.WaitingListDTO;
import com.eyeson.tikon.web.rest.mapper.WaitingListMapper;
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
 * Service Implementation for managing WaitingList.
 */
@Service
@Transactional
public class WaitingListService {

    private final Logger log = LoggerFactory.getLogger(WaitingListService.class);
    
    @Inject
    private WaitingListRepository waitingListRepository;
    
    @Inject
    private WaitingListMapper waitingListMapper;
    
    @Inject
    private WaitingListSearchRepository waitingListSearchRepository;
    
    /**
     * Save a waitingList.
     * 
     * @param waitingListDTO the entity to save
     * @return the persisted entity
     */
    public WaitingListDTO save(WaitingListDTO waitingListDTO) {
        log.debug("Request to save WaitingList : {}", waitingListDTO);
        WaitingList waitingList = waitingListMapper.waitingListDTOToWaitingList(waitingListDTO);
        waitingList = waitingListRepository.save(waitingList);
        WaitingListDTO result = waitingListMapper.waitingListToWaitingListDTO(waitingList);
        waitingListSearchRepository.save(waitingList);
        return result;
    }

    /**
     *  Get all the waitingLists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<WaitingList> findAll(Pageable pageable) {
        log.debug("Request to get all WaitingLists");
        Page<WaitingList> result = waitingListRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one waitingList by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public WaitingListDTO findOne(Long id) {
        log.debug("Request to get WaitingList : {}", id);
        WaitingList waitingList = waitingListRepository.findOne(id);
        WaitingListDTO waitingListDTO = waitingListMapper.waitingListToWaitingListDTO(waitingList);
        return waitingListDTO;
    }

    /**
     *  Delete the  waitingList by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WaitingList : {}", id);
        waitingListRepository.delete(id);
        waitingListSearchRepository.delete(id);
    }

    /**
     * Search for the waitingList corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WaitingList> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WaitingLists for query {}", query);
        return waitingListSearchRepository.search(queryStringQuery(query), pageable);
    }
}
