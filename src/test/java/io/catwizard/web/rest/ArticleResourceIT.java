package io.catwizard.web.rest;

import io.catwizard.IndexerApp;
import io.catwizard.domain.Article;
import io.catwizard.domain.NewsApiCategory;
import io.catwizard.domain.Source;
import io.catwizard.domain.Paragraph;
import io.catwizard.domain.Part;
import io.catwizard.repository.ArticleRepository;
import io.catwizard.repository.search.ArticleSearchRepository;
import io.catwizard.service.ArticleService;
import io.catwizard.web.rest.errors.ExceptionTranslator;
import io.catwizard.service.dto.ArticleCriteria;
import io.catwizard.service.ArticleQueryService;

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
 * Integration tests for the {@link ArticleResource} REST controller.
 */
@SpringBootTest(classes = IndexerApp.class)
public class ArticleResourceIT {

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_URL_TO_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_URL_TO_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLISHED_AT = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHED_AT = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_CODE = "AA";
    private static final String UPDATED_COUNTRY_CODE = "BB";

    private static final String DEFAULT_LANGUAGE_CODE = "AA";
    private static final String UPDATED_LANGUAGE_CODE = "BB";

    private static final String DEFAULT_SENTIMENT = "AAAAAAAAAA";
    private static final String UPDATED_SENTIMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_READABILITY = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_READABILITY = "BBBBBBBBBB";

    private static final Long DEFAULT_NUMBER_OF_PARTS = 1L;
    private static final Long UPDATED_NUMBER_OF_PARTS = 2L;
    private static final Long SMALLER_NUMBER_OF_PARTS = 1L - 1L;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    /**
     * This repository is mocked in the io.catwizard.repository.search test package.
     *
     * @see io.catwizard.repository.search.ArticleSearchRepositoryMockConfiguration
     */
    @Autowired
    private ArticleSearchRepository mockArticleSearchRepository;

    @Autowired
    private ArticleQueryService articleQueryService;

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

    private MockMvc restArticleMockMvc;

    private Article article;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArticleResource articleResource = new ArticleResource(articleService, articleQueryService);
        this.restArticleMockMvc = MockMvcBuilders.standaloneSetup(articleResource)
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
    public static Article createEntity(EntityManager em) {
        Article article = new Article()
            .author(DEFAULT_AUTHOR)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .urlToImage(DEFAULT_URL_TO_IMAGE)
            .publishedAt(DEFAULT_PUBLISHED_AT)
            .category(DEFAULT_CATEGORY)
            .content(DEFAULT_CONTENT)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .languageCode(DEFAULT_LANGUAGE_CODE)
            .sentiment(DEFAULT_SENTIMENT)
            .textReadability(DEFAULT_TEXT_READABILITY)
            .numberOfParts(DEFAULT_NUMBER_OF_PARTS);
        return article;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createUpdatedEntity(EntityManager em) {
        Article article = new Article()
            .author(UPDATED_AUTHOR)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .urlToImage(UPDATED_URL_TO_IMAGE)
            .publishedAt(UPDATED_PUBLISHED_AT)
            .category(UPDATED_CATEGORY)
            .content(UPDATED_CONTENT)
            .countryCode(UPDATED_COUNTRY_CODE)
            .languageCode(UPDATED_LANGUAGE_CODE)
            .sentiment(UPDATED_SENTIMENT)
            .textReadability(UPDATED_TEXT_READABILITY)
            .numberOfParts(UPDATED_NUMBER_OF_PARTS);
        return article;
    }

    @BeforeEach
    public void initTest() {
        article = createEntity(em);
    }

    @Test
    @Transactional
    public void createArticle() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        // Create the Article
        restArticleMockMvc.perform(post("/api/articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isCreated());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate + 1);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testArticle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testArticle.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testArticle.getUrlToImage()).isEqualTo(DEFAULT_URL_TO_IMAGE);
        assertThat(testArticle.getPublishedAt()).isEqualTo(DEFAULT_PUBLISHED_AT);
        assertThat(testArticle.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testArticle.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testArticle.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testArticle.getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
        assertThat(testArticle.getSentiment()).isEqualTo(DEFAULT_SENTIMENT);
        assertThat(testArticle.getTextReadability()).isEqualTo(DEFAULT_TEXT_READABILITY);
        assertThat(testArticle.getNumberOfParts()).isEqualTo(DEFAULT_NUMBER_OF_PARTS);

        // Validate the Article in Elasticsearch
        verify(mockArticleSearchRepository, times(1)).save(testArticle);
    }

    @Test
    @Transactional
    public void createArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        // Create the Article with an existing ID
        article.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleMockMvc.perform(post("/api/articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate);

        // Validate the Article in Elasticsearch
        verify(mockArticleSearchRepository, times(0)).save(article);
    }


    @Test
    @Transactional
    public void getAllArticles() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].urlToImage").value(hasItem(DEFAULT_URL_TO_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].publishedAt").value(hasItem(DEFAULT_PUBLISHED_AT.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE.toString())))
            .andExpect(jsonPath("$.[*].sentiment").value(hasItem(DEFAULT_SENTIMENT.toString())))
            .andExpect(jsonPath("$.[*].textReadability").value(hasItem(DEFAULT_TEXT_READABILITY.toString())))
            .andExpect(jsonPath("$.[*].numberOfParts").value(hasItem(DEFAULT_NUMBER_OF_PARTS.intValue())));
    }
    
