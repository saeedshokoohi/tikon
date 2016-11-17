package com.eyeson.tikon.service;

import com.eyeson.tikon.domain.ImageData;
import com.eyeson.tikon.repository.ImageDataRepository;
import com.eyeson.tikon.repository.search.ImageDataSearchRepository;
import com.eyeson.tikon.web.rest.dto.ImageDataDTO;
import com.eyeson.tikon.web.rest.mapper.ImageDataMapper;
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
 * Service Implementation for managing ImageData.
 */
@Service
@Transactional
public class ImageDataService {

    private final Logger log = LoggerFactory.getLogger(ImageDataService.class);

    @Inject
    private ImageDataRepository imageDataRepository;

    @Inject
    private ImageDataMapper imageDataMapper;

    @Inject
    private ImageDataSearchRepository imageDataSearchRepository;

    /**
     * Save a imageData.
     *
     * @param imageDataDTO the entity to save
     * @return the persisted entity
     */
    public ImageDataDTO save(ImageDataDTO imageDataDTO) {
        log.debug("Request to save ImageData : {}", imageDataDTO);
        ImageData imageData = imageDataMapper.imageDataDTOToImageData(imageDataDTO);
        imageData = imageDataRepository.save(imageData);
        ImageDataDTO result = imageDataMapper.imageDataToImageDataDTO(imageData);
        imageDataSearchRepository.save(imageData);
        return result;
    }

    /**
     *  Get all the imageData.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ImageData> findAll(Pageable pageable) {
        log.debug("Request to get all ImageData");
        Page<ImageData> result = imageDataRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one imageData by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ImageDataDTO findOne(Long id) {
        log.debug("Request to get ImageData : {}", id);
        ImageData imageData = imageDataRepository.findOne(id);
        ImageDataDTO imageDataDTO = imageDataMapper.imageDataToImageDataDTO(imageData);
        return imageDataDTO;
    }

    /**
     *  Delete the  imageData by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ImageData : {}", id);
        imageDataRepository.delete(id);
        imageDataSearchRepository.delete(id);
    }

    /**
     * Search for the imageData corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ImageData> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ImageData for query {}", query);
        return imageDataSearchRepository.search(queryStringQuery(query), pageable);
    }

    public List<ImageData> findByAlbumId(Long albumid) {
        return imageDataRepository.findByAlbumInfoId(albumid);
    }
}
