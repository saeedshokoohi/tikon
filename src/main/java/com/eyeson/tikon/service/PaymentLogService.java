package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.PaymentLog;
import com.eyeson.tikon.repository.PaymentLogRepository;
import com.eyeson.tikon.repository.search.PaymentLogSearchRepository;
import com.eyeson.tikon.web.rest.dto.PaymentLogDTO;
import com.eyeson.tikon.web.rest.mapper.PaymentLogMapper;
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
 * Service Implementation for managing PaymentLog.
 */
@Service
@Transactional
public class PaymentLogService {

    private final Logger log = LoggerFactory.getLogger(PaymentLogService.class);
    
    @Inject
    private PaymentLogRepository paymentLogRepository;
    
    @Inject
    private PaymentLogMapper paymentLogMapper;
    
    @Inject
    private PaymentLogSearchRepository paymentLogSearchRepository;
    
    /**
     * Save a paymentLog.
     * 
     * @param paymentLogDTO the entity to save
     * @return the persisted entity
     */
    public PaymentLogDTO save(PaymentLogDTO paymentLogDTO) {
        log.debug("Request to save PaymentLog : {}", paymentLogDTO);
        PaymentLog paymentLog = paymentLogMapper.paymentLogDTOToPaymentLog(paymentLogDTO);
        paymentLog = paymentLogRepository.save(paymentLog);
        PaymentLogDTO result = paymentLogMapper.paymentLogToPaymentLogDTO(paymentLog);
        paymentLogSearchRepository.save(paymentLog);
        return result;
    }

    /**
     *  Get all the paymentLogs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PaymentLog> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentLogs");
        Page<PaymentLog> result = paymentLogRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one paymentLog by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PaymentLogDTO findOne(Long id) {
        log.debug("Request to get PaymentLog : {}", id);
        PaymentLog paymentLog = paymentLogRepository.findOne(id);
        PaymentLogDTO paymentLogDTO = paymentLogMapper.paymentLogToPaymentLogDTO(paymentLog);
        return paymentLogDTO;
    }

    /**
     *  Delete the  paymentLog by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentLog : {}", id);
        paymentLogRepository.delete(id);
        paymentLogSearchRepository.delete(id);
    }

    /**
     * Search for the paymentLog corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PaymentLog> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentLogs for query {}", query);
        return paymentLogSearchRepository.search(queryStringQuery(query), pageable);
    }
}
