package com.net128.app.chat1.config;

//import ch.qos.logback.access.servlet.TeeFilter;
import com.net128.app.chat1.servlet.HttpLoggingFilter;
import com.net128.app.chat1.util.servlet.filter.RequestLoggingFilter;
import org.apache.catalina.filters.RequestDumperFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
//import org.zalando.logbook.Logbook;
//import org.zalando.logbook.servlet.LogbookFilter;

import javax.inject.Inject;
import javax.servlet.*;
import java.util.EnumSet;

import static javax.servlet.DispatcherType.ASYNC;
import static javax.servlet.DispatcherType.ERROR;
import static javax.servlet.DispatcherType.REQUEST;

@Configuration
public class HttpLoggingFilterConfig {
//    @Inject
//    private Logbook logbook;

//    @Bean
//    public FilterRegistrationBean requestRequestLoggingFilter() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        HttpLoggingFilter filter = new HttpLoggingFilter();
//        registrationBean.setFilter(filter);
//        registrationBean.setOrder(Integer.MIN_VALUE);
//        registrationBean.addUrlPatterns("/*");
//        return registrationBean;
//    }
//    @Bean
//    public FilterRegistrationBean requestRequestLoggingFilter() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        RequestLoggingFilter filter = new RequestLoggingFilter();
//        registrationBean.setFilter(filter);
//        registrationBean.setOrder(Integer.MIN_VALUE);
//        registrationBean.addUrlPatterns("/*");
//        return registrationBean;
//    }

//    @Bean
//    public FilterRegistrationBean requestLogbookFilter() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        LogbookFilter filter = new LogbookFilter(logbook);
//
//        registrationBean.setFilter(filter);
//        registrationBean.setOrder(Integer.MIN_VALUE);
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setDispatcherTypes(EnumSet.of(REQUEST, ASYNC, ERROR));
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean requestCommonsRequestLoggingFilter() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
//        filter.setIncludeClientInfo(true);
//        filter.setIncludeQueryString(true);
//        filter.setIncludePayload(true);
//        registrationBean.setFilter(filter);
//        registrationBean.setOrder(Integer.MIN_VALUE);
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setDispatcherTypes(EnumSet.of(REQUEST, ASYNC, ERROR));
//        return registrationBean;
//    }

}
