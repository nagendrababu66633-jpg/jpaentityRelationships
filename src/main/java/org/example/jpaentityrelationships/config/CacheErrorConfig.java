package org.example.jpaentityrelationships.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CacheErrorConfig implements CachingConfigurer {

    @Override
    public CacheErrorHandler errorHandler() {

        return new CacheErrorHandler() {

            @Override
            public void handleCacheGetError(
                    RuntimeException exception,
                    Cache cache,
                    Object key) {

                log.error(
                        "Redis cache GET failed. Cache: {}, Key: {}",
                        cache.getName(),
                        key,
                        exception
                );

                // Do not stop the application because Redis failed.
            }

            @Override
            public void handleCachePutError(
                    RuntimeException exception,
                    Cache cache,
                    Object key,
                    Object value) {

                log.error(
                        "Redis cache PUT failed. Cache: {}, Key: {}",
                        cache.getName(),
                        key,
                        exception
                );
            }

            @Override
            public void handleCacheEvictError(
                    RuntimeException exception,
                    Cache cache,
                    Object key) {

                log.error(
                        "Redis cache EVICT failed. Cache: {}, Key: {}",
                        cache.getName(),
                        key,
                        exception
                );
            }

            @Override
            public void handleCacheClearError(
                    RuntimeException exception,
                    Cache cache) {

                log.error(
                        "Redis cache CLEAR failed. Cache: {}",
                        cache.getName(),
                        exception
                );
            }
        };
    }
}