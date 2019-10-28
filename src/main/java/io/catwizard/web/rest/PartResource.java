package io.catwizard.web.rest;

import io.catwizard.domain.Part;
import io.catwizard.repository.PartRepository;
import io.catwizard.repository.search.PartSearchRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.catwizard.domain.Part}.
 */
@RestController
@RequestMapping("/api")
public class PartResource {

    private final Logger log = LoggerFactory.getLogger(PartResource.class);

    private static final String ENTITY_NAME = "part";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartRepository partRepository;

    private final PartSearchRepository partSearchRepository;

    public PartResource(PartRepository partRepository, PartSearchRepository partSearchRepository) {
        this.partRepository = partRepository;
        this.partSearchRepository = partSearchRepository;
    }

    /**
     * {@code POST  /parts} : Create a new part.
     *
     * @param part the part to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new part, or with status {@code 400 (Bad Request)} if the part has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parts")
    public ResponseEntity<Part> createPart(@Valid @RequestBody Part part) throws URISyntaxException {
        log.debug("REST request to save Part : {}", part);
        if (part.getId() != null) {
            throw new BadRequestAlertException("A new part cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Part result = partRepository.save(part);
        partSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/parts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parts} : Updates an existing part.
     *
     * @param part the part to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated part,
     * or with status {@code 400 (Bad Request)} if the part is not valid,
     * or with status {@code 500 (Internal Server Error)} if the part couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parts")
    public ResponseEntity<Part> updatePart(@Valid @RequestBody Part part) throws URISyntaxException {
        log.debug("REST request to update Part : {}", part);
        if (part.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Part result = partRepository.save(part);
        partSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, part.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /parts} : get all the parts.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parts in body.
     */
    @GetMapping("/parts")
    public ResponseEntity<List<Part>> getAllParts(Pageable pageable) {
        log.debug("REST request to get a page of Parts");
        Page<Part> page = partRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parts/:id} : get the "id" part.
     *
     * @param id the id of the part to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the part, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parts/{id}")
    public ResponseEntity<Part> getPart(@PathVariable Long id) {
        log.debug("REST request to get Part : {}", id);
        Optional<Part> part = partRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(part);
    }

    /**
     * {@code DELETE  /parts/:id} : delete the "id" part.
     *
     * @param id the id of the part to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parts/{id}")
    public ResponseEntity<Void> deletePart(@PathVariable Long id) {
        log.debug("REST request to delete Part : {}", id);
        partRepository.deleteById(id);
        partSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/parts?query=:query} : search for the part corresponding
     * to the query.
     *
     * @param query the query of the part search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/parts")
    public ResponseEntity<List<Part>> searchParts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Parts for query {}", query);
        Page<Part> page = partSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
