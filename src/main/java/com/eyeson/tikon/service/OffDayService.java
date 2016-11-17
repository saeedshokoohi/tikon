package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.OffDay;
import com.eyeson.tikon.repository.OffDayRepository;
import com.eyeson.tikon.repository.search.OffDaySearchRepository;
import com.eyeson.tikon.web.rest.dto.OffDayDTO;
import com.eyeson.tikon.web.rest.mapper.OffDayMapper;
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
 * Service Implementation for managing OffDay.
 */
@Service
@Transactional
public class OffDayService {

    private final Logger log = LoggerFactory.getLogger(OffDayService.class);
    
    @Inject
    private OffDayRepository offDayRepository;
    
    @Inject
    private OffDayMapper offDayMapper;
    
    @Inject
    private OffDaySearchRepository offDaySearchRepository;
    
    /**
     * Save a offDay.
     * 
     * @param offDayDTO the entity to save
     * @return the persisted entity
     */
    public OffDayDTO save(OffDayDTO offDayDTO) {
        log.debug("Request to save OffDay : {}", offDayDTO);
        OffDay offDay = offDayMapper.offDayDTOToOffDay(offDayDTO);
        offDay = offDayRepository.save(offDay);
        OffDayDTO result = offDayMapper.offDayToOffDayDTO(offDay);
        offDaySearchRepository.save(offDay);
        return result;
    }

    /**
     *  Get all the offDays.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<OffDay> findAll(Pageable pageable) {
        log.debug("Request to get all OffDays");
        Page<OffDay> result = offDayRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one offDay by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public OffDayDTO findOne(Long id) {
        log.debug("Request to get OffDay : {}", id);
        OffDay offDay = offDayRepository.findOne(id);
        OffDayDTO offDayDTO = offDayMapper.offDayToOffDayDTO(offDay);
        return offDayDTO;
    }

    /**
     *  Delete the  offDay by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OffDay : {}", id);
        offDayRepository.delete(id);
        offDaySearchRepository.delete(id);
    }

    /**
     * Search for the offDay corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OffDay> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OffDays for query {}", query);
        return offDaySearchRepository.search(queryStringQuery(query), pageable);
    }
}
