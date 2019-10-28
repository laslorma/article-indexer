package io.catwizard.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link NlpServerConfSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class NlpServerConfSearchRepositoryMockConfiguration {

    @MockBean
    private NlpServerConfSearchRepository mockNlpServerConfSearchRepository;

}
