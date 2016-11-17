package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.PriceInfoDtail;
import com.eyeson.tikon.domain.ScheduleInfo;
import com.eyeson.tikon.repository.PriceInfoDtailRepository;
import com.eyeson.tikon.repository.extended.PriceInfoDtailExtendedRepository;
import com.eyeson.tikon.repository.extended.ScheduleInfoExtendedRepository;
import com.eyeson.tikon.repository.search.PriceInfoDtailSearchRepository;
import com.eyeson.tikon.web.rest.dto.PriceInfoDtailDTO;
import com.eyeson.tikon.web.rest.mapper.PriceInfoDtailMapper;
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
 * Service Implementation for managing PriceInfoDtail.
 */
@Service
@Transactional
public class PriceInfoDtailService {

    private final Logger log = LoggerFactory.getLogger(PriceInfoDtailService.class);

    @Inject
    private PriceInfoDtailRepository priceInfoDtailRepository;

    @Inject
    private PriceInfoDtailMapper priceInfoDtailMapper;

    @Inject
    private PriceInfoDtailSearchRepository priceInfoDtailSearchRepository;

    /**
     * Save a priceInfoDtail.
     *
     * @param priceInfoDtailDTO the entity to save
     * @return the persisted entity
     */
    public PriceInfoDtailDTO save(PriceInfoDtailDTO priceInfoDtailDTO) {
        log.debug("Request to save PriceInfoDtail : {}", priceInfoDtailDTO);
        PriceInfoDtail priceInfoDtail = priceInfoDtailMapper.priceInfoDtailDTOToPriceInfoDtail(priceInfoDtailDTO);
        priceInfoDtail = priceInfoDtailRepository.save(priceInfoDtail);
        PriceInfoDtailDTO result = priceInfoDtailMapper.priceInfoDtailToPriceInfoDtailDTO(priceInfoDtail);
        priceInfoDtailSearchRepository.save(priceInfoDtail);
        return result;
    }

    /**
     *  Get all the priceInfoDtails.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PriceInfoDtail> findAll(Pageable pageable) {
        log.debug("Request to get all PriceInfoDtails");
        Page<PriceInfoDtail> result = priceInfoDtailRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one priceInfoDtail by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PriceInfoDtailDTO findOne(Long id) {
        log.debug("Request to get PriceInfoDtail : {}", id);
        PriceInfoDtail priceInfoDtail = priceInfoDtailRepository.findOne(id);
        PriceInfoDtailDTO priceInfoDtailDTO = priceInfoDtailMapper.priceInfoDtailToPriceInfoDtailDTO(priceInfoDtail);
        return priceInfoDtailDTO;
    }

    /**
     *  Delete the  priceInfoDtail by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PriceInfoDtail : {}", id);
        priceInfoDtailRepository.delete(id);
        priceInfoDtailSearchRepository.delete(id);
    }

    /**
     * Search for the priceInfoDtail corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PriceInfoDtail> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PriceInfoDtails for query {}", query);
        return priceInfoDtailSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Inject
    PriceInfoDtailExtendedRepository priceInfoDtailExtendedRepository;
    public List<PriceInfoDtail> getPriceInfoDtailsByServiceItem(Long serviceItemId) {
        return priceInfoDtailExtendedRepository.getPriceInfoDtailsByServiceItem(serviceItemId) ;
    }
}
