package io.catwizard.web.rest;

import io.catwizard.IndexerApp;
import io.catwizard.domain.LingoToken;
import io.catwizard.repository.LingoTokenRepository;
import io.catwizard.repository.search.LingoTokenSearchRepository;
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
 * Integration tests for the {@link LingoTokenResource} REST controller.
 */
@SpringBootTest(classes = IndexerApp.class)
public class LingoTokenResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_BLANK_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_BLANK_TEXT = "BBBBBBBBBB";

    private static final Integer DEFAULT_LINGO_ORDER = 1;
    private static final Integer UPDATED_LINGO_ORDER = 2;

    private static final String DEFAULT_POS_TAG = "AAAAAAAAAA";
    private static final String UPDATED_POS_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_LEMMA = "AAAAAAAAAA";
    private static final String UPDATED_LEMMA = "BBBBBBBBBB";

    private static final String DEFAULT_NER_TAG = "AAAAAAAAAA";
    private static final String UPDATED_NER_TAG = "BBBBBBBBBB";

    @Autowired
    private LingoTokenRepository lingoTokenRepository;

    /**
     * This repository is mocked in the io.catwizard.repository.search test package.
     *
     * @see io.catwizard.repository.search.LingoTokenSearchRepositoryMockConfiguration
     */
    @Autowired
    private LingoTokenSearchRepository mockLingoTokenSearchRepository;

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

    private MockMvc restLingoTokenMockMvc;

    private LingoToken lingoToken;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LingoTokenResource lingoTokenResource = new LingoTokenResource(lingoTokenRepository, mockLingoTokenSearchRepository);
        this.restLingoTokenMockMvc = MockMvcBuilders.standaloneSetup(lingoTokenResource)
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
    public static LingoToken createEntity(EntityManager em) {
        LingoToken lingoToken = new LingoToken()
            .text(DEFAULT_TEXT)
            .blankText(DEFAULT_BLANK_TEXT)
            .lingoOrder(DEFAULT_LINGO_ORDER)
            .posTag(DEFAULT_POS_TAG)
            .lemma(DEFAULT_LEMMA)
            .nerTag(DEFAULT_NER_TAG);
        return lingoToken;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LingoToken createUpdatedEntity(EntityManager em) {
        LingoToken lingoToken = new LingoToken()
            .text(UPDATED_TEXT)
            .blankText(UPDATED_BLANK_TEXT)
            .lingoOrder(UPDATED_LINGO_ORDER)
            .posTag(UPDATED_POS_TAG)
            .lemma(UPDATED_LEMMA)
            .nerTag(UPDATED_NER_TAG);
        return lingoToken;
    }

    @BeforeEach
    public void initTest() {
        lingoToken = createEntity(em);
    }

    @Test
    @Transactional
    public void createLingoToken() throws Exception {
        int databaseSizeBeforeCreate = lingoTokenRepository.findAll().size();

        // Create the LingoToken
        restLingoTokenMockMvc.perform(post("/api/lingo-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lingoToken)))
            .andExpect(status().isCreated());

        // Validate the LingoToken in the database
        List<LingoToken> lingoTokenList = lingoTokenRepository.findAll();
        assertThat(lingoTokenList).hasSize(databaseSizeBeforeCreate + 1);
        LingoToken testLingoToken = lingoTokenList.get(lingoTokenList.size() - 1);
        assertThat(testLingoToken.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testLingoToken.getBlankText()).isEqualTo(DEFAULT_BLANK_TEXT);
        assertThat(testLingoToken.getLingoOrder()).isEqualTo(DEFAULT_LINGO_ORDER);
        assertThat(testLingoToken.getPosTag()).isEqualTo(DEFAULT_POS_TAG);
        assertThat(testLingoToken.getLemma()).isEqualTo(DEFAULT_LEMMA);
        assertThat(testLingoToken.getNerTag()).isEqualTo(DEFAULT_NER_TAG);

        // Validate the LingoToken in Elasticsearch
        verify(mockLingoTokenSearchRepository, times(1)).save(testLingoToken);
    }

    @Test
    @Transactional
    public void createLingoTokenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lingoTokenRepository.findAll().size();

        // Create the LingoToken with an existing ID
        lingoToken.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLingoTokenMockMvc.perform(post("/api/lingo-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lingoToken)))
            .andExpect(status().isBadRequest());

        // Validate the LingoToken in the database
        List<LingoToken> lingoTokenList = lingoTokenRepository.findAll();
        assertThat(lingoTokenList).hasSize(databaseSizeBeforeCreate);

        // Validate the LingoToken in Elasticsearch
        verify(mockLingoTokenSearchRepository, times(0)).save(lingoToken);
    }


    @Test
    @Transactional
    public void getAllLingoTokens() throws Exception {
        // Initialize the database
        lingoTokenRepository.saveAndFlush(lingoToken);

        // Get all the lingoTokenList
        restLingoTokenMockMvc.perform(get("/api/lingo-tokens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lingoToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].blankText").value(hasItem(DEFAULT_BLANK_TEXT)))
            .andExpect(jsonPath("$.[*].lingoOrder").value(hasItem(DEFAULT_LINGO_ORDER)))
            .andExpect(jsonPath("$.[*].posTag").value(hasItem(DEFAULT_POS_TAG)))
            .andExpect(jsonPath("$.[*].lemma").value(hasItem(DEFAULT_LEMMA)))
            .andExpect(jsonPath("$.[*].nerTag").value(hasItem(DEFAULT_NER_TAG)));
    }
    
    @Test
    @Transactional
    public void getLingoToken() throws Exception {
        // Initialize the database
        lingoTokenRepository.saveAndFlush(lingoToken);

        // Get the lingoToken
        restLingoTokenMockMvc.perform(get("/api/lingo-tokens/{id}", lingoToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lingoToken.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.blankText").value(DEFAULT_BLANK_TEXT))
            .andExpect(jsonPath("$.lingoOrder").value(DEFAULT_LINGO_ORDER))
            .andExpect(jsonPath("$.posTag").value(DEFAULT_POS_TAG))
            .andExpect(jsonPath("$.lemma").value(DEFAULT_LEMMA))
            .andExpect(jsonPath("$.nerTag").value(DEFAULT_NER_TAG));
    }

    @Test
    @Transactional
    public void getNonExistingLingoToken() throws Exception {
        // Get the lingoToken
        restLingoTokenMockMvc.perform(get("/api/lingo-tokens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLingoToken() throws Exception {
        // Initialize the database
        lingoTokenRepository.saveAndFlush(lingoToken);

        int databaseSizeBeforeUpdate = lingoTokenRepository.findAll().size();

        // Update the lingoToken
        LingoToken updatedLingoToken = lingoTokenRepository.findById(lingoToken.getId()).get();
        // Disconnect from session so that the updates on updatedLingoToken are not directly saved in db
        em.detach(updatedLingoToken);
        updatedLingoToken
            .text(UPDATED_TEXT)
            .blankText(UPDATED_BLANK_TEXT)
            .lingoOrder(UPDATED_LINGO_ORDER)
            .posTag(UPDATED_POS_TAG)
            .lemma(UPDATED_LEMMA)
            .nerTag(UPDATED_NER_TAG);

        restLingoTokenMockMvc.perform(put("/api/lingo-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLingoToken)))
            .andExpect(status().isOk());

        // Validate the LingoToken in the database
        List<LingoToken> lingoTokenList = lingoTokenRepository.findAll();
        assertThat(lingoTokenList).hasSize(databaseSizeBeforeUpdate);
        LingoToken testLingoToken = lingoTokenList.get(lingoTokenList.size() - 1);
        assertThat(testLingoToken.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testLingoToken.getBlankText()).isEqualTo(UPDATED_BLANK_TEXT);
        assertThat(testLingoToken.getLingoOrder()).isEqualTo(UPDATED_LINGO_ORDER);
        assertThat(testLingoToken.getPosTag()).isEqualTo(UPDATED_POS_TAG);
        assertThat(testLingoToken.getLemma()).isEqualTo(UPDATED_LEMMA);
        assertThat(testLingoToken.getNerTag()).isEqualTo(UPDATED_NER_TAG);

        // Validate the LingoToken in Elasticsearch
        verify(mockLingoTokenSearchRepository, times(1)).save(testLingoToken);
    }

    @Test
    @Transactional
    public void updateNonExistingLingoToken() throws Exception {
        int databaseSizeBeforeUpdate = lingoTokenRepository.findAll().size();

        // Create the LingoToken

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLingoTokenMockMvc.perform(put("/api/lingo-tokens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lingoToken)))
            .andExpect(status().isBadRequest());

        // Validate the LingoToken in the database
        List<LingoToken> lingoTokenList = lingoTokenRepository.findAll();
        assertThat(lingoTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LingoToken in Elasticsearch
        verify(mockLingoTokenSearchRepository, times(0)).save(lingoToken);
    }

    @Test
    @Transactional
    public void deleteLingoToken() throws Exception {
        // Initialize the database
        lingoTokenRepository.saveAndFlush(lingoToken);

        int databaseSizeBeforeDelete = lingoTokenRepository.findAll().size();

        // Delete the lingoToken
        restLingoTokenMockMvc.perform(delete("/api/lingo-tokens/{id}", lingoToken.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LingoToken> lingoTokenList = lingoTokenRepository.findAll();
        assertThat(lingoTokenList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LingoToken in Elasticsearch
        verify(mockLingoTokenSearchRepository, times(1)).deleteById(lingoToken.getId());
    }

    @Test
    @Transactional
    public void searchLingoToken() throws Exception {
        // Initialize the database
        lingoTokenRepository.saveAndFlush(lingoToken);
        when(mockLingoTokenSearchRepository.search(queryStringQuery("id:" + lingoToken.getId())))
            .thenReturn(Collections.singletonList(lingoToken));
        // Search the lingoToken
        restLingoTokenMockMvc.perform(get("/api/_search/lingo-tokens?query=id:" + lingoToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lingoToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].blankText").value(hasItem(DEFAULT_BLANK_TEXT)))
            .andExpect(jsonPath("$.[*].lingoOrder").value(hasItem(DEFAULT_LINGO_ORDER)))
            .andExpect(jsonPath("$.[*].posTag").value(hasItem(DEFAULT_POS_TAG)))
            .andExpect(jsonPath("$.[*].lemma").value(hasItem(DEFAULT_LEMMA)))
            .andExpect(jsonPath("$.[*].nerTag").value(hasItem(DEFAULT_NER_TAG)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LingoToken.class);
        LingoToken lingoToken1 = new LingoToken();
        lingoToken1.setId(1L);
        LingoToken lingoToken2 = new LingoToken();
        lingoToken2.setId(lingoToken1.getId());
        assertThat(lingoToken1).isEqualTo(lingoToken2);
        lingoToken2.setId(2L);
        assertThat(lingoToken1).isNotEqualTo(lingoToken2);
        lingoToken1.setId(null);
        assertThat(lingoToken1).isNotEqualTo(lingoToken2);
    }
}
