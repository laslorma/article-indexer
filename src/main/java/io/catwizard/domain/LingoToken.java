package io.catwizard.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A LingoToken.
 */
@Entity
@Table(name = "token")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "lingotoken")
public class LingoToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "blank_text")
    private String blankText;

    @Column(name = "lingo_order")
    private Integer lingoOrder;

    @Column(name = "pos_tag")
    private String posTag;

    @Column(name = "lemma")
    private String lemma;

    @Column(name = "ner_tag")
    private String nerTag;

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

    public LingoToken text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBlankText() {
        return blankText;
    }

    public LingoToken blankText(String blankText) {
        this.blankText = blankText;
        return this;
    }

    public void setBlankText(String blankText) {
        this.blankText = blankText;
    }

    public Integer getLingoOrder() {
        return lingoOrder;
    }

    public LingoToken lingoOrder(Integer lingoOrder) {
        this.lingoOrder = lingoOrder;
        return this;
    }

    public void setLingoOrder(Integer lingoOrder) {
        this.lingoOrder = lingoOrder;
    }

    public String getPosTag() {
        return posTag;
    }

    public LingoToken posTag(String posTag) {
        this.posTag = posTag;
        return this;
    }

    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

    public String getLemma() {
        return lemma;
    }

    public LingoToken lemma(String lemma) {
        this.lemma = lemma;
        return this;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getNerTag() {
        return nerTag;
    }

    public LingoToken nerTag(String nerTag) {
        this.nerTag = nerTag;
        return this;
    }

    public void setNerTag(String nerTag) {
        this.nerTag = nerTag;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LingoToken)) {
            return false;
        }
        return id != null && id.equals(((LingoToken) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LingoToken{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", blankText='" + getBlankText() + "'" +
            ", lingoOrder=" + getLingoOrder() +
            ", posTag='" + getPosTag() + "'" +
            ", lemma='" + getLemma() + "'" +
            ", nerTag='" + getNerTag() + "'" +
            "}";
    }
}
