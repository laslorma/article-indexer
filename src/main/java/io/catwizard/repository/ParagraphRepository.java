package io.catwizard.repository;
import io.catwizard.domain.Paragraph;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Paragraph entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

}
