package com.net128.app.chat1.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpLoggingFilter3 extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(HttpLoggingFilter3.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ConsumingHttpServletRequestWrapper requestWrapper = new ConsumingHttpServletRequestWrapper(new ContentCachingRequestWrapper(request));
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            String requestBody = new String(requestWrapper.getContentAsByteArray());
            logger.info("Request body: {}", requestBody);
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {

            String responseBody = new String(responseWrapper.getContentAsByteArray());
            logger.info("Response body: {}", responseBody);

            // Do not forget this line after reading response content or actual response will be empty!
            responseWrapper.copyBodyToResponse();
        }
    }
}