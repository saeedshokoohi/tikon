package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.AlbumInfo;
import com.eyeson.tikon.repository.AlbumInfoRepository;
import com.eyeson.tikon.repository.search.AlbumInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.AlbumInfoDTO;
import com.eyeson.tikon.web.rest.mapper.AlbumInfoMapper;
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
 * Service Implementation for managing AlbumInfo.
 */
@Service
@Transactional
public class AlbumInfoService {

    private final Logger log = LoggerFactory.getLogger(AlbumInfoService.class);
    
    @Inject
    private AlbumInfoRepository albumInfoRepository;
    
    @Inject
    private AlbumInfoMapper albumInfoMapper;
    
    @Inject
    private AlbumInfoSearchRepository albumInfoSearchRepository;
    
    /**
     * Save a albumInfo.
     * 
     * @param albumInfoDTO the entity to save
     * @return the persisted entity
     */
    public AlbumInfoDTO save(AlbumInfoDTO albumInfoDTO) {
        log.debug("Request to save AlbumInfo : {}", albumInfoDTO);
        AlbumInfo albumInfo = albumInfoMapper.albumInfoDTOToAlbumInfo(albumInfoDTO);
        albumInfo = albumInfoRepository.save(albumInfo);
        AlbumInfoDTO result = albumInfoMapper.albumInfoToAlbumInfoDTO(albumInfo);
        albumInfoSearchRepository.save(albumInfo);
        return result;
    }

    /**
     *  Get all the albumInfos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AlbumInfo> findAll(Pageable pageable) {
        log.debug("Request to get all AlbumInfos");
        Page<AlbumInfo> result = albumInfoRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one albumInfo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AlbumInfoDTO findOne(Long id) {
        log.debug("Request to get AlbumInfo : {}", id);
        AlbumInfo albumInfo = albumInfoRepository.findOne(id);
        AlbumInfoDTO albumInfoDTO = albumInfoMapper.albumInfoToAlbumInfoDTO(albumInfo);
        return albumInfoDTO;
    }

    /**
     *  Delete the  albumInfo by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AlbumInfo : {}", id);
        albumInfoRepository.delete(id);
        albumInfoSearchRepository.delete(id);
    }

    /**
     * Search for the albumInfo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AlbumInfo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AlbumInfos for query {}", query);
        return albumInfoSearchRepository.search(queryStringQuery(query), pageable);
    }
}
