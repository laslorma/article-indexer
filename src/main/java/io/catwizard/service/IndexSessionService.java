package io.catwizard.service;

import io.catwizard.domain.IndexSession;
import io.catwizard.repository.IndexSessionRepository;
import io.catwizard.repository.search.IndexSessionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link IndexSession}.
 */
@Service
@Transactional
public class IndexSessionService {

    private final Logger log = LoggerFactory.getLogger(IndexSessionService.class);

    private final IndexSessionRepository indexSessionRepository;

    private final IndexSessionSearchRepository indexSessionSearchRepository;

    public IndexSessionService(IndexSessionRepository indexSessionRepository, IndexSessionSearchRepository indexSessionSearchRepository) {
        this.indexSessionRepository = indexSessionRepository;
        this.indexSessionSearchRepository = indexSessionSearchRepository;
    }

    /**
     * Save a indexSession.
     *
     * @param indexSession the entity to save.
     * @return the persisted entity.
     */
    public IndexSession save(IndexSession indexSession) {
        log.debug("Request to save IndexSession : {}", indexSession);
        IndexSession result = indexSessionRepository.save(indexSession);
        indexSessionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the indexSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexSession> findAll(Pageable pageable) {
        log.debug("Request to get all IndexSessions");
        return indexSessionRepository.findAll(pageable);
    }


    /**
     * Get one indexSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndexSession> findOne(Long id) {
        log.debug("Request to get IndexSession : {}", id);
        return indexSessionRepository.findById(id);
    }

    /**
     * Delete the indexSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete IndexSession : {}", id);
        indexSessionRepository.deleteById(id);
        indexSessionSearchRepository.deleteById(id);
    }

    /**
     * Search for the indexSession corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<IndexSession> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IndexSessions for query {}", query);
        return indexSessionSearchRepository.search(queryStringQuery(query), pageable);    }
}
