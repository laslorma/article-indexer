package io.catwizard.repository;
import io.catwizard.domain.Source;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Spring Data  repository for the Source entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM source where 1=1", nativeQuery = true)
    void truncateSource();

}
