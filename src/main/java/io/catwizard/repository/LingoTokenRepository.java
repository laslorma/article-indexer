package io.catwizard.repository;

import io.catwizard.domain.LingoToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LingoToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LingoTokenRepository extends JpaRepository<LingoToken, Long> {

}
