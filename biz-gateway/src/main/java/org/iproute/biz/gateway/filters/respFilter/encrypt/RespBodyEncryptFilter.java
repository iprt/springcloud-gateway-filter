package org.iproute.biz.gateway.filters.respFilter.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.filters.BizGlobalFilter;
import org.iproute.biz.gateway.filters.predicate.BizPredicate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * TmpFilter
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
@Profile(BizGatewayApplication.PROFILES.ENCRYPT_DECRYPT)
@Component
@Slf4j
public class RespBodyEncryptFilter implements BizGlobalFilter {

    private final BizPredicate encryptDecryptFilterPredicateChain;
    private final GatewayFilter delegate;

    public RespBodyEncryptFilter(@Qualifier("encryptDecryptFilterPredicateChain") BizPredicate encryptDecryptFilterPredicateChain,
                                 ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory,
                                 RespBodyEncryptRewriter respBodyEncryptRewriter) {
        this.encryptDecryptFilterPredicateChain = encryptDecryptFilterPredicateChain;

        this.delegate = modifyResponseBodyGatewayFilterFactory.apply(
                new ModifyResponseBodyGatewayFilterFactory.Config()
                        .setInClass(byte[].class)
                        .setOutClass(byte[].class)
                        // todo: not valid ???
                        // .setNewContentType(MediaType.TEXT_PLAIN_VALUE)
                        .setRewriteFunction(respBodyEncryptRewriter)
        );
    }

    @Override
    public BizPredicate bizPredicate() {
        return this.encryptDecryptFilterPredicateChain;
    }

    @Override
    public int getOrder() {
        return -40;
    }


    @Override
    public Mono<Void> bizFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return delegate.filter(exchange, chain);
    }

}
