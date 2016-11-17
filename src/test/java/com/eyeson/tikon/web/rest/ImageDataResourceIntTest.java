package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ImageData;
import com.eyeson.tikon.repository.ImageDataRepository;
import com.eyeson.tikon.service.ImageDataService;
import com.eyeson.tikon.repository.search.ImageDataSearchRepository;
import com.eyeson.tikon.web.rest.dto.ImageDataDTO;
import com.eyeson.tikon.web.rest.mapper.ImageDataMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ImageDataResource REST controller.
 *
 * @see ImageDataResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ImageDataResourceIntTest {

    private static final String DEFAULT_FILE_NAME = "AAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBB";
    private static final String DEFAULT_CAPTION = "AAAAA";
    private static final String UPDATED_CAPTION = "BBBBB";
    private static final String DEFAULT_FILE_TYPE = "AAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBB";

    private static final byte[] DEFAULT_FILE_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_DATA = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_DATA_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_THUMBNAIL_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL_DATA = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_THUMBNAIL_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_DATA_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_IS_COVER_IMAGE = false;
    private static final Boolean UPDATED_IS_COVER_IMAGE = true;

    @Inject
    private ImageDataRepository imageDataRepository;

    @Inject
    private ImageDataMapper imageDataMapper;

    @Inject
    private ImageDataService imageDataService;

    @Inject
    private ImageDataSearchRepository imageDataSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restImageDataMockMvc;

    private ImageData imageData;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImageDataResource imageDataResource = new ImageDataResource();
        ReflectionTestUtils.setField(imageDataResource, "imageDataService", imageDataService);
        ReflectionTestUtils.setField(imageDataResource, "imageDataMapper", imageDataMapper);
        this.restImageDataMockMvc = MockMvcBuilders.standaloneSetup(imageDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        imageDataSearchRepository.deleteAll();
        imageData = new ImageData();
        imageData.setFileName(DEFAULT_FILE_NAME);
        imageData.setCaption(DEFAULT_CAPTION);
        imageData.setFileType(DEFAULT_FILE_TYPE);
        imageData.setFileData(DEFAULT_FILE_DATA);
        imageData.setFileDataContentType(DEFAULT_FILE_DATA_CONTENT_TYPE);
        imageData.setThumbnailData(DEFAULT_THUMBNAIL_DATA);
        imageData.setThumbnailDataContentType(DEFAULT_THUMBNAIL_DATA_CONTENT_TYPE);
        imageData.setIsCoverImage(DEFAULT_IS_COVER_IMAGE);
    }

    @Test
    @Transactional
    public void createImageData() throws Exception {
        int databaseSizeBeforeCreate = imageDataRepository.findAll().size();

        // Create the ImageData
        ImageDataDTO imageDataDTO = imageDataMapper.imageDataToImageDataDTO(imageData);

        restImageDataMockMvc.perform(post("/api/image-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageDataDTO)))
                .andExpect(status().isCreated());

        // Validate the ImageData in the database
        List<ImageData> imageData = imageDataRepository.findAll();
        assertThat(imageData).hasSize(databaseSizeBeforeCreate + 1);
        ImageData testImageData = imageData.get(imageData.size() - 1);
        assertThat(testImageData.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testImageData.getCaption()).isEqualTo(DEFAULT_CAPTION);
        assertThat(testImageData.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
        assertThat(testImageData.getFileData()).isEqualTo(DEFAULT_FILE_DATA);
        assertThat(testImageData.getFileDataContentType()).isEqualTo(DEFAULT_FILE_DATA_CONTENT_TYPE);
        assertThat(testImageData.getThumbnailData()).isEqualTo(DEFAULT_THUMBNAIL_DATA);
        assertThat(testImageData.getThumbnailDataContentType()).isEqualTo(DEFAULT_THUMBNAIL_DATA_CONTENT_TYPE);
        assertThat(testImageData.isIsCoverImage()).isEqualTo(DEFAULT_IS_COVER_IMAGE);

        // Validate the ImageData in ElasticSearch
        ImageData imageDataEs = imageDataSearchRepository.findOne(testImageData.getId());
        assertThat(imageDataEs).isEqualToComparingFieldByField(testImageData);
    }

    @Test
    @Transactional
    public void getAllImageData() throws Exception {
        // Initialize the database
        imageDataRepository.saveAndFlush(imageData);

        // Get all the imageData
        restImageDataMockMvc.perform(get("/api/image-data?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(imageData.getId().intValue())))
                .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
                .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION.toString())))
                .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].fileDataContentType").value(hasItem(DEFAULT_FILE_DATA_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].fileData").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_DATA))))
                .andExpect(jsonPath("$.[*].thumbnailDataContentType").value(hasItem(DEFAULT_THUMBNAIL_DATA_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].thumbnailData").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_DATA))))
                .andExpect(jsonPath("$.[*].isCoverImage").value(hasItem(DEFAULT_IS_COVER_IMAGE.booleanValue())));
    }

    @Test
    @Transactional
    public void getImageData() throws Exception {
        // Initialize the database
        imageDataRepository.saveAndFlush(imageData);

        // Get the imageData
        restImageDataMockMvc.perform(get("/api/image-data/{id}", imageData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(imageData.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.caption").value(DEFAULT_CAPTION.toString()))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()))
            .andExpect(jsonPath("$.fileDataContentType").value(DEFAULT_FILE_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileData").value(Base64Utils.encodeToString(DEFAULT_FILE_DATA)))
            .andExpect(jsonPath("$.thumbnailDataContentType").value(DEFAULT_THUMBNAIL_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnailData").value(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_DATA)))
            .andExpect(jsonPath("$.isCoverImage").value(DEFAULT_IS_COVER_IMAGE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingImageData() throws Exception {
        // Get the imageData
        restImageDataMockMvc.perform(get("/api/image-data/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImageData() throws Exception {
        // Initialize the database
        imageDataRepository.saveAndFlush(imageData);
        imageDataSearchRepository.save(imageData);
        int databaseSizeBeforeUpdate = imageDataRepository.findAll().size();

        // Update the imageData
        ImageData updatedImageData = new ImageData();
        updatedImageData.setId(imageData.getId());
        updatedImageData.setFileName(UPDATED_FILE_NAME);
        updatedImageData.setCaption(UPDATED_CAPTION);
        updatedImageData.setFileType(UPDATED_FILE_TYPE);
        updatedImageData.setFileData(UPDATED_FILE_DATA);
        updatedImageData.setFileDataContentType(UPDATED_FILE_DATA_CONTENT_TYPE);
        updatedImageData.setThumbnailData(UPDATED_THUMBNAIL_DATA);
        updatedImageData.setThumbnailDataContentType(UPDATED_THUMBNAIL_DATA_CONTENT_TYPE);
        updatedImageData.setIsCoverImage(UPDATED_IS_COVER_IMAGE);
        ImageDataDTO imageDataDTO = imageDataMapper.imageDataToImageDataDTO(updatedImageData);

        restImageDataMockMvc.perform(put("/api/image-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageDataDTO)))
                .andExpect(status().isOk());

        // Validate the ImageData in the database
        List<ImageData> imageData = imageDataRepository.findAll();
        assertThat(imageData).hasSize(databaseSizeBeforeUpdate);
        ImageData testImageData = imageData.get(imageData.size() - 1);
        assertThat(testImageData.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testImageData.getCaption()).isEqualTo(UPDATED_CAPTION);
        assertThat(testImageData.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testImageData.getFileData()).isEqualTo(UPDATED_FILE_DATA);
        assertThat(testImageData.getFileDataContentType()).isEqualTo(UPDATED_FILE_DATA_CONTENT_TYPE);
        assertThat(testImageData.getThumbnailData()).isEqualTo(UPDATED_THUMBNAIL_DATA);
        assertThat(testImageData.getThumbnailDataContentType()).isEqualTo(UPDATED_THUMBNAIL_DATA_CONTENT_TYPE);
        assertThat(testImageData.isIsCoverImage()).isEqualTo(UPDATED_IS_COVER_IMAGE);

        // Validate the ImageData in ElasticSearch
        ImageData imageDataEs = imageDataSearchRepository.findOne(testImageData.getId());
        assertThat(imageDataEs).isEqualToComparingFieldByField(testImageData);
    }

    @Test
    @Transactional
    public void deleteImageData() throws Exception {
        // Initialize the database
        imageDataRepository.saveAndFlush(imageData);
        imageDataSearchRepository.save(imageData);
        int databaseSizeBeforeDelete = imageDataRepository.findAll().size();

        // Get the imageData
        restImageDataMockMvc.perform(delete("/api/image-data/{id}", imageData.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean imageDataExistsInEs = imageDataSearchRepository.exists(imageData.getId());
        assertThat(imageDataExistsInEs).isFalse();

        // Validate the database is empty
        List<ImageData> imageData = imageDataRepository.findAll();
        assertThat(imageData).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchImageData() throws Exception {
        // Initialize the database
        imageDataRepository.saveAndFlush(imageData);
        imageDataSearchRepository.save(imageData);

        // Search the imageData
        restImageDataMockMvc.perform(get("/api/_search/image-data?query=id:" + imageData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageData.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION.toString())))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileDataContentType").value(hasItem(DEFAULT_FILE_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileData").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_DATA))))
            .andExpect(jsonPath("$.[*].thumbnailDataContentType").value(hasItem(DEFAULT_THUMBNAIL_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnailData").value(hasItem(Base64Utils.encodeToString(DEFAULT_THUMBNAIL_DATA))))
            .andExpect(jsonPath("$.[*].isCoverImage").value(hasItem(DEFAULT_IS_COVER_IMAGE.booleanValue())));
    }
}
