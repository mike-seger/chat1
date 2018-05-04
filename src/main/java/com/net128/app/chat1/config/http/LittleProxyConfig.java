package com.net128.app.chat1.config.http;

import com.net128.app.chat1.util.Hexdump;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Configuration
@Profile("dev")
public class LittleProxyConfig {

    private final static Logger logger = LoggerFactory.getLogger(LittleProxyConfig.class);

    @Value("${littleproxy.enabled}")
    private boolean enabled;

    @Value("${littleproxy.request.buffer-size}")
    private int requestBufferSize;

    @Value("${littleproxy.response.buffer-size}")
    private int responseBufferSize;

    @Value("${littleproxy.server.port}")
    private int proxyServerPort;

    @Value("${server.port}")
    private int serverPort;

    @PostConstruct
    private void init() {
        if(!enabled) {
            return;
        }
        DefaultHttpProxyServer.bootstrap()
            .withPort(proxyServerPort)
            .withFiltersSource(new HttpFiltersSourceAdapter() {
                @Override
                public int getMaximumResponseBufferSizeInBytes() {
                    return responseBufferSize;
                }
                @Override
                public int getMaximumRequestBufferSizeInBytes() {
                    return requestBufferSize;
                }
                public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                    return new HttpFiltersAdapter(originalRequest) {
                        @Override
                        public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                            if(httpObject instanceof FullHttpRequest){
                                FullHttpRequest request = (FullHttpRequest) httpObject;
                                ByteBuf byteBuf=request.content();
                                if(logger.isDebugEnabled()) {
                                    logger.debug("Request:\n{}", byteBuf.toString(CharsetUtil.UTF_8));
                                }
                                if(logger.isTraceEnabled()) {
                                    try {
                                        byte[] requestBody = new byte[byteBuf.readableBytes()];
                                        byteBuf=byteBuf.copy();
                                        byteBuf.readBytes(requestBody);
                                        logger.trace("Request Trace:\n{}", Hexdump.toHexdump(requestBody));
                                    } catch (Exception e) {
                                        logger.error("Error logging content as hex dump.", e);
                                    }
                                }

                                //Act as a reverse proxy for server.port
                                String host=request.headers().get("Host");
                                request.headers().remove("Host");
                                host=host.replace(""+proxyServerPort, ""+serverPort);
                                request.headers().add("Host", host);
                            }
                            return null;
                        }

                        @Override
                        public HttpObject serverToProxyResponse(HttpObject httpObject) {
                            return httpObject;
                        }
                    };
                }
            })
            .start();
    }
}
