package io.catwizard.repository.search;

import io.catwizard.domain.Paragraph;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Paragraph} entity.
 */
public interface ParagraphSearchRepository extends ElasticsearchRepository<Paragraph, Long> {
}
