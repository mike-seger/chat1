package com.net128.app.chat1.util.servlet.filter;

import org.apache.commons.io.output.TeeOutputStream;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

//@Component
public class MultiReadFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request = new BufferedRequestWrapper((HttpServletRequest) request);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringWriter sw = new StringWriter();
        request.setAttribute("_log_baos", baos);
        request.setAttribute("_log_sw", sw);
        request.setAttribute("_log_req", request);
        processRequest(request);
        chain.doFilter(request, new HttpServletResponseWrapper((HttpServletResponse) response) {
            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                ServletOutputStream outputStream = super.getOutputStream();
                TeeOutputStream targetStream = new TeeOutputStream(outputStream, baos);
                return new DelegatingServletOutputStream(targetStream);
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                PrintWriter writer = super.getWriter();
                TeeWriter teeWriter = new TeeWriter(writer, sw);
                return new PrintWriter(teeWriter);
            }
        });
        processResult(request, getByteArrayOutputStream(request).toByteArray());
    }

    protected void processResult(ServletRequest request, byte [] result) throws IOException {

    }

    protected void processRequest(ServletRequest request) throws IOException {

    }

    private ByteArrayOutputStream getByteArrayOutputStream(ServletRequest request) {
        return (ByteArrayOutputStream)request.getAttribute("_log_baos");
    }

    @Override
    public void destroy() {

    }

    public static class DelegatingServletOutputStream extends ServletOutputStream {
        private final OutputStream targetStream;

        public DelegatingServletOutputStream(OutputStream targetStream) {
            Assert.notNull(targetStream, "Target OutputStream must not be null");
            this.targetStream = targetStream;
        }

        public final OutputStream getTargetStream() {
            return this.targetStream;
        }

        public void write(int b) throws IOException {
            this.targetStream.write(b);
        }

        public void flush() throws IOException {
            super.flush();
            this.targetStream.flush();
        }

        public void close() throws IOException {
            super.close();
            this.targetStream.close();
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }

    }

    static final class TeeWriter extends Writer {
        private final List<Writer> writers;

        TeeWriter(Writer... writers) {
            this(Arrays.asList(writers));
        }

        TeeWriter(Iterable<? extends Writer> writers) {
            this.writers = new ArrayList<Writer>();
            for (Writer writer : writers) {
                checkNotNull(writer);
                this.writers.add(writer);
            }
        }

        @Override
        public TeeWriter append(char c) throws IOException {
            for (Writer writer : writers) {
                writer.append(c);
            }
            return this;
        }

        @Override
        public TeeWriter append(CharSequence sequence) throws IOException {
            for (Writer writer : writers) {
                writer.append(sequence);
            }
            return this;
        }

        @Override
        public TeeWriter append(CharSequence sequence, int start, int end)
                throws IOException {
            return append(sequence.subSequence(start, end));
        }

        @Override
        public void write(int c) throws IOException {
            for (Writer writer : writers) {
                writer.write(c);
            }
        }

        @Override
        public void write(char[] buf) throws IOException {
            for (Writer writer : writers) {
                writer.write(buf);
            }
        }

        @Override
        public void write(char[] buf, int off, int len) throws IOException {
            for (Writer writer : writers) {
                writer.write(buf, off, len);
            }
        }

        @Override
        public void write(String str) throws IOException {
            for (Writer writer : writers) {
                writer.write(str);
            }
        }

        @Override
        public void write(String str, int off, int len) throws IOException {
            write(str.substring(off, off + len));
        }

        @Override
        public void flush() throws IOException {
            for (Writer writer : writers) {
                writer.flush();
            }
        }

        @Override
        public void close() {
            for (Writer writer : writers) {
                closeQuietly(writer);
            }
        }

        private void closeQuietly(Writer writer) {
            try {
                writer.close();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public static class BufferedRequestWrapper extends HttpServletRequestWrapper {
        private static final class BufferedServletInputStream extends ServletInputStream {
            private final ServletInputStream inputStream;
            private ByteArrayInputStream bais;

            public BufferedServletInputStream(ByteArrayInputStream bais, ServletInputStream inputStream) {
                this.bais = bais;
                this.inputStream = inputStream;
            }

            @Override
            public int available() {
                return bais.available();
            }

            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public int read(byte[] buf, int off, int len) {
                return bais.read(buf, off, len);
            }

            @Override
            public boolean isFinished() {
                return inputStream.isFinished();
            }

            @Override
            public boolean isReady() {
                return inputStream.isReady();
            }

            @Override
            public void setReadListener(ReadListener listener) {
                inputStream.setReadListener(listener);
            }
        }

        private byte[] mBodyBuffer;

        public BufferedRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);

            InputStream in = request.getInputStream();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) > 0) {
                baos.write(buffer, 0, bytesRead);
            }
            mBodyBuffer = baos.toByteArray();
        }

        public String getRequestBody() {
            return new String(mBodyBuffer, StandardCharsets.UTF_8);
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            ByteArrayInputStream in = new ByteArrayInputStream(mBodyBuffer);
            return new BufferedServletInputStream(in, super.getInputStream());
        }
    }
}