package org.iproute.biz.gateway.config.filters.resp;

import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.config.filters.BizGlobalFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * TmpFilter
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
@Profile(BizGatewayApplication.PROFILES.WRAPPER)
@Component
@Slf4j
public class RespBodyWrapperFilter implements BizGlobalFilter {

    private final GatewayFilter delegate;

    public RespBodyWrapperFilter(ModifyResponseBodyGatewayFilterFactory modifyResponseBodyGatewayFilterFactory,
                                 RespBodyWrapperRewriter respBodyWrapperRewriter) {
        this.delegate = modifyResponseBodyGatewayFilterFactory.apply(
                new ModifyResponseBodyGatewayFilterFactory.Config()
                        .setInClass(byte[].class)
                        .setOutClass(byte[].class)
                        .setNewContentType(MediaType.APPLICATION_JSON.toString())
                        .setRewriteFunction(respBodyWrapperRewriter)

        );
    }

    @Override
    public int getOrder() {
        return -20;
    }

    @Override
    public Mono<Void> bizFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return delegate.filter(exchange, chain);
    }


}
