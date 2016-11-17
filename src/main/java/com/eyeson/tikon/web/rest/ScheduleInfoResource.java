package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.ScheduleInfo;
import com.eyeson.tikon.service.ScheduleInfoService;
import com.eyeson.tikon.web.rest.dto.TimingInfoDTO;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.ScheduleInfoDTO;
import com.eyeson.tikon.web.rest.mapper.ScheduleInfoMapper;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ScheduleInfo.
 */
@RestController
@RequestMapping("/api")
public class ScheduleInfoResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleInfoResource.class);

    @Inject
    private ScheduleInfoService scheduleInfoService;

    @Inject
    private ScheduleInfoMapper scheduleInfoMapper;

    /**
     * POST  /schedule-infos : Create a new scheduleInfo.
     *
     * @param scheduleInfoDTO the scheduleInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduleInfoDTO, or with status 400 (Bad Request) if the scheduleInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/schedule-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduleInfoDTO> createScheduleInfo(@RequestBody ScheduleInfoDTO scheduleInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ScheduleInfo : {}", scheduleInfoDTO);
        if (scheduleInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("scheduleInfo", "idexists", "A new scheduleInfo cannot already have an ID")).body(null);
        }
        ScheduleInfoDTO result = scheduleInfoService.save(scheduleInfoDTO);
        return ResponseEntity.created(new URI("/api/schedule-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scheduleInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schedule-infos : Updates an existing scheduleInfo.
     *
     * @param scheduleInfoDTO the scheduleInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scheduleInfoDTO,
     * or with status 400 (Bad Request) if the scheduleInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the scheduleInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/schedule-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduleInfoDTO> updateScheduleInfo(@RequestBody ScheduleInfoDTO scheduleInfoDTO) throws URISyntaxException {
        log.debug("REST request to update ScheduleInfo : {}", scheduleInfoDTO);
        if (scheduleInfoDTO.getId() == null) {
            return createScheduleInfo(scheduleInfoDTO);
        }
        ScheduleInfoDTO result = scheduleInfoService.save(scheduleInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scheduleInfo", scheduleInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schedule-infos : get all the scheduleInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scheduleInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/schedule-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ScheduleInfoDTO>> getAllScheduleInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ScheduleInfos");
        Page<ScheduleInfo> page = scheduleInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schedule-infos");
        return new ResponseEntity<>(scheduleInfoMapper.scheduleInfosToScheduleInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /schedule-infos/:id : get the "id" scheduleInfo.
     *
     * @param id the id of the scheduleInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scheduleInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/schedule-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ScheduleInfoDTO> getScheduleInfo(@PathVariable Long id) {
        log.debug("REST request to get ScheduleInfo : {}", id);
        ScheduleInfoDTO scheduleInfoDTO = scheduleInfoService.findOne(id);
        scheduleInfoDTO.setTimingInfo(scheduleInfoService.fillCurrentTimingInfo(scheduleInfoDTO));

        return Optional.ofNullable(scheduleInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/generateTimingInfo",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TimingInfoDTO> generateTimingInfo(@RequestBody ScheduleInfoDTO scheduleInfoDTO) throws URISyntaxException {
        log.debug("REST request to make TimingInfo : {}", scheduleInfoDTO);

        TimingInfoDTO result = scheduleInfoService.getTimingInfo(scheduleInfoDTO);
        return ResponseEntity.created(new URI("/api/generateTimingInfo/" ))
//            .headers(HeaderUtil.createEntityCreationAlert("scheduleInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * DELETE  /schedule-infos/:id : delete the "id" scheduleInfo.
     *
     * @param id the id of the scheduleInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/schedule-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScheduleInfo(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleInfo : {}", id);
        scheduleInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scheduleInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/schedule-infos?query=:query : search for the scheduleInfo corresponding
     * to the query.
     *
     * @param query the query of the scheduleInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/schedule-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ScheduleInfoDTO>> searchScheduleInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ScheduleInfos for query {}", query);
        Page<ScheduleInfo> page = scheduleInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/schedule-infos");
        return new ResponseEntity<>(scheduleInfoMapper.scheduleInfosToScheduleInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }
    @RequestMapping(value = "/primary-schedule-info-by-service-item/{serviceItemId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public List<ScheduleInfoDTO> getPrimaryScheduleInfoByServiceItem(@PathVariable("serviceItemId") String serviceItemId)
    {
        List<ScheduleInfoDTO> retList=new ArrayList<>();
        List<ScheduleInfo> schInfoList = scheduleInfoService.getPrimaryScheduleInfoByServiceItem(Long.parseLong(serviceItemId));
        for(ScheduleInfo sch:schInfoList)
        {
            retList.add(scheduleInfoMapper.scheduleInfoToScheduleInfoDTO(sch));
        }

        return  retList;
    }

}
