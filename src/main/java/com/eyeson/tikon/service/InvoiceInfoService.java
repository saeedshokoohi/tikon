package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.InvoiceInfo;
import com.eyeson.tikon.repository.InvoiceInfoRepository;
import com.eyeson.tikon.repository.search.InvoiceInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.InvoiceInfoDTO;
import com.eyeson.tikon.web.rest.mapper.InvoiceInfoMapper;
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
 * Service Implementation for managing InvoiceInfo.
 */
@Service
@Transactional
public class InvoiceInfoService {

    private final Logger log = LoggerFactory.getLogger(InvoiceInfoService.class);
    
    @Inject
    private InvoiceInfoRepository invoiceInfoRepository;
    
    @Inject
    private InvoiceInfoMapper invoiceInfoMapper;
    
    @Inject
    private InvoiceInfoSearchRepository invoiceInfoSearchRepository;
    
    /**
     * Save a invoiceInfo.
     * 
     * @param invoiceInfoDTO the entity to save
     * @return the persisted entity
     */
    public InvoiceInfoDTO save(InvoiceInfoDTO invoiceInfoDTO) {
        log.debug("Request to save InvoiceInfo : {}", invoiceInfoDTO);
        InvoiceInfo invoiceInfo = invoiceInfoMapper.invoiceInfoDTOToInvoiceInfo(invoiceInfoDTO);
        invoiceInfo = invoiceInfoRepository.save(invoiceInfo);
        InvoiceInfoDTO result = invoiceInfoMapper.invoiceInfoToInvoiceInfoDTO(invoiceInfo);
        invoiceInfoSearchRepository.save(invoiceInfo);
        return result;
    }

    /**
     *  Get all the invoiceInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<InvoiceInfo> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceInfos");
        Page<InvoiceInfo> result = invoiceInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one invoiceInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public InvoiceInfoDTO findOne(Long id) {
        log.debug("Request to get InvoiceInfo : {}", id);
        InvoiceInfo invoiceInfo = invoiceInfoRepository.findOneWithEagerRelationships(id);
        InvoiceInfoDTO invoiceInfoDTO = invoiceInfoMapper.invoiceInfoToInvoiceInfoDTO(invoiceInfo);
        return invoiceInfoDTO;
    }

    /**
     *  Delete the  invoiceInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceInfo : {}", id);
        invoiceInfoRepository.delete(id);
        invoiceInfoSearchRepository.delete(id);
    }

    /**
     * Search for the invoiceInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InvoiceInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InvoiceInfos for query {}", query);
        return invoiceInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
