package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.AlbumInfo;
import com.eyeson.tikon.repository.AlbumInfoRepository;
import com.eyeson.tikon.service.AlbumInfoService;
import com.eyeson.tikon.repository.search.AlbumInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.AlbumInfoDTO;
import com.eyeson.tikon.web.rest.mapper.AlbumInfoMapper;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AlbumInfoResource REST controller.
 *
 * @see AlbumInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class AlbumInfoResourceIntTest {


    private static final Boolean DEFAULT_IS_SINGLE_IMAGE = false;
    private static final Boolean UPDATED_IS_SINGLE_IMAGE = true;
    private static final String DEFAULT_CAPTION = "AAAAA";
    private static final String UPDATED_CAPTION = "BBBBB";

    @Inject
    private AlbumInfoRepository albumInfoRepository;

    @Inject
    private AlbumInfoMapper albumInfoMapper;

    @Inject
    private AlbumInfoService albumInfoService;

    @Inject
    private AlbumInfoSearchRepository albumInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAlbumInfoMockMvc;

    private AlbumInfo albumInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AlbumInfoResource albumInfoResource = new AlbumInfoResource();
        ReflectionTestUtils.setField(albumInfoResource, "albumInfoService", albumInfoService);
        ReflectionTestUtils.setField(albumInfoResource, "albumInfoMapper", albumInfoMapper);
        this.restAlbumInfoMockMvc = MockMvcBuilders.standaloneSetup(albumInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        albumInfoSearchRepository.deleteAll();
        albumInfo = new AlbumInfo();
        albumInfo.setIsSingleImage(DEFAULT_IS_SINGLE_IMAGE);
        albumInfo.setCaption(DEFAULT_CAPTION);
    }

    @Test
    @Transactional
    public void createAlbumInfo() throws Exception {
        int databaseSizeBeforeCreate = albumInfoRepository.findAll().size();

        // Create the AlbumInfo
        AlbumInfoDTO albumInfoDTO = albumInfoMapper.albumInfoToAlbumInfoDTO(albumInfo);

        restAlbumInfoMockMvc.perform(post("/api/album-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(albumInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the AlbumInfo in the database
        List<AlbumInfo> albumInfos = albumInfoRepository.findAll();
        assertThat(albumInfos).hasSize(databaseSizeBeforeCreate + 1);
        AlbumInfo testAlbumInfo = albumInfos.get(albumInfos.size() - 1);
        assertThat(testAlbumInfo.isIsSingleImage()).isEqualTo(DEFAULT_IS_SINGLE_IMAGE);
        assertThat(testAlbumInfo.getCaption()).isEqualTo(DEFAULT_CAPTION);

        // Validate the AlbumInfo in ElasticSearch
        AlbumInfo albumInfoEs = albumInfoSearchRepository.findOne(testAlbumInfo.getId());
        assertThat(albumInfoEs).isEqualToComparingFieldByField(testAlbumInfo);
    }

    @Test
    @Transactional
    public void getAllAlbumInfos() throws Exception {
        // Initialize the database
        albumInfoRepository.saveAndFlush(albumInfo);

        // Get all the albumInfos
        restAlbumInfoMockMvc.perform(get("/api/album-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(albumInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].isSingleImage").value(hasItem(DEFAULT_IS_SINGLE_IMAGE.booleanValue())))
                .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION.toString())));
    }

    @Test
    @Transactional
    public void getAlbumInfo() throws Exception {
        // Initialize the database
        albumInfoRepository.saveAndFlush(albumInfo);

        // Get the albumInfo
        restAlbumInfoMockMvc.perform(get("/api/album-infos/{id}", albumInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(albumInfo.getId().intValue()))
            .andExpect(jsonPath("$.isSingleImage").value(DEFAULT_IS_SINGLE_IMAGE.booleanValue()))
            .andExpect(jsonPath("$.caption").value(DEFAULT_CAPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAlbumInfo() throws Exception {
        // Get the albumInfo
        restAlbumInfoMockMvc.perform(get("/api/album-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAlbumInfo() throws Exception {
        // Initialize the database
        albumInfoRepository.saveAndFlush(albumInfo);
        albumInfoSearchRepository.save(albumInfo);
        int databaseSizeBeforeUpdate = albumInfoRepository.findAll().size();

        // Update the albumInfo
        AlbumInfo updatedAlbumInfo = new AlbumInfo();
        updatedAlbumInfo.setId(albumInfo.getId());
        updatedAlbumInfo.setIsSingleImage(UPDATED_IS_SINGLE_IMAGE);
        updatedAlbumInfo.setCaption(UPDATED_CAPTION);
        AlbumInfoDTO albumInfoDTO = albumInfoMapper.albumInfoToAlbumInfoDTO(updatedAlbumInfo);

        restAlbumInfoMockMvc.perform(put("/api/album-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(albumInfoDTO)))
                .andExpect(status().isOk());

        // Validate the AlbumInfo in the database
        List<AlbumInfo> albumInfos = albumInfoRepository.findAll();
        assertThat(albumInfos).hasSize(databaseSizeBeforeUpdate);
        AlbumInfo testAlbumInfo = albumInfos.get(albumInfos.size() - 1);
        assertThat(testAlbumInfo.isIsSingleImage()).isEqualTo(UPDATED_IS_SINGLE_IMAGE);
        assertThat(testAlbumInfo.getCaption()).isEqualTo(UPDATED_CAPTION);

        // Validate the AlbumInfo in ElasticSearch
        AlbumInfo albumInfoEs = albumInfoSearchRepository.findOne(testAlbumInfo.getId());
        assertThat(albumInfoEs).isEqualToComparingFieldByField(testAlbumInfo);
    }

    @Test
    @Transactional
    public void deleteAlbumInfo() throws Exception {
        // Initialize the database
        albumInfoRepository.saveAndFlush(albumInfo);
        albumInfoSearchRepository.save(albumInfo);
        int databaseSizeBeforeDelete = albumInfoRepository.findAll().size();

        // Get the albumInfo
        restAlbumInfoMockMvc.perform(delete("/api/album-infos/{id}", albumInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean albumInfoExistsInEs = albumInfoSearchRepository.exists(albumInfo.getId());
        assertThat(albumInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<AlbumInfo> albumInfos = albumInfoRepository.findAll();
        assertThat(albumInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAlbumInfo() throws Exception {
        // Initialize the database
        albumInfoRepository.saveAndFlush(albumInfo);
        albumInfoSearchRepository.save(albumInfo);

        // Search the albumInfo
        restAlbumInfoMockMvc.perform(get("/api/_search/album-infos?query=id:" + albumInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(albumInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].isSingleImage").value(hasItem(DEFAULT_IS_SINGLE_IMAGE.booleanValue())))
            .andExpect(jsonPath("$.[*].caption").value(hasItem(DEFAULT_CAPTION.toString())));
    }
}
