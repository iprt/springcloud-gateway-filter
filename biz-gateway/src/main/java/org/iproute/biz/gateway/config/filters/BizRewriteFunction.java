package org.iproute.biz.gateway.config.filters;

import org.iproute.biz.gateway.config.filters.predicate.BizPredicate;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * BizRewriteFunction
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
public interface BizRewriteFunction extends RewriteFunction<byte[], byte[]> {

    default BizPredicate bizPredicate() {
        return null;
    }

    /**
     * 默认使用业务
     *
     * @return the boolean
     */
    default boolean useBiz(ServerWebExchange exchange) {
        if (bizPredicate() == null) {
            return true;
        }
        return bizPredicate().useBiz(exchange);
    }


    /**
     * biz apply publisher.
     *
     * @param exchange the exchange
     * @param bytes    the bytes
     * @return the publisher
     */
    Publisher<byte[]> bizApply(ServerWebExchange exchange, byte[] bytes);

    default boolean requireEmptyBytes() {
        return false;
    }

    @Override
    default Publisher<byte[]> apply(ServerWebExchange exchange, byte[] bytes) {
        if (!requireEmptyBytes() && (bytes == null || bytes.length == 0)) {
            return Mono.empty();
        }

        if (useBiz(exchange)) {
            return bizApply(exchange, bytes);
        }
        // do noting
        return Mono.just(bytes);
    }
}
