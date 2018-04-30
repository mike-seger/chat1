package com.net128.app.chat1.swagger;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.service.Contact;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import javax.servlet.ServletContext;

@Configuration
@EnableSwagger2
@Profile("dev")
public class SwaggerConfig {
    @Bean
    public Docket api(ServletContext servletContext) {
        return new Docket(DocumentationType.SWAGGER_2)
//                .pathProvider(new RelativePathProvider(servletContext) {
//                    @Override
//                    public String getApplicationBasePath() {
//                        return "/";
//                    }
//                })
                .apiInfo(new ApiInfo(
                    "Message Service", 
                    "This is the message service", 
                    "1.0", "http://termsofservice", 
                    new Contact("Name", "http://name.com", "abc@name.com"), 
                    "License", "http://licanse", new HashSet<>()))
            .select()
            .apis(RequestHandlerSelectors.any())
            //.apis(Predicates.not(RequestHandlerSelectors.basePackage(MessageDevController.class.getPackage().getName())))
            .apis(Predicates.not(RequestHandlerSelectors.basePackage(getClass().getPackage().getName())))
            .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework")))
            .paths(PathSelectors.any())
            .build();
    }
}
