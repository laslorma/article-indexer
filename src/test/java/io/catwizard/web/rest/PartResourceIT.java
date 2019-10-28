package io.catwizard.web.rest;

import io.catwizard.IndexerApp;
import io.catwizard.domain.Part;
import io.catwizard.repository.PartRepository;
import io.catwizard.repository.search.PartSearchRepository;
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
 * Integration tests for the {@link PartResource} REST controller.
 */
@SpringBootTest(classes = IndexerApp.class)
public class PartResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_POSIBLE_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_POSIBLE_OPTIONS = "BBBBBBBBBB";

    @Autowired
    private PartRepository partRepository;

    /**
     * This repository is mocked in the io.catwizard.repository.search test package.
     *
     * @see io.catwizard.repository.search.PartSearchRepositoryMockConfiguration
     */
    @Autowired
    private PartSearchRepository mockPartSearchRepository;

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

    private MockMvc restPartMockMvc;

    private Part part;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PartResource partResource = new PartResource(partRepository, mockPartSearchRepository);
        this.restPartMockMvc = MockMvcBuilders.standaloneSetup(partResource)
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
    public static Part createEntity(EntityManager em) {
        Part part = new Part()
            .text(DEFAULT_TEXT)
            .posibleOptions(DEFAULT_POSIBLE_OPTIONS);
        return part;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Part createUpdatedEntity(EntityManager em) {
        Part part = new Part()
            .text(UPDATED_TEXT)
            .posibleOptions(UPDATED_POSIBLE_OPTIONS);
        return part;
    }

    @BeforeEach
    public void initTest() {
        part = createEntity(em);
    }

    @Test
    @Transactional
    public void createPart() throws Exception {
        int databaseSizeBeforeCreate = partRepository.findAll().size();

        // Create the Part
        restPartMockMvc.perform(post("/api/parts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(part)))
            .andExpect(status().isCreated());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeCreate + 1);
        Part testPart = partList.get(partList.size() - 1);
        assertThat(testPart.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testPart.getPosibleOptions()).isEqualTo(DEFAULT_POSIBLE_OPTIONS);

        // Validate the Part in Elasticsearch
        verify(mockPartSearchRepository, times(1)).save(testPart);
    }

    @Test
    @Transactional
    public void createPartWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = partRepository.findAll().size();

        // Create the Part with an existing ID
        part.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartMockMvc.perform(post("/api/parts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(part)))
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeCreate);

        // Validate the Part in Elasticsearch
        verify(mockPartSearchRepository, times(0)).save(part);
    }


    @Test
    @Transactional
    public void getAllParts() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList
        restPartMockMvc.perform(get("/api/parts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(part.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].posibleOptions").value(hasItem(DEFAULT_POSIBLE_OPTIONS.toString())));
    }
    
    @Test
    @Transactional
    public void getPart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get the part
        restPartMockMvc.perform(get("/api/parts/{id}", part.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(part.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.posibleOptions").value(DEFAULT_POSIBLE_OPTIONS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPart() throws Exception {
        // Get the part
        restPartMockMvc.perform(get("/api/parts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        int databaseSizeBeforeUpdate = partRepository.findAll().size();

        // Update the part
        Part updatedPart = partRepository.findById(part.getId()).get();
        // Disconnect from session so that the updates on updatedPart are not directly saved in db
        em.detach(updatedPart);
        updatedPart
            .text(UPDATED_TEXT)
            .posibleOptions(UPDATED_POSIBLE_OPTIONS);

        restPartMockMvc.perform(put("/api/parts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPart)))
            .andExpect(status().isOk());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
        Part testPart = partList.get(partList.size() - 1);
        assertThat(testPart.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testPart.getPosibleOptions()).isEqualTo(UPDATED_POSIBLE_OPTIONS);

        // Validate the Part in Elasticsearch
        verify(mockPartSearchRepository, times(1)).save(testPart);
    }

    @Test
    @Transactional
    public void updateNonExistingPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();

        // Create the Part

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartMockMvc.perform(put("/api/parts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(part)))
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Part in Elasticsearch
        verify(mockPartSearchRepository, times(0)).save(part);
    }

    @Test
    @Transactional
    public void deletePart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        int databaseSizeBeforeDelete = partRepository.findAll().size();

        // Delete the part
        restPartMockMvc.perform(delete("/api/parts/{id}", part.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Part in Elasticsearch
        verify(mockPartSearchRepository, times(1)).deleteById(part.getId());
    }

    @Test
    @Transactional
    public void searchPart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);
        when(mockPartSearchRepository.search(queryStringQuery("id:" + part.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(part), PageRequest.of(0, 1), 1));
        // Search the part
        restPartMockMvc.perform(get("/api/_search/parts?query=id:" + part.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(part.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].posibleOptions").value(hasItem(DEFAULT_POSIBLE_OPTIONS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Part.class);
        Part part1 = new Part();
        part1.setId(1L);
        Part part2 = new Part();
        part2.setId(part1.getId());
        assertThat(part1).isEqualTo(part2);
        part2.setId(2L);
        assertThat(part1).isNotEqualTo(part2);
        part1.setId(null);
        assertThat(part1).isNotEqualTo(part2);
    }
}
