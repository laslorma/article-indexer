package io.catwizard.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "author")
    private String author;

    @Size(max = 1000)
    @Column(name = "title", length = 1000)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Size(max = 2000)
    @Column(name = "url", length = 2000)
    private String url;

    @Size(max = 2000)
    @Column(name = "url_to_image", length = 2000)
    private String urlToImage;

    @Column(name = "published_at")
    private String publishedAt;

    @Column(name = "category")
    private String category;

    @Lob
    @Column(name = "content")
    private String content;

    @Size(min = 2, max = 2)
    @Column(name = "country_code", length = 2)
    private String countryCode;

    @Size(min = 2, max = 2)
    @Column(name = "language_code", length = 2)
    private String languageCode;

    @Column(name = "sentiment")
    private String sentiment;

    @Column(name = "text_readability")
    private String textReadability;

    @Column(name = "number_of_parts")
    private Long numberOfParts;

    @ManyToOne
    @JsonIgnoreProperties("articles")
    private NewsApiCategory newsApiCategory;

    @ManyToOne
    @JsonIgnoreProperties("articles")
    private Source source;

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Paragraph> paragraphs = new HashSet<>();

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Part> parts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public Article author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public Article title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Article description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public Article url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public Article urlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
        return this;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public Article publishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getCategory() {
        return category;
    }

    public Article category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public Article content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Article countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public Article languageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getSentiment() {
        return sentiment;
    }

    public Article sentiment(String sentiment) {
        this.sentiment = sentiment;
        return this;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getTextReadability() {
        return textReadability;
    }

    public Article textReadability(String textReadability) {
        this.textReadability = textReadability;
        return this;
    }

    public void setTextReadability(String textReadability) {
        this.textReadability = textReadability;
    }

    public Long getNumberOfParts() {
        return numberOfParts;
    }

    public Article numberOfParts(Long numberOfParts) {
        this.numberOfParts = numberOfParts;
        return this;
    }

    public void setNumberOfParts(Long numberOfParts) {
        this.numberOfParts = numberOfParts;
    }

    public NewsApiCategory getNewsApiCategory() {
        return newsApiCategory;
    }

    public Article newsApiCategory(NewsApiCategory newsApiCategory) {
        this.newsApiCategory = newsApiCategory;
        return this;
    }

    public void setNewsApiCategory(NewsApiCategory newsApiCategory) {
        this.newsApiCategory = newsApiCategory;
    }

    public Source getSource() {
        return source;
    }

    public Article source(Source source) {
        this.source = source;
        return this;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Set<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public Article paragraphs(Set<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
        return this;
    }

    public Article addParagraph(Paragraph paragraph) {
        this.paragraphs.add(paragraph);
        paragraph.setArticle(this);
        return this;
    }

    public Article removeParagraph(Paragraph paragraph) {
        this.paragraphs.remove(paragraph);
        paragraph.setArticle(null);
        return this;
    }

    public void setParagraphs(Set<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public Set<Part> getParts() {
        return parts;
    }

    public Article parts(Set<Part> parts) {
        this.parts = parts;
        return this;
    }

    public Article addPart(Part part) {
        this.parts.add(part);
        part.setArticle(this);
        return this;
    }

    public Article removePart(Part part) {
        this.parts.remove(part);
        part.setArticle(null);
        return this;
    }

    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", author='" + getAuthor() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            ", urlToImage='" + getUrlToImage() + "'" +
            ", publishedAt='" + getPublishedAt() + "'" +
            ", category='" + getCategory() + "'" +
            ", content='" + getContent() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", languageCode='" + getLanguageCode() + "'" +
            ", sentiment='" + getSentiment() + "'" +
            ", textReadability='" + getTextReadability() + "'" +
            ", numberOfParts=" + getNumberOfParts() +
            "}";
    }
}
