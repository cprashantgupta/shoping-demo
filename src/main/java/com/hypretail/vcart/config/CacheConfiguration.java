package com.hypretail.vcart.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import com.hypretail.vcart.util.JHipsterProperties;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Factory;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.hibernate.cache.jcache.ConfigSettings;

import javax.cache.CacheManager;
import javax.inject.Singleton;

@Factory
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Singleton
    public CacheManager cacheManager(ApplicationContext applicationContext) {
        CacheManager cacheManager = new EhcacheCachingProvider().getCacheManager(
            null, applicationContext.getClassLoader(), new Properties());
        customizeCacheManager(cacheManager);
        return cacheManager;
    }

    private void customizeCacheManager(CacheManager cm) {
        createCache(cm, com.hypretail.vcart.repository.UserRepository.USERS_BY_LOGIN_CACHE);
        createCache(cm, com.hypretail.vcart.repository.UserRepository.USERS_BY_EMAIL_CACHE);
        createCache(cm, com.hypretail.vcart.domain.User.class.getName());
        createCache(cm, com.hypretail.vcart.domain.Authority.class.getName());
        createCache(cm, com.hypretail.vcart.domain.User.class.getName() + ".authorities");
        createCache(cm, com.hypretail.vcart.domain.Category.class.getName());
        createCache(cm, com.hypretail.vcart.domain.Category.class.getName() + ".subCategories");
        createCache(cm, com.hypretail.vcart.domain.SubCategory.class.getName());
        createCache(cm, com.hypretail.vcart.domain.SubCategory.class.getName() + ".products");
        createCache(cm, com.hypretail.vcart.domain.Product.class.getName());
        createCache(cm, com.hypretail.vcart.domain.Product.class.getName() + ".vendors");
        createCache(cm, com.hypretail.vcart.domain.Vendor.class.getName());
        createCache(cm, com.hypretail.vcart.domain.Vendor.class.getName() + ".products");
        createCache(cm, com.hypretail.vcart.domain.Purchaser.class.getName());
        // jhipster-needle-ehcache-add-entry
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
