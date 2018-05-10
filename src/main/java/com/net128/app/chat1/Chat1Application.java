package com.net128.app.chat1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(
    basePackageClasses = { Chat1Application.class, Jsr310JpaConverters.class }
)
@SpringBootApplication
@EnableCaching
public class Chat1Application {
    public static void main(String[] args) {
        SpringApplication.run(Chat1Application.class, args);
    }
}
