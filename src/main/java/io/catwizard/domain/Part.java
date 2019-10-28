package io.catwizard.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Part.
 */
@Entity
@Table(name = "part")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "part")
public class Part implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Size(max = 500)
    @Column(name = "text", length = 500)
    private String text;

    @Column(name = "posible_options")
    private String posibleOptions;

    @ManyToOne
    @JsonIgnoreProperties("parts")
    private Article article;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Part text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPosibleOptions() {
        return posibleOptions;
    }

    public Part posibleOptions(String posibleOptions) {
        this.posibleOptions = posibleOptions;
        return this;
    }

    public void setPosibleOptions(String posibleOptions) {
        this.posibleOptions = posibleOptions;
    }

    public Article getArticle() {
        return article;
    }

    public Part article(Article article) {
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
        if (!(o instanceof Part)) {
            return false;
        }
        return id != null && id.equals(((Part) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Part{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", posibleOptions='" + getPosibleOptions() + "'" +
            "}";
    }
}
