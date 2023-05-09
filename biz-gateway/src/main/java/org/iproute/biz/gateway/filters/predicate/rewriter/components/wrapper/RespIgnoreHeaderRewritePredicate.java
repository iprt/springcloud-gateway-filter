package org.iproute.biz.gateway.filters.predicate.rewriter.components.wrapper;

import lombok.AllArgsConstructor;
import org.iproute.biz.gateway.filters.predicate.BizPredicate;
import org.iproute.biz.gateway.filters.predicate.rewriter.properties.WrapperIgnoreHeaderProperties;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * RespIgnoreHeaderRewritePredicate
 *
 * @author zhuzhenjie
 * @since 5 /1/2023
 */
@AllArgsConstructor
@Component
public class RespIgnoreHeaderRewritePredicate implements BizPredicate {
    private final WrapperIgnoreHeaderProperties wrapperIgnoreHeaderProperties;

    @Override
    public boolean useBiz(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        // 如果存在 header 使用默认filter,如果不存在header, 切换业务filter
        // return !response.getHeaders().containsKey(IGNORE_HEADER);

        List<String> ignore = wrapperIgnoreHeaderProperties.getIgnore();
        for (String i : ignore) {
            if (response.getHeaders().containsKey(i)) {
                return false;
            }
        }
        return true;
    }
}
