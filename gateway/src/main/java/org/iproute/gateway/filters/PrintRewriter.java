package org.iproute.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * PrintRewriter
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
@Component
@Slf4j
public class PrintRewriter implements RewriteFunction<byte[], byte[]> {

    @Override
    public Publisher<byte[]> apply(ServerWebExchange exchange, byte[] bytes) {

        printRequest(exchange.getRequest());

        if (!ServerWebExchangeUtils.isAlreadyRouted(exchange)) {
            log.info("request ....");
        } else {
            log.info("response ....");
        }
        return Mono.just(bytes);
    }

    private void printRequest(ServerHttpRequest request) {

        log.info("request id = {}", request.getId());
        log.info("request method {}", request.getMethod());
        log.info("request path = {}", request.getPath());

    }

}
