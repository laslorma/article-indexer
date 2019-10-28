package io.catwizard.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Paragraph.
 */
@Entity
@Table(name = "paragraph")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "paragraph")
public class Paragraph implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Lob
    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "original_cleaned_content")
    private String originalCleanedContent;

    @Column(name = "total_words")
    private Integer totalWords;

    @Column(name = "header")
    private Boolean header;

    @Column(name = "readability")
    private String readability;

    @ManyToOne
    @JsonIgnoreProperties("paragraphs")
    private Article article;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Paragraph content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOriginalCleanedContent() {
        return originalCleanedContent;
    }

    public Paragraph originalCleanedContent(String originalCleanedContent) {
        this.originalCleanedContent = originalCleanedContent;
        return this;
    }

    public void setOriginalCleanedContent(String originalCleanedContent) {
        this.originalCleanedContent = originalCleanedContent;
    }

    public Integer getTotalWords() {
        return totalWords;
    }

    public Paragraph totalWords(Integer totalWords) {
        this.totalWords = totalWords;
        return this;
    }

    public void setTotalWords(Integer totalWords) {
        this.totalWords = totalWords;
    }

    public Boolean isHeader() {
        return header;
    }

    public Paragraph header(Boolean header) {
        this.header = header;
        return this;
    }

    public void setHeader(Boolean header) {
        this.header = header;
    }

    public String getReadability() {
        return readability;
    }

    public Paragraph readability(String readability) {
        this.readability = readability;
        return this;
    }

    public void setReadability(String readability) {
        this.readability = readability;
    }

    public Article getArticle() {
        return article;
    }

    public Paragraph article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paragraph)) {
            return false;
        }
        return id != null && id.equals(((Paragraph) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Paragraph{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", originalCleanedContent='" + getOriginalCleanedContent() + "'" +
            ", totalWords=" + getTotalWords() +
            ", header='" + isHeader() + "'" +
            ", readability='" + getReadability() + "'" +
            "}";
    }
}
