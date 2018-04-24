package com.net128.app.chat1.config.http;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
public class SolrProxyServletConfiguration {
    @Inject
    private SolrProxyConfig solrProxyConfig;

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new ProxyServlet(), solrProxyConfig.proxyPath);
        servletRegistrationBean.addInitParameter("targetUri", solrProxyConfig.targetUrl);
        servletRegistrationBean.addInitParameter(ProxyServlet.P_LOG, solrProxyConfig.loggingEnabled+"");
        return servletRegistrationBean;
    }

    @Configuration
    @ConfigurationProperties(prefix="proxy.solr")
    public static class SolrProxyConfig {
        public String proxyPath;
        public String targetUrl;
        public boolean loggingEnabled;

        public void setProxyPath(String proxyPath) {
            this.proxyPath = proxyPath;
        }

        public void setTargetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
        }

        public void setLoggingEnabled(boolean loggingEnabled) {
            this.loggingEnabled = loggingEnabled;
        }

        @Override
        public String toString() {
            return "SolrProxyConfig{" +
                "proxyPath='" + proxyPath + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", loggingEnabled=" + loggingEnabled +
                '}';
        }
    }
}