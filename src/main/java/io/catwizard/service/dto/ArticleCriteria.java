package io.catwizard.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.catwizard.domain.Article} entity. This class is used
 * in {@link io.catwizard.web.rest.ArticleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /articles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArticleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter author;

    private StringFilter title;

    private StringFilter url;

    private StringFilter urlToImage;

    private StringFilter publishedAt;

    private StringFilter category;

    private StringFilter countryCode;

    private StringFilter languageCode;

    private StringFilter sentiment;

    private StringFilter textReadability;

    private LongFilter numberOfParts;

    private LongFilter newsApiCategoryId;

    private LongFilter sourceId;

    private LongFilter paragraphId;

    private LongFilter partId;

    public ArticleCriteria(){
    }

    public ArticleCriteria(ArticleCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.author = other.author == null ? null : other.author.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.urlToImage = other.urlToImage == null ? null : other.urlToImage.copy();
        this.publishedAt = other.publishedAt == null ? null : other.publishedAt.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.countryCode = other.countryCode == null ? null : other.countryCode.copy();
        this.languageCode = other.languageCode == null ? null : other.languageCode.copy();
        this.sentiment = other.sentiment == null ? null : other.sentiment.copy();
        this.textReadability = other.textReadability == null ? null : other.textReadability.copy();
        this.numberOfParts = other.numberOfParts == null ? null : other.numberOfParts.copy();
        this.newsApiCategoryId = other.newsApiCategoryId == null ? null : other.newsApiCategoryId.copy();
        this.sourceId = other.sourceId == null ? null : other.sourceId.copy();
        this.paragraphId = other.paragraphId == null ? null : other.paragraphId.copy();
        this.partId = other.partId == null ? null : other.partId.copy();
    }

    @Override
    public ArticleCriteria copy() {
        return new ArticleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAuthor() {
        return author;
    }

    public void setAuthor(StringFilter author) {
        this.author = author;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(StringFilter urlToImage) {
        this.urlToImage = urlToImage;
    }

    public StringFilter getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(StringFilter publishedAt) {
        this.publishedAt = publishedAt;
    }

    public StringFilter getCategory() {
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public StringFilter getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(StringFilter countryCode) {
        this.countryCode = countryCode;
    }

    public StringFilter getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(StringFilter languageCode) {
        this.languageCode = languageCode;
    }

    public StringFilter getSentiment() {
        return sentiment;
    }

    public void setSentiment(StringFilter sentiment) {
        this.sentiment = sentiment;
    }

    public StringFilter getTextReadability() {
        return textReadability;
    }

    public void setTextReadability(StringFilter textReadability) {
        this.textReadability = textReadability;
    }

    public LongFilter getNumberOfParts() {
        return numberOfParts;
    }

    public void setNumberOfParts(LongFilter numberOfParts) {
        this.numberOfParts = numberOfParts;
    }

    public LongFilter getNewsApiCategoryId() {
        return newsApiCategoryId;
    }

    public void setNewsApiCategoryId(LongFilter newsApiCategoryId) {
        this.newsApiCategoryId = newsApiCategoryId;
    }

    public LongFilter getSourceId() {
        return sourceId;
    }

    public void setSourceId(LongFilter sourceId) {
        this.sourceId = sourceId;
    }

    public LongFilter getParagraphId() {
        return paragraphId;
    }

    public void setParagraphId(LongFilter paragraphId) {
        this.paragraphId = paragraphId;
    }

    public LongFilter getPartId() {
        return partId;
    }

    public void setPartId(LongFilter partId) {
        this.partId = partId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArticleCriteria that = (ArticleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(author, that.author) &&
            Objects.equals(title, that.title) &&
            Objects.equals(url, that.url) &&
            Objects.equals(urlToImage, that.urlToImage) &&
            Objects.equals(publishedAt, that.publishedAt) &&
            Objects.equals(category, that.category) &&
            Objects.equals(countryCode, that.countryCode) &&
            Objects.equals(languageCode, that.languageCode) &&
            Objects.equals(sentiment, that.sentiment) &&
            Objects.equals(textReadability, that.textReadability) &&
            Objects.equals(numberOfParts, that.numberOfParts) &&
            Objects.equals(newsApiCategoryId, that.newsApiCategoryId) &&
            Objects.equals(sourceId, that.sourceId) &&
            Objects.equals(paragraphId, that.paragraphId) &&
            Objects.equals(partId, that.partId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        author,
        title,
        url,
        urlToImage,
        publishedAt,
        category,
        countryCode,
        languageCode,
        sentiment,
        textReadability,
        numberOfParts,
        newsApiCategoryId,
        sourceId,
        paragraphId,
        partId
        );
    }

    @Override
    public String toString() {
        return "ArticleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (author != null ? "author=" + author + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (urlToImage != null ? "urlToImage=" + urlToImage + ", " : "") +
                (publishedAt != null ? "publishedAt=" + publishedAt + ", " : "") +
                (category != null ? "category=" + category + ", " : "") +
                (countryCode != null ? "countryCode=" + countryCode + ", " : "") +
                (languageCode != null ? "languageCode=" + languageCode + ", " : "") +
                (sentiment != null ? "sentiment=" + sentiment + ", " : "") +
                (textReadability != null ? "textReadability=" + textReadability + ", " : "") +
                (numberOfParts != null ? "numberOfParts=" + numberOfParts + ", " : "") +
                (newsApiCategoryId != null ? "newsApiCategoryId=" + newsApiCategoryId + ", " : "") +
                (sourceId != null ? "sourceId=" + sourceId + ", " : "") +
                (paragraphId != null ? "paragraphId=" + paragraphId + ", " : "") +
                (partId != null ? "partId=" + partId + ", " : "") +
            "}";
    }

}
