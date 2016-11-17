package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.PriceInfo;
import com.eyeson.tikon.repository.PriceInfoRepository;
import com.eyeson.tikon.repository.extended.PriceInfoExtendedRepository;
import com.eyeson.tikon.repository.search.PriceInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.PriceInfoDTO;
import com.eyeson.tikon.web.rest.mapper.PriceInfoMapper;
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
 * Service Implementation for managing PriceInfo.
 */
@Service
@Transactional
public class PriceInfoService {

    private final Logger log = LoggerFactory.getLogger(PriceInfoService.class);

    @Inject
    private PriceInfoRepository priceInfoRepository;

    @Inject
    private PriceInfoMapper priceInfoMapper;

    @Inject
    private PriceInfoSearchRepository priceInfoSearchRepository;

    /**
     * Save a priceInfo.
     *
     * @param priceInfoDTO the entity to save
     * @return the persisted entity
     */
    public PriceInfoDTO save(PriceInfoDTO priceInfoDTO) {
        log.debug("Request to save PriceInfo : {}", priceInfoDTO);
        PriceInfo priceInfo = priceInfoMapper.priceInfoDTOToPriceInfo(priceInfoDTO);
        priceInfo = priceInfoRepository.save(priceInfo);
        PriceInfoDTO result = priceInfoMapper.priceInfoToPriceInfoDTO(priceInfo);
        priceInfoSearchRepository.save(priceInfo);
        return result;
    }

    /**
     *  Get all the priceInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PriceInfo> findAll(Pageable pageable) {
        log.debug("Request to get all PriceInfos");
        Page<PriceInfo> result = priceInfoRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one priceInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PriceInfoDTO findOne(Long id) {
        log.debug("Request to get PriceInfo : {}", id);
        PriceInfo priceInfo = priceInfoRepository.findOneWithEagerRelationships(id);
        PriceInfoDTO priceInfoDTO = priceInfoMapper.priceInfoToPriceInfoDTO(priceInfo);
        return priceInfoDTO;
    }

    /**
     *  Delete the  priceInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PriceInfo : {}", id);
        priceInfoRepository.delete(id);
        priceInfoSearchRepository.delete(id);
    }

    /**
     * Search for the priceInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PriceInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PriceInfos for query {}", query);
        return priceInfoSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Inject
    PriceInfoExtendedRepository priceInfoExtendedRepository;
    public List<PriceInfo> getPriceInfosByServiceItem(Long serviceItemId) {
        return priceInfoExtendedRepository.getPriceInfosByServiceItem(serviceItemId) ;
    }
}
