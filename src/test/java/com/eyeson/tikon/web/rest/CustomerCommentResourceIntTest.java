package com.eyeson.tikon.web.rest;

import com.eyeson.tikon.TikonApp;
import com.eyeson.tikon.domain.CustomerComment;
import com.eyeson.tikon.repository.CustomerCommentRepository;
import com.eyeson.tikon.service.CustomerCommentService;
import com.eyeson.tikon.repository.search.CustomerCommentSearchRepository;
import com.eyeson.tikon.web.rest.dto.CustomerCommentDTO;
import com.eyeson.tikon.web.rest.mapper.CustomerCommentMapper;

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
 * Test class for the CustomerCommentResource REST controller.
 *
 * @see CustomerCommentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TikonApp.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerCommentResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);
    private static final String DEFAULT_COMMENT_TEXT = "AAAAA";
    private static final String UPDATED_COMMENT_TEXT = "BBBBB";

    @Inject
    private CustomerCommentRepository customerCommentRepository;

    @Inject
    private CustomerCommentMapper customerCommentMapper;

    @Inject
    private CustomerCommentService customerCommentService;

    @Inject
    private CustomerCommentSearchRepository customerCommentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerCommentMockMvc;

    private CustomerComment customerComment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerCommentResource customerCommentResource = new CustomerCommentResource();
        ReflectionTestUtils.setField(customerCommentResource, "customerCommentService", customerCommentService);
        ReflectionTestUtils.setField(customerCommentResource, "customerCommentMapper", customerCommentMapper);
        this.restCustomerCommentMockMvc = MockMvcBuilders.standaloneSetup(customerCommentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerCommentSearchRepository.deleteAll();
        customerComment = new CustomerComment();
        customerComment.setCreateDate(DEFAULT_CREATE_DATE);
        customerComment.setCommentText(DEFAULT_COMMENT_TEXT);
    }

    @Test
    @Transactional
    public void createCustomerComment() throws Exception {
        int databaseSizeBeforeCreate = customerCommentRepository.findAll().size();

        // Create the CustomerComment
        CustomerCommentDTO customerCommentDTO = customerCommentMapper.customerCommentToCustomerCommentDTO(customerComment);

        restCustomerCommentMockMvc.perform(post("/api/customer-comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerCommentDTO)))
                .andExpect(status().isCreated());

        // Validate the CustomerComment in the database
        List<CustomerComment> customerComments = customerCommentRepository.findAll();
        assertThat(customerComments).hasSize(databaseSizeBeforeCreate + 1);
        CustomerComment testCustomerComment = customerComments.get(customerComments.size() - 1);
        assertThat(testCustomerComment.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCustomerComment.getCommentText()).isEqualTo(DEFAULT_COMMENT_TEXT);

        // Validate the CustomerComment in ElasticSearch
        CustomerComment customerCommentEs = customerCommentSearchRepository.findOne(testCustomerComment.getId());
        assertThat(customerCommentEs).isEqualToComparingFieldByField(testCustomerComment);
    }

    @Test
    @Transactional
    public void getAllCustomerComments() throws Exception {
        // Initialize the database
        customerCommentRepository.saveAndFlush(customerComment);

        // Get all the customerComments
        restCustomerCommentMockMvc.perform(get("/api/customer-comments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerComment.getId().intValue())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].commentText").value(hasItem(DEFAULT_COMMENT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getCustomerComment() throws Exception {
        // Initialize the database
        customerCommentRepository.saveAndFlush(customerComment);

        // Get the customerComment
        restCustomerCommentMockMvc.perform(get("/api/customer-comments/{id}", customerComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerComment.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.commentText").value(DEFAULT_COMMENT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerComment() throws Exception {
        // Get the customerComment
        restCustomerCommentMockMvc.perform(get("/api/customer-comments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerComment() throws Exception {
        // Initialize the database
        customerCommentRepository.saveAndFlush(customerComment);
        customerCommentSearchRepository.save(customerComment);
        int databaseSizeBeforeUpdate = customerCommentRepository.findAll().size();

        // Update the customerComment
        CustomerComment updatedCustomerComment = new CustomerComment();
        updatedCustomerComment.setId(customerComment.getId());
        updatedCustomerComment.setCreateDate(UPDATED_CREATE_DATE);
        updatedCustomerComment.setCommentText(UPDATED_COMMENT_TEXT);
        CustomerCommentDTO customerCommentDTO = customerCommentMapper.customerCommentToCustomerCommentDTO(updatedCustomerComment);

        restCustomerCommentMockMvc.perform(put("/api/customer-comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerCommentDTO)))
                .andExpect(status().isOk());

        // Validate the CustomerComment in the database
        List<CustomerComment> customerComments = customerCommentRepository.findAll();
        assertThat(customerComments).hasSize(databaseSizeBeforeUpdate);
        CustomerComment testCustomerComment = customerComments.get(customerComments.size() - 1);
        assertThat(testCustomerComment.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCustomerComment.getCommentText()).isEqualTo(UPDATED_COMMENT_TEXT);

        // Validate the CustomerComment in ElasticSearch
        CustomerComment customerCommentEs = customerCommentSearchRepository.findOne(testCustomerComment.getId());
        assertThat(customerCommentEs).isEqualToComparingFieldByField(testCustomerComment);
    }

    @Test
    @Transactional
    public void deleteCustomerComment() throws Exception {
        // Initialize the database
        customerCommentRepository.saveAndFlush(customerComment);
        customerCommentSearchRepository.save(customerComment);
        int databaseSizeBeforeDelete = customerCommentRepository.findAll().size();

        // Get the customerComment
        restCustomerCommentMockMvc.perform(delete("/api/customer-comments/{id}", customerComment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean customerCommentExistsInEs = customerCommentSearchRepository.exists(customerComment.getId());
        assertThat(customerCommentExistsInEs).isFalse();

        // Validate the database is empty
        List<CustomerComment> customerComments = customerCommentRepository.findAll();
        assertThat(customerComments).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomerComment() throws Exception {
        // Initialize the database
        customerCommentRepository.saveAndFlush(customerComment);
        customerCommentSearchRepository.save(customerComment);

        // Search the customerComment
        restCustomerCommentMockMvc.perform(get("/api/_search/customer-comments?query=id:" + customerComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
            .andExpect(jsonPath("$.[*].commentText").value(hasItem(DEFAULT_COMMENT_TEXT.toString())));
    }
}
