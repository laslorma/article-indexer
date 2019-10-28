package io.catwizard.repository.search;

import io.catwizard.domain.Part;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Part} entity.
 */
public interface PartSearchRepository extends ElasticsearchRepository<Part, Long> {
}
