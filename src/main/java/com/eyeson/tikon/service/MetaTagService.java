package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.MetaTag;
import com.eyeson.tikon.repository.MetaTagRepository;
import com.eyeson.tikon.repository.search.MetaTagSearchRepository;
import com.eyeson.tikon.web.rest.dto.MetaTagDTO;
import com.eyeson.tikon.web.rest.mapper.MetaTagMapper;
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
 * Service Implementation for managing MetaTag.
 */
@Service
@Transactional
public class MetaTagService {

    private final Logger log = LoggerFactory.getLogger(MetaTagService.class);
    
    @Inject
    private MetaTagRepository metaTagRepository;
    
    @Inject
    private MetaTagMapper metaTagMapper;
    
    @Inject
    private MetaTagSearchRepository metaTagSearchRepository;
    
    /**
     * Save a metaTag.
     * 
     * @param metaTagDTO the entity to save
     * @return the persisted entity
     */
    public MetaTagDTO save(MetaTagDTO metaTagDTO) {
        log.debug("Request to save MetaTag : {}", metaTagDTO);
        MetaTag metaTag = metaTagMapper.metaTagDTOToMetaTag(metaTagDTO);
        metaTag = metaTagRepository.save(metaTag);
        MetaTagDTO result = metaTagMapper.metaTagToMetaTagDTO(metaTag);
        metaTagSearchRepository.save(metaTag);
        return result;
    }

    /**
     *  Get all the metaTags.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<MetaTag> findAll(Pageable pageable) {
        log.debug("Request to get all MetaTags");
        Page<MetaTag> result = metaTagRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one metaTag by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MetaTagDTO findOne(Long id) {
        log.debug("Request to get MetaTag : {}", id);
        MetaTag metaTag = metaTagRepository.findOne(id);
        MetaTagDTO metaTagDTO = metaTagMapper.metaTagToMetaTagDTO(metaTag);
        return metaTagDTO;
    }

    /**
     *  Delete the  metaTag by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MetaTag : {}", id);
        metaTagRepository.delete(id);
        metaTagSearchRepository.delete(id);
    }

    /**
     * Search for the metaTag corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MetaTag> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MetaTags for query {}", query);
        return metaTagSearchRepository.search(queryStringQuery(query), pageable);
    }
}
