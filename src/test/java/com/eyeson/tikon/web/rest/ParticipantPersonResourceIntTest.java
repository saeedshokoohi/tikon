package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.ParticipantPerson;
import com.eyeson.tikon.repository.ParticipantPersonRepository;
import com.eyeson.tikon.service.ParticipantPersonService;
import com.eyeson.tikon.repository.search.ParticipantPersonSearchRepository;
import com.eyeson.tikon.web.rest.dto.ParticipantPersonDTO;
import com.eyeson.tikon.web.rest.mapper.ParticipantPersonMapper;

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
 * Test class for the ParticipantPersonResource REST controller.
 *
 * @see ParticipantPersonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class ParticipantPersonResourceIntTest {


    @Inject
    private ParticipantPersonRepository participantPersonRepository;

    @Inject
    private ParticipantPersonMapper participantPersonMapper;

    @Inject
    private ParticipantPersonService participantPersonService;

    @Inject
    private ParticipantPersonSearchRepository participantPersonSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restParticipantPersonMockMvc;

    private ParticipantPerson participantPerson;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParticipantPersonResource participantPersonResource = new ParticipantPersonResource();
        ReflectionTestUtils.setField(participantPersonResource, "participantPersonService", participantPersonService);
        ReflectionTestUtils.setField(participantPersonResource, "participantPersonMapper", participantPersonMapper);
        this.restParticipantPersonMockMvc = MockMvcBuilders.standaloneSetup(participantPersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        participantPersonSearchRepository.deleteAll();
        participantPerson = new ParticipantPerson();
    }

    @Test
    @Transactional
    public void createParticipantPerson() throws Exception {
        int databaseSizeBeforeCreate = participantPersonRepository.findAll().size();

        // Create the ParticipantPerson
        ParticipantPersonDTO participantPersonDTO = participantPersonMapper.participantPersonToParticipantPersonDTO(participantPerson);

        restParticipantPersonMockMvc.perform(post("/api/participant-people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(participantPersonDTO)))
                .andExpect(status().isCreated());

        // Validate the ParticipantPerson in the database
        List<ParticipantPerson> participantPeople = participantPersonRepository.findAll();
        assertThat(participantPeople).hasSize(databaseSizeBeforeCreate + 1);
        ParticipantPerson testParticipantPerson = participantPeople.get(participantPeople.size() - 1);

        // Validate the ParticipantPerson in ElasticSearch
        ParticipantPerson participantPersonEs = participantPersonSearchRepository.findOne(testParticipantPerson.getId());
        assertThat(participantPersonEs).isEqualToComparingFieldByField(testParticipantPerson);
    }

    @Test
    @Transactional
    public void getAllParticipantPeople() throws Exception {
        // Initialize the database
        participantPersonRepository.saveAndFlush(participantPerson);

        // Get all the participantPeople
        restParticipantPersonMockMvc.perform(get("/api/participant-people?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(participantPerson.getId().intValue())));
    }

    @Test
    @Transactional
    public void getParticipantPerson() throws Exception {
        // Initialize the database
        participantPersonRepository.saveAndFlush(participantPerson);

        // Get the participantPerson
        restParticipantPersonMockMvc.perform(get("/api/participant-people/{id}", participantPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(participantPerson.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingParticipantPerson() throws Exception {
        // Get the participantPerson
        restParticipantPersonMockMvc.perform(get("/api/participant-people/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipantPerson() throws Exception {
        // Initialize the database
        participantPersonRepository.saveAndFlush(participantPerson);
        participantPersonSearchRepository.save(participantPerson);
        int databaseSizeBeforeUpdate = participantPersonRepository.findAll().size();

        // Update the participantPerson
        ParticipantPerson updatedParticipantPerson = new ParticipantPerson();
        updatedParticipantPerson.setId(participantPerson.getId());
        ParticipantPersonDTO participantPersonDTO = participantPersonMapper.participantPersonToParticipantPersonDTO(updatedParticipantPerson);

        restParticipantPersonMockMvc.perform(put("/api/participant-people")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(participantPersonDTO)))
                .andExpect(status().isOk());

        // Validate the ParticipantPerson in the database
        List<ParticipantPerson> participantPeople = participantPersonRepository.findAll();
        assertThat(participantPeople).hasSize(databaseSizeBeforeUpdate);
        ParticipantPerson testParticipantPerson = participantPeople.get(participantPeople.size() - 1);

        // Validate the ParticipantPerson in ElasticSearch
        ParticipantPerson participantPersonEs = participantPersonSearchRepository.findOne(testParticipantPerson.getId());
        assertThat(participantPersonEs).isEqualToComparingFieldByField(testParticipantPerson);
    }

    @Test
    @Transactional
    public void deleteParticipantPerson() throws Exception {
        // Initialize the database
        participantPersonRepository.saveAndFlush(participantPerson);
        participantPersonSearchRepository.save(participantPerson);
        int databaseSizeBeforeDelete = participantPersonRepository.findAll().size();

        // Get the participantPerson
        restParticipantPersonMockMvc.perform(delete("/api/participant-people/{id}", participantPerson.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean participantPersonExistsInEs = participantPersonSearchRepository.exists(participantPerson.getId());
        assertThat(participantPersonExistsInEs).isFalse();

        // Validate the database is empty
        List<ParticipantPerson> participantPeople = participantPersonRepository.findAll();
        assertThat(participantPeople).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchParticipantPerson() throws Exception {
        // Initialize the database
        participantPersonRepository.saveAndFlush(participantPerson);
        participantPersonSearchRepository.save(participantPerson);

        // Search the participantPerson
        restParticipantPersonMockMvc.perform(get("/api/_search/participant-people?query=id:" + participantPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participantPerson.getId().intValue())));
    }
}
