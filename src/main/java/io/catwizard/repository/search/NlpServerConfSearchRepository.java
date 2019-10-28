package io.catwizard.repository.search;

import io.catwizard.domain.NlpServerConf;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link NlpServerConf} entity.
 */
public interface NlpServerConfSearchRepository extends ElasticsearchRepository<NlpServerConf, Long> {
}
