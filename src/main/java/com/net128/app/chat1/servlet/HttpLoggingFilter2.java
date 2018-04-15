package com.net128.app.chat1.servlet;

import com.net128.app.chat1.util.Hexdump;
import com.net128.app.chat1.util.servlet.filter.MultiReadFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import java.io.IOException;

public class HttpLoggingFilter2 extends MultiReadFilter {
    private static final Logger logger = LoggerFactory.getLogger(HttpLoggingFilter2.class);

    @Override
    protected void processRequest(ServletRequest request) throws IOException {
        if(logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("HTTP REQUEST BODY:\n");
            Hexdump.hexdump(request.getInputStream(), sb);
            logger.debug(sb.toString());
        }
    }

    @Override
    protected void processResult(ServletRequest request, byte [] result) throws IOException {
        if(logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("HTTP RESPONSE:\n");
            Hexdump.hexdump(result, sb);
            logger.debug(sb.toString());
        }
    }
}
