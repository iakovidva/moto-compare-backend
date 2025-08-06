package com.vaiak.moto_compare.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache popularCache = new CaffeineCache("popular", Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .maximumSize(100)
                .build());

        CaffeineCache categoryStatsCache = new CaffeineCache("categoryStats", Caffeine.newBuilder()
                .maximumSize(100)
                .build());

        CaffeineCache manufacturerStatsCache = new CaffeineCache("manufacturerStats", Caffeine.newBuilder()
                .maximumSize(100)
                .build());

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(popularCache, categoryStatsCache, manufacturerStatsCache));
        return cacheManager;
    }
}
