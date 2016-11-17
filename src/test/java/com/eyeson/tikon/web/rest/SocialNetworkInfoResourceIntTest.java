package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.SocialNetworkInfo;
import com.eyeson.tikon.repository.SocialNetworkInfoRepository;
import com.eyeson.tikon.service.SocialNetworkInfoService;
import com.eyeson.tikon.repository.search.SocialNetworkInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.SocialNetworkInfoDTO;
import com.eyeson.tikon.web.rest.mapper.SocialNetworkInfoMapper;

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

import com.eyeson.tikon.domain.enumeration.SocialNetworkType;

/**
 * Test class for the SocialNetworkInfoResource REST controller.
 *
 * @see SocialNetworkInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class SocialNetworkInfoResourceIntTest {


    private static final SocialNetworkType DEFAULT_SOCIAL_NETWORK_TYPE = SocialNetworkType.FACEBOOK;
    private static final SocialNetworkType UPDATED_SOCIAL_NETWORK_TYPE = SocialNetworkType.TWITTER;
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private SocialNetworkInfoRepository socialNetworkInfoRepository;

    @Inject
    private SocialNetworkInfoMapper socialNetworkInfoMapper;

    @Inject
    private SocialNetworkInfoService socialNetworkInfoService;

    @Inject
    private SocialNetworkInfoSearchRepository socialNetworkInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSocialNetworkInfoMockMvc;

    private SocialNetworkInfo socialNetworkInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SocialNetworkInfoResource socialNetworkInfoResource = new SocialNetworkInfoResource();
        ReflectionTestUtils.setField(socialNetworkInfoResource, "socialNetworkInfoService", socialNetworkInfoService);
        ReflectionTestUtils.setField(socialNetworkInfoResource, "socialNetworkInfoMapper", socialNetworkInfoMapper);
        this.restSocialNetworkInfoMockMvc = MockMvcBuilders.standaloneSetup(socialNetworkInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        socialNetworkInfoSearchRepository.deleteAll();
        socialNetworkInfo = new SocialNetworkInfo();
        socialNetworkInfo.setSocialNetworkType(DEFAULT_SOCIAL_NETWORK_TYPE);
        socialNetworkInfo.setTitle(DEFAULT_TITLE);
        socialNetworkInfo.setUrl(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createSocialNetworkInfo() throws Exception {
        int databaseSizeBeforeCreate = socialNetworkInfoRepository.findAll().size();

        // Create the SocialNetworkInfo
        SocialNetworkInfoDTO socialNetworkInfoDTO = socialNetworkInfoMapper.socialNetworkInfoToSocialNetworkInfoDTO(socialNetworkInfo);

        restSocialNetworkInfoMockMvc.perform(post("/api/social-network-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialNetworkInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the SocialNetworkInfo in the database
        List<SocialNetworkInfo> socialNetworkInfos = socialNetworkInfoRepository.findAll();
        assertThat(socialNetworkInfos).hasSize(databaseSizeBeforeCreate + 1);
        SocialNetworkInfo testSocialNetworkInfo = socialNetworkInfos.get(socialNetworkInfos.size() - 1);
        assertThat(testSocialNetworkInfo.getSocialNetworkType()).isEqualTo(DEFAULT_SOCIAL_NETWORK_TYPE);
        assertThat(testSocialNetworkInfo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSocialNetworkInfo.getUrl()).isEqualTo(DEFAULT_URL);

        // Validate the SocialNetworkInfo in ElasticSearch
        SocialNetworkInfo socialNetworkInfoEs = socialNetworkInfoSearchRepository.findOne(testSocialNetworkInfo.getId());
        assertThat(socialNetworkInfoEs).isEqualToComparingFieldByField(testSocialNetworkInfo);
    }

    @Test
    @Transactional
    public void getAllSocialNetworkInfos() throws Exception {
        // Initialize the database
        socialNetworkInfoRepository.saveAndFlush(socialNetworkInfo);

        // Get all the socialNetworkInfos
        restSocialNetworkInfoMockMvc.perform(get("/api/social-network-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(socialNetworkInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].socialNetworkType").value(hasItem(DEFAULT_SOCIAL_NETWORK_TYPE.toString())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getSocialNetworkInfo() throws Exception {
        // Initialize the database
        socialNetworkInfoRepository.saveAndFlush(socialNetworkInfo);

        // Get the socialNetworkInfo
        restSocialNetworkInfoMockMvc.perform(get("/api/social-network-infos/{id}", socialNetworkInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(socialNetworkInfo.getId().intValue()))
            .andExpect(jsonPath("$.socialNetworkType").value(DEFAULT_SOCIAL_NETWORK_TYPE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSocialNetworkInfo() throws Exception {
        // Get the socialNetworkInfo
        restSocialNetworkInfoMockMvc.perform(get("/api/social-network-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSocialNetworkInfo() throws Exception {
        // Initialize the database
        socialNetworkInfoRepository.saveAndFlush(socialNetworkInfo);
        socialNetworkInfoSearchRepository.save(socialNetworkInfo);
        int databaseSizeBeforeUpdate = socialNetworkInfoRepository.findAll().size();

        // Update the socialNetworkInfo
        SocialNetworkInfo updatedSocialNetworkInfo = new SocialNetworkInfo();
        updatedSocialNetworkInfo.setId(socialNetworkInfo.getId());
        updatedSocialNetworkInfo.setSocialNetworkType(UPDATED_SOCIAL_NETWORK_TYPE);
        updatedSocialNetworkInfo.setTitle(UPDATED_TITLE);
        updatedSocialNetworkInfo.setUrl(UPDATED_URL);
        SocialNetworkInfoDTO socialNetworkInfoDTO = socialNetworkInfoMapper.socialNetworkInfoToSocialNetworkInfoDTO(updatedSocialNetworkInfo);

        restSocialNetworkInfoMockMvc.perform(put("/api/social-network-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(socialNetworkInfoDTO)))
                .andExpect(status().isOk());

        // Validate the SocialNetworkInfo in the database
        List<SocialNetworkInfo> socialNetworkInfos = socialNetworkInfoRepository.findAll();
        assertThat(socialNetworkInfos).hasSize(databaseSizeBeforeUpdate);
        SocialNetworkInfo testSocialNetworkInfo = socialNetworkInfos.get(socialNetworkInfos.size() - 1);
        assertThat(testSocialNetworkInfo.getSocialNetworkType()).isEqualTo(UPDATED_SOCIAL_NETWORK_TYPE);
        assertThat(testSocialNetworkInfo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSocialNetworkInfo.getUrl()).isEqualTo(UPDATED_URL);

        // Validate the SocialNetworkInfo in ElasticSearch
        SocialNetworkInfo socialNetworkInfoEs = socialNetworkInfoSearchRepository.findOne(testSocialNetworkInfo.getId());
        assertThat(socialNetworkInfoEs).isEqualToComparingFieldByField(testSocialNetworkInfo);
    }

    @Test
    @Transactional
    public void deleteSocialNetworkInfo() throws Exception {
        // Initialize the database
        socialNetworkInfoRepository.saveAndFlush(socialNetworkInfo);
        socialNetworkInfoSearchRepository.save(socialNetworkInfo);
        int databaseSizeBeforeDelete = socialNetworkInfoRepository.findAll().size();

        // Get the socialNetworkInfo
        restSocialNetworkInfoMockMvc.perform(delete("/api/social-network-infos/{id}", socialNetworkInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean socialNetworkInfoExistsInEs = socialNetworkInfoSearchRepository.exists(socialNetworkInfo.getId());
        assertThat(socialNetworkInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<SocialNetworkInfo> socialNetworkInfos = socialNetworkInfoRepository.findAll();
        assertThat(socialNetworkInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSocialNetworkInfo() throws Exception {
        // Initialize the database
        socialNetworkInfoRepository.saveAndFlush(socialNetworkInfo);
        socialNetworkInfoSearchRepository.save(socialNetworkInfo);

        // Search the socialNetworkInfo
        restSocialNetworkInfoMockMvc.perform(get("/api/_search/social-network-infos?query=id:" + socialNetworkInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialNetworkInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].socialNetworkType").value(hasItem(DEFAULT_SOCIAL_NETWORK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
}
