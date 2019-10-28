package io.catwizard.web.rest;

import io.catwizard.IndexerApp;
import io.catwizard.domain.NlpServerConf;
import io.catwizard.repository.NlpServerConfRepository;
import io.catwizard.repository.search.NlpServerConfSearchRepository;
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
 * Integration tests for the {@link NlpServerConfResource} REST controller.
 */
@SpringBootTest(classes = IndexerApp.class)
public class NlpServerConfResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PORT = "AAAAAAAAAA";
    private static final String UPDATED_PORT = "BBBBBBBBBB";

    @Autowired
    private NlpServerConfRepository nlpServerConfRepository;

    /**
     * This repository is mocked in the io.catwizard.repository.search test package.
     *
     * @see io.catwizard.repository.search.NlpServerConfSearchRepositoryMockConfiguration
     */
    @Autowired
    private NlpServerConfSearchRepository mockNlpServerConfSearchRepository;

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

    private MockMvc restNlpServerConfMockMvc;

    private NlpServerConf nlpServerConf;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NlpServerConfResource nlpServerConfResource = new NlpServerConfResource(nlpServerConfRepository, mockNlpServerConfSearchRepository);
        this.restNlpServerConfMockMvc = MockMvcBuilders.standaloneSetup(nlpServerConfResource)
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
    public static NlpServerConf createEntity(EntityManager em) {
        NlpServerConf nlpServerConf = new NlpServerConf()
            .url(DEFAULT_URL)
            .port(DEFAULT_PORT);
        return nlpServerConf;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NlpServerConf createUpdatedEntity(EntityManager em) {
        NlpServerConf nlpServerConf = new NlpServerConf()
            .url(UPDATED_URL)
            .port(UPDATED_PORT);
        return nlpServerConf;
    }

    @BeforeEach
    public void initTest() {
        nlpServerConf = createEntity(em);
    }

    @Test
    @Transactional
    public void createNlpServerConf() throws Exception {
        int databaseSizeBeforeCreate = nlpServerConfRepository.findAll().size();

        // Create the NlpServerConf
        restNlpServerConfMockMvc.perform(post("/api/nlp-server-confs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nlpServerConf)))
            .andExpect(status().isCreated());

        // Validate the NlpServerConf in the database
        List<NlpServerConf> nlpServerConfList = nlpServerConfRepository.findAll();
        assertThat(nlpServerConfList).hasSize(databaseSizeBeforeCreate + 1);
        NlpServerConf testNlpServerConf = nlpServerConfList.get(nlpServerConfList.size() - 1);
        assertThat(testNlpServerConf.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testNlpServerConf.getPort()).isEqualTo(DEFAULT_PORT);

        // Validate the NlpServerConf in Elasticsearch
        verify(mockNlpServerConfSearchRepository, times(1)).save(testNlpServerConf);
    }

    @Test
    @Transactional
    public void createNlpServerConfWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nlpServerConfRepository.findAll().size();

        // Create the NlpServerConf with an existing ID
        nlpServerConf.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNlpServerConfMockMvc.perform(post("/api/nlp-server-confs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nlpServerConf)))
            .andExpect(status().isBadRequest());

        // Validate the NlpServerConf in the database
        List<NlpServerConf> nlpServerConfList = nlpServerConfRepository.findAll();
        assertThat(nlpServerConfList).hasSize(databaseSizeBeforeCreate);

        // Validate the NlpServerConf in Elasticsearch
        verify(mockNlpServerConfSearchRepository, times(0)).save(nlpServerConf);
    }


    @Test
    @Transactional
    public void getAllNlpServerConfs() throws Exception {
        // Initialize the database
        nlpServerConfRepository.saveAndFlush(nlpServerConf);

        // Get all the nlpServerConfList
        restNlpServerConfMockMvc.perform(get("/api/nlp-server-confs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nlpServerConf.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)));
    }
    
    @Test
    @Transactional
    public void getNlpServerConf() throws Exception {
        // Initialize the database
        nlpServerConfRepository.saveAndFlush(nlpServerConf);

        // Get the nlpServerConf
        restNlpServerConfMockMvc.perform(get("/api/nlp-server-confs/{id}", nlpServerConf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nlpServerConf.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT));
    }

    @Test
    @Transactional
    public void getNonExistingNlpServerConf() throws Exception {
        // Get the nlpServerConf
        restNlpServerConfMockMvc.perform(get("/api/nlp-server-confs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNlpServerConf() throws Exception {
        // Initialize the database
        nlpServerConfRepository.saveAndFlush(nlpServerConf);

        int databaseSizeBeforeUpdate = nlpServerConfRepository.findAll().size();

        // Update the nlpServerConf
        NlpServerConf updatedNlpServerConf = nlpServerConfRepository.findById(nlpServerConf.getId()).get();
        // Disconnect from session so that the updates on updatedNlpServerConf are not directly saved in db
        em.detach(updatedNlpServerConf);
        updatedNlpServerConf
            .url(UPDATED_URL)
            .port(UPDATED_PORT);

        restNlpServerConfMockMvc.perform(put("/api/nlp-server-confs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNlpServerConf)))
            .andExpect(status().isOk());

        // Validate the NlpServerConf in the database
        List<NlpServerConf> nlpServerConfList = nlpServerConfRepository.findAll();
        assertThat(nlpServerConfList).hasSize(databaseSizeBeforeUpdate);
        NlpServerConf testNlpServerConf = nlpServerConfList.get(nlpServerConfList.size() - 1);
        assertThat(testNlpServerConf.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testNlpServerConf.getPort()).isEqualTo(UPDATED_PORT);

        // Validate the NlpServerConf in Elasticsearch
        verify(mockNlpServerConfSearchRepository, times(1)).save(testNlpServerConf);
    }

    @Test
    @Transactional
    public void updateNonExistingNlpServerConf() throws Exception {
        int databaseSizeBeforeUpdate = nlpServerConfRepository.findAll().size();

        // Create the NlpServerConf

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNlpServerConfMockMvc.perform(put("/api/nlp-server-confs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nlpServerConf)))
            .andExpect(status().isBadRequest());

        // Validate the NlpServerConf in the database
        List<NlpServerConf> nlpServerConfList = nlpServerConfRepository.findAll();
        assertThat(nlpServerConfList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NlpServerConf in Elasticsearch
        verify(mockNlpServerConfSearchRepository, times(0)).save(nlpServerConf);
    }

    @Test
    @Transactional
    public void deleteNlpServerConf() throws Exception {
        // Initialize the database
        nlpServerConfRepository.saveAndFlush(nlpServerConf);

        int databaseSizeBeforeDelete = nlpServerConfRepository.findAll().size();

        // Delete the nlpServerConf
        restNlpServerConfMockMvc.perform(delete("/api/nlp-server-confs/{id}", nlpServerConf.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NlpServerConf> nlpServerConfList = nlpServerConfRepository.findAll();
        assertThat(nlpServerConfList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NlpServerConf in Elasticsearch
        verify(mockNlpServerConfSearchRepository, times(1)).deleteById(nlpServerConf.getId());
    }

    @Test
    @Transactional
    public void searchNlpServerConf() throws Exception {
        // Initialize the database
        nlpServerConfRepository.saveAndFlush(nlpServerConf);
        when(mockNlpServerConfSearchRepository.search(queryStringQuery("id:" + nlpServerConf.getId())))
            .thenReturn(Collections.singletonList(nlpServerConf));
        // Search the nlpServerConf
        restNlpServerConfMockMvc.perform(get("/api/_search/nlp-server-confs?query=id:" + nlpServerConf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nlpServerConf.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NlpServerConf.class);
        NlpServerConf nlpServerConf1 = new NlpServerConf();
        nlpServerConf1.setId(1L);
        NlpServerConf nlpServerConf2 = new NlpServerConf();
        nlpServerConf2.setId(nlpServerConf1.getId());
        assertThat(nlpServerConf1).isEqualTo(nlpServerConf2);
        nlpServerConf2.setId(2L);
        assertThat(nlpServerConf1).isNotEqualTo(nlpServerConf2);
        nlpServerConf1.setId(null);
        assertThat(nlpServerConf1).isNotEqualTo(nlpServerConf2);
    }
}
