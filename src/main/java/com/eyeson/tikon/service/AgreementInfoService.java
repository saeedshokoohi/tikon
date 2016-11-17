package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.AgreementInfo;
import com.eyeson.tikon.repository.AgreementInfoRepository;
import com.eyeson.tikon.repository.search.AgreementInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.AgreementInfoDTO;
import com.eyeson.tikon.web.rest.mapper.AgreementInfoMapper;
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
 * Service Implementation for managing AgreementInfo.
 */
@Service
@Transactional
public class AgreementInfoService {

    private final Logger log = LoggerFactory.getLogger(AgreementInfoService.class);

    @Inject
    private AgreementInfoRepository agreementInfoRepository;

    @Inject
    private AgreementInfoMapper agreementInfoMapper;

    @Inject
    private AgreementInfoSearchRepository agreementInfoSearchRepository;

    /**
     * Save a agreementInfo.
     *
     * @param agreementInfoDTO the entity to save
     * @return the persisted entity
     */
    public AgreementInfoDTO save(AgreementInfoDTO agreementInfoDTO) {
        log.debug("Request to save AgreementInfo : {}", agreementInfoDTO);
        AgreementInfo agreementInfo = agreementInfoMapper.agreementInfoDTOToAgreementInfo(agreementInfoDTO);
        agreementInfo = agreementInfoRepository.save(agreementInfo);
        AgreementInfoDTO result = agreementInfoMapper.agreementInfoToAgreementInfoDTO(agreementInfo);
        agreementInfoSearchRepository.save(agreementInfo);
        return result;
    }

    /**
     *  Get all the agreementInfos.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AgreementInfo> findAll(Pageable pageable) {
        log.debug("Request to get all AgreementInfos");
        Page<AgreementInfo> result = agreementInfoRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one agreementInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AgreementInfoDTO findOne(Long id) {
        log.debug("Request to get AgreementInfo : {}", id);
        AgreementInfo agreementInfo = agreementInfoRepository.findOne(id);
        AgreementInfoDTO agreementInfoDTO = agreementInfoMapper.agreementInfoToAgreementInfoDTO(agreementInfo);
        return agreementInfoDTO;
    }

    /**
     *  Delete the  agreementInfo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AgreementInfo : {}", id);
        agreementInfoRepository.delete(id);
        agreementInfoSearchRepository.delete(id);
    }

    /**
     * Search for the agreementInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AgreementInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AgreementInfos for query {}", query);
        return agreementInfoSearchRepository.search(queryStringQuery(query), pageable);
    }

}
