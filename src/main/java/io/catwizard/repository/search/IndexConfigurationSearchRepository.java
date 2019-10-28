package io.catwizard.repository.search;
import io.catwizard.domain.IndexConfiguration;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link IndexConfiguration} entity.
 */
public interface IndexConfigurationSearchRepository extends ElasticsearchRepository<IndexConfiguration, Long> {
}
