package org.iproute.biz.gateway.config.filters.req;

import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.config.filters.BizGlobalFilter;
import org.iproute.biz.gateway.config.filters.predicate.BizPredicate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * ReqJsonBodyDecryptFilter
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
@Profile(BizGatewayApplication.PROFILES.ENCRYPT_DECRYPT)
@Component
public class ReqJsonBodyDecryptFilter implements BizGlobalFilter {
    private final BizPredicate jsonContentTypeFilterPredicateChain;
    private final GatewayFilter delegate;

    public ReqJsonBodyDecryptFilter(@Qualifier("jsonContentTypeFilterPredicateChain") BizPredicate jsonContentTypeFilterPredicateChain,
                                    ModifyRequestBodyGatewayFilterFactory modifyRequestBodyGatewayFilterFactory,
                                    ReqJsonBodyDecryptRewriter reqJsonBodyDecryptRewriter) {
        this.jsonContentTypeFilterPredicateChain = jsonContentTypeFilterPredicateChain;
        // if you need decrypt ,must convert to application/json
        this.delegate = modifyRequestBodyGatewayFilterFactory.apply(
                new ModifyRequestBodyGatewayFilterFactory.Config()
                        .setInClass(byte[].class)
                        .setOutClass(byte[].class)
                        .setContentType(MediaType.APPLICATION_JSON.toString())
                        .setRewriteFunction(reqJsonBodyDecryptRewriter)
        );
    }

    @Override
    public int getOrder() {
        return -11;
    }

    @Override
    public BizPredicate bizPredicate() {
        return this.jsonContentTypeFilterPredicateChain;
    }

    @Override
    public Mono<Void> bizFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return this.delegate.filter(exchange, chain);
    }

}
