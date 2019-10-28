package io.catwizard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PythonRequest {

    private String langCodeIn;
    private String text;
    private String langCode;
    private List<String> stringList;
    private List<LingoToken> tokenList;
    private Sentiment sentiment;
    private String readability;
    private String readingSpeed; // Amazon Polly Preset speeds: x-slow, slow, medium, fast, and x-fast.
    private String posTag;
    private String wikipediaSummary;
    private String wikipediaImage;
    private Integer wordNetMax;


    public PythonRequest() {
    }

    public PythonRequest(String text, String langCodeOut) {
        this.text = text;
        this.langCode = langCodeOut;
    }

    public PythonRequest(String text) {
        this.text = text;
    }

    public PythonRequest(String word, String lang_in, String lang_out) {
        this.text = word;
        this.langCodeIn = lang_in;
        this.langCode = lang_out;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLangCodeIn() {
        return langCodeIn;
    }

    public void setLangCodeIn(String langCodeIn) {
        this.langCodeIn = langCodeIn;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    public String getReadability() {
        return readability;
    }

    public void setReadability(String readability) {
        this.readability = readability;
    }

    public String getReadingSpeed() {
        return readingSpeed;
    }

    public void setReadingSpeed(String readingSpeed) {
        this.readingSpeed = readingSpeed;
    }

    public String getPosTag() {
        return posTag;
    }

    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

    public List<LingoToken> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<LingoToken> tokenList) {
        this.tokenList = tokenList;
    }

    public String getWikipediaSummary() {
        return wikipediaSummary;
    }

    public void setWikipediaSummary(String wikipediaSummary) {
        this.wikipediaSummary = wikipediaSummary;
    }

    public String getWikipediaImage() {
        return wikipediaImage;
    }

    public void setWikipediaImage(String wikipediaImage) {
        this.wikipediaImage = wikipediaImage;
    }

    public void setWordNetMax(Integer wordNetMax) {
        this.wordNetMax = wordNetMax;
    }

    public Integer getWordNetMax() {
        return wordNetMax;
    }
}
