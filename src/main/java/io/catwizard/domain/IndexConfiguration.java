package io.catwizard.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A IndexConfiguration.
 */
@Entity
@Table(name = "index_configuration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "indexconfiguration")
public class IndexConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "generate_corpuses")
    private Boolean generateCorpuses;

    @Column(name = "corpuses_output_path")
    private String corpusesOutputPath;

    @Column(name = "news_api_key")
    private String newsApiKey;

    @Column(name = "activate_all_categories_and_countries")
    private Boolean activateAllCategoriesAndCountries;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isGenerateCorpuses() {
        return generateCorpuses;
    }

    public IndexConfiguration generateCorpuses(Boolean generateCorpuses) {
        this.generateCorpuses = generateCorpuses;
        return this;
    }

    public void setGenerateCorpuses(Boolean generateCorpuses) {
        this.generateCorpuses = generateCorpuses;
    }

    public String getCorpusesOutputPath() {
        return corpusesOutputPath;
    }

    public IndexConfiguration corpusesOutputPath(String corpusesOutputPath) {
        this.corpusesOutputPath = corpusesOutputPath;
        return this;
    }

    public void setCorpusesOutputPath(String corpusesOutputPath) {
        this.corpusesOutputPath = corpusesOutputPath;
    }

    public String getNewsApiKey() {
        return newsApiKey;
    }

    public IndexConfiguration newsApiKey(String newsApiKey) {
        this.newsApiKey = newsApiKey;
        return this;
    }

    public void setNewsApiKey(String newsApiKey) {
        this.newsApiKey = newsApiKey;
    }

    public Boolean isActivateAllCategoriesAndCountries() {
        return activateAllCategoriesAndCountries;
    }

    public IndexConfiguration activateAllCategoriesAndCountries(Boolean activateAllCategoriesAndCountries) {
        this.activateAllCategoriesAndCountries = activateAllCategoriesAndCountries;
        return this;
    }

    public void setActivateAllCategoriesAndCountries(Boolean activateAllCategoriesAndCountries) {
        this.activateAllCategoriesAndCountries = activateAllCategoriesAndCountries;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IndexConfiguration)) {
            return false;
        }
        return id != null && id.equals(((IndexConfiguration) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "IndexConfiguration{" +
            "id=" + getId() +
            ", generateCorpuses='" + isGenerateCorpuses() + "'" +
            ", corpusesOutputPath='" + getCorpusesOutputPath() + "'" +
            ", newsApiKey='" + getNewsApiKey() + "'" +
            ", activateAllCategoriesAndCountries='" + isActivateAllCategoriesAndCountries() + "'" +
            "}";
    }
}
