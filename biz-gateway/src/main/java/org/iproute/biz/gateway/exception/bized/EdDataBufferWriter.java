package org.iproute.biz.gateway.exception.bized;

import org.iproute.biz.gateway.BizGatewayApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author zhuzhenjie
 * @since 2020/10/27
 */
@Profile(BizGatewayApplication.PROFILES.ENCRYPT_DECRYPT)
@Component
@Slf4j
public class EdDataBufferWriter {

    public <T> Mono<Void> write(ServerHttpResponse httpResponse, String encryptString) {
        return httpResponse
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = httpResponse.bufferFactory();
                    try {
                        return bufferFactory.wrap(encryptString.getBytes(StandardCharsets.UTF_8));
                    } catch (Exception ex) {
                        log.warn("Error writing response", ex);
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }
}