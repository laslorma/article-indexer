package io.catwizard.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, io.catwizard.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, io.catwizard.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, io.catwizard.domain.User.class.getName());
            createCache(cm, io.catwizard.domain.Authority.class.getName());
            createCache(cm, io.catwizard.domain.User.class.getName() + ".authorities");
            createCache(cm, io.catwizard.domain.Article.class.getName());
            createCache(cm, io.catwizard.domain.Article.class.getName() + ".paragraphs");
            createCache(cm, io.catwizard.domain.Article.class.getName() + ".parts");
            createCache(cm, io.catwizard.domain.NewsApiCategory.class.getName());
            createCache(cm, io.catwizard.domain.NewsApiCategory.class.getName() + ".articles");
            createCache(cm, io.catwizard.domain.Paragraph.class.getName());
            createCache(cm, io.catwizard.domain.Part.class.getName());
            createCache(cm, io.catwizard.domain.Country.class.getName());
            createCache(cm, io.catwizard.domain.IndexSession.class.getName());
            createCache(cm, io.catwizard.domain.Source.class.getName());
            createCache(cm, io.catwizard.domain.Source.class.getName() + ".articles");
            createCache(cm, io.catwizard.domain.IndexConfiguration.class.getName());
            createCache(cm, io.catwizard.domain.NlpServerConf.class.getName());
            createCache(cm, io.catwizard.domain.LingoToken.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}
