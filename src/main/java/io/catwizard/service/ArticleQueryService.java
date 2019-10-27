package io.catwizard.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.catwizard.domain.Article;
import io.catwizard.domain.*; // for static metamodels
import io.catwizard.repository.ArticleRepository;
import io.catwizard.repository.search.ArticleSearchRepository;
import io.catwizard.service.dto.ArticleCriteria;

/**
 * Service for executing complex queries for {@link Article} entities in the database.
 * The main input is a {@link ArticleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Article} or a {@link Page} of {@link Article} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArticleQueryService extends QueryService<Article> {

    private final Logger log = LoggerFactory.getLogger(ArticleQueryService.class);

    private final ArticleRepository articleRepository;

    private final ArticleSearchRepository articleSearchRepository;

    public ArticleQueryService(ArticleRepository articleRepository, ArticleSearchRepository articleSearchRepository) {
        this.articleRepository = articleRepository;
        this.articleSearchRepository = articleSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Article} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Article> findByCriteria(ArticleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Article} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Article> findByCriteria(ArticleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArticleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.count(specification);
    }

    /**
     * Function to convert ConsumerCriteria to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */    
    protected Specification<Article> createSpecification(ArticleCriteria criteria) {
        Specification<Article> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Article_.id));
            }
            if (criteria.getAuthor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthor(), Article_.author));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Article_.title));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Article_.url));
            }
            if (criteria.getUrlToImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlToImage(), Article_.urlToImage));
            }
            if (criteria.getPublishedAt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPublishedAt(), Article_.publishedAt));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), Article_.category));
            }
            if (criteria.getCountryCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountryCode(), Article_.countryCode));
            }
            if (criteria.getLanguageCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLanguageCode(), Article_.languageCode));
            }
            if (criteria.getSentiment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSentiment(), Article_.sentiment));
            }
            if (criteria.getTextReadability() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTextReadability(), Article_.textReadability));
            }
            if (criteria.getNumberOfParts() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumberOfParts(), Article_.numberOfParts));
            }
            if (criteria.getNewsApiCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getNewsApiCategoryId(),
                    root -> root.join(Article_.newsApiCategory, JoinType.LEFT).get(NewsApiCategory_.id)));
            }
            if (criteria.getSourceId() != null) {
                specification = specification.and(buildSpecification(criteria.getSourceId(),
                    root -> root.join(Article_.source, JoinType.LEFT).get(Source_.id)));
            }
            if (criteria.getParagraphId() != null) {
                specification = specification.and(buildSpecification(criteria.getParagraphId(),
                    root -> root.join(Article_.paragraphs, JoinType.LEFT).get(Paragraph_.id)));
            }
            if (criteria.getPartId() != null) {
                specification = specification.and(buildSpecification(criteria.getPartId(),
                    root -> root.join(Article_.parts, JoinType.LEFT).get(Part_.id)));
            }
        }
        return specification;
    }
}
