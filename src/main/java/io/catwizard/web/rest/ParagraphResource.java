package io.catwizard.web.rest;

import io.catwizard.domain.Paragraph;
import io.catwizard.repository.ParagraphRepository;
import io.catwizard.repository.search.ParagraphSearchRepository;
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
 * REST controller for managing {@link io.catwizard.domain.Paragraph}.
 */
@RestController
@RequestMapping("/api")
public class ParagraphResource {

    private final Logger log = LoggerFactory.getLogger(ParagraphResource.class);

    private static final String ENTITY_NAME = "paragraph";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParagraphRepository paragraphRepository;

    private final ParagraphSearchRepository paragraphSearchRepository;

    public ParagraphResource(ParagraphRepository paragraphRepository, ParagraphSearchRepository paragraphSearchRepository) {
        this.paragraphRepository = paragraphRepository;
        this.paragraphSearchRepository = paragraphSearchRepository;
    }

    /**
     * {@code POST  /paragraphs} : Create a new paragraph.
     *
     * @param paragraph the paragraph to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paragraph, or with status {@code 400 (Bad Request)} if the paragraph has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paragraphs")
    public ResponseEntity<Paragraph> createParagraph(@RequestBody Paragraph paragraph) throws URISyntaxException {
        log.debug("REST request to save Paragraph : {}", paragraph);
        if (paragraph.getId() != null) {
            throw new BadRequestAlertException("A new paragraph cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Paragraph result = paragraphRepository.save(paragraph);
        paragraphSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/paragraphs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paragraphs} : Updates an existing paragraph.
     *
     * @param paragraph the paragraph to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paragraph,
     * or with status {@code 400 (Bad Request)} if the paragraph is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paragraph couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paragraphs")
    public ResponseEntity<Paragraph> updateParagraph(@RequestBody Paragraph paragraph) throws URISyntaxException {
        log.debug("REST request to update Paragraph : {}", paragraph);
        if (paragraph.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Paragraph result = paragraphRepository.save(paragraph);
        paragraphSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paragraph.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /paragraphs} : get all the paragraphs.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paragraphs in body.
     */
    @GetMapping("/paragraphs")
    public ResponseEntity<List<Paragraph>> getAllParagraphs(Pageable pageable) {
        log.debug("REST request to get a page of Paragraphs");
        Page<Paragraph> page = paragraphRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /paragraphs/:id} : get the "id" paragraph.
     *
     * @param id the id of the paragraph to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paragraph, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paragraphs/{id}")
    public ResponseEntity<Paragraph> getParagraph(@PathVariable Long id) {
        log.debug("REST request to get Paragraph : {}", id);
        Optional<Paragraph> paragraph = paragraphRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(paragraph);
    }

    /**
     * {@code DELETE  /paragraphs/:id} : delete the "id" paragraph.
     *
     * @param id the id of the paragraph to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paragraphs/{id}")
    public ResponseEntity<Void> deleteParagraph(@PathVariable Long id) {
        log.debug("REST request to delete Paragraph : {}", id);
        paragraphRepository.deleteById(id);
        paragraphSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/paragraphs?query=:query} : search for the paragraph corresponding
     * to the query.
     *
     * @param query the query of the paragraph search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/paragraphs")
    public ResponseEntity<List<Paragraph>> searchParagraphs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Paragraphs for query {}", query);
        Page<Paragraph> page = paragraphSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
