package org.iproute.biz.gateway.filters.reqFilter.header;

import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.filters.BizGlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * ReqRequireContentTypeFilter
 *
 * @author zhuzhenjie
 * @since 5/8/2023
 */
@Profile(BizGatewayApplication.PROFILES.ENCRYPT_DECRYPT)
// @Component
public class ReqRequireContentTypeFilter implements BizGlobalFilter {

    @Override
    public Mono<Void> bizFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        MediaType contentType = request.getHeaders().getContentType();
        if (contentType == null) {
            return Mono.error(new RuntimeException("lost Content-Type"));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -3;
    }
}
