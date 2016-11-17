package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.NotificationSetting;
import com.eyeson.tikon.service.NotificationSettingService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.NotificationSettingDTO;
import com.eyeson.tikon.web.rest.mapper.NotificationSettingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing NotificationSetting.
 */
@RestController
@RequestMapping("/api")
public class NotificationSettingResource {

    private final Logger log = LoggerFactory.getLogger(NotificationSettingResource.class);
        
    @Inject
    private NotificationSettingService notificationSettingService;
    
    @Inject
    private NotificationSettingMapper notificationSettingMapper;
    
    /**
     * POST  /notification-settings : Create a new notificationSetting.
     *
     * @param notificationSettingDTO the notificationSettingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationSettingDTO, or with status 400 (Bad Request) if the notificationSetting has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-settings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationSettingDTO> createNotificationSetting(@RequestBody NotificationSettingDTO notificationSettingDTO) throws URISyntaxException {
        log.debug("REST request to save NotificationSetting : {}", notificationSettingDTO);
        if (notificationSettingDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("notificationSetting", "idexists", "A new notificationSetting cannot already have an ID")).body(null);
        }
        NotificationSettingDTO result = notificationSettingService.save(notificationSettingDTO);
        return ResponseEntity.created(new URI("/api/notification-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("notificationSetting", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-settings : Updates an existing notificationSetting.
     *
     * @param notificationSettingDTO the notificationSettingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationSettingDTO,
     * or with status 400 (Bad Request) if the notificationSettingDTO is not valid,
     * or with status 500 (Internal Server Error) if the notificationSettingDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-settings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationSettingDTO> updateNotificationSetting(@RequestBody NotificationSettingDTO notificationSettingDTO) throws URISyntaxException {
        log.debug("REST request to update NotificationSetting : {}", notificationSettingDTO);
        if (notificationSettingDTO.getId() == null) {
            return createNotificationSetting(notificationSettingDTO);
        }
        NotificationSettingDTO result = notificationSettingService.save(notificationSettingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("notificationSetting", notificationSettingDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-settings : get all the notificationSettings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notificationSettings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/notification-settings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<NotificationSettingDTO>> getAllNotificationSettings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of NotificationSettings");
        Page<NotificationSetting> page = notificationSettingService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notification-settings");
        return new ResponseEntity<>(notificationSettingMapper.notificationSettingsToNotificationSettingDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /notification-settings/:id : get the "id" notificationSetting.
     *
     * @param id the id of the notificationSettingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationSettingDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/notification-settings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationSettingDTO> getNotificationSetting(@PathVariable Long id) {
        log.debug("REST request to get NotificationSetting : {}", id);
        NotificationSettingDTO notificationSettingDTO = notificationSettingService.findOne(id);
        return Optional.ofNullable(notificationSettingDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notification-settings/:id : delete the "id" notificationSetting.
     *
     * @param id the id of the notificationSettingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/notification-settings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNotificationSetting(@PathVariable Long id) {
        log.debug("REST request to delete NotificationSetting : {}", id);
        notificationSettingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("notificationSetting", id.toString())).build();
    }

    /**
     * SEARCH  /_search/notification-settings?query=:query : search for the notificationSetting corresponding
     * to the query.
     *
     * @param query the query of the notificationSetting search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/notification-settings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<NotificationSettingDTO>> searchNotificationSettings(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of NotificationSettings for query {}", query);
        Page<NotificationSetting> page = notificationSettingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/notification-settings");
        return new ResponseEntity<>(notificationSettingMapper.notificationSettingsToNotificationSettingDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
