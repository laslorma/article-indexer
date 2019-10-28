package io.catwizard.web.rest;

import io.catwizard.IndexerApp;
import io.catwizard.domain.IndexSession;
import io.catwizard.repository.IndexSessionRepository;
import io.catwizard.repository.search.IndexSessionSearchRepository;
import io.catwizard.service.IndexSessionService;
import io.catwizard.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static io.catwizard.web.rest.TestUtil.sameInstant;
import static io.catwizard.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link IndexSessionResource} REST controller.
 */
@SpringBootTest(classes = IndexerApp.class)
public class IndexSessionResourceIT {

    private static final Long DEFAULT_NEWS_API_CALLS = 1L;
    private static final Long UPDATED_NEWS_API_CALLS = 2L;
    private static final Long SMALLER_NEWS_API_CALLS = 1L - 1L;

    private static final Long DEFAULT_FIVE_FILTER_API_CALLS = 1L;
    private static final Long UPDATED_FIVE_FILTER_API_CALLS = 2L;
    private static final Long SMALLER_FIVE_FILTER_API_CALLS = 1L - 1L;

    private static final ZonedDateTime DEFAULT_STARTED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STARTED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_STARTED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_ENDED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ENDED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ENDED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Long DEFAULT_DURATION = 1L;
    private static final Long UPDATED_DURATION = 2L;
    private static final Long SMALLER_DURATION = 1L - 1L;

    private static final Long DEFAULT_TOTAL_ARTICLES = 1L;
    private static final Long UPDATED_TOTAL_ARTICLES = 2L;
    private static final Long SMALLER_TOTAL_ARTICLES = 1L - 1L;

    private static final Boolean DEFAULT_INDEXING = false;
    private static final Boolean UPDATED_INDEXING = true;

    private static final Long DEFAULT_ARTICLES_SAVED = 1L;
    private static final Long UPDATED_ARTICLES_SAVED = 2L;
    private static final Long SMALLER_ARTICLES_SAVED = 1L - 1L;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HAD_ERROR = false;
    private static final Boolean UPDATED_HAD_ERROR = true;

    @Autowired
    private IndexSessionRepository indexSessionRepository;

    @Autowired
    private IndexSessionService indexSessionService;

    /**
     * This repository is mocked in the io.catwizard.repository.search test package.
     *
     * @see io.catwizard.repository.search.IndexSessionSearchRepositoryMockConfiguration
     */
    @Autowired
    private IndexSessionSearchRepository mockIndexSessionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restIndexSessionMockMvc;

