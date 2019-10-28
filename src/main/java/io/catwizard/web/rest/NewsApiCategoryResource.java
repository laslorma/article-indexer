package io.catwizard.web.rest;

import io.catwizard.domain.NewsApiCategory;
import io.catwizard.repository.NewsApiCategoryRepository;
import io.catwizard.repository.search.NewsApiCategorySearchRepository;
import io.catwizard.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.catwizard.domain.NewsApiCategory}.
 */
@RestController
@RequestMapping("/api")
public class NewsApiCategoryResource {

    private final Logger log = LoggerFactory.getLogger(NewsApiCategoryResource.class);

    private static final String ENTITY_NAME = "newsApiCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NewsApiCategoryRepository newsApiCategoryRepository;

    private final NewsApiCategorySearchRepository newsApiCategorySearchRepository;

    public NewsApiCategoryResource(NewsApiCategoryRepository newsApiCategoryRepository, NewsApiCategorySearchRepository newsApiCategorySearchRepository) {
        this.newsApiCategoryRepository = newsApiCategoryRepository;
        this.newsApiCategorySearchRepository = newsApiCategorySearchRepository;
    }

    /**
     * {@code POST  /news-api-categories} : Create a new newsApiCategory.
     *
     * @param newsApiCategory the newsApiCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new newsApiCategory, or with status {@code 400 (Bad Request)} if the newsApiCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/news-api-categories")
    public ResponseEntity<NewsApiCategory> createNewsApiCategory(@Valid @RequestBody NewsApiCategory newsApiCategory) throws URISyntaxException {
        log.debug("REST request to save NewsApiCategory : {}", newsApiCategory);
        if (newsApiCategory.getId() != null) {
            throw new BadRequestAlertException("A new newsApiCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NewsApiCategory result = newsApiCategoryRepository.save(newsApiCategory);
        newsApiCategorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/news-api-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /news-api-categories} : Updates an existing newsApiCategory.
     *
     * @param newsApiCategory the newsApiCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated newsApiCategory,
     * or with status {@code 400 (Bad Request)} if the newsApiCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the newsApiCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/news-api-categories")
    public ResponseEntity<NewsApiCategory> updateNewsApiCategory(@Valid @RequestBody NewsApiCategory newsApiCategory) throws URISyntaxException {
        log.debug("REST request to update NewsApiCategory : {}", newsApiCategory);
        if (newsApiCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NewsApiCategory result = newsApiCategoryRepository.save(newsApiCategory);
        newsApiCategorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, newsApiCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /news-api-categories} : get all the newsApiCategories.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of newsApiCategories in body.
     */
    @GetMapping("/news-api-categories")
    public List<NewsApiCategory> getAllNewsApiCategories() {
        log.debug("REST request to get all NewsApiCategories");
        return newsApiCategoryRepository.findAll();
    }

    /**
     * {@code GET  /news-api-categories/:id} : get the "id" newsApiCategory.
     *
     * @param id the id of the newsApiCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the newsApiCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/news-api-categories/{id}")
    public ResponseEntity<NewsApiCategory> getNewsApiCategory(@PathVariable Long id) {
        log.debug("REST request to get NewsApiCategory : {}", id);
        Optional<NewsApiCategory> newsApiCategory = newsApiCategoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(newsApiCategory);
    }

    /**
     * {@code DELETE  /news-api-categories/:id} : delete the "id" newsApiCategory.
     *
     * @param id the id of the newsApiCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/news-api-categories/{id}")
    public ResponseEntity<Void> deleteNewsApiCategory(@PathVariable Long id) {
        log.debug("REST request to delete NewsApiCategory : {}", id);
        newsApiCategoryRepository.deleteById(id);
        newsApiCategorySearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/news-api-categories?query=:query} : search for the newsApiCategory corresponding
     * to the query.
     *
     * @param query the query of the newsApiCategory search.
     * @return the result of the search.
     */
    @GetMapping("/_search/news-api-categories")
    public List<NewsApiCategory> searchNewsApiCategories(@RequestParam String query) {
        log.debug("REST request to search NewsApiCategories for query {}", query);
        return StreamSupport
            .stream(newsApiCategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
