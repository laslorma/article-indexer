package io.catwizard.repository;

import io.catwizard.domain.NlpServerConf;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the NlpServerConf entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NlpServerConfRepository extends JpaRepository<NlpServerConf, Long> {

}
