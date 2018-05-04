package com.net128.app.chat1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EntityScan(
    basePackageClasses = { Chat1Application.class, Jsr310JpaConverters.class }
)
@EnableZuulProxy
@SpringBootApplication
public class Chat1Application {
    public static void main(String[] args) {
        SpringApplication.run(Chat1Application.class, args);
    }
}
