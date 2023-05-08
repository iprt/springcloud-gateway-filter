package org.iproute.biz.gateway.config.filters;

import org.iproute.biz.gateway.config.filters.predicate.BizPredicate;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * BizGlobalFilter
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
public interface BizGlobalFilter extends GlobalFilter, Ordered {

    default BizPredicate bizPredicate() {
        return null;
    }

    /**
     * 是否选择默认的的filter
     * <p>
     * true: 自定义
     * <p>
     * false: 默认
     * <p>
     * 默认使用自定义
     *
     * @param exchange the exchange
     * @return the boolean
     */
    default boolean useBiz(ServerWebExchange exchange) {
        if (bizPredicate() == null) {
            return true;
        }
        return bizPredicate().useBiz(exchange);
    }

    /**
     * 业务自定义的filter
     *
     * @param exchange the exchange
     * @param chain    the chain
     * @return the supplier
     */
    Mono<Void> bizFilter(ServerWebExchange exchange, GatewayFilterChain chain);

    @Override
    default Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (useBiz(exchange)) {
            return bizFilter(exchange, chain);
        }
        return chain.filter(exchange);
    }

}
