package org.iproute.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * PrintResponseBodyFilter
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
@Component
@Slf4j
public class PrintResponseBodyFilter implements GlobalFilter, Ordered {

    private final GatewayFilter delegate;

    @Override
    public int getOrder() {
        return -100;
    }

    public PrintResponseBodyFilter(ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory,
                                   PrintRewriter printRewriter) {
        this.delegate = modifyResponseBodyGatewayFilterFactory.apply(
                new ModifyResponseBodyGatewayFilterFactory.Config()
                        .setInClass(byte[].class)
                        .setOutClass(byte[].class)
                        .setRewriteFunction(printRewriter)
        );
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("ResponseBodyFilter response body filter invoked ...");
        return delegate.filter(exchange, chain);
    }

}
