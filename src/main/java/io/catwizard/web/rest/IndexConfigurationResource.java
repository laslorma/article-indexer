package io.catwizard.web.rest;

import io.catwizard.domain.IndexConfiguration;
import io.catwizard.repository.IndexConfigurationRepository;
import io.catwizard.repository.search.IndexConfigurationSearchRepository;
import io.catwizard.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.catwizard.domain.IndexConfiguration}.
 */
@RestController
@RequestMapping("/api")
public class IndexConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(IndexConfigurationResource.class);

    private static final String ENTITY_NAME = "indexConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexConfigurationRepository indexConfigurationRepository;

    private final IndexConfigurationSearchRepository indexConfigurationSearchRepository;

    public IndexConfigurationResource(IndexConfigurationRepository indexConfigurationRepository, IndexConfigurationSearchRepository indexConfigurationSearchRepository) {
        this.indexConfigurationRepository = indexConfigurationRepository;
        this.indexConfigurationSearchRepository = indexConfigurationSearchRepository;
    }

    /**
     * {@code POST  /index-configurations} : Create a new indexConfiguration.
     *
     * @param indexConfiguration the indexConfiguration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexConfiguration, or with status {@code 400 (Bad Request)} if the indexConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/index-configurations")
    public ResponseEntity<IndexConfiguration> createIndexConfiguration(@RequestBody IndexConfiguration indexConfiguration) throws URISyntaxException {
        log.debug("REST request to save IndexConfiguration : {}", indexConfiguration);
        if (indexConfiguration.getId() != null) {
            throw new BadRequestAlertException("A new indexConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndexConfiguration result = indexConfigurationRepository.save(indexConfiguration);
        indexConfigurationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/index-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /index-configurations} : Updates an existing indexConfiguration.
     *
     * @param indexConfiguration the indexConfiguration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexConfiguration,
     * or with status {@code 400 (Bad Request)} if the indexConfiguration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexConfiguration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/index-configurations")
    public ResponseEntity<IndexConfiguration> updateIndexConfiguration(@RequestBody IndexConfiguration indexConfiguration) throws URISyntaxException {
        log.debug("REST request to update IndexConfiguration : {}", indexConfiguration);
        if (indexConfiguration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IndexConfiguration result = indexConfigurationRepository.save(indexConfiguration);
        indexConfigurationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indexConfiguration.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /index-configurations} : get all the indexConfigurations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexConfigurations in body.
     */
    @GetMapping("/index-configurations")
    public List<IndexConfiguration> getAllIndexConfigurations() {
        log.debug("REST request to get all IndexConfigurations");
        return indexConfigurationRepository.findAll();
    }

    /**
     * {@code GET  /index-configurations/:id} : get the "id" indexConfiguration.
     *
     * @param id the id of the indexConfiguration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexConfiguration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/index-configurations/{id}")
    public ResponseEntity<IndexConfiguration> getIndexConfiguration(@PathVariable Long id) {
        log.debug("REST request to get IndexConfiguration : {}", id);
        Optional<IndexConfiguration> indexConfiguration = indexConfigurationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(indexConfiguration);
    }

    /**
     * {@code DELETE  /index-configurations/:id} : delete the "id" indexConfiguration.
     *
     * @param id the id of the indexConfiguration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/index-configurations/{id}")
    public ResponseEntity<Void> deleteIndexConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete IndexConfiguration : {}", id);
        indexConfigurationRepository.deleteById(id);
        indexConfigurationSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/index-configurations?query=:query} : search for the indexConfiguration corresponding
     * to the query.
     *
     * @param query the query of the indexConfiguration search.
     * @return the result of the search.
     */
    @GetMapping("/_search/index-configurations")
    public List<IndexConfiguration> searchIndexConfigurations(@RequestParam String query) {
        log.debug("REST request to search IndexConfigurations for query {}", query);
        return StreamSupport
            .stream(indexConfigurationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
