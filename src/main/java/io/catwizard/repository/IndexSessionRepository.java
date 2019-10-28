package io.catwizard.repository;
import io.catwizard.domain.IndexSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IndexSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexSessionRepository extends JpaRepository<IndexSession, Long> {

}
