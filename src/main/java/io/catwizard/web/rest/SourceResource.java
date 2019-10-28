package io.catwizard.web.rest;

import io.catwizard.domain.Source;
import io.catwizard.repository.SourceRepository;
import io.catwizard.repository.search.SourceSearchRepository;
import io.catwizard.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
 * REST controller for managing {@link io.catwizard.domain.Source}.
 */
@RestController
@RequestMapping("/api")
public class SourceResource {

    private final Logger log = LoggerFactory.getLogger(SourceResource.class);

    private static final String ENTITY_NAME = "source";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceRepository sourceRepository;

    private final SourceSearchRepository sourceSearchRepository;

    public SourceResource(SourceRepository sourceRepository, SourceSearchRepository sourceSearchRepository) {
        this.sourceRepository = sourceRepository;
        this.sourceSearchRepository = sourceSearchRepository;
    }

    /**
     * {@code POST  /sources} : Create a new source.
     *
     * @param source the source to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new source, or with status {@code 400 (Bad Request)} if the source has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sources")
    public ResponseEntity<Source> createSource(@RequestBody Source source) throws URISyntaxException {
        log.debug("REST request to save Source : {}", source);
        if (source.getId() != null) {
            throw new BadRequestAlertException("A new source cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Source result = sourceRepository.save(source);
        sourceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sources} : Updates an existing source.
     *
     * @param source the source to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated source,
     * or with status {@code 400 (Bad Request)} if the source is not valid,
     * or with status {@code 500 (Internal Server Error)} if the source couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sources")
    public ResponseEntity<Source> updateSource(@RequestBody Source source) throws URISyntaxException {
        log.debug("REST request to update Source : {}", source);
        if (source.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Source result = sourceRepository.save(source);
        sourceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, source.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sources} : get all the sources.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sources in body.
     */
    @GetMapping("/sources")
    public ResponseEntity<List<Source>> getAllSources(Pageable pageable) {
        log.debug("REST request to get a page of Sources");
        Page<Source> page = sourceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sources/:id} : get the "id" source.
     *
     * @param id the id of the source to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the source, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sources/{id}")
    public ResponseEntity<Source> getSource(@PathVariable Long id) {
        log.debug("REST request to get Source : {}", id);
        Optional<Source> source = sourceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(source);
    }

    /**
     * {@code DELETE  /sources/:id} : delete the "id" source.
     *
     * @param id the id of the source to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sources/{id}")
    public ResponseEntity<Void> deleteSource(@PathVariable Long id) {
        log.debug("REST request to delete Source : {}", id);
        sourceRepository.deleteById(id);
        sourceSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/sources?query=:query} : search for the source corresponding
     * to the query.
     *
     * @param query the query of the source search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/sources")
    public ResponseEntity<List<Source>> searchSources(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Sources for query {}", query);
        Page<Source> page = sourceSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
