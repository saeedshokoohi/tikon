package com.eyeson.tikon.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyeson.tikon.domain.AgreementInfo;
import com.eyeson.tikon.service.AgreementInfoService;
import com.eyeson.tikon.web.rest.util.HeaderUtil;
import com.eyeson.tikon.web.rest.util.PaginationUtil;
import com.eyeson.tikon.web.rest.dto.AgreementInfoDTO;
import com.eyeson.tikon.web.rest.mapper.AgreementInfoMapper;
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
 * REST controller for managing AgreementInfo.
 */
@RestController
@RequestMapping("/api")
public class AgreementInfoResource {

    private final Logger log = LoggerFactory.getLogger(AgreementInfoResource.class);

    @Inject
    private AgreementInfoService agreementInfoService;

    @Inject
    private AgreementInfoMapper agreementInfoMapper;

    /**
     * POST  /agreement-infos : Create a new agreementInfo.
     *
     * @param agreementInfoDTO the agreementInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agreementInfoDTO, or with status 400 (Bad Request) if the agreementInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/agreement-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AgreementInfoDTO> createAgreementInfo(@RequestBody AgreementInfoDTO agreementInfoDTO) throws URISyntaxException {
        log.debug("REST request to save AgreementInfo : {}", agreementInfoDTO);
        if (agreementInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("agreementInfo", "idexists", "A new agreementInfo cannot already have an ID")).body(null);
        }
        AgreementInfoDTO result = agreementInfoService.save(agreementInfoDTO);
        return ResponseEntity.created(new URI("/api/agreement-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("agreementInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agreement-infos : Updates an existing agreementInfo.
     *
     * @param agreementInfoDTO the agreementInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agreementInfoDTO,
     * or with status 400 (Bad Request) if the agreementInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the agreementInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/agreement-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AgreementInfoDTO> updateAgreementInfo(@RequestBody AgreementInfoDTO agreementInfoDTO) throws URISyntaxException {
        log.debug("REST request to update AgreementInfo : {}", agreementInfoDTO);
        if (agreementInfoDTO.getId() == null) {
            return createAgreementInfo(agreementInfoDTO);
        }
        AgreementInfoDTO result = agreementInfoService.save(agreementInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("agreementInfo", agreementInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agreement-infos : get all the agreementInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of agreementInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/agreement-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AgreementInfoDTO>> getAllAgreementInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of AgreementInfos");
        Page<AgreementInfo> page = agreementInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agreement-infos");
        return new ResponseEntity<>(agreementInfoMapper.agreementInfosToAgreementInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /agreement-infos/:id : get the "id" agreementInfo.
     *
     * @param id the id of the agreementInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agreementInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/agreement-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AgreementInfoDTO> getAgreementInfo(@PathVariable Long id) {
        log.debug("REST request to get AgreementInfo : {}", id);
        AgreementInfoDTO agreementInfoDTO = agreementInfoService.findOne(id);
        return Optional.ofNullable(agreementInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /agreement-infos/:id : delete the "id" agreementInfo.
     *
     * @param id the id of the agreementInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/agreement-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAgreementInfo(@PathVariable Long id) {
        log.debug("REST request to delete AgreementInfo : {}", id);
        agreementInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("agreementInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/agreement-infos?query=:query : search for the agreementInfo corresponding
     * to the query.
     *
     * @param query the query of the agreementInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/agreement-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<AgreementInfoDTO>> searchAgreementInfos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of AgreementInfos for query {}", query);
        Page<AgreementInfo> page = agreementInfoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/agreement-infos");
        return new ResponseEntity<>(agreementInfoMapper.agreementInfosToAgreementInfoDTOs(page.getContent()), headers, HttpStatus.OK);
    }

//    @RequestMapping(value = "/agreement-info-by-service-item/{serviceItemId}",
//        method = RequestMethod.GET,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Transactional(readOnly = true)
//    public AgreementInfoDTO getAgreementInfoByServiceItem(@PathVariable("serviceItemId") String serviceItemId)
//    {
//        List<AgreementInfo> aldInfoList = agreementInfoService.getAgreementInfoByServiceItem(Long.parseLong(serviceItemId));
//
//        if(aldInfoList.size()>0)
//        {
//            return agreementInfoMapper.agreementInfoToAgreementInfoDTO(aldInfoList.get(0));
//        }
//        else
//        {
//            return  null;
//
//        }
//    }

}
