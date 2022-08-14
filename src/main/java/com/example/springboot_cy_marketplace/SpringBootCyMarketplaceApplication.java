package com.example.springboot_cy_marketplace;

import com.example.springboot_cy_marketplace.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import java.util.TimeZone;

@SpringBootApplication
@EnableCaching //enables Spring Caching functionality
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class SpringBootCyMarketplaceApplication {
    public static void main(String[] args) {
//        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SpringApplication.run(SpringBootCyMarketplaceApplication.class, args);
    }
}
