package com.net128.app.chat1.swagger;

import com.google.common.base.Predicates;
import com.net128.app.chat1.controller.MessageDevController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile("dev")
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .apis(Predicates.not(RequestHandlerSelectors.basePackage(MessageDevController.class.getPackage().getName())))
            .apis(Predicates.not(RequestHandlerSelectors.basePackage(getClass().getPackage().getName())))
            .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework")))
            .paths(PathSelectors.any())
            .build();
    }
}
