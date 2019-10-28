package io.catwizard.repository.search;
import io.catwizard.domain.IndexSession;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link IndexSession} entity.
 */
public interface IndexSessionSearchRepository extends ElasticsearchRepository<IndexSession, Long> {
}
