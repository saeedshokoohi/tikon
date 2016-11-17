package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.PersonInfo;
import com.eyeson.tikon.repository.PersonInfoRepository;
import com.eyeson.tikon.service.PersonInfoService;
import com.eyeson.tikon.repository.search.PersonInfoSearchRepository;
import com.eyeson.tikon.web.rest.dto.PersonInfoDTO;
import com.eyeson.tikon.web.rest.mapper.PersonInfoMapper;

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
 * Test class for the PersonInfoResource REST controller.
 *
 * @see PersonInfoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class PersonInfoResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NATIONAL_CODE = "AAAAA";
    private static final String UPDATED_NATIONAL_CODE = "BBBBB";
    private static final String DEFAULT_FISRT_NAME = "AAAAA";
    private static final String UPDATED_FISRT_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final Boolean DEFAULT_GENDER = false;
    private static final Boolean UPDATED_GENDER = true;
    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    private static final ZonedDateTime DEFAULT_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BIRTH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BIRTH_DATE_STR = dateTimeFormatter.format(DEFAULT_BIRTH_DATE);

    @Inject
    private PersonInfoRepository personInfoRepository;

    @Inject
    private PersonInfoMapper personInfoMapper;

    @Inject
    private PersonInfoService personInfoService;

    @Inject
    private PersonInfoSearchRepository personInfoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPersonInfoMockMvc;

    private PersonInfo personInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonInfoResource personInfoResource = new PersonInfoResource();
        ReflectionTestUtils.setField(personInfoResource, "personInfoService", personInfoService);
        ReflectionTestUtils.setField(personInfoResource, "personInfoMapper", personInfoMapper);
        this.restPersonInfoMockMvc = MockMvcBuilders.standaloneSetup(personInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        personInfoSearchRepository.deleteAll();
        personInfo = new PersonInfo();
        personInfo.setNationalCode(DEFAULT_NATIONAL_CODE);
        personInfo.setFisrtName(DEFAULT_FISRT_NAME);
        personInfo.setLastName(DEFAULT_LAST_NAME);
        personInfo.setGender(DEFAULT_GENDER);
        personInfo.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        personInfo.setBirthDate(DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void createPersonInfo() throws Exception {
        int databaseSizeBeforeCreate = personInfoRepository.findAll().size();

        // Create the PersonInfo
        PersonInfoDTO personInfoDTO = personInfoMapper.personInfoToPersonInfoDTO(personInfo);

        restPersonInfoMockMvc.perform(post("/api/person-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the PersonInfo in the database
        List<PersonInfo> personInfos = personInfoRepository.findAll();
        assertThat(personInfos).hasSize(databaseSizeBeforeCreate + 1);
        PersonInfo testPersonInfo = personInfos.get(personInfos.size() - 1);
        assertThat(testPersonInfo.getNationalCode()).isEqualTo(DEFAULT_NATIONAL_CODE);
        assertThat(testPersonInfo.getFisrtName()).isEqualTo(DEFAULT_FISRT_NAME);
        assertThat(testPersonInfo.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPersonInfo.isGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPersonInfo.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPersonInfo.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);

        // Validate the PersonInfo in ElasticSearch
        PersonInfo personInfoEs = personInfoSearchRepository.findOne(testPersonInfo.getId());
        assertThat(personInfoEs).isEqualToComparingFieldByField(testPersonInfo);
    }

    @Test
    @Transactional
    public void getAllPersonInfos() throws Exception {
        // Initialize the database
        personInfoRepository.saveAndFlush(personInfo);

        // Get all the personInfos
        restPersonInfoMockMvc.perform(get("/api/person-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(personInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].nationalCode").value(hasItem(DEFAULT_NATIONAL_CODE.toString())))
                .andExpect(jsonPath("$.[*].fisrtName").value(hasItem(DEFAULT_FISRT_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.booleanValue())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE_STR)));
    }

    @Test
    @Transactional
    public void getPersonInfo() throws Exception {
        // Initialize the database
        personInfoRepository.saveAndFlush(personInfo);

        // Get the personInfo
        restPersonInfoMockMvc.perform(get("/api/person-infos/{id}", personInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(personInfo.getId().intValue()))
            .andExpect(jsonPath("$.nationalCode").value(DEFAULT_NATIONAL_CODE.toString()))
            .andExpect(jsonPath("$.fisrtName").value(DEFAULT_FISRT_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.booleanValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPersonInfo() throws Exception {
        // Get the personInfo
        restPersonInfoMockMvc.perform(get("/api/person-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonInfo() throws Exception {
        // Initialize the database
        personInfoRepository.saveAndFlush(personInfo);
        personInfoSearchRepository.save(personInfo);
        int databaseSizeBeforeUpdate = personInfoRepository.findAll().size();

        // Update the personInfo
        PersonInfo updatedPersonInfo = new PersonInfo();
        updatedPersonInfo.setId(personInfo.getId());
        updatedPersonInfo.setNationalCode(UPDATED_NATIONAL_CODE);
        updatedPersonInfo.setFisrtName(UPDATED_FISRT_NAME);
        updatedPersonInfo.setLastName(UPDATED_LAST_NAME);
        updatedPersonInfo.setGender(UPDATED_GENDER);
        updatedPersonInfo.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedPersonInfo.setBirthDate(UPDATED_BIRTH_DATE);
        PersonInfoDTO personInfoDTO = personInfoMapper.personInfoToPersonInfoDTO(updatedPersonInfo);

        restPersonInfoMockMvc.perform(put("/api/person-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personInfoDTO)))
                .andExpect(status().isOk());

        // Validate the PersonInfo in the database
        List<PersonInfo> personInfos = personInfoRepository.findAll();
        assertThat(personInfos).hasSize(databaseSizeBeforeUpdate);
        PersonInfo testPersonInfo = personInfos.get(personInfos.size() - 1);
        assertThat(testPersonInfo.getNationalCode()).isEqualTo(UPDATED_NATIONAL_CODE);
        assertThat(testPersonInfo.getFisrtName()).isEqualTo(UPDATED_FISRT_NAME);
        assertThat(testPersonInfo.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPersonInfo.isGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPersonInfo.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPersonInfo.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);

        // Validate the PersonInfo in ElasticSearch
        PersonInfo personInfoEs = personInfoSearchRepository.findOne(testPersonInfo.getId());
        assertThat(personInfoEs).isEqualToComparingFieldByField(testPersonInfo);
    }

    @Test
    @Transactional
    public void deletePersonInfo() throws Exception {
        // Initialize the database
        personInfoRepository.saveAndFlush(personInfo);
        personInfoSearchRepository.save(personInfo);
        int databaseSizeBeforeDelete = personInfoRepository.findAll().size();

        // Get the personInfo
        restPersonInfoMockMvc.perform(delete("/api/person-infos/{id}", personInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean personInfoExistsInEs = personInfoSearchRepository.exists(personInfo.getId());
        assertThat(personInfoExistsInEs).isFalse();

        // Validate the database is empty
        List<PersonInfo> personInfos = personInfoRepository.findAll();
        assertThat(personInfos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPersonInfo() throws Exception {
        // Initialize the database
        personInfoRepository.saveAndFlush(personInfo);
        personInfoSearchRepository.save(personInfo);

        // Search the personInfo
        restPersonInfoMockMvc.perform(get("/api/_search/person-infos?query=id:" + personInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nationalCode").value(hasItem(DEFAULT_NATIONAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].fisrtName").value(hasItem(DEFAULT_FISRT_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.booleanValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE_STR)));
    }
}
