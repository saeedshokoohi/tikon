package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.LocationInfo;
import com.eyeson.tikon.repository.LocationInfoRepository;
import com.eyeson.tikon.service.LocationInfoService;
import com.eyeson.tikon.repository.search.LocationInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.LocationInfoDTO;
import com.eyeson.tikon.web.rest.mapper.LocationInfoMapper;

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
 * Test class for the LocationInfoResource REST controller.
 *
 * @see LocationInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class LocationInfoResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_MAP_X = "AAAAA";
    private static final String UPDATED_MAP_X = "BBBBB";
    private static final String DEFAULT_MAP_Y = "AAAAA";
    private static final String UPDATED_MAP_Y = "BBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Inject
    private LocationInfoRepository locationInfoRepository;

    @Inject
    private LocationInfoMapper locationInfoMapper;

    @Inject
    private LocationInfoService locationInfoService;

    @Inject
    private LocationInfoSearchRepository locationInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLocationInfoMockMvc;

    private LocationInfo locationInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocationInfoResource locationInfoResource = new LocationInfoResource();
        ReflectionTestUtils.setField(locationInfoResource, "locationInfoService", locationInfoService);
        ReflectionTestUtils.setField(locationInfoResource, "locationInfoMapper", locationInfoMapper);
        this.restLocationInfoMockMvc = MockMvcBuilders.standaloneSetup(locationInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        locationInfoSearchRepository.deleteAll();
        locationInfo = new LocationInfo();
        locationInfo.setTitle(DEFAULT_TITLE);
        locationInfo.setAddress(DEFAULT_ADDRESS);
        locationInfo.setMapX(DEFAULT_MAP_X);
        locationInfo.setMapY(DEFAULT_MAP_Y);
        locationInfo.setIsActive(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createLocationInfo() throws Exception {
        int databaseSizeBeforeCreate = locationInfoRepository.findAll().size();

        // Create the LocationInfo
        LocationInfoDTO locationInfoDTO = locationInfoMapper.locationInfoToLocationInfoDTO(locationInfo);

        restLocationInfoMockMvc.perform(post("/api/location-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locationInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the LocationInfo in the database
        List<LocationInfo> locationInfos = locationInfoRepository.findAll();
        assertThat(locationInfos).hasSize(databaseSizeBeforeCreate + 1);
        LocationInfo testLocationInfo = locationInfos.get(locationInfos.size() - 1);
        assertThat(testLocationInfo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLocationInfo.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testLocationInfo.getMapX()).isEqualTo(DEFAULT_MAP_X);
        assertThat(testLocationInfo.getMapY()).isEqualTo(DEFAULT_MAP_Y);
        assertThat(testLocationInfo.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);

        // Validate the LocationInfo in ElasticSearch
        LocationInfo locationInfoEs = locationInfoSearchRepository.findOne(testLocationInfo.getId());
        assertThat(locationInfoEs).isEqualToComparingFieldByField(testLocationInfo);
    }

    @Test
    @Transactional
    public void getAllLocationInfos() throws Exception {
        // Initialize the database
        locationInfoRepository.saveAndFlush(locationInfo);

        // Get all the locationInfos
        restLocationInfoMockMvc.perform(get("/api/location-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(locationInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].mapX").value(hasItem(DEFAULT_MAP_X.toString())))
                .andExpect(jsonPath("$.[*].mapY").value(hasItem(DEFAULT_MAP_Y.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getLocationInfo() throws Exception {
        // Initialize the database
        locationInfoRepository.saveAndFlush(locationInfo);

        // Get the locationInfo
        restLocationInfoMockMvc.perform(get("/api/location-infos/{id}", locationInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(locationInfo.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.mapX").value(DEFAULT_MAP_X.toString()))
            .andExpect(jsonPath("$.mapY").value(DEFAULT_MAP_Y.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLocationInfo() throws Exception {
        // Get the locationInfo
        restLocationInfoMockMvc.perform(get("/api/location-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocationInfo() throws Exception {
        // Initialize the database
        locationInfoRepository.saveAndFlush(locationInfo);
        locationInfoSearchRepository.save(locationInfo);
        int databaseSizeBeforeUpdate = locationInfoRepository.findAll().size();

        // Update the locationInfo
        LocationInfo updatedLocationInfo = new LocationInfo();
        updatedLocationInfo.setId(locationInfo.getId());
        updatedLocationInfo.setTitle(UPDATED_TITLE);
        updatedLocationInfo.setAddress(UPDATED_ADDRESS);
        updatedLocationInfo.setMapX(UPDATED_MAP_X);
        updatedLocationInfo.setMapY(UPDATED_MAP_Y);
        updatedLocationInfo.setIsActive(UPDATED_IS_ACTIVE);
        LocationInfoDTO locationInfoDTO = locationInfoMapper.locationInfoToLocationInfoDTO(updatedLocationInfo);

        restLocationInfoMockMvc.perform(put("/api/location-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(locationInfoDTO)))
                .andExpect(status().isOk());

        // Validate the LocationInfo in the database
        List<LocationInfo> locationInfos = locationInfoRepository.findAll();
        assertThat(locationInfos).hasSize(databaseSizeBeforeUpdate);
        LocationInfo testLocationInfo = locationInfos.get(locationInfos.size() - 1);
        assertThat(testLocationInfo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLocationInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testLocationInfo.getMapX()).isEqualTo(UPDATED_MAP_X);
        assertThat(testLocationInfo.getMapY()).isEqualTo(UPDATED_MAP_Y);
        assertThat(testLocationInfo.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);

        // Validate the LocationInfo in ElasticSearch
        LocationInfo locationInfoEs = locationInfoSearchRepository.findOne(testLocationInfo.getId());
        assertThat(locationInfoEs).isEqualToComparingFieldByField(testLocationInfo);
    }

    @Test
    @Transactional
    public void deleteLocationInfo() throws Exception {
        // Initialize the database
        locationInfoRepository.saveAndFlush(locationInfo);
        locationInfoSearchRepository.save(locationInfo);
        int databaseSizeBeforeDelete = locationInfoRepository.findAll().size();

        // Get the locationInfo
        restLocationInfoMockMvc.perform(delete("/api/location-infos/{id}", locationInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean locationInfoExistsInEs = locationInfoSearchRepository.exists(locationInfo.getId());
        assertThat(locationInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<LocationInfo> locationInfos = locationInfoRepository.findAll();
        assertThat(locationInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLocationInfo() throws Exception {
        // Initialize the database
        locationInfoRepository.saveAndFlush(locationInfo);
        locationInfoSearchRepository.save(locationInfo);

        // Search the locationInfo
        restLocationInfoMockMvc.perform(get("/api/_search/location-infos?query=id:" + locationInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].mapX").value(hasItem(DEFAULT_MAP_X.toString())))
            .andExpect(jsonPath("$.[*].mapY").value(hasItem(DEFAULT_MAP_Y.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }
}