    private IndexSession indexSession;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IndexSessionResource indexSessionResource = new IndexSessionResource(indexSessionService);
        this.restIndexSessionMockMvc = MockMvcBuilders.standaloneSetup(indexSessionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexSession createEntity(EntityManager em) {
        IndexSession indexSession = new IndexSession()
            .newsApiCalls(DEFAULT_NEWS_API_CALLS)
            .fiveFilterApiCalls(DEFAULT_FIVE_FILTER_API_CALLS)
            .started(DEFAULT_STARTED)
            .ended(DEFAULT_ENDED)
            .duration(DEFAULT_DURATION)
            .totalArticles(DEFAULT_TOTAL_ARTICLES)
            .indexing(DEFAULT_INDEXING)
            .articlesSaved(DEFAULT_ARTICLES_SAVED)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .hadError(DEFAULT_HAD_ERROR);
        return indexSession;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexSession createUpdatedEntity(EntityManager em) {
        IndexSession indexSession = new IndexSession()
            .newsApiCalls(UPDATED_NEWS_API_CALLS)
            .fiveFilterApiCalls(UPDATED_FIVE_FILTER_API_CALLS)
            .started(UPDATED_STARTED)
            .ended(UPDATED_ENDED)
            .duration(UPDATED_DURATION)
            .totalArticles(UPDATED_TOTAL_ARTICLES)
            .indexing(UPDATED_INDEXING)
            .articlesSaved(UPDATED_ARTICLES_SAVED)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .hadError(UPDATED_HAD_ERROR);
        return indexSession;
    }

    @BeforeEach
    public void initTest() {
        indexSession = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndexSession() throws Exception {
        int databaseSizeBeforeCreate = indexSessionRepository.findAll().size();

        // Create the IndexSession
        restIndexSessionMockMvc.perform(post("/api/index-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indexSession)))
            .andExpect(status().isCreated());

        // Validate the IndexSession in the database
        List<IndexSession> indexSessionList = indexSessionRepository.findAll();
        assertThat(indexSessionList).hasSize(databaseSizeBeforeCreate + 1);
        IndexSession testIndexSession = indexSessionList.get(indexSessionList.size() - 1);
        assertThat(testIndexSession.getNewsApiCalls()).isEqualTo(DEFAULT_NEWS_API_CALLS);
        assertThat(testIndexSession.getFiveFilterApiCalls()).isEqualTo(DEFAULT_FIVE_FILTER_API_CALLS);
        assertThat(testIndexSession.getStarted()).isEqualTo(DEFAULT_STARTED);
        assertThat(testIndexSession.getEnded()).isEqualTo(DEFAULT_ENDED);
        assertThat(testIndexSession.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testIndexSession.getTotalArticles()).isEqualTo(DEFAULT_TOTAL_ARTICLES);
        assertThat(testIndexSession.isIndexing()).isEqualTo(DEFAULT_INDEXING);
        assertThat(testIndexSession.getArticlesSaved()).isEqualTo(DEFAULT_ARTICLES_SAVED);
        assertThat(testIndexSession.getErrorMessage()).isEqualTo(DEFAULT_ERROR_MESSAGE);
        assertThat(testIndexSession.isHadError()).isEqualTo(DEFAULT_HAD_ERROR);

        // Validate the IndexSession in Elasticsearch
        verify(mockIndexSessionSearchRepository, times(1)).save(testIndexSession);
    }

    @Test
    @Transactional
    public void createIndexSessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = indexSessionRepository.findAll().size();

        // Create the IndexSession with an existing ID
        indexSession.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexSessionMockMvc.perform(post("/api/index-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indexSession)))
            .andExpect(status().isBadRequest());

        // Validate the IndexSession in the database
        List<IndexSession> indexSessionList = indexSessionRepository.findAll();
        assertThat(indexSessionList).hasSize(databaseSizeBeforeCreate);

        // Validate the IndexSession in Elasticsearch
        verify(mockIndexSessionSearchRepository, times(0)).save(indexSession);
    }


    @Test
    @Transactional
    public void getAllIndexSessions() throws Exception {
        // Initialize the database
        indexSessionRepository.saveAndFlush(indexSession);

        // Get all the indexSessionList
        restIndexSessionMockMvc.perform(get("/api/index-sessions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].newsApiCalls").value(hasItem(DEFAULT_NEWS_API_CALLS.intValue())))
            .andExpect(jsonPath("$.[*].fiveFilterApiCalls").value(hasItem(DEFAULT_FIVE_FILTER_API_CALLS.intValue())))
            .andExpect(jsonPath("$.[*].started").value(hasItem(sameInstant(DEFAULT_STARTED))))
            .andExpect(jsonPath("$.[*].ended").value(hasItem(sameInstant(DEFAULT_ENDED))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].totalArticles").value(hasItem(DEFAULT_TOTAL_ARTICLES.intValue())))
            .andExpect(jsonPath("$.[*].indexing").value(hasItem(DEFAULT_INDEXING.booleanValue())))
            .andExpect(jsonPath("$.[*].articlesSaved").value(hasItem(DEFAULT_ARTICLES_SAVED.intValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].hadError").value(hasItem(DEFAULT_HAD_ERROR.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getIndexSession() throws Exception {
        // Initialize the database
        indexSessionRepository.saveAndFlush(indexSession);

        // Get the indexSession
        restIndexSessionMockMvc.perform(get("/api/index-sessions/{id}", indexSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(indexSession.getId().intValue()))
            .andExpect(jsonPath("$.newsApiCalls").value(DEFAULT_NEWS_API_CALLS.intValue()))
            .andExpect(jsonPath("$.fiveFilterApiCalls").value(DEFAULT_FIVE_FILTER_API_CALLS.intValue()))
            .andExpect(jsonPath("$.started").value(sameInstant(DEFAULT_STARTED)))
            .andExpect(jsonPath("$.ended").value(sameInstant(DEFAULT_ENDED)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()))
            .andExpect(jsonPath("$.totalArticles").value(DEFAULT_TOTAL_ARTICLES.intValue()))
            .andExpect(jsonPath("$.indexing").value(DEFAULT_INDEXING.booleanValue()))
            .andExpect(jsonPath("$.articlesSaved").value(DEFAULT_ARTICLES_SAVED.intValue()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE.toString()))
            .andExpect(jsonPath("$.hadError").value(DEFAULT_HAD_ERROR.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingIndexSession() throws Exception {
        // Get the indexSession
        restIndexSessionMockMvc.perform(get("/api/index-sessions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndexSession() throws Exception {
        // Initialize the database
        indexSessionService.save(indexSession);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockIndexSessionSearchRepository);

        int databaseSizeBeforeUpdate = indexSessionRepository.findAll().size();

        // Update the indexSession
        IndexSession updatedIndexSession = indexSessionRepository.findById(indexSession.getId()).get();
        // Disconnect from session so that the updates on updatedIndexSession are not directly saved in db
        em.detach(updatedIndexSession);
        updatedIndexSession
            .newsApiCalls(UPDATED_NEWS_API_CALLS)
            .fiveFilterApiCalls(UPDATED_FIVE_FILTER_API_CALLS)
            .started(UPDATED_STARTED)
            .ended(UPDATED_ENDED)
            .duration(UPDATED_DURATION)
            .totalArticles(UPDATED_TOTAL_ARTICLES)
            .indexing(UPDATED_INDEXING)
            .articlesSaved(UPDATED_ARTICLES_SAVED)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .hadError(UPDATED_HAD_ERROR);

        restIndexSessionMockMvc.perform(put("/api/index-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIndexSession)))
            .andExpect(status().isOk());

        // Validate the IndexSession in the database
        List<IndexSession> indexSessionList = indexSessionRepository.findAll();
        assertThat(indexSessionList).hasSize(databaseSizeBeforeUpdate);
        IndexSession testIndexSession = indexSessionList.get(indexSessionList.size() - 1);
        assertThat(testIndexSession.getNewsApiCalls()).isEqualTo(UPDATED_NEWS_API_CALLS);
        assertThat(testIndexSession.getFiveFilterApiCalls()).isEqualTo(UPDATED_FIVE_FILTER_API_CALLS);
        assertThat(testIndexSession.getStarted()).isEqualTo(UPDATED_STARTED);
        assertThat(testIndexSession.getEnded()).isEqualTo(UPDATED_ENDED);
        assertThat(testIndexSession.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testIndexSession.getTotalArticles()).isEqualTo(UPDATED_TOTAL_ARTICLES);
        assertThat(testIndexSession.isIndexing()).isEqualTo(UPDATED_INDEXING);
        assertThat(testIndexSession.getArticlesSaved()).isEqualTo(UPDATED_ARTICLES_SAVED);
        assertThat(testIndexSession.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
        assertThat(testIndexSession.isHadError()).isEqualTo(UPDATED_HAD_ERROR);

        // Validate the IndexSession in Elasticsearch
        verify(mockIndexSessionSearchRepository, times(1)).save(testIndexSession);
    }

    @Test
    @Transactional
    public void updateNonExistingIndexSession() throws Exception {
        int databaseSizeBeforeUpdate = indexSessionRepository.findAll().size();

        // Create the IndexSession

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexSessionMockMvc.perform(put("/api/index-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indexSession)))
            .andExpect(status().isBadRequest());

        // Validate the IndexSession in the database
        List<IndexSession> indexSessionList = indexSessionRepository.findAll();
        assertThat(indexSessionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IndexSession in Elasticsearch
        verify(mockIndexSessionSearchRepository, times(0)).save(indexSession);
    }

    @Test
    @Transactional
    public void deleteIndexSession() throws Exception {
        // Initialize the database
        indexSessionService.save(indexSession);

        int databaseSizeBeforeDelete = indexSessionRepository.findAll().size();

        // Delete the indexSession
        restIndexSessionMockMvc.perform(delete("/api/index-sessions/{id}", indexSession.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IndexSession> indexSessionList = indexSessionRepository.findAll();
        assertThat(indexSessionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IndexSession in Elasticsearch
        verify(mockIndexSessionSearchRepository, times(1)).deleteById(indexSession.getId());
    }

    @Test
    @Transactional
    public void searchIndexSession() throws Exception {
        // Initialize the database
        indexSessionService.save(indexSession);
        when(mockIndexSessionSearchRepository.search(queryStringQuery("id:" + indexSession.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(indexSession), PageRequest.of(0, 1), 1));
        // Search the indexSession
        restIndexSessionMockMvc.perform(get("/api/_search/index-sessions?query=id:" + indexSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].newsApiCalls").value(hasItem(DEFAULT_NEWS_API_CALLS.intValue())))
            .andExpect(jsonPath("$.[*].fiveFilterApiCalls").value(hasItem(DEFAULT_FIVE_FILTER_API_CALLS.intValue())))
            .andExpect(jsonPath("$.[*].started").value(hasItem(sameInstant(DEFAULT_STARTED))))
            .andExpect(jsonPath("$.[*].ended").value(hasItem(sameInstant(DEFAULT_ENDED))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].totalArticles").value(hasItem(DEFAULT_TOTAL_ARTICLES.intValue())))
            .andExpect(jsonPath("$.[*].indexing").value(hasItem(DEFAULT_INDEXING.booleanValue())))
            .andExpect(jsonPath("$.[*].articlesSaved").value(hasItem(DEFAULT_ARTICLES_SAVED.intValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].hadError").value(hasItem(DEFAULT_HAD_ERROR.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexSession.class);
        IndexSession indexSession1 = new IndexSession();
        indexSession1.setId(1L);
        IndexSession indexSession2 = new IndexSession();
        indexSession2.setId(indexSession1.getId());
        assertThat(indexSession1).isEqualTo(indexSession2);
        indexSession2.setId(2L);
        assertThat(indexSession1).isNotEqualTo(indexSession2);
        indexSession1.setId(null);
        assertThat(indexSession1).isNotEqualTo(indexSession2);
    }
}
