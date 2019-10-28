package io.catwizard.web.rest;

import io.catwizard.domain.NlpServerConf;
import io.catwizard.repository.NlpServerConfRepository;
import io.catwizard.repository.search.NlpServerConfSearchRepository;
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
 * REST controller for managing {@link io.catwizard.domain.NlpServerConf}.
 */
@RestController
@RequestMapping("/api")
public class NlpServerConfResource {

    private final Logger log = LoggerFactory.getLogger(NlpServerConfResource.class);

    private static final String ENTITY_NAME = "nlpServerConf";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NlpServerConfRepository nlpServerConfRepository;

    private final NlpServerConfSearchRepository nlpServerConfSearchRepository;

    public NlpServerConfResource(NlpServerConfRepository nlpServerConfRepository, NlpServerConfSearchRepository nlpServerConfSearchRepository) {
        this.nlpServerConfRepository = nlpServerConfRepository;
        this.nlpServerConfSearchRepository = nlpServerConfSearchRepository;
    }

    /**
     * {@code POST  /nlp-server-confs} : Create a new nlpServerConf.
     *
     * @param nlpServerConf the nlpServerConf to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nlpServerConf, or with status {@code 400 (Bad Request)} if the nlpServerConf has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nlp-server-confs")
    public ResponseEntity<NlpServerConf> createNlpServerConf(@RequestBody NlpServerConf nlpServerConf) throws URISyntaxException {
        log.debug("REST request to save NlpServerConf : {}", nlpServerConf);
        if (nlpServerConf.getId() != null) {
            throw new BadRequestAlertException("A new nlpServerConf cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NlpServerConf result = nlpServerConfRepository.save(nlpServerConf);
        nlpServerConfSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/nlp-server-confs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nlp-server-confs} : Updates an existing nlpServerConf.
     *
     * @param nlpServerConf the nlpServerConf to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nlpServerConf,
     * or with status {@code 400 (Bad Request)} if the nlpServerConf is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nlpServerConf couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nlp-server-confs")
    public ResponseEntity<NlpServerConf> updateNlpServerConf(@RequestBody NlpServerConf nlpServerConf) throws URISyntaxException {
        log.debug("REST request to update NlpServerConf : {}", nlpServerConf);
        if (nlpServerConf.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NlpServerConf result = nlpServerConfRepository.save(nlpServerConf);
        nlpServerConfSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nlpServerConf.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /nlp-server-confs} : get all the nlpServerConfs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nlpServerConfs in body.
     */
    @GetMapping("/nlp-server-confs")
    public List<NlpServerConf> getAllNlpServerConfs() {
        log.debug("REST request to get all NlpServerConfs");
        return nlpServerConfRepository.findAll();
    }

    /**
     * {@code GET  /nlp-server-confs/:id} : get the "id" nlpServerConf.
     *
     * @param id the id of the nlpServerConf to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nlpServerConf, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nlp-server-confs/{id}")
    public ResponseEntity<NlpServerConf> getNlpServerConf(@PathVariable Long id) {
        log.debug("REST request to get NlpServerConf : {}", id);
        Optional<NlpServerConf> nlpServerConf = nlpServerConfRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(nlpServerConf);
    }

    /**
     * {@code DELETE  /nlp-server-confs/:id} : delete the "id" nlpServerConf.
     *
     * @param id the id of the nlpServerConf to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nlp-server-confs/{id}")
    public ResponseEntity<Void> deleteNlpServerConf(@PathVariable Long id) {
        log.debug("REST request to delete NlpServerConf : {}", id);
        nlpServerConfRepository.deleteById(id);
        nlpServerConfSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/nlp-server-confs?query=:query} : search for the nlpServerConf corresponding
     * to the query.
     *
     * @param query the query of the nlpServerConf search.
     * @return the result of the search.
     */
    @GetMapping("/_search/nlp-server-confs")
    public List<NlpServerConf> searchNlpServerConfs(@RequestParam String query) {
        log.debug("REST request to search NlpServerConfs for query {}", query);
        return StreamSupport
            .stream(nlpServerConfSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
