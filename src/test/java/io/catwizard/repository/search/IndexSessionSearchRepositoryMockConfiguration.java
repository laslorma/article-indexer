package io.catwizard.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link IndexSessionSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class IndexSessionSearchRepositoryMockConfiguration {

    @MockBean
    private IndexSessionSearchRepository mockIndexSessionSearchRepository;

}