    @Test
    @Transactional
    public void getArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.urlToImage").value(DEFAULT_URL_TO_IMAGE.toString()))
            .andExpect(jsonPath("$.publishedAt").value(DEFAULT_PUBLISHED_AT.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE.toString()))
            .andExpect(jsonPath("$.languageCode").value(DEFAULT_LANGUAGE_CODE.toString()))
            .andExpect(jsonPath("$.sentiment").value(DEFAULT_SENTIMENT.toString()))
            .andExpect(jsonPath("$.textReadability").value(DEFAULT_TEXT_READABILITY.toString()))
            .andExpect(jsonPath("$.numberOfParts").value(DEFAULT_NUMBER_OF_PARTS.intValue()));
    }

    @Test
    @Transactional
    public void getAllArticlesByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where author equals to DEFAULT_AUTHOR
        defaultArticleShouldBeFound("author.equals=" + DEFAULT_AUTHOR);

        // Get all the articleList where author equals to UPDATED_AUTHOR
        defaultArticleShouldNotBeFound("author.equals=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllArticlesByAuthorIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where author in DEFAULT_AUTHOR or UPDATED_AUTHOR
        defaultArticleShouldBeFound("author.in=" + DEFAULT_AUTHOR + "," + UPDATED_AUTHOR);

        // Get all the articleList where author equals to UPDATED_AUTHOR
        defaultArticleShouldNotBeFound("author.in=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllArticlesByAuthorIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where author is not null
        defaultArticleShouldBeFound("author.specified=true");

        // Get all the articleList where author is null
        defaultArticleShouldNotBeFound("author.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where title equals to DEFAULT_TITLE
        defaultArticleShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the articleList where title equals to UPDATED_TITLE
        defaultArticleShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllArticlesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultArticleShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the articleList where title equals to UPDATED_TITLE
        defaultArticleShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllArticlesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where title is not null
        defaultArticleShouldBeFound("title.specified=true");

        // Get all the articleList where title is null
        defaultArticleShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where url equals to DEFAULT_URL
        defaultArticleShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the articleList where url equals to UPDATED_URL
        defaultArticleShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllArticlesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where url in DEFAULT_URL or UPDATED_URL
        defaultArticleShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the articleList where url equals to UPDATED_URL
        defaultArticleShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllArticlesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where url is not null
        defaultArticleShouldBeFound("url.specified=true");

        // Get all the articleList where url is null
        defaultArticleShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByUrlToImageIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where urlToImage equals to DEFAULT_URL_TO_IMAGE
        defaultArticleShouldBeFound("urlToImage.equals=" + DEFAULT_URL_TO_IMAGE);

        // Get all the articleList where urlToImage equals to UPDATED_URL_TO_IMAGE
        defaultArticleShouldNotBeFound("urlToImage.equals=" + UPDATED_URL_TO_IMAGE);
    }

    @Test
    @Transactional
    public void getAllArticlesByUrlToImageIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where urlToImage in DEFAULT_URL_TO_IMAGE or UPDATED_URL_TO_IMAGE
        defaultArticleShouldBeFound("urlToImage.in=" + DEFAULT_URL_TO_IMAGE + "," + UPDATED_URL_TO_IMAGE);

        // Get all the articleList where urlToImage equals to UPDATED_URL_TO_IMAGE
        defaultArticleShouldNotBeFound("urlToImage.in=" + UPDATED_URL_TO_IMAGE);
    }

    @Test
    @Transactional
    public void getAllArticlesByUrlToImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where urlToImage is not null
        defaultArticleShouldBeFound("urlToImage.specified=true");

        // Get all the articleList where urlToImage is null
        defaultArticleShouldNotBeFound("urlToImage.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByPublishedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where publishedAt equals to DEFAULT_PUBLISHED_AT
        defaultArticleShouldBeFound("publishedAt.equals=" + DEFAULT_PUBLISHED_AT);

        // Get all the articleList where publishedAt equals to UPDATED_PUBLISHED_AT
        defaultArticleShouldNotBeFound("publishedAt.equals=" + UPDATED_PUBLISHED_AT);
    }

    @Test
    @Transactional
    public void getAllArticlesByPublishedAtIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where publishedAt in DEFAULT_PUBLISHED_AT or UPDATED_PUBLISHED_AT
        defaultArticleShouldBeFound("publishedAt.in=" + DEFAULT_PUBLISHED_AT + "," + UPDATED_PUBLISHED_AT);

        // Get all the articleList where publishedAt equals to UPDATED_PUBLISHED_AT
        defaultArticleShouldNotBeFound("publishedAt.in=" + UPDATED_PUBLISHED_AT);
    }

    @Test
    @Transactional
    public void getAllArticlesByPublishedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where publishedAt is not null
        defaultArticleShouldBeFound("publishedAt.specified=true");

        // Get all the articleList where publishedAt is null
        defaultArticleShouldNotBeFound("publishedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where category equals to DEFAULT_CATEGORY
        defaultArticleShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the articleList where category equals to UPDATED_CATEGORY
        defaultArticleShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllArticlesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultArticleShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the articleList where category equals to UPDATED_CATEGORY
        defaultArticleShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllArticlesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where category is not null
        defaultArticleShouldBeFound("category.specified=true");

        // Get all the articleList where category is null
        defaultArticleShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByCountryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where countryCode equals to DEFAULT_COUNTRY_CODE
        defaultArticleShouldBeFound("countryCode.equals=" + DEFAULT_COUNTRY_CODE);

        // Get all the articleList where countryCode equals to UPDATED_COUNTRY_CODE
        defaultArticleShouldNotBeFound("countryCode.equals=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    public void getAllArticlesByCountryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where countryCode in DEFAULT_COUNTRY_CODE or UPDATED_COUNTRY_CODE
        defaultArticleShouldBeFound("countryCode.in=" + DEFAULT_COUNTRY_CODE + "," + UPDATED_COUNTRY_CODE);

        // Get all the articleList where countryCode equals to UPDATED_COUNTRY_CODE
        defaultArticleShouldNotBeFound("countryCode.in=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    public void getAllArticlesByCountryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where countryCode is not null
        defaultArticleShouldBeFound("countryCode.specified=true");

        // Get all the articleList where countryCode is null
        defaultArticleShouldNotBeFound("countryCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByLanguageCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where languageCode equals to DEFAULT_LANGUAGE_CODE
        defaultArticleShouldBeFound("languageCode.equals=" + DEFAULT_LANGUAGE_CODE);

        // Get all the articleList where languageCode equals to UPDATED_LANGUAGE_CODE
        defaultArticleShouldNotBeFound("languageCode.equals=" + UPDATED_LANGUAGE_CODE);
    }

    @Test
    @Transactional
    public void getAllArticlesByLanguageCodeIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where languageCode in DEFAULT_LANGUAGE_CODE or UPDATED_LANGUAGE_CODE
        defaultArticleShouldBeFound("languageCode.in=" + DEFAULT_LANGUAGE_CODE + "," + UPDATED_LANGUAGE_CODE);

        // Get all the articleList where languageCode equals to UPDATED_LANGUAGE_CODE
        defaultArticleShouldNotBeFound("languageCode.in=" + UPDATED_LANGUAGE_CODE);
    }

    @Test
    @Transactional
    public void getAllArticlesByLanguageCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where languageCode is not null
        defaultArticleShouldBeFound("languageCode.specified=true");

        // Get all the articleList where languageCode is null
        defaultArticleShouldNotBeFound("languageCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesBySentimentIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sentiment equals to DEFAULT_SENTIMENT
        defaultArticleShouldBeFound("sentiment.equals=" + DEFAULT_SENTIMENT);

        // Get all the articleList where sentiment equals to UPDATED_SENTIMENT
        defaultArticleShouldNotBeFound("sentiment.equals=" + UPDATED_SENTIMENT);
    }

    @Test
    @Transactional
    public void getAllArticlesBySentimentIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sentiment in DEFAULT_SENTIMENT or UPDATED_SENTIMENT
        defaultArticleShouldBeFound("sentiment.in=" + DEFAULT_SENTIMENT + "," + UPDATED_SENTIMENT);

        // Get all the articleList where sentiment equals to UPDATED_SENTIMENT
        defaultArticleShouldNotBeFound("sentiment.in=" + UPDATED_SENTIMENT);
    }

    @Test
    @Transactional
    public void getAllArticlesBySentimentIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sentiment is not null
        defaultArticleShouldBeFound("sentiment.specified=true");

        // Get all the articleList where sentiment is null
        defaultArticleShouldNotBeFound("sentiment.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByTextReadabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where textReadability equals to DEFAULT_TEXT_READABILITY
        defaultArticleShouldBeFound("textReadability.equals=" + DEFAULT_TEXT_READABILITY);

        // Get all the articleList where textReadability equals to UPDATED_TEXT_READABILITY
        defaultArticleShouldNotBeFound("textReadability.equals=" + UPDATED_TEXT_READABILITY);
    }

    @Test
    @Transactional
    public void getAllArticlesByTextReadabilityIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where textReadability in DEFAULT_TEXT_READABILITY or UPDATED_TEXT_READABILITY
        defaultArticleShouldBeFound("textReadability.in=" + DEFAULT_TEXT_READABILITY + "," + UPDATED_TEXT_READABILITY);

        // Get all the articleList where textReadability equals to UPDATED_TEXT_READABILITY
        defaultArticleShouldNotBeFound("textReadability.in=" + UPDATED_TEXT_READABILITY);
    }

    @Test
    @Transactional
    public void getAllArticlesByTextReadabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where textReadability is not null
        defaultArticleShouldBeFound("textReadability.specified=true");

        // Get all the articleList where textReadability is null
        defaultArticleShouldNotBeFound("textReadability.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByNumberOfPartsIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where numberOfParts equals to DEFAULT_NUMBER_OF_PARTS
        defaultArticleShouldBeFound("numberOfParts.equals=" + DEFAULT_NUMBER_OF_PARTS);

        // Get all the articleList where numberOfParts equals to UPDATED_NUMBER_OF_PARTS
        defaultArticleShouldNotBeFound("numberOfParts.equals=" + UPDATED_NUMBER_OF_PARTS);
    }

    @Test
    @Transactional
    public void getAllArticlesByNumberOfPartsIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where numberOfParts in DEFAULT_NUMBER_OF_PARTS or UPDATED_NUMBER_OF_PARTS
        defaultArticleShouldBeFound("numberOfParts.in=" + DEFAULT_NUMBER_OF_PARTS + "," + UPDATED_NUMBER_OF_PARTS);

        // Get all the articleList where numberOfParts equals to UPDATED_NUMBER_OF_PARTS
        defaultArticleShouldNotBeFound("numberOfParts.in=" + UPDATED_NUMBER_OF_PARTS);
    }

    @Test
    @Transactional
    public void getAllArticlesByNumberOfPartsIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where numberOfParts is not null
        defaultArticleShouldBeFound("numberOfParts.specified=true");

        // Get all the articleList where numberOfParts is null
        defaultArticleShouldNotBeFound("numberOfParts.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByNumberOfPartsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where numberOfParts is greater than or equal to DEFAULT_NUMBER_OF_PARTS
        defaultArticleShouldBeFound("numberOfParts.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PARTS);

        // Get all the articleList where numberOfParts is greater than or equal to UPDATED_NUMBER_OF_PARTS
        defaultArticleShouldNotBeFound("numberOfParts.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PARTS);
    }

    @Test
    @Transactional
    public void getAllArticlesByNumberOfPartsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where numberOfParts is less than or equal to DEFAULT_NUMBER_OF_PARTS
        defaultArticleShouldBeFound("numberOfParts.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PARTS);

        // Get all the articleList where numberOfParts is less than or equal to SMALLER_NUMBER_OF_PARTS
        defaultArticleShouldNotBeFound("numberOfParts.lessThanOrEqual=" + SMALLER_NUMBER_OF_PARTS);
    }

    @Test
    @Transactional
    public void getAllArticlesByNumberOfPartsIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where numberOfParts is less than DEFAULT_NUMBER_OF_PARTS
        defaultArticleShouldNotBeFound("numberOfParts.lessThan=" + DEFAULT_NUMBER_OF_PARTS);

        // Get all the articleList where numberOfParts is less than UPDATED_NUMBER_OF_PARTS
        defaultArticleShouldBeFound("numberOfParts.lessThan=" + UPDATED_NUMBER_OF_PARTS);
    }

    @Test
    @Transactional
    public void getAllArticlesByNumberOfPartsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where numberOfParts is greater than DEFAULT_NUMBER_OF_PARTS
        defaultArticleShouldNotBeFound("numberOfParts.greaterThan=" + DEFAULT_NUMBER_OF_PARTS);

        // Get all the articleList where numberOfParts is greater than SMALLER_NUMBER_OF_PARTS
        defaultArticleShouldBeFound("numberOfParts.greaterThan=" + SMALLER_NUMBER_OF_PARTS);
    }


    @Test
    @Transactional
    public void getAllArticlesByNewsApiCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        NewsApiCategory newsApiCategory = NewsApiCategoryResourceIT.createEntity(em);
        em.persist(newsApiCategory);
        em.flush();
        article.setNewsApiCategory(newsApiCategory);
        articleRepository.saveAndFlush(article);
        Long newsApiCategoryId = newsApiCategory.getId();

        // Get all the articleList where newsApiCategory equals to newsApiCategoryId
        defaultArticleShouldBeFound("newsApiCategoryId.equals=" + newsApiCategoryId);

        // Get all the articleList where newsApiCategory equals to newsApiCategoryId + 1
        defaultArticleShouldNotBeFound("newsApiCategoryId.equals=" + (newsApiCategoryId + 1));
    }


    @Test
    @Transactional
    public void getAllArticlesBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        Source source = SourceResourceIT.createEntity(em);
        em.persist(source);
        em.flush();
        article.setSource(source);
        articleRepository.saveAndFlush(article);
        Long sourceId = source.getId();

        // Get all the articleList where source equals to sourceId
        defaultArticleShouldBeFound("sourceId.equals=" + sourceId);

        // Get all the articleList where source equals to sourceId + 1
        defaultArticleShouldNotBeFound("sourceId.equals=" + (sourceId + 1));
    }


    @Test
    @Transactional
    public void getAllArticlesByParagraphIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        Paragraph paragraph = ParagraphResourceIT.createEntity(em);
        em.persist(paragraph);
        em.flush();
        article.addParagraph(paragraph);
        articleRepository.saveAndFlush(article);
        Long paragraphId = paragraph.getId();

        // Get all the articleList where paragraph equals to paragraphId
        defaultArticleShouldBeFound("paragraphId.equals=" + paragraphId);

        // Get all the articleList where paragraph equals to paragraphId + 1
        defaultArticleShouldNotBeFound("paragraphId.equals=" + (paragraphId + 1));
    }


    @Test
    @Transactional
    public void getAllArticlesByPartIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        Part part = PartResourceIT.createEntity(em);
        em.persist(part);
        em.flush();
        article.addPart(part);
        articleRepository.saveAndFlush(article);
        Long partId = part.getId();

        // Get all the articleList where part equals to partId
        defaultArticleShouldBeFound("partId.equals=" + partId);

        // Get all the articleList where part equals to partId + 1
        defaultArticleShouldNotBeFound("partId.equals=" + (partId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArticleShouldBeFound(String filter) throws Exception {
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].urlToImage").value(hasItem(DEFAULT_URL_TO_IMAGE)))
            .andExpect(jsonPath("$.[*].publishedAt").value(hasItem(DEFAULT_PUBLISHED_AT)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE)))
            .andExpect(jsonPath("$.[*].sentiment").value(hasItem(DEFAULT_SENTIMENT)))
            .andExpect(jsonPath("$.[*].textReadability").value(hasItem(DEFAULT_TEXT_READABILITY)))
            .andExpect(jsonPath("$.[*].numberOfParts").value(hasItem(DEFAULT_NUMBER_OF_PARTS.intValue())));

        // Check, that the count call also returns 1
        restArticleMockMvc.perform(get("/api/articles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArticleShouldNotBeFound(String filter) throws Exception {
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArticleMockMvc.perform(get("/api/articles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingArticle() throws Exception {
        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticle() throws Exception {
        // Initialize the database
        articleService.save(article);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockArticleSearchRepository);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article
        Article updatedArticle = articleRepository.findById(article.getId()).get();
        // Disconnect from session so that the updates on updatedArticle are not directly saved in db
        em.detach(updatedArticle);
        updatedArticle
            .author(UPDATED_AUTHOR)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .urlToImage(UPDATED_URL_TO_IMAGE)
            .publishedAt(UPDATED_PUBLISHED_AT)
            .category(UPDATED_CATEGORY)
            .content(UPDATED_CONTENT)
            .countryCode(UPDATED_COUNTRY_CODE)
            .languageCode(UPDATED_LANGUAGE_CODE)
            .sentiment(UPDATED_SENTIMENT)
            .textReadability(UPDATED_TEXT_READABILITY)
            .numberOfParts(UPDATED_NUMBER_OF_PARTS);

        restArticleMockMvc.perform(put("/api/articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArticle)))
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testArticle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testArticle.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testArticle.getUrlToImage()).isEqualTo(UPDATED_URL_TO_IMAGE);
        assertThat(testArticle.getPublishedAt()).isEqualTo(UPDATED_PUBLISHED_AT);
        assertThat(testArticle.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testArticle.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testArticle.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testArticle.getLanguageCode()).isEqualTo(UPDATED_LANGUAGE_CODE);
        assertThat(testArticle.getSentiment()).isEqualTo(UPDATED_SENTIMENT);
        assertThat(testArticle.getTextReadability()).isEqualTo(UPDATED_TEXT_READABILITY);
        assertThat(testArticle.getNumberOfParts()).isEqualTo(UPDATED_NUMBER_OF_PARTS);

        // Validate the Article in Elasticsearch
        verify(mockArticleSearchRepository, times(1)).save(testArticle);
    }

    @Test
    @Transactional
    public void updateNonExistingArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Create the Article

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc.perform(put("/api/articles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Article in Elasticsearch
        verify(mockArticleSearchRepository, times(0)).save(article);
    }

    @Test
    @Transactional
    public void deleteArticle() throws Exception {
        // Initialize the database
        articleService.save(article);

        int databaseSizeBeforeDelete = articleRepository.findAll().size();

        // Delete the article
        restArticleMockMvc.perform(delete("/api/articles/{id}", article.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Article in Elasticsearch
        verify(mockArticleSearchRepository, times(1)).deleteById(article.getId());
    }

    @Test
    @Transactional
    public void searchArticle() throws Exception {
        // Initialize the database
        articleService.save(article);
        when(mockArticleSearchRepository.search(queryStringQuery("id:" + article.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(article), PageRequest.of(0, 1), 1));
        // Search the article
        restArticleMockMvc.perform(get("/api/_search/articles?query=id:" + article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].urlToImage").value(hasItem(DEFAULT_URL_TO_IMAGE)))
            .andExpect(jsonPath("$.[*].publishedAt").value(hasItem(DEFAULT_PUBLISHED_AT)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE)))
            .andExpect(jsonPath("$.[*].sentiment").value(hasItem(DEFAULT_SENTIMENT)))
            .andExpect(jsonPath("$.[*].textReadability").value(hasItem(DEFAULT_TEXT_READABILITY)))
            .andExpect(jsonPath("$.[*].numberOfParts").value(hasItem(DEFAULT_NUMBER_OF_PARTS.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Article.class);
        Article article1 = new Article();
        article1.setId(1L);
        Article article2 = new Article();
        article2.setId(article1.getId());
        assertThat(article1).isEqualTo(article2);
        article2.setId(2L);
        assertThat(article1).isNotEqualTo(article2);
        article1.setId(null);
        assertThat(article1).isNotEqualTo(article2);
    }
}
