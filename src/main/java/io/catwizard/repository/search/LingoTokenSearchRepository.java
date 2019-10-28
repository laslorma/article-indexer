package io.catwizard.repository.search;

import io.catwizard.domain.LingoToken;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link LingoToken} entity.
 */
public interface LingoTokenSearchRepository extends ElasticsearchRepository<LingoToken, Long> {
}
