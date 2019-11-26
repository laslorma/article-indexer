package io.catwizard.service;

import com.sun.syndication.io.FeedException;
import io.catwizard.domain.Article;
import io.catwizard.domain.Country;
import io.catwizard.domain.IndexSession;
import io.catwizard.domain.NewsApiCategory;
import io.catwizard.repository.ArticleRepository;
import io.catwizard.repository.CountryRepository;
import io.catwizard.repository.NewsApiCategoryRepository;
import io.catwizard.repository.SourceRepository;
import io.catwizard.web.rest.CountryResource;
import io.catwizard.web.rest.errors.IndexServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class IndexService {

    private final Logger log = LoggerFactory.getLogger(IndexService.class);

    private ArticleService articleService;

    private IndexSessionService indexSessionService;

    private NewsApiCategoryRepository newsApiCategoryRepository;

    private NewsApiService newsApiService;

    private CountryRepository countryRepository;

    private ArticleRepository articleRepository;

    private FiveFilterHtmlRssParserService fiveFilterHtmlRssParserService;

    private SourceRepository sourceRepository;


    public IndexService(ArticleService articleService, IndexSessionService indexSessionService, NewsApiCategoryRepository newsApiCategoryRepository, NewsApiService newsApiService, CountryRepository countryRepository, ArticleRepository articleRepository, FiveFilterHtmlRssParserService fiveFilterHtmlRssParserService, SourceRepository sourceRepository) {
        this.articleService = articleService;
        this.indexSessionService = indexSessionService;
        this.newsApiCategoryRepository = newsApiCategoryRepository;
        this.newsApiService = newsApiService;
        this.countryRepository = countryRepository;
        this.articleRepository = articleRepository;
        this.fiveFilterHtmlRssParserService = fiveFilterHtmlRssParserService;
        this.sourceRepository = sourceRepository;
    }

    /**
     * initialDelay 1800000 ms = 30 minutes to allow tests to run
     * 3600000 one hour
     * every 8 hours == 3 times a day
     * @return
     */
    @Scheduled(fixedDelay = 8*3600000, initialDelay = 18000)
    //@Transactional(noRollbackFor = Exception.class)
    public IndexSession indexArticlesByCategoryTrending() throws IndexServiceException {

        int pageSize = 100;
        int page=0;

        long startTime = 0;
        long endTime = 0;

        IndexSession indexSession = new IndexSession();


        // Starts process
        log.debug("Starting process of Indexing NewsApi...");

        indexSession.setStarted(ZonedDateTime.now(ZoneId.systemDefault()));
        indexSession.setHadError(false);
        indexSession.setIndexing(true);
        indexSession.setNewsApiCalls(0l);
        indexSession.setFiveFilterApiCalls(0l);
        indexSessionService.save(indexSession);

        List<NewsApiCategory> categoryList = newsApiCategoryRepository.findByActiveTrue();

        List<Country> countryList  = countryRepository.findByActiveTrue();

        ArrayList<Article> articleArrayList = new ArrayList<>();

        startTime = System.currentTimeMillis();

        // Get references
        for (Country country : countryList) {

            for (NewsApiCategory category : categoryList) {

                log.debug("retrieving articles by category: " + category.getName() + ", country: " + country.getName());

                ArrayList<Article>  tmpList = null;

                try {
                    tmpList = newsApiService.searchSourcesByCategoryTrendingNewsApi(
                        // Params
                        category.getName(), country, pageSize, page, indexSession

                    );
                } catch (Exception e) {

                    handleIndexerError(indexSession, e);

                    throw  new IndexServiceException(e.getMessage());


                }

                assert tmpList != null;
                articleArrayList.addAll(tmpList);
            }


        }
//
//        indexSessionService.save(indexSession);

        endTime = System.currentTimeMillis();
        log.debug("Finishing pulling information, took: " + (endTime - startTime) + " milliseconds");

        log.debug("Total Articles : " + articleArrayList.size());

        // FILTER Revisa articulo por articulo, les agrega el contenido y elimna los que no se pueden parsear
        filterAndFillArticlesContent(articleArrayList, indexSession);

        endTime = System.currentTimeMillis();
        log.debug("Finishing Filtering articles, took: " + (endTime - startTime) + " milliseconds");
        log.debug("Total Articles after the filtering : " + articleArrayList.size());

        // Calculate Stats
        log.debug("Calculating Article Stats...");
        long statsStartTime = System.currentTimeMillis();

        log.debug("Calculating sentiment and readability for all {} articles",articleArrayList.size());
        int count = 1;
        for (Article article : articleArrayList){
            log.debug("Analyzing article {}", count);
            articleService.calculateReadability(article);
            count++;
        }


        endTime = System.currentTimeMillis();
        log.debug("Finishing Calculating Article Stats, took: " + (endTime - statsStartTime) + " milliseconds");


        // THIS IS WHEN THE ARTICLES ARE SAVED, ARTICLES TABLE UPDATED
        updateArticles(articleArrayList);

        log.debug("All article saved! Saving index session.");
        // wrap it up!
        indexSession.setEnded(ZonedDateTime.now(ZoneId.systemDefault()));
        endTime = System.currentTimeMillis();
        indexSession.setDuration(endTime - startTime);
        indexSession.setIndexing(false);
        indexSession.setTotalArticles((long) articleArrayList.size());
        indexSessionService.save(indexSession);


        log.info("Finished index process, exiting...");
        return indexSession;


    }

    /**
     * Fills articles content and removes unsuitable ones
     * @param allCategoriesList
     * @param indexSession
     * @throws IOException
     */
    public void filterAndFillArticlesContent(ArrayList<Article> allCategoriesList, IndexSession indexSession)  {

        ArrayList<Article> deletedArticles = new ArrayList<>();
        int counter=0;

        Iterator<Article> iterator = allCategoriesList.iterator();
        log.debug("Going through article list, filtering articles unable to parse");
        while (iterator.hasNext()) {

            counter++;

            Article article = iterator.next();

            try {

                // Feedback of progress on the console, give feedback every 50 articles
                if (counter % 50 == 0) {
                    log.debug(counter + " articles have been filtered");
                }
                String htmlUnclutteredContent = fiveFilterHtmlRssParserService.getArticleFullText(article.getUrl());


                // If there is a problem retreiving content, or if it's too short
                if (htmlUnclutteredContent.contains(ArticleService.UNABLE_TO_RETRIEVE_FULL_TEXT_CONTENT) ||
                    // Do not accept small articles
                    (htmlUnclutteredContent.length()<1500)){

                    removeAndReportArticle(deletedArticles, iterator, article);

                }else{

                    String rawContent = articleService.htmlToText(htmlUnclutteredContent);
                    article.setContent(rawContent);

                }


            } catch (FeedException | IOException | NullPointerException e ) {

                log.debug(e.getMessage());

                removeAndReportArticle(deletedArticles, iterator, article);

            }

            indexSession.setFiveFilterApiCalls(indexSession.getFiveFilterApiCalls() + 1);

        }

        log.debug("Deleted Articles that gave parsing errors: ");
        for (Article article : deletedArticles) {

            log.debug(article.getUrl()+"\n");

        }
        log.debug("==================================================================================================== ");

    }

    private void updateArticles(ArrayList<Article> articleList) {
        long startTime;
        long endTime;
        if (articleList.size()>0) {
            /// Deleting article table
//
//            log.debug("Deleting article table START");
//            articleRepository.truncateArticle();
//            log.debug("Deleting article table DONE");
//
//            log.debug("Deleting source table START");
//            sourceRepository.truncateSource();
//            log.debug("Deleting source table DONE");

            // Updating article table

            startTime = System.currentTimeMillis();
            log.debug("Saving articles table START");
            saveOneByOne(articleList);
            log.debug("Saving articles table END");
            endTime = System.currentTimeMillis();
            log.debug("Article table updated, took: " + (endTime - startTime) + " milliseconds");
        }else{
            log.debug("No articles found, leaving article table as it is");
        }
    }


    private void removeAndReportArticle(ArrayList<Article> deletedArticles, Iterator<Article> iterator, Article article) {
        log.debug("An article has failed the fivefilter service or failed to parse for some reason. Removing from list " + article.getUrl());

        deletedArticles.add(article);
        // Removes article from list
        iterator.remove();
    }

    private void handleIndexerError(IndexSession indexSession, Exception e) {
        indexSession.setEnded(ZonedDateTime.now(ZoneId.systemDefault()));
        indexSession.setHadError(true);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        indexSession.setErrorMessage(sStackTrace);
        log.error("Index Service Error", e);
    }

    public void saveOneByOne(ArrayList<Article> allCategoriesList) {

        int size = allCategoriesList.size();
        int index = 1;

        log.debug("Saving " + size + " articles");

        for (Article article : allCategoriesList) {
            try
            {

                articleService.save(article);
                log.debug("Saved article "+index+" of "+size);
                index++;

            }catch (Exception e){

                log.error("Error saving article ");
                log.error(e.getMessage());
                log.error("On article "+article.getUrl());

            }
        }
    }

}
