package io.catwizard.service;

import io.catwizard.IndexerApp;
import io.catwizard.domain.IndexSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = IndexerApp.class)
public class IndexServiceT {

    @Autowired
    IndexService indexService;

    @Autowired
    ArticleService articleService;

    @Test
    @Transactional
    public void indexArticlesByCategoryTest() throws IOException, Exception {

        IndexSession indexSession = indexService.indexArticlesByCategoryTrending();


        assertThat(indexSession.getId()).isGreaterThan(0);
        assertThat(indexSession.getNewsApiCalls()).isGreaterThan(0);
        assertThat(indexSession.getFiveFilterApiCalls()).isGreaterThan(0);
        assertThat(indexSession.getDuration()).isGreaterThan(0);
        assertThat(indexSession.getStarted()).isNotNull();
        assertThat(indexSession.isIndexing()).isFalse();
        assertThat(indexSession.getTotalArticles()).isGreaterThan(0);
        //        assertThat(indexSession.getArticlesSaved()).isGreaterThan(0);

        assertThat(articleService.findAll().size() > 0);

    }

}
