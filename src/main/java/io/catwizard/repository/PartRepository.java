package io.catwizard.repository;
import io.catwizard.domain.Part;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Part entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

}
