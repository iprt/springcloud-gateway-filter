package org.iproute.biz.gateway.filters.predicate.rewriterScope.components.wrapper;

import org.iproute.biz.gateway.filters.predicate.BizPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * RespHttpCodeRewritePredicate
 * <p>
 * 判断response http code 是否需要封装
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
@Component
public class RespHttpCodeRewritePredicate implements BizPredicate {

    @Override
    public boolean useBiz(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        HttpStatus status = response.getStatusCode();
        if (status == null) {
            return false;
        }
        return status.isError() || status.value() == HttpStatus.OK.value();
    }

}
