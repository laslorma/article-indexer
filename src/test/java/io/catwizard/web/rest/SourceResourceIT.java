package io.catwizard.web.rest;

import io.catwizard.IndexerApp;
import io.catwizard.domain.Source;
import io.catwizard.repository.SourceRepository;
import io.catwizard.repository.search.SourceSearchRepository;
import io.catwizard.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.Disabled;
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
 * Integration tests for the {@link SourceResource} REST controller.
 */
@SpringBootTest(classes = IndexerApp.class)
public class SourceResourceIT {

    private static final String DEFAULT_SOURCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private SourceRepository sourceRepository;

    /**
     * This repository is mocked in the io.catwizard.repository.search test package.
     *
     * @see io.catwizard.repository.search.SourceSearchRepositoryMockConfiguration
     */
    @Autowired
    private SourceSearchRepository mockSourceSearchRepository;

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

    private MockMvc restSourceMockMvc;

    private Source source;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SourceResource sourceResource = new SourceResource(sourceRepository, mockSourceSearchRepository);
        this.restSourceMockMvc = MockMvcBuilders.standaloneSetup(sourceResource)
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
    public static Source createEntity(EntityManager em) {
        Source source = new Source()
            .sourceId(DEFAULT_SOURCE_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .category(DEFAULT_CATEGORY)
            .language(DEFAULT_LANGUAGE)
            .country(DEFAULT_COUNTRY)
            .active(DEFAULT_ACTIVE);
        return source;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createUpdatedEntity(EntityManager em) {
        Source source = new Source()
            .sourceId(UPDATED_SOURCE_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .category(UPDATED_CATEGORY)
            .language(UPDATED_LANGUAGE)
            .country(UPDATED_COUNTRY)
            .active(UPDATED_ACTIVE);
        return source;
    }

    @BeforeEach
    public void initTest() {
        source = createEntity(em);
    }

    @Test
    @Transactional
    public void createSource() throws Exception {
        int databaseSizeBeforeCreate = sourceRepository.findAll().size();

        // Create the Source
        restSourceMockMvc.perform(post("/api/sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isCreated());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate + 1);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testSource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSource.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSource.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testSource.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testSource.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testSource.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testSource.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Source in Elasticsearch
        verify(mockSourceSearchRepository, times(1)).save(testSource);
    }

    @Test
    @Transactional
    @Disabled
    public void createSourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceRepository.findAll().size();

        // Create the Source with an existing ID
        source.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceMockMvc.perform(post("/api/sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Source in Elasticsearch
        verify(mockSourceSearchRepository, times(0)).save(source);
    }


    @Test
    @Transactional
    @Disabled
    public void getAllSources() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList
        restSourceMockMvc.perform(get("/api/sources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(source.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    @Disabled
    public void getSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get the source
        restSourceMockMvc.perform(get("/api/sources/{id}", source.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(source.getId().intValue()))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSource() throws Exception {
        // Get the source
        restSourceMockMvc.perform(get("/api/sources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Disabled
    public void updateSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Update the source
        Source updatedSource = sourceRepository.findById(source.getId()).get();
        // Disconnect from session so that the updates on updatedSource are not directly saved in db
        em.detach(updatedSource);
        updatedSource
            .sourceId(UPDATED_SOURCE_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .category(UPDATED_CATEGORY)
            .language(UPDATED_LANGUAGE)
            .country(UPDATED_COUNTRY)
            .active(UPDATED_ACTIVE);

        restSourceMockMvc.perform(put("/api/sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSource)))
            .andExpect(status().isOk());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSource.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testSource.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testSource.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testSource.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testSource.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Source in Elasticsearch
        verify(mockSourceSearchRepository, times(1)).save(testSource);
    }

    @Test
    @Transactional
    public void updateNonExistingSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Create the Source

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceMockMvc.perform(put("/api/sources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Source in Elasticsearch
        verify(mockSourceSearchRepository, times(0)).save(source);
    }

    @Test
    @Transactional
    public void deleteSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        int databaseSizeBeforeDelete = sourceRepository.findAll().size();

        // Delete the source
        restSourceMockMvc.perform(delete("/api/sources/{id}", source.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Source in Elasticsearch
        verify(mockSourceSearchRepository, times(1)).deleteById(source.getId());
    }

    @Test
    @Transactional
    @Disabled
    public void searchSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);
        when(mockSourceSearchRepository.search(queryStringQuery("id:" + source.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(source), PageRequest.of(0, 1), 1));
        // Search the source
        restSourceMockMvc.perform(get("/api/_search/sources?query=id:" + source.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(source.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Source.class);
        Source source1 = new Source();
        source1.setId(1L);
        Source source2 = new Source();
        source2.setId(source1.getId());
        assertThat(source1).isEqualTo(source2);
        source2.setId(2L);
        assertThat(source1).isNotEqualTo(source2);
        source1.setId(null);
        assertThat(source1).isNotEqualTo(source2);
    }
}
