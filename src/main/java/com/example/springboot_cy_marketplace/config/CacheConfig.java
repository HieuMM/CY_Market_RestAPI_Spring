package com.example.springboot_cy_marketplace.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {
    public static final String CACHE_NAME = "wait-list-product";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Bean
    public CacheManager cacheManager(){
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(CACHE_NAME);
        return cacheManager;
    }

    @CacheEvict(allEntries = true, value = {CACHE_NAME})
    @Scheduled(fixedDelay = 15 * 24 * 60 * 60 * 1000 ,  initialDelay = 500) //initialDelay: 0,5 giây, fixedDelay: 5 giây
    public void reportCacheEvict() {
        System.out.println("Flush Cache " + dateFormat.format(new Date()));
    }
}
