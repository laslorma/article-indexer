package io.catwizard.repository;
import io.catwizard.domain.NewsApiCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the NewsApiCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsApiCategoryRepository extends JpaRepository<NewsApiCategory, Long> {

}
