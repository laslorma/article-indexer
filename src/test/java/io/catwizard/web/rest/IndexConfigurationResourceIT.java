package io.catwizard.web.rest;

import io.catwizard.IndexerApp;
import io.catwizard.domain.IndexConfiguration;
import io.catwizard.repository.IndexConfigurationRepository;
import io.catwizard.repository.search.IndexConfigurationSearchRepository;
import io.catwizard.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static io.catwizard.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link IndexConfigurationResource} REST controller.
 */
@SpringBootTest(classes = IndexerApp.class)
public class IndexConfigurationResourceIT {

    private static final Boolean DEFAULT_GENERATE_CORPUSES = false;
    private static final Boolean UPDATED_GENERATE_CORPUSES = true;

    private static final String DEFAULT_CORPUSES_OUTPUT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_CORPUSES_OUTPUT_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_NEWS_API_KEY = "AAAAAAAAAA";
    private static final String UPDATED_NEWS_API_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATE_ALL_CATEGORIES_AND_COUNTRIES = false;
    private static final Boolean UPDATED_ACTIVATE_ALL_CATEGORIES_AND_COUNTRIES = true;

    @Autowired
    private IndexConfigurationRepository indexConfigurationRepository;

    /**
     * This repository is mocked in the io.catwizard.repository.search test package.
     *
     * @see io.catwizard.repository.search.IndexConfigurationSearchRepositoryMockConfiguration
     */
    @Autowired
    private IndexConfigurationSearchRepository mockIndexConfigurationSearchRepository;

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

    private MockMvc restIndexConfigurationMockMvc;

