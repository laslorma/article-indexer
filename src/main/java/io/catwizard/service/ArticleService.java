package io.catwizard.service;

import io.catwizard.domain.Article;
import io.catwizard.repository.ArticleRepository;
import io.catwizard.repository.search.ArticleSearchRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Article}.
 */
@Service
@Transactional
public class ArticleService {

    private final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    private final ArticleSearchRepository articleSearchRepository;

    private final NlpFlaskPythonService nlpFlaskPythonService;

    public static final String UNABLE_TO_RETRIEVE_FULL_TEXT_CONTENT = "[unable to retrieve full-text content]";


    public ArticleService(ArticleRepository articleRepository, ArticleSearchRepository articleSearchRepository, NlpFlaskPythonService nlpFlaskPythonService) {
        this.articleRepository = articleRepository;
        this.articleSearchRepository = articleSearchRepository;
        this.nlpFlaskPythonService = nlpFlaskPythonService;
    }

    /**
     * Save a article.
     *
     * @param article the entity to save.
     * @return the persisted entity.
     */
    public Article save(Article article) {
        log.debug("Request to save Article : {}", article);
        Article result = articleRepository.save(article);
        articleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Article> findAll(Pageable pageable) {
        log.debug("Request to get all Articles");
        return articleRepository.findAll(pageable);
    }


    /**
     * Get one article by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Article> findOne(Long id) {
        log.debug("Request to get Article : {}", id);
        return articleRepository.findById(id);
    }

    /**
     * Delete the article by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Article : {}", id);
        articleRepository.deleteById(id);
        articleSearchRepository.deleteById(id);
    }

    /**
     * Search for the article corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Article> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Articles for query {}", query);
        return articleSearchRepository.search(queryStringQuery(query), pageable);    }

// ERNESTO

    @Transactional(readOnly = true)
    public List<Article> findAll() {
        log.debug("Request to get all Articles");
        return articleRepository.findAll();
    }


    public String parseTextOnly(String html) {

        Document doc = Jsoup.parse(html);

        return doc.body().text();
    }


    public void calculateReadability(Article article) {

        if (article.getContent()!= null) {

            String content = this.parseTextOnly(article.getContent());

            String readability = nlpFlaskPythonService.calculateReadingDifficulty(content, article.getLanguageCode()).getReadability();

            article.setTextReadability(readability);

        }
    }
}
