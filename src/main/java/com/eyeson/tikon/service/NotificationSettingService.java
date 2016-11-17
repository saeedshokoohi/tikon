package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.NotificationSetting;
import com.eyeson.tikon.repository.NotificationSettingRepository;
import com.eyeson.tikon.repository.search.NotificationSettingSearchRepository;
import com.eyeson.tikon.web.rest.dto.NotificationSettingDTO;
import com.eyeson.tikon.web.rest.mapper.NotificationSettingMapper;
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
 * Service Implementation for managing NotificationSetting.
 */
@Service
@Transactional
public class NotificationSettingService {

    private final Logger log = LoggerFactory.getLogger(NotificationSettingService.class);
    
    @Inject
    private NotificationSettingRepository notificationSettingRepository;
    
    @Inject
    private NotificationSettingMapper notificationSettingMapper;
    
    @Inject
    private NotificationSettingSearchRepository notificationSettingSearchRepository;
    
    /**
     * Save a notificationSetting.
     * 
     * @param notificationSettingDTO the entity to save
     * @return the persisted entity
     */
    public NotificationSettingDTO save(NotificationSettingDTO notificationSettingDTO) {
        log.debug("Request to save NotificationSetting : {}", notificationSettingDTO);
        NotificationSetting notificationSetting = notificationSettingMapper.notificationSettingDTOToNotificationSetting(notificationSettingDTO);
        notificationSetting = notificationSettingRepository.save(notificationSetting);
        NotificationSettingDTO result = notificationSettingMapper.notificationSettingToNotificationSettingDTO(notificationSetting);
        notificationSettingSearchRepository.save(notificationSetting);
        return result;
    }

    /**
     *  Get all the notificationSettings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<NotificationSetting> findAll(Pageable pageable) {
        log.debug("Request to get all NotificationSettings");
        Page<NotificationSetting> result = notificationSettingRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one notificationSetting by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public NotificationSettingDTO findOne(Long id) {
        log.debug("Request to get NotificationSetting : {}", id);
        NotificationSetting notificationSetting = notificationSettingRepository.findOne(id);
        NotificationSettingDTO notificationSettingDTO = notificationSettingMapper.notificationSettingToNotificationSettingDTO(notificationSetting);
        return notificationSettingDTO;
    }

    /**
     *  Delete the  notificationSetting by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete NotificationSetting : {}", id);
        notificationSettingRepository.delete(id);
        notificationSettingSearchRepository.delete(id);
    }

    /**
     * Search for the notificationSetting corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NotificationSetting> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of NotificationSettings for query {}", query);
        return notificationSettingSearchRepository.search(queryStringQuery(query), pageable);
    }
}