    private IndexConfiguration indexConfiguration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IndexConfigurationResource indexConfigurationResource = new IndexConfigurationResource(indexConfigurationRepository, mockIndexConfigurationSearchRepository);
        this.restIndexConfigurationMockMvc = MockMvcBuilders.standaloneSetup(indexConfigurationResource)
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
    public static IndexConfiguration createEntity(EntityManager em) {
        IndexConfiguration indexConfiguration = new IndexConfiguration()
            .generateCorpuses(DEFAULT_GENERATE_CORPUSES)
            .corpusesOutputPath(DEFAULT_CORPUSES_OUTPUT_PATH)
            .newsApiKey(DEFAULT_NEWS_API_KEY)
            .activateAllCategoriesAndCountries(DEFAULT_ACTIVATE_ALL_CATEGORIES_AND_COUNTRIES);
        return indexConfiguration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndexConfiguration createUpdatedEntity(EntityManager em) {
        IndexConfiguration indexConfiguration = new IndexConfiguration()
            .generateCorpuses(UPDATED_GENERATE_CORPUSES)
            .corpusesOutputPath(UPDATED_CORPUSES_OUTPUT_PATH)
            .newsApiKey(UPDATED_NEWS_API_KEY)
            .activateAllCategoriesAndCountries(UPDATED_ACTIVATE_ALL_CATEGORIES_AND_COUNTRIES);
        return indexConfiguration;
    }

    @BeforeEach
    public void initTest() {
        indexConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndexConfiguration() throws Exception {
        int databaseSizeBeforeCreate = indexConfigurationRepository.findAll().size();

        // Create the IndexConfiguration
        restIndexConfigurationMockMvc.perform(post("/api/index-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indexConfiguration)))
            .andExpect(status().isCreated());

        // Validate the IndexConfiguration in the database
        List<IndexConfiguration> indexConfigurationList = indexConfigurationRepository.findAll();
        assertThat(indexConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        IndexConfiguration testIndexConfiguration = indexConfigurationList.get(indexConfigurationList.size() - 1);
        assertThat(testIndexConfiguration.isGenerateCorpuses()).isEqualTo(DEFAULT_GENERATE_CORPUSES);
        assertThat(testIndexConfiguration.getCorpusesOutputPath()).isEqualTo(DEFAULT_CORPUSES_OUTPUT_PATH);
        assertThat(testIndexConfiguration.getNewsApiKey()).isEqualTo(DEFAULT_NEWS_API_KEY);
        assertThat(testIndexConfiguration.isActivateAllCategoriesAndCountries()).isEqualTo(DEFAULT_ACTIVATE_ALL_CATEGORIES_AND_COUNTRIES);

        // Validate the IndexConfiguration in Elasticsearch
        verify(mockIndexConfigurationSearchRepository, times(1)).save(testIndexConfiguration);
    }

    @Test
    @Transactional
    public void createIndexConfigurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = indexConfigurationRepository.findAll().size();

        // Create the IndexConfiguration with an existing ID
        indexConfiguration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndexConfigurationMockMvc.perform(post("/api/index-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indexConfiguration)))
            .andExpect(status().isBadRequest());

        // Validate the IndexConfiguration in the database
        List<IndexConfiguration> indexConfigurationList = indexConfigurationRepository.findAll();
        assertThat(indexConfigurationList).hasSize(databaseSizeBeforeCreate);

        // Validate the IndexConfiguration in Elasticsearch
        verify(mockIndexConfigurationSearchRepository, times(0)).save(indexConfiguration);
    }


    @Test
    @Transactional
    public void getAllIndexConfigurations() throws Exception {
        // Initialize the database
        indexConfigurationRepository.saveAndFlush(indexConfiguration);

        // Get all the indexConfigurationList
        restIndexConfigurationMockMvc.perform(get("/api/index-configurations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].generateCorpuses").value(hasItem(DEFAULT_GENERATE_CORPUSES.booleanValue())))
            .andExpect(jsonPath("$.[*].corpusesOutputPath").value(hasItem(DEFAULT_CORPUSES_OUTPUT_PATH)))
            .andExpect(jsonPath("$.[*].newsApiKey").value(hasItem(DEFAULT_NEWS_API_KEY)))
            .andExpect(jsonPath("$.[*].activateAllCategoriesAndCountries").value(hasItem(DEFAULT_ACTIVATE_ALL_CATEGORIES_AND_COUNTRIES.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getIndexConfiguration() throws Exception {
        // Initialize the database
        indexConfigurationRepository.saveAndFlush(indexConfiguration);

        // Get the indexConfiguration
        restIndexConfigurationMockMvc.perform(get("/api/index-configurations/{id}", indexConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(indexConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.generateCorpuses").value(DEFAULT_GENERATE_CORPUSES.booleanValue()))
            .andExpect(jsonPath("$.corpusesOutputPath").value(DEFAULT_CORPUSES_OUTPUT_PATH))
            .andExpect(jsonPath("$.newsApiKey").value(DEFAULT_NEWS_API_KEY))
            .andExpect(jsonPath("$.activateAllCategoriesAndCountries").value(DEFAULT_ACTIVATE_ALL_CATEGORIES_AND_COUNTRIES.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingIndexConfiguration() throws Exception {
        // Get the indexConfiguration
        restIndexConfigurationMockMvc.perform(get("/api/index-configurations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndexConfiguration() throws Exception {
        // Initialize the database
        indexConfigurationRepository.saveAndFlush(indexConfiguration);

        int databaseSizeBeforeUpdate = indexConfigurationRepository.findAll().size();

        // Update the indexConfiguration
        IndexConfiguration updatedIndexConfiguration = indexConfigurationRepository.findById(indexConfiguration.getId()).get();
        // Disconnect from session so that the updates on updatedIndexConfiguration are not directly saved in db
        em.detach(updatedIndexConfiguration);
        updatedIndexConfiguration
            .generateCorpuses(UPDATED_GENERATE_CORPUSES)
            .corpusesOutputPath(UPDATED_CORPUSES_OUTPUT_PATH)
            .newsApiKey(UPDATED_NEWS_API_KEY)
            .activateAllCategoriesAndCountries(UPDATED_ACTIVATE_ALL_CATEGORIES_AND_COUNTRIES);

        restIndexConfigurationMockMvc.perform(put("/api/index-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIndexConfiguration)))
            .andExpect(status().isOk());

        // Validate the IndexConfiguration in the database
        List<IndexConfiguration> indexConfigurationList = indexConfigurationRepository.findAll();
        assertThat(indexConfigurationList).hasSize(databaseSizeBeforeUpdate);
        IndexConfiguration testIndexConfiguration = indexConfigurationList.get(indexConfigurationList.size() - 1);
        assertThat(testIndexConfiguration.isGenerateCorpuses()).isEqualTo(UPDATED_GENERATE_CORPUSES);
        assertThat(testIndexConfiguration.getCorpusesOutputPath()).isEqualTo(UPDATED_CORPUSES_OUTPUT_PATH);
        assertThat(testIndexConfiguration.getNewsApiKey()).isEqualTo(UPDATED_NEWS_API_KEY);
        assertThat(testIndexConfiguration.isActivateAllCategoriesAndCountries()).isEqualTo(UPDATED_ACTIVATE_ALL_CATEGORIES_AND_COUNTRIES);

        // Validate the IndexConfiguration in Elasticsearch
        verify(mockIndexConfigurationSearchRepository, times(1)).save(testIndexConfiguration);
    }

    @Test
    @Transactional
    public void updateNonExistingIndexConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = indexConfigurationRepository.findAll().size();

        // Create the IndexConfiguration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndexConfigurationMockMvc.perform(put("/api/index-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(indexConfiguration)))
            .andExpect(status().isBadRequest());

        // Validate the IndexConfiguration in the database
        List<IndexConfiguration> indexConfigurationList = indexConfigurationRepository.findAll();
        assertThat(indexConfigurationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IndexConfiguration in Elasticsearch
        verify(mockIndexConfigurationSearchRepository, times(0)).save(indexConfiguration);
    }

    @Test
    @Transactional
    public void deleteIndexConfiguration() throws Exception {
        // Initialize the database
        indexConfigurationRepository.saveAndFlush(indexConfiguration);

        int databaseSizeBeforeDelete = indexConfigurationRepository.findAll().size();

        // Delete the indexConfiguration
        restIndexConfigurationMockMvc.perform(delete("/api/index-configurations/{id}", indexConfiguration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IndexConfiguration> indexConfigurationList = indexConfigurationRepository.findAll();
        assertThat(indexConfigurationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IndexConfiguration in Elasticsearch
        verify(mockIndexConfigurationSearchRepository, times(1)).deleteById(indexConfiguration.getId());
    }

    @Test
    @Transactional
    public void searchIndexConfiguration() throws Exception {
        // Initialize the database
        indexConfigurationRepository.saveAndFlush(indexConfiguration);
        when(mockIndexConfigurationSearchRepository.search(queryStringQuery("id:" + indexConfiguration.getId())))
            .thenReturn(Collections.singletonList(indexConfiguration));
        // Search the indexConfiguration
        restIndexConfigurationMockMvc.perform(get("/api/_search/index-configurations?query=id:" + indexConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(indexConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].generateCorpuses").value(hasItem(DEFAULT_GENERATE_CORPUSES.booleanValue())))
            .andExpect(jsonPath("$.[*].corpusesOutputPath").value(hasItem(DEFAULT_CORPUSES_OUTPUT_PATH)))
            .andExpect(jsonPath("$.[*].newsApiKey").value(hasItem(DEFAULT_NEWS_API_KEY)))
            .andExpect(jsonPath("$.[*].activateAllCategoriesAndCountries").value(hasItem(DEFAULT_ACTIVATE_ALL_CATEGORIES_AND_COUNTRIES.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndexConfiguration.class);
        IndexConfiguration indexConfiguration1 = new IndexConfiguration();
        indexConfiguration1.setId(1L);
        IndexConfiguration indexConfiguration2 = new IndexConfiguration();
        indexConfiguration2.setId(indexConfiguration1.getId());
        assertThat(indexConfiguration1).isEqualTo(indexConfiguration2);
        indexConfiguration2.setId(2L);
        assertThat(indexConfiguration1).isNotEqualTo(indexConfiguration2);
        indexConfiguration1.setId(null);
        assertThat(indexConfiguration1).isNotEqualTo(indexConfiguration2);
    }
}
