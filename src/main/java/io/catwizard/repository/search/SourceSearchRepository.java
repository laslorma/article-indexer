package io.catwizard.repository.search;
import io.catwizard.domain.Source;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Source} entity.
 */
public interface SourceSearchRepository extends ElasticsearchRepository<Source, Long> {
}
