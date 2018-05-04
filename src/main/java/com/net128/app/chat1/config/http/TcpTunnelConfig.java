package com.net128.app.chat1.config.http;

import com.net128.app.chat1.util.Hexdump;
import net.kanstren.tcptunnel.Main;
import net.kanstren.tcptunnel.Params;
import net.kanstren.tcptunnel.observers.TCPObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "tcptunnel")
@Profile("dev")
public class TcpTunnelConfig {
    private final static Logger logger = LoggerFactory.getLogger(TcpTunnelConfig.class);

    private boolean enabled;
    private int bufferSize;
    private int maxDumpSize;
    private List<Tunnel> tunnels;

    @PostConstruct
    private void init() {
        for(Tunnel tunnel : tunnels) {
            Params params = new Params(tunnel.getPort(), tunnel.getRemoteHost(), tunnel.getRemotePort());
            if (enabled) {
                params.setBufferSize(bufferSize);
                params.getUpObservers().add(new TcpDumpLogger("Request"));
                params.getDownObservers().add(new TcpDumpLogger("Response"));
            }
            Main main = new Main(params);
            main.start();
        }
    }

    private class TcpDumpLogger implements TCPObserver {
        private String message;
        private TcpDumpLogger(String message) {
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
                sb.append("\n");
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

    public static class Tunnel {
        private int port;
        private int remotePort;
        private String remoteHost;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getRemotePort() {
            return remotePort;
        }

        public void setRemotePort(int remotePort) {
            this.remotePort = remotePort;
        }

        public String getRemoteHost() {
            return remoteHost;
        }

        public void setRemoteHost(String remoteHost) {
            this.remoteHost = remoteHost;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getMaxDumpSize() {
        return maxDumpSize;
    }

    public void setMaxDumpSize(int maxDumpSize) {
        this.maxDumpSize = maxDumpSize;
    }

    public List<Tunnel> getTunnels() {
        return tunnels;
    }

    public void setTunnels(List<Tunnel> tunnels) {
        this.tunnels = tunnels;
    }
}
