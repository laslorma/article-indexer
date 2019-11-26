package io.catwizard.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.catwizard.domain.*;
import io.catwizard.repository.IndexConfigurationRepository;
import javassist.NotFoundException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class NewsApiService {

    // ernesto.butto@gmail.com

    //private String NEWS_API = "238b26fce35a4b9a847172329c25e0dd";

    // poolebu@gmail.com
    // private String NEWS_API = "9499d0cd82d64052837be9240bbe0125";

    IndexConfigurationRepository indexConfigurationRepository;

    private final Logger log = LoggerFactory.getLogger(NewsApiService.class);

    public NewsApiService(IndexConfigurationRepository indexConfigurationRepository) {

        this.indexConfigurationRepository = indexConfigurationRepository;

    }

    private Map<String, Object> sendGetRequest(String url) {



        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        Map<String, Object> jsonResponse = null;

        ObjectMapper mapper = new ObjectMapper();


        try {

            HttpGet request = new HttpGet(url);

            request.addHeader("content-type", "application/json");

            HttpResponse result = httpClient.execute(request);

            String resultString = EntityUtils.toString(result.getEntity(), "UTF-8");

            jsonResponse = mapper.readValue(resultString,Map.class);

            return jsonResponse;

        } catch (IOException ex) {

            System.out.print(ex.getMessage());

        }

        return jsonResponse;
    }




    public ArrayList<Source> searchSourcesByCategoryNewsApi(String categoryName) throws IOException {
        //https://newsapi.org/v1/sources?language=en&category=general

        Optional<IndexConfiguration> indexConfiguration = indexConfigurationRepository.findById(1l);
        String url  = "http://newsapi.org/v1/sources?language=en&category="+categoryName+"&sortBy=latest&apiKey="+ indexConfiguration.get().getNewsApiKey();

        Map<String, Object> stringObjectMap = sendGetRequest(url);

        ArrayList<LinkedHashMap> arrayList = (ArrayList) stringObjectMap.get("sources");
        ArrayList<Source> sourceArrayList = new ArrayList<>();

        for (LinkedHashMap linkedHashMap : arrayList){

            ObjectMapper objectMapper = new ObjectMapper();

            String jsonString = new JSONObject(linkedHashMap).toString();
            Source source = objectMapper.readValue(jsonString,Source.class);
            sourceArrayList.add(source);

        }

        return sourceArrayList;

    }


    public ArrayList<Article> searchSourcesByCategoryTrendingNewsApi(String categoryName, Country country, int pageSize, int page, IndexSession indexSession) throws NotFoundException {

        //https://newsapi.org/v1/sources?language=en&category=general
        Optional<IndexConfiguration> indexConfiguration = indexConfigurationRepository.findById(1l);
        String url  = "https://newsapi.org/v2/top-headlines?category="+categoryName+"&country="+country.getCode()
            +"&pageSize="+pageSize+"&page="+page+"&apiKey="+indexConfiguration.get().getNewsApiKey();

        ArrayList<LinkedHashMap> arrayList = new ArrayList<>();
        ArrayList<Article> articleArrayList = new ArrayList<>();

        Map<String, Object> stringObjectMap = null;


        stringObjectMap = sendGetRequest(url);


        if (stringObjectMap!=null) {

            if (stringObjectMap.containsKey("status")&&(stringObjectMap.get("status").toString().equalsIgnoreCase("error"))) {
                throw new NotFoundException(stringObjectMap.toString());
            } if (stringObjectMap.get("articles")==null){
                throw new NotFoundException("ERROR WITH: https://newsapi.org ");
            }

            arrayList = (ArrayList<LinkedHashMap>) stringObjectMap.get("articles");
        }else {
            return articleArrayList;
        }


        log.info("Total Articles received: " + arrayList.size());
        int count = 1;
        for (LinkedHashMap linkedHashMap : arrayList){

            log.info("Parsing article " + count +" of " + arrayList.size());

            try {
                ObjectMapper objectMapper = new ObjectMapper();

                String jsonString = new JSONObject(linkedHashMap).toString();
                Article article = null;

                article = objectMapper.readValue(jsonString, Article.class);
                article.setCategory(categoryName);
                article.setCountryCode(country.getCode());
                article.setLanguageCode(country.getLanguage());
                articleArrayList.add(article);
            } catch (Exception e){
                log.error("Error parsing article " + e.getMessage());
            }

            count++;
        }

        indexSession.setNewsApiCalls(indexSession.getNewsApiCalls()+1);

        return articleArrayList;
    }


    public ArrayList<Article> searchArticlesBySourceNewsAPI(Source source) throws Exception {

        // https://newsapi.org/v1/articles?source=abc-news-au&sortBy=top&apiKey=9499d0cd82d64052837be9240bbe0125
        Optional<IndexConfiguration> indexConfiguration = indexConfigurationRepository.findById(1l);
        String url  = "http://newsapi.org/v1/articles?source="+source.getSourceId()+"&apiKey="+indexConfiguration.get().getNewsApiKey();
        ArrayList<LinkedHashMap> arrayList = new ArrayList<>();
        ArrayList<Article> articleArrayList = new ArrayList<>();

        Map<String, Object> stringObjectMap = null;



        stringObjectMap = sendGetRequest(url);
        if (stringObjectMap!=null) {

            if (stringObjectMap.containsKey("status")&&(stringObjectMap.get("status").toString().equalsIgnoreCase("error"))) {
                throw new Exception(stringObjectMap.toString());
            } if (stringObjectMap.get("articles")==null){
                throw new Exception("ERROR WITH: https://newsapi.org ");
            }

            arrayList = (ArrayList<LinkedHashMap>) stringObjectMap.get("articles");
        }else {
            return articleArrayList;
        }



        for (LinkedHashMap linkedHashMap : arrayList){

            ObjectMapper objectMapper = new ObjectMapper();

            String jsonString = new JSONObject(linkedHashMap).toString();
            Article article = null;

            article = objectMapper.readValue(jsonString,Article.class);
            article.setCategory(source.getCategory());
            article.setSource(source);

            articleArrayList.add(article);

        }

        return articleArrayList;
    }
}
