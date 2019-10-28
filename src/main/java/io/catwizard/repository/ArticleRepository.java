package io.catwizard.repository;
import io.catwizard.domain.Article;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Spring Data  repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    // El @Modifying es necesario para metodos que no devuelven nada y modifican data
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM article where 1=1", nativeQuery = true)
    void truncateArticle();

    List<Article> findAll();

}
