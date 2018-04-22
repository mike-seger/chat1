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

    @Value("${server.port}")
    private int serverPort;

    @PostConstruct
    private void init() {
        String remoteHost="localhost";
        Params params = new Params(tunnelPort, remoteHost, serverPort);
        params.getUpObservers().add(new TcpDumpLogger("Request"));
        params.getDownObservers().add(new TcpDumpLogger("Response"));
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
                byte [] data=new byte[count];
                System.arraycopy(buffer,0,data,0, count);
                logger.debug("{} ASCII:\n{}", message, Hexdump.escapeAsciiChars(data));
                if(logger.isTraceEnabled()) {
                    try {
                        logger.trace("{} HEX/ASCII Dump:\n{}", message, Hexdump.toHexdump(data));
                    } catch (Exception e) {
                        logger.error("Error logging content as hex dump.", e);
                    }
                }
            }
        }
    }
}
