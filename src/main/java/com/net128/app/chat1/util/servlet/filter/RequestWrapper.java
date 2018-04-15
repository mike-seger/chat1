package com.net128.app.chat1.util.servlet.filter;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {
    private byte[] requestData=null;
    private HttpServletRequest request;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        this.request=request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(requestData==null) {
            requestData = IOUtils.toByteArray(request.getInputStream());
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestData);
        ServletInputStream servletInputStream = new ServletInputStream() {
            private ReadListener readListener;

            public int read() throws IOException {
                int read = byteArrayInputStream.read();
                if (readListener != null && isFinished()) {
                    readListener.onAllDataRead();
                }
                return read;
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() <= 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                readListener = listener;
                try {
                    readListener.onDataAvailable();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }
}