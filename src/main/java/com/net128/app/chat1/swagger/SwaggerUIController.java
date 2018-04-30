package com.net128.app.chat1.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Profile("dev")
public class SwaggerUIController {
    private static Logger logger = LoggerFactory.getLogger(SwaggerUIController.class);
    public static final String APPLICATION_YML_VALUE = "text/yaml";

    @Autowired
    private SwaggerUIService service;

    @RequestMapping("/swagger")
    public String swaggerUI() {
        return "redirect:swagger-ui.html";
    }

    @RequestMapping("/swagger.yml")
    public ResponseEntity<String> swaggerYmlSpec() {
        try {
            String yml = service.getSwaggerYmlSpec("default");
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_YML_VALUE);
            return new ResponseEntity<>(yml, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}