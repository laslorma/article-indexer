package io.catwizard.repository;

import io.catwizard.domain.IndexConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IndexConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexConfigurationRepository extends JpaRepository<IndexConfiguration, Long> {

}
