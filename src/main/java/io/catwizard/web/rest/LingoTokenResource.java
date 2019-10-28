package io.catwizard.web.rest;

import io.catwizard.domain.LingoToken;
import io.catwizard.repository.LingoTokenRepository;
import io.catwizard.repository.search.LingoTokenSearchRepository;
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
 * REST controller for managing {@link io.catwizard.domain.LingoToken}.
 */
@RestController
@RequestMapping("/api")
public class LingoTokenResource {

    private final Logger log = LoggerFactory.getLogger(LingoTokenResource.class);

    private static final String ENTITY_NAME = "lingoToken";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LingoTokenRepository lingoTokenRepository;

    private final LingoTokenSearchRepository lingoTokenSearchRepository;

    public LingoTokenResource(LingoTokenRepository lingoTokenRepository, LingoTokenSearchRepository lingoTokenSearchRepository) {
        this.lingoTokenRepository = lingoTokenRepository;
        this.lingoTokenSearchRepository = lingoTokenSearchRepository;
    }

    /**
     * {@code POST  /lingo-tokens} : Create a new lingoToken.
     *
     * @param lingoToken the lingoToken to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lingoToken, or with status {@code 400 (Bad Request)} if the lingoToken has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lingo-tokens")
    public ResponseEntity<LingoToken> createLingoToken(@RequestBody LingoToken lingoToken) throws URISyntaxException {
        log.debug("REST request to save LingoToken : {}", lingoToken);
        if (lingoToken.getId() != null) {
            throw new BadRequestAlertException("A new lingoToken cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LingoToken result = lingoTokenRepository.save(lingoToken);
        lingoTokenSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/lingo-tokens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lingo-tokens} : Updates an existing lingoToken.
     *
     * @param lingoToken the lingoToken to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lingoToken,
     * or with status {@code 400 (Bad Request)} if the lingoToken is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lingoToken couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lingo-tokens")
    public ResponseEntity<LingoToken> updateLingoToken(@RequestBody LingoToken lingoToken) throws URISyntaxException {
        log.debug("REST request to update LingoToken : {}", lingoToken);
        if (lingoToken.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LingoToken result = lingoTokenRepository.save(lingoToken);
        lingoTokenSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lingoToken.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /lingo-tokens} : get all the lingoTokens.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lingoTokens in body.
     */
    @GetMapping("/lingo-tokens")
    public List<LingoToken> getAllLingoTokens() {
        log.debug("REST request to get all LingoTokens");
        return lingoTokenRepository.findAll();
    }

    /**
     * {@code GET  /lingo-tokens/:id} : get the "id" lingoToken.
     *
     * @param id the id of the lingoToken to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lingoToken, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lingo-tokens/{id}")
    public ResponseEntity<LingoToken> getLingoToken(@PathVariable Long id) {
        log.debug("REST request to get LingoToken : {}", id);
        Optional<LingoToken> lingoToken = lingoTokenRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lingoToken);
    }

    /**
     * {@code DELETE  /lingo-tokens/:id} : delete the "id" lingoToken.
     *
     * @param id the id of the lingoToken to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lingo-tokens/{id}")
    public ResponseEntity<Void> deleteLingoToken(@PathVariable Long id) {
        log.debug("REST request to delete LingoToken : {}", id);
        lingoTokenRepository.deleteById(id);
        lingoTokenSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/lingo-tokens?query=:query} : search for the lingoToken corresponding
     * to the query.
     *
     * @param query the query of the lingoToken search.
     * @return the result of the search.
     */
    @GetMapping("/_search/lingo-tokens")
    public List<LingoToken> searchLingoTokens(@RequestParam String query) {
        log.debug("REST request to search LingoTokens for query {}", query);
        return StreamSupport
            .stream(lingoTokenSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
