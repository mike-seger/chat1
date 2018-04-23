package com.net128.app.chat1.config.http;

import com.net128.app.chat1.util.Hexdump;
import net.kanstren.tcptunnel.Main;
import net.kanstren.tcptunnel.Params;
import net.kanstren.tcptunnel.observers.TCPObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class TcpTunnelConfig {
    private final static Logger logger = LoggerFactory.getLogger(TcpTunnelConfig.class);

    @Value("${tcptunnel.enabled}")
    private boolean enabled;

    @Value("${tcptunnel.server.port}")
    private int tunnelPort;

    @Value("${tcptunnel.bufferSize}")
    private int bufferSize;

    @Value("${tcptunnel.maxDumpSize}")
    private int maxDumpSize;

    @Value("${server.port}")
    private int serverPort;

    @PostConstruct
    private void init() {
        String remoteHost="localhost";
        Params params = new Params(tunnelPort, remoteHost, serverPort);
        if(enabled) {
            params.setBufferSize(bufferSize);
            params.getUpObservers().add(new TcpDumpLogger("Request"));
            params.getDownObservers().add(new TcpDumpLogger("Response"));
        }
        Main main = new Main(params);
        main.start();
    }

    private class TcpDumpLogger implements TCPObserver {
        private String message;
        public TcpDumpLogger(String message) {
            this.message=message;
        }
        @Override
        public void observe(byte[] buffer, int start, int count) throws IOException {
            if(logger.isDebugEnabled()) {
                int size=Math.min(count, maxDumpSize);
                byte [] data=new byte[size];
                System.arraycopy(buffer,0,data,0, size);
                StringBuilder sb=new StringBuilder();
                sb.append(Hexdump.escapeAsciiChars(data));
                if(!logger.isTraceEnabled()) {
                    appendNotDumpedInfo(sb, size, count);
                }
                logger.debug("{} ASCII:\n{}", message, sb.toString());
                if(logger.isTraceEnabled()) {
                    try {
                        sb=new StringBuilder();
                        Hexdump.hexdump(data, sb);
                        appendNotDumpedInfo(sb, size, count);
                        logger.trace("{} HEX/ASCII Dump:\n{}", message, sb.toString());
                    } catch (Exception e) {
                        logger.error("Error logging content as hex dump.", e);
                    }
                }
            }
        }

        private void appendNotDumpedInfo(StringBuilder sb, int size, int total) {
            if(size<total) {
                sb.append("... ");
                sb.append(""+(total-size));
                sb.append(" bytes not dumped. Total bytes: "+total);
                sb.append('\n');
            }
        }
    }
}
