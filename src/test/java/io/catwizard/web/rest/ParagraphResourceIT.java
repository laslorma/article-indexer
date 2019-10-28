package io.catwizard.web.rest;

import io.catwizard.IndexerApp;
import io.catwizard.domain.Paragraph;
import io.catwizard.repository.ParagraphRepository;
import io.catwizard.repository.search.ParagraphSearchRepository;
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
 * Integration tests for the {@link ParagraphResource} REST controller.
 */
@SpringBootTest(classes = IndexerApp.class)
public class ParagraphResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_CLEANED_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_CLEANED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_WORDS = 1;
    private static final Integer UPDATED_TOTAL_WORDS = 2;
    private static final Integer SMALLER_TOTAL_WORDS = 1 - 1;

    private static final Boolean DEFAULT_HEADER = false;
    private static final Boolean UPDATED_HEADER = true;

    private static final String DEFAULT_READABILITY = "AAAAAAAAAA";
    private static final String UPDATED_READABILITY = "BBBBBBBBBB";

    @Autowired
    private ParagraphRepository paragraphRepository;

    /**
     * This repository is mocked in the io.catwizard.repository.search test package.
     *
     * @see io.catwizard.repository.search.ParagraphSearchRepositoryMockConfiguration
     */
    @Autowired
    private ParagraphSearchRepository mockParagraphSearchRepository;

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

    private MockMvc restParagraphMockMvc;

    private Paragraph paragraph;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParagraphResource paragraphResource = new ParagraphResource(paragraphRepository, mockParagraphSearchRepository);
        this.restParagraphMockMvc = MockMvcBuilders.standaloneSetup(paragraphResource)
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
    public static Paragraph createEntity(EntityManager em) {
        Paragraph paragraph = new Paragraph()
            .content(DEFAULT_CONTENT)
            .originalCleanedContent(DEFAULT_ORIGINAL_CLEANED_CONTENT)
            .totalWords(DEFAULT_TOTAL_WORDS)
            .header(DEFAULT_HEADER)
            .readability(DEFAULT_READABILITY);
        return paragraph;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paragraph createUpdatedEntity(EntityManager em) {
        Paragraph paragraph = new Paragraph()
            .content(UPDATED_CONTENT)
            .originalCleanedContent(UPDATED_ORIGINAL_CLEANED_CONTENT)
            .totalWords(UPDATED_TOTAL_WORDS)
            .header(UPDATED_HEADER)
            .readability(UPDATED_READABILITY);
        return paragraph;
    }

    @BeforeEach
    public void initTest() {
        paragraph = createEntity(em);
    }

    @Test
    @Transactional
    public void createParagraph() throws Exception {
        int databaseSizeBeforeCreate = paragraphRepository.findAll().size();

        // Create the Paragraph
        restParagraphMockMvc.perform(post("/api/paragraphs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paragraph)))
            .andExpect(status().isCreated());

        // Validate the Paragraph in the database
        List<Paragraph> paragraphList = paragraphRepository.findAll();
        assertThat(paragraphList).hasSize(databaseSizeBeforeCreate + 1);
        Paragraph testParagraph = paragraphList.get(paragraphList.size() - 1);
        assertThat(testParagraph.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testParagraph.getOriginalCleanedContent()).isEqualTo(DEFAULT_ORIGINAL_CLEANED_CONTENT);
        assertThat(testParagraph.getTotalWords()).isEqualTo(DEFAULT_TOTAL_WORDS);
        assertThat(testParagraph.isHeader()).isEqualTo(DEFAULT_HEADER);
        assertThat(testParagraph.getReadability()).isEqualTo(DEFAULT_READABILITY);

        // Validate the Paragraph in Elasticsearch
        verify(mockParagraphSearchRepository, times(1)).save(testParagraph);
    }

    @Test
    @Transactional
    public void createParagraphWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paragraphRepository.findAll().size();

        // Create the Paragraph with an existing ID
        paragraph.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParagraphMockMvc.perform(post("/api/paragraphs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paragraph)))
            .andExpect(status().isBadRequest());

        // Validate the Paragraph in the database
        List<Paragraph> paragraphList = paragraphRepository.findAll();
        assertThat(paragraphList).hasSize(databaseSizeBeforeCreate);

        // Validate the Paragraph in Elasticsearch
        verify(mockParagraphSearchRepository, times(0)).save(paragraph);
    }


    @Test
    @Transactional
    public void getAllParagraphs() throws Exception {
        // Initialize the database
        paragraphRepository.saveAndFlush(paragraph);

        // Get all the paragraphList
        restParagraphMockMvc.perform(get("/api/paragraphs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paragraph.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].originalCleanedContent").value(hasItem(DEFAULT_ORIGINAL_CLEANED_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].totalWords").value(hasItem(DEFAULT_TOTAL_WORDS)))
            .andExpect(jsonPath("$.[*].header").value(hasItem(DEFAULT_HEADER.booleanValue())))
            .andExpect(jsonPath("$.[*].readability").value(hasItem(DEFAULT_READABILITY.toString())));
    }
    
    @Test
    @Transactional
    public void getParagraph() throws Exception {
        // Initialize the database
        paragraphRepository.saveAndFlush(paragraph);

        // Get the paragraph
        restParagraphMockMvc.perform(get("/api/paragraphs/{id}", paragraph.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paragraph.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.originalCleanedContent").value(DEFAULT_ORIGINAL_CLEANED_CONTENT.toString()))
            .andExpect(jsonPath("$.totalWords").value(DEFAULT_TOTAL_WORDS))
            .andExpect(jsonPath("$.header").value(DEFAULT_HEADER.booleanValue()))
            .andExpect(jsonPath("$.readability").value(DEFAULT_READABILITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParagraph() throws Exception {
        // Get the paragraph
        restParagraphMockMvc.perform(get("/api/paragraphs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParagraph() throws Exception {
        // Initialize the database
        paragraphRepository.saveAndFlush(paragraph);

        int databaseSizeBeforeUpdate = paragraphRepository.findAll().size();

        // Update the paragraph
        Paragraph updatedParagraph = paragraphRepository.findById(paragraph.getId()).get();
        // Disconnect from session so that the updates on updatedParagraph are not directly saved in db
        em.detach(updatedParagraph);
        updatedParagraph
            .content(UPDATED_CONTENT)
            .originalCleanedContent(UPDATED_ORIGINAL_CLEANED_CONTENT)
            .totalWords(UPDATED_TOTAL_WORDS)
            .header(UPDATED_HEADER)
            .readability(UPDATED_READABILITY);

        restParagraphMockMvc.perform(put("/api/paragraphs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParagraph)))
            .andExpect(status().isOk());

        // Validate the Paragraph in the database
        List<Paragraph> paragraphList = paragraphRepository.findAll();
        assertThat(paragraphList).hasSize(databaseSizeBeforeUpdate);
        Paragraph testParagraph = paragraphList.get(paragraphList.size() - 1);
        assertThat(testParagraph.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testParagraph.getOriginalCleanedContent()).isEqualTo(UPDATED_ORIGINAL_CLEANED_CONTENT);
        assertThat(testParagraph.getTotalWords()).isEqualTo(UPDATED_TOTAL_WORDS);
        assertThat(testParagraph.isHeader()).isEqualTo(UPDATED_HEADER);
        assertThat(testParagraph.getReadability()).isEqualTo(UPDATED_READABILITY);

        // Validate the Paragraph in Elasticsearch
        verify(mockParagraphSearchRepository, times(1)).save(testParagraph);
    }

    @Test
    @Transactional
    public void updateNonExistingParagraph() throws Exception {
        int databaseSizeBeforeUpdate = paragraphRepository.findAll().size();

        // Create the Paragraph

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParagraphMockMvc.perform(put("/api/paragraphs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paragraph)))
            .andExpect(status().isBadRequest());

        // Validate the Paragraph in the database
        List<Paragraph> paragraphList = paragraphRepository.findAll();
        assertThat(paragraphList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Paragraph in Elasticsearch
        verify(mockParagraphSearchRepository, times(0)).save(paragraph);
    }

    @Test
    @Transactional
    public void deleteParagraph() throws Exception {
        // Initialize the database
        paragraphRepository.saveAndFlush(paragraph);

        int databaseSizeBeforeDelete = paragraphRepository.findAll().size();

        // Delete the paragraph
        restParagraphMockMvc.perform(delete("/api/paragraphs/{id}", paragraph.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paragraph> paragraphList = paragraphRepository.findAll();
        assertThat(paragraphList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Paragraph in Elasticsearch
        verify(mockParagraphSearchRepository, times(1)).deleteById(paragraph.getId());
    }

    @Test
    @Transactional
    public void searchParagraph() throws Exception {
        // Initialize the database
        paragraphRepository.saveAndFlush(paragraph);
        when(mockParagraphSearchRepository.search(queryStringQuery("id:" + paragraph.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(paragraph), PageRequest.of(0, 1), 1));
        // Search the paragraph
        restParagraphMockMvc.perform(get("/api/_search/paragraphs?query=id:" + paragraph.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paragraph.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].originalCleanedContent").value(hasItem(DEFAULT_ORIGINAL_CLEANED_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].totalWords").value(hasItem(DEFAULT_TOTAL_WORDS)))
            .andExpect(jsonPath("$.[*].header").value(hasItem(DEFAULT_HEADER.booleanValue())))
            .andExpect(jsonPath("$.[*].readability").value(hasItem(DEFAULT_READABILITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paragraph.class);
        Paragraph paragraph1 = new Paragraph();
        paragraph1.setId(1L);
        Paragraph paragraph2 = new Paragraph();
        paragraph2.setId(paragraph1.getId());
        assertThat(paragraph1).isEqualTo(paragraph2);
        paragraph2.setId(2L);
        assertThat(paragraph1).isNotEqualTo(paragraph2);
        paragraph1.setId(null);
        assertThat(paragraph1).isNotEqualTo(paragraph2);
    }
}
