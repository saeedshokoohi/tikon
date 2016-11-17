package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.SelectorData;
import com.eyeson.tikon.repository.SelectorDataRepository;
import com.eyeson.tikon.repository.search.SelectorDataSearchRepository;
import com.eyeson.tikon.web.rest.dto.SelectorDataDTO;
import com.eyeson.tikon.web.rest.mapper.SelectorDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SelectorData.
 */
@Service
@Transactional
public class SelectorDataService {

    private final Logger log = LoggerFactory.getLogger(SelectorDataService.class);

    @Inject
    private SelectorDataRepository selectorDataRepository;
    @PersistenceContext
    private EntityManager em;
    @Inject
    private SelectorDataMapper selectorDataMapper;

    @Inject
    private SelectorDataSearchRepository selectorDataSearchRepository;

    /**
     * Save a selectorData.
     *
     * @param selectorDataDTO the entity to save
     * @return the persisted entity
     */
    public SelectorDataDTO save(SelectorDataDTO selectorDataDTO) {
        log.debug("Request to save SelectorData : {}", selectorDataDTO);
        SelectorData selectorData = selectorDataMapper.selectorDataDTOToSelectorData(selectorDataDTO);
        selectorData = selectorDataRepository.save(selectorData);
        SelectorDataDTO result = selectorDataMapper.selectorDataToSelectorDataDTO(selectorData);
        selectorDataSearchRepository.save(selectorData);
        return result;
    }

    /**
     *  Get all the selectorData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SelectorData> findAll(Pageable pageable) {
        log.debug("Request to get all SelectorData");
        Page<SelectorData> result = selectorDataRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one selectorData by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public SelectorDataDTO findOne(Long id) {
        log.debug("Request to get SelectorData : {}", id);
        SelectorData selectorData = selectorDataRepository.findOne(id);
        SelectorDataDTO selectorDataDTO = selectorDataMapper.selectorDataToSelectorDataDTO(selectorData);
        return selectorDataDTO;
    }

    /**
     *  Delete the  selectorData by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SelectorData : {}", id);
        selectorDataRepository.delete(id);
        selectorDataSearchRepository.delete(id);
    }

    /**
     * Search for the selectorData corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SelectorData> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SelectorData for query {}", query);
        return selectorDataSearchRepository.search(queryStringQuery(query), pageable);
    }
    @Transactional(readOnly = true)
    public List<SelectorData> findByTypeKeyAndParentId(String typeName,Long parentid) {


        if(parentid==null)

            return  selectorDataRepository.findByTypeKey(typeName);
        else
        return selectorDataRepository.findByTypeKeyAndParentId(typeName, parentid);
    }

    public List<SelectorData> sampleQuery(String p) {

        List<SelectorData> l = em.createQuery("select sd from SelectorData sd where sd.parent.text like :p ").setParameter("p",p).getResultList();

       return l;
    }





}
