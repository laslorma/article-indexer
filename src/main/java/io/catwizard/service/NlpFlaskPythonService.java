package io.catwizard.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.catwizard.domain.LexicalEntry;
import io.catwizard.domain.LingoToken;
import io.catwizard.domain.NlpServerConf;
import io.catwizard.domain.PythonRequest;
import io.catwizard.repository.NlpServerConfRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class NlpFlaskPythonService {

    private final NlpServerConfRepository nlpServerConfRepository;


    private static final String WIKIPEDIA_URL = "/wikipedia" ;
    private static final String LEMMA = "/lemma";
    private static final String DETECT = "/detect";
    private static final String TRANSLATE = "/translate";
    private static final String WORDNET = "/wordnet";
    private static final String TOKENS = "/tokens";
    private static final String SPEAK = "/ai/speak";
    private static final String SENTIMENT = "/ai/sentiment";
    private static final String TEXT_DIFFICULTY = "/ai/readability-flesch";
    private static final String SENTENCE = "/sentence";
    public static final String DEFAULT_READING_SPEED = "slow";


    private final Logger log = LoggerFactory.getLogger(NlpFlaskPythonService.class);

    public NlpFlaskPythonService(NlpServerConfRepository nlpServerConfRepository) {
        this.nlpServerConfRepository = nlpServerConfRepository;
        // Initialize HOST variable

    }

    private String loadHost(){

        Optional<NlpServerConf> nlpServerConf = nlpServerConfRepository.findById(1l);
        return nlpServerConf.get().getUrl() + ":" + nlpServerConf.get().getPort();
    }

    public void verifyConnection() {

        String result = getRequest(loadHost());

        int a = 1;

    }

    private String getRequest(String urlStringRequest) {
        try {
            URL url = new URL(urlStringRequest);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            //    urlConnection.setRequestProperty("app_id",app_id);
            //   urlConnection.setRequestProperty("app_key",app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        }
        catch (FileNotFoundException e) {
            // Not found in oxford dictionary
            // e.printStackTrace();
            log.error(e.toString());
            return null;
        }
        catch (ConnectException e) {

            log.error(e.toString());
            return null;

        }catch (Exception e){

            log.error(e.toString());
            return null;

        }

    }

    public String getLemma(String word, String langcode) {

        // Prepare request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create Request Object
        PythonRequest pythonRequest = new PythonRequest(word.toLowerCase(),langcode);
        HttpEntity<PythonRequest> entity = new HttpEntity<PythonRequest>(pythonRequest, headers);

        return restTemplate.postForObject(loadHost() + LEMMA, entity, String.class);
    }

    public String detectLang(String sent) {

        // Prepare request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create Request Object
        PythonRequest pythonRequest = new PythonRequest(sent,null);
        HttpEntity<PythonRequest> entity = new HttpEntity<PythonRequest>(pythonRequest, headers);
        try{


            return restTemplate.postForObject(loadHost() + DETECT, entity, String.class);

        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    public PythonRequest wikipedia(String word, String lang_in, String lang_out) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create Request Object
        PythonRequest pythonRequest = new PythonRequest(word,lang_in,lang_out);

        HttpEntity<PythonRequest> entity = new HttpEntity<PythonRequest>(pythonRequest, headers);

        PythonRequest result = restTemplate.postForObject(loadHost() + WIKIPEDIA_URL, entity, PythonRequest.class);

        return result;

    }

    public List<LexicalEntry> wordNet(String word, String lang_in, String lang_out, String posTag, Integer wordnetOption) {
        // Prepare request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        // Create Request Object
        PythonRequest pythonRequest = new PythonRequest(word,lang_in,lang_out);

        if (posTag!=null){
            pythonRequest.setPosTag(posTag);
        }

        pythonRequest.setWordNetMax(wordnetOption);

        HttpEntity<PythonRequest> entity = new HttpEntity<PythonRequest>(pythonRequest, headers);

        ArrayList<LexicalEntry> lexicalEntryArrayList = restTemplate.postForObject(loadHost() + WORDNET, entity, ArrayList.class);

        ObjectMapper mapper = new ObjectMapper();

        ArrayList<LexicalEntry> result = mapper.convertValue(lexicalEntryArrayList, new TypeReference<ArrayList<LexicalEntry>>() { });

        return result;

    }

    public String translate(String text, String destLangCode) {

        // Prepare request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create Request Object
        PythonRequest pythonRequest = new PythonRequest(text,destLangCode);
        HttpEntity<PythonRequest> entity = new HttpEntity<PythonRequest>(pythonRequest, headers);

        return restTemplate.postForObject(loadHost() + TRANSLATE, entity, String.class);
    }

    public ArrayList<LingoToken> tokenizer(String sent, String langCode) {

        // Prepare request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create Request Object
        PythonRequest pythonRequest = new PythonRequest(sent,langCode);
        HttpEntity<PythonRequest> entity = new HttpEntity<PythonRequest>(pythonRequest, headers);

        ArrayList<LinkedHashMap> lingoTokenList = restTemplate.postForObject(loadHost() + TOKENS, entity, ArrayList.class);

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<LingoToken> result = mapper.convertValue(lingoTokenList, new TypeReference<ArrayList<LingoToken>>() { });

        return result;
    }

    public List<String> sentence(String text, String langCode) {

        // Prepare request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create Request Object
        PythonRequest pythonRequest = new PythonRequest(text,langCode);
        HttpEntity<PythonRequest> entity = new HttpEntity<PythonRequest>(pythonRequest, headers);
        // todo add a catch
        PythonRequest result = restTemplate.postForObject(loadHost() + SENTENCE, entity, PythonRequest.class);

        return result.getStringList();
    }


    public String speak(String text,String langCode, String readingSpeed) {
        // Prepare request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create Request Object

        // Defined in the python server:
        // Lang Code Valid Values: de-DE | en-US | es-US | fr-FR | it-IT |
        // Voice Eng: Joanna
        // Voice Es: Penélope
        // Voice it: Bianca
        // Voice German: Hans
        // Voice French: Mathieu

        PythonRequest pythonRequest = new PythonRequest(text,langCode);
        pythonRequest.setReadingSpeed(readingSpeed);

        HttpEntity<PythonRequest> entity = new HttpEntity<PythonRequest>(pythonRequest, headers);

        return restTemplate.postForObject(loadHost() + SPEAK, entity, String.class);
    }

    public PythonRequest calculateSentimentAnalysis(String text, String langCode) {
        // Prepare request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // If no lang code provided attempt to detect
        if (langCode==null){
            langCode = this.detectLang(text);
        }

        // If detectation was not posible, then do not attempt readability
        if (langCode==null){
            return null;
        }

        PythonRequest pythonRequest = new PythonRequest(text,langCode);
        HttpEntity<PythonRequest> entity = new HttpEntity<PythonRequest>(pythonRequest, headers);

        return restTemplate.postForObject(loadHost() + SENTIMENT, entity, PythonRequest.class);
    }

    public PythonRequest calculateReadingDifficulty(String text, String langCode) {
        // Prepare request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // If no lang code provided attempt to detect
        if (langCode==null){
            langCode = this.detectLang(text);
        }

        // If detectation was not posible, then do not attempt readability
        if (langCode==null){
            return null;
        }

        PythonRequest pythonRequest = new PythonRequest(text,langCode);
        HttpEntity<PythonRequest> entity = new HttpEntity<PythonRequest>(pythonRequest, headers);


        try{

            pythonRequest =  restTemplate.postForObject(loadHost() + TEXT_DIFFICULTY, entity, PythonRequest.class);

        }catch (Exception e){

            log.error("Error calculating readability for lang",langCode," Text: ", text);

            return null;

        }

        return pythonRequest;
    }
}
