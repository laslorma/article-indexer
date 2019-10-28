package io.catwizard.web.rest;

import io.catwizard.domain.IndexSession;
import io.catwizard.service.IndexSessionService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.catwizard.domain.IndexSession}.
 */
@RestController
@RequestMapping("/api")
public class IndexSessionResource {

    private final Logger log = LoggerFactory.getLogger(IndexSessionResource.class);

    private static final String ENTITY_NAME = "indexSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndexSessionService indexSessionService;

    public IndexSessionResource(IndexSessionService indexSessionService) {
        this.indexSessionService = indexSessionService;
    }

    /**
     * {@code POST  /index-sessions} : Create a new indexSession.
     *
     * @param indexSession the indexSession to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new indexSession, or with status {@code 400 (Bad Request)} if the indexSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/index-sessions")
    public ResponseEntity<IndexSession> createIndexSession(@RequestBody IndexSession indexSession) throws URISyntaxException {
        log.debug("REST request to save IndexSession : {}", indexSession);
        if (indexSession.getId() != null) {
            throw new BadRequestAlertException("A new indexSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndexSession result = indexSessionService.save(indexSession);
        return ResponseEntity.created(new URI("/api/index-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /index-sessions} : Updates an existing indexSession.
     *
     * @param indexSession the indexSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated indexSession,
     * or with status {@code 400 (Bad Request)} if the indexSession is not valid,
     * or with status {@code 500 (Internal Server Error)} if the indexSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/index-sessions")
    public ResponseEntity<IndexSession> updateIndexSession(@RequestBody IndexSession indexSession) throws URISyntaxException {
        log.debug("REST request to update IndexSession : {}", indexSession);
        if (indexSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IndexSession result = indexSessionService.save(indexSession);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, indexSession.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /index-sessions} : get all the indexSessions.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of indexSessions in body.
     */
    @GetMapping("/index-sessions")
    public ResponseEntity<List<IndexSession>> getAllIndexSessions(Pageable pageable) {
        log.debug("REST request to get a page of IndexSessions");
        Page<IndexSession> page = indexSessionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /index-sessions/:id} : get the "id" indexSession.
     *
     * @param id the id of the indexSession to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the indexSession, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/index-sessions/{id}")
    public ResponseEntity<IndexSession> getIndexSession(@PathVariable Long id) {
        log.debug("REST request to get IndexSession : {}", id);
        Optional<IndexSession> indexSession = indexSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(indexSession);
    }

    /**
     * {@code DELETE  /index-sessions/:id} : delete the "id" indexSession.
     *
     * @param id the id of the indexSession to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/index-sessions/{id}")
    public ResponseEntity<Void> deleteIndexSession(@PathVariable Long id) {
        log.debug("REST request to delete IndexSession : {}", id);
        indexSessionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/index-sessions?query=:query} : search for the indexSession corresponding
     * to the query.
     *
     * @param query the query of the indexSession search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/index-sessions")
    public ResponseEntity<List<IndexSession>> searchIndexSessions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IndexSessions for query {}", query);
        Page<IndexSession> page = indexSessionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
