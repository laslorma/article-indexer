package io.catwizard.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A IndexSession.
 */
@Entity
@Table(name = "index_session")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "indexsession")
public class IndexSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "news_api_calls")
    private Long newsApiCalls;

    @Column(name = "five_filter_api_calls")
    private Long fiveFilterApiCalls;

    @Column(name = "started")
    private ZonedDateTime started;

    @Column(name = "ended")
    private ZonedDateTime ended;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "total_articles")
    private Long totalArticles;

    @Column(name = "indexing")
    private Boolean indexing;

    @Column(name = "articles_saved")
    private Long articlesSaved;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "had_error")
    private Boolean hadError;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewsApiCalls() {
        return newsApiCalls;
    }

    public IndexSession newsApiCalls(Long newsApiCalls) {
        this.newsApiCalls = newsApiCalls;
        return this;
    }

    public void setNewsApiCalls(Long newsApiCalls) {
        this.newsApiCalls = newsApiCalls;
    }

    public Long getFiveFilterApiCalls() {
        return fiveFilterApiCalls;
    }

    public IndexSession fiveFilterApiCalls(Long fiveFilterApiCalls) {
        this.fiveFilterApiCalls = fiveFilterApiCalls;
        return this;
    }

    public void setFiveFilterApiCalls(Long fiveFilterApiCalls) {
        this.fiveFilterApiCalls = fiveFilterApiCalls;
    }

    public ZonedDateTime getStarted() {
        return started;
    }

    public IndexSession started(ZonedDateTime started) {
        this.started = started;
        return this;
    }

    public void setStarted(ZonedDateTime started) {
        this.started = started;
    }

    public ZonedDateTime getEnded() {
        return ended;
    }

    public IndexSession ended(ZonedDateTime ended) {
        this.ended = ended;
        return this;
    }

    public void setEnded(ZonedDateTime ended) {
        this.ended = ended;
    }

    public Long getDuration() {
        return duration;
    }

    public IndexSession duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getTotalArticles() {
        return totalArticles;
    }

    public IndexSession totalArticles(Long totalArticles) {
        this.totalArticles = totalArticles;
        return this;
    }

    public void setTotalArticles(Long totalArticles) {
        this.totalArticles = totalArticles;
    }

    public Boolean isIndexing() {
        return indexing;
    }

    public IndexSession indexing(Boolean indexing) {
        this.indexing = indexing;
        return this;
    }

    public void setIndexing(Boolean indexing) {
        this.indexing = indexing;
    }

    public Long getArticlesSaved() {
        return articlesSaved;
    }

    public IndexSession articlesSaved(Long articlesSaved) {
        this.articlesSaved = articlesSaved;
        return this;
    }

    public void setArticlesSaved(Long articlesSaved) {
        this.articlesSaved = articlesSaved;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public IndexSession errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean isHadError() {
        return hadError;
    }

    public IndexSession hadError(Boolean hadError) {
        this.hadError = hadError;
        return this;
    }

    public void setHadError(Boolean hadError) {
        this.hadError = hadError;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndexSession)) {
            return false;
        }
        return id != null && id.equals(((IndexSession) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "IndexSession{" +
            "id=" + getId() +
            ", newsApiCalls=" + getNewsApiCalls() +
            ", fiveFilterApiCalls=" + getFiveFilterApiCalls() +
            ", started='" + getStarted() + "'" +
            ", ended='" + getEnded() + "'" +
            ", duration=" + getDuration() +
            ", totalArticles=" + getTotalArticles() +
            ", indexing='" + isIndexing() + "'" +
            ", articlesSaved=" + getArticlesSaved() +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", hadError='" + isHadError() + "'" +
            "}";
    }
}
