package io.catwizard.repository.search;
import io.catwizard.domain.NewsApiCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link NewsApiCategory} entity.
 */
public interface NewsApiCategorySearchRepository extends ElasticsearchRepository<NewsApiCategory, Long> {
}
