package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.WaitingList;
import com.eyeson.tikon.repository.WaitingListRepository;
import com.eyeson.tikon.service.WaitingListService;
import com.eyeson.tikon.repository.search.WaitingListSearchRepository;
import com.eyeson.tikon.web.rest.dto.WaitingListDTO;
import com.eyeson.tikon.web.rest.mapper.WaitingListMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the WaitingListResource REST controller.
 *
 * @see WaitingListResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class WaitingListResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;

    private static final ZonedDateTime DEFAULT_RESERVE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_RESERVE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_RESERVE_TIME_STR = dateTimeFormatter.format(DEFAULT_RESERVE_TIME);
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private WaitingListRepository waitingListRepository;

    @Inject
    private WaitingListMapper waitingListMapper;

    @Inject
    private WaitingListService waitingListService;

    @Inject
    private WaitingListSearchRepository waitingListSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWaitingListMockMvc;

    private WaitingList waitingList;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WaitingListResource waitingListResource = new WaitingListResource();
        ReflectionTestUtils.setField(waitingListResource, "waitingListService", waitingListService);
        ReflectionTestUtils.setField(waitingListResource, "waitingListMapper", waitingListMapper);
        this.restWaitingListMockMvc = MockMvcBuilders.standaloneSetup(waitingListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        waitingListSearchRepository.deleteAll();
        waitingList = new WaitingList();
        waitingList.setQty(DEFAULT_QTY);
        waitingList.setReserveTime(DEFAULT_RESERVE_TIME);
        waitingList.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createWaitingList() throws Exception {
        int databaseSizeBeforeCreate = waitingListRepository.findAll().size();

        // Create the WaitingList
        WaitingListDTO waitingListDTO = waitingListMapper.waitingListToWaitingListDTO(waitingList);

        restWaitingListMockMvc.perform(post("/api/waiting-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(waitingListDTO)))
                .andExpect(status().isCreated());

        // Validate the WaitingList in the database
        List<WaitingList> waitingLists = waitingListRepository.findAll();
        assertThat(waitingLists).hasSize(databaseSizeBeforeCreate + 1);
        WaitingList testWaitingList = waitingLists.get(waitingLists.size() - 1);
        assertThat(testWaitingList.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testWaitingList.getReserveTime()).isEqualTo(DEFAULT_RESERVE_TIME);
        assertThat(testWaitingList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the WaitingList in ElasticSearch
        WaitingList waitingListEs = waitingListSearchRepository.findOne(testWaitingList.getId());
        assertThat(waitingListEs).isEqualToComparingFieldByField(testWaitingList);
    }

    @Test
    @Transactional
    public void getAllWaitingLists() throws Exception {
        // Initialize the database
        waitingListRepository.saveAndFlush(waitingList);

        // Get all the waitingLists
        restWaitingListMockMvc.perform(get("/api/waiting-lists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(waitingList.getId().intValue())))
                .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
                .andExpect(jsonPath("$.[*].reserveTime").value(hasItem(DEFAULT_RESERVE_TIME_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getWaitingList() throws Exception {
        // Initialize the database
        waitingListRepository.saveAndFlush(waitingList);

        // Get the waitingList
        restWaitingListMockMvc.perform(get("/api/waiting-lists/{id}", waitingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(waitingList.getId().intValue()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.reserveTime").value(DEFAULT_RESERVE_TIME_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWaitingList() throws Exception {
        // Get the waitingList
        restWaitingListMockMvc.perform(get("/api/waiting-lists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWaitingList() throws Exception {
        // Initialize the database
        waitingListRepository.saveAndFlush(waitingList);
        waitingListSearchRepository.save(waitingList);
        int databaseSizeBeforeUpdate = waitingListRepository.findAll().size();

        // Update the waitingList
        WaitingList updatedWaitingList = new WaitingList();
        updatedWaitingList.setId(waitingList.getId());
        updatedWaitingList.setQty(UPDATED_QTY);
        updatedWaitingList.setReserveTime(UPDATED_RESERVE_TIME);
        updatedWaitingList.setDescription(UPDATED_DESCRIPTION);
        WaitingListDTO waitingListDTO = waitingListMapper.waitingListToWaitingListDTO(updatedWaitingList);

        restWaitingListMockMvc.perform(put("/api/waiting-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(waitingListDTO)))
                .andExpect(status().isOk());

        // Validate the WaitingList in the database
        List<WaitingList> waitingLists = waitingListRepository.findAll();
        assertThat(waitingLists).hasSize(databaseSizeBeforeUpdate);
        WaitingList testWaitingList = waitingLists.get(waitingLists.size() - 1);
        assertThat(testWaitingList.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testWaitingList.getReserveTime()).isEqualTo(UPDATED_RESERVE_TIME);
        assertThat(testWaitingList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the WaitingList in ElasticSearch
        WaitingList waitingListEs = waitingListSearchRepository.findOne(testWaitingList.getId());
        assertThat(waitingListEs).isEqualToComparingFieldByField(testWaitingList);
    }

    @Test
    @Transactional
    public void deleteWaitingList() throws Exception {
        // Initialize the database
        waitingListRepository.saveAndFlush(waitingList);
        waitingListSearchRepository.save(waitingList);
        int databaseSizeBeforeDelete = waitingListRepository.findAll().size();

        // Get the waitingList
        restWaitingListMockMvc.perform(delete("/api/waiting-lists/{id}", waitingList.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean waitingListExistsInEs = waitingListSearchRepository.exists(waitingList.getId());
        assertThat(waitingListExistsInEs).isFalse();

        // Validate the database is empty
        List<WaitingList> waitingLists = waitingListRepository.findAll();
        assertThat(waitingLists).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWaitingList() throws Exception {
        // Initialize the database
        waitingListRepository.saveAndFlush(waitingList);
        waitingListSearchRepository.save(waitingList);

        // Search the waitingList
        restWaitingListMockMvc.perform(get("/api/_search/waiting-lists?query=id:" + waitingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(waitingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].reserveTime").value(hasItem(DEFAULT_RESERVE_TIME_STR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
