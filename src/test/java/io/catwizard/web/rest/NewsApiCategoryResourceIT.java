package io.catwizard.web.rest;

import io.catwizard.IndexerApp;
import io.catwizard.domain.NewsApiCategory;
import io.catwizard.repository.NewsApiCategoryRepository;
import io.catwizard.repository.search.NewsApiCategorySearchRepository;
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
 * Integration tests for the {@link NewsApiCategoryResource} REST controller.
 */
@SpringBootTest(classes = IndexerApp.class)
public class NewsApiCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private NewsApiCategoryRepository newsApiCategoryRepository;

    /**
     * This repository is mocked in the io.catwizard.repository.search test package.
     *
     * @see io.catwizard.repository.search.NewsApiCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private NewsApiCategorySearchRepository mockNewsApiCategorySearchRepository;

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

    private MockMvc restNewsApiCategoryMockMvc;

    private NewsApiCategory newsApiCategory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NewsApiCategoryResource newsApiCategoryResource = new NewsApiCategoryResource(newsApiCategoryRepository, mockNewsApiCategorySearchRepository);
        this.restNewsApiCategoryMockMvc = MockMvcBuilders.standaloneSetup(newsApiCategoryResource)
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
    public static NewsApiCategory createEntity(EntityManager em) {
        NewsApiCategory newsApiCategory = new NewsApiCategory()
            .name(DEFAULT_NAME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .active(DEFAULT_ACTIVE);
        return newsApiCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NewsApiCategory createUpdatedEntity(EntityManager em) {
        NewsApiCategory newsApiCategory = new NewsApiCategory()
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .active(UPDATED_ACTIVE);
        return newsApiCategory;
    }

    @BeforeEach
    public void initTest() {
        newsApiCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createNewsApiCategory() throws Exception {
        int databaseSizeBeforeCreate = newsApiCategoryRepository.findAll().size();

        // Create the NewsApiCategory
        restNewsApiCategoryMockMvc.perform(post("/api/news-api-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsApiCategory)))
            .andExpect(status().isCreated());

        // Validate the NewsApiCategory in the database
        List<NewsApiCategory> newsApiCategoryList = newsApiCategoryRepository.findAll();
        assertThat(newsApiCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        NewsApiCategory testNewsApiCategory = newsApiCategoryList.get(newsApiCategoryList.size() - 1);
        assertThat(testNewsApiCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNewsApiCategory.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testNewsApiCategory.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testNewsApiCategory.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the NewsApiCategory in Elasticsearch
        verify(mockNewsApiCategorySearchRepository, times(1)).save(testNewsApiCategory);
    }

    @Test
    @Transactional
    public void createNewsApiCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = newsApiCategoryRepository.findAll().size();

        // Create the NewsApiCategory with an existing ID
        newsApiCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNewsApiCategoryMockMvc.perform(post("/api/news-api-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsApiCategory)))
            .andExpect(status().isBadRequest());

        // Validate the NewsApiCategory in the database
        List<NewsApiCategory> newsApiCategoryList = newsApiCategoryRepository.findAll();
        assertThat(newsApiCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the NewsApiCategory in Elasticsearch
        verify(mockNewsApiCategorySearchRepository, times(0)).save(newsApiCategory);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsApiCategoryRepository.findAll().size();
        // set the field null
        newsApiCategory.setName(null);

        // Create the NewsApiCategory, which fails.

        restNewsApiCategoryMockMvc.perform(post("/api/news-api-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsApiCategory)))
            .andExpect(status().isBadRequest());

        List<NewsApiCategory> newsApiCategoryList = newsApiCategoryRepository.findAll();
        assertThat(newsApiCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNewsApiCategories() throws Exception {
        // Initialize the database
        newsApiCategoryRepository.saveAndFlush(newsApiCategory);

        // Get all the newsApiCategoryList
        restNewsApiCategoryMockMvc.perform(get("/api/news-api-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newsApiCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getNewsApiCategory() throws Exception {
        // Initialize the database
        newsApiCategoryRepository.saveAndFlush(newsApiCategory);

        // Get the newsApiCategory
        restNewsApiCategoryMockMvc.perform(get("/api/news-api-categories/{id}", newsApiCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(newsApiCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNewsApiCategory() throws Exception {
        // Get the newsApiCategory
        restNewsApiCategoryMockMvc.perform(get("/api/news-api-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNewsApiCategory() throws Exception {
        // Initialize the database
        newsApiCategoryRepository.saveAndFlush(newsApiCategory);

        int databaseSizeBeforeUpdate = newsApiCategoryRepository.findAll().size();

        // Update the newsApiCategory
        NewsApiCategory updatedNewsApiCategory = newsApiCategoryRepository.findById(newsApiCategory.getId()).get();
        // Disconnect from session so that the updates on updatedNewsApiCategory are not directly saved in db
        em.detach(updatedNewsApiCategory);
        updatedNewsApiCategory
            .name(UPDATED_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .active(UPDATED_ACTIVE);

        restNewsApiCategoryMockMvc.perform(put("/api/news-api-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNewsApiCategory)))
            .andExpect(status().isOk());

        // Validate the NewsApiCategory in the database
        List<NewsApiCategory> newsApiCategoryList = newsApiCategoryRepository.findAll();
        assertThat(newsApiCategoryList).hasSize(databaseSizeBeforeUpdate);
        NewsApiCategory testNewsApiCategory = newsApiCategoryList.get(newsApiCategoryList.size() - 1);
        assertThat(testNewsApiCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNewsApiCategory.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testNewsApiCategory.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testNewsApiCategory.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the NewsApiCategory in Elasticsearch
        verify(mockNewsApiCategorySearchRepository, times(1)).save(testNewsApiCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingNewsApiCategory() throws Exception {
        int databaseSizeBeforeUpdate = newsApiCategoryRepository.findAll().size();

        // Create the NewsApiCategory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsApiCategoryMockMvc.perform(put("/api/news-api-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(newsApiCategory)))
            .andExpect(status().isBadRequest());

        // Validate the NewsApiCategory in the database
        List<NewsApiCategory> newsApiCategoryList = newsApiCategoryRepository.findAll();
        assertThat(newsApiCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NewsApiCategory in Elasticsearch
        verify(mockNewsApiCategorySearchRepository, times(0)).save(newsApiCategory);
    }

    @Test
    @Transactional
    public void deleteNewsApiCategory() throws Exception {
        // Initialize the database
        newsApiCategoryRepository.saveAndFlush(newsApiCategory);

        int databaseSizeBeforeDelete = newsApiCategoryRepository.findAll().size();

        // Delete the newsApiCategory
        restNewsApiCategoryMockMvc.perform(delete("/api/news-api-categories/{id}", newsApiCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NewsApiCategory> newsApiCategoryList = newsApiCategoryRepository.findAll();
        assertThat(newsApiCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NewsApiCategory in Elasticsearch
        verify(mockNewsApiCategorySearchRepository, times(1)).deleteById(newsApiCategory.getId());
    }

    @Test
    @Transactional
    public void searchNewsApiCategory() throws Exception {
        // Initialize the database
        newsApiCategoryRepository.saveAndFlush(newsApiCategory);
        when(mockNewsApiCategorySearchRepository.search(queryStringQuery("id:" + newsApiCategory.getId())))
            .thenReturn(Collections.singletonList(newsApiCategory));
        // Search the newsApiCategory
        restNewsApiCategoryMockMvc.perform(get("/api/_search/news-api-categories?query=id:" + newsApiCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newsApiCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NewsApiCategory.class);
        NewsApiCategory newsApiCategory1 = new NewsApiCategory();
        newsApiCategory1.setId(1L);
        NewsApiCategory newsApiCategory2 = new NewsApiCategory();
        newsApiCategory2.setId(newsApiCategory1.getId());
        assertThat(newsApiCategory1).isEqualTo(newsApiCategory2);
        newsApiCategory2.setId(2L);
        assertThat(newsApiCategory1).isNotEqualTo(newsApiCategory2);
        newsApiCategory1.setId(null);
        assertThat(newsApiCategory1).isNotEqualTo(newsApiCategory2);
    }
}
