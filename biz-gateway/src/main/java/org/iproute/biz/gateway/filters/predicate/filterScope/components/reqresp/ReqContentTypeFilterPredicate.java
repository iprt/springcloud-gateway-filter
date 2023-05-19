package org.iproute.biz.gateway.filters.predicate.filterScope.components.reqresp;

import org.iproute.biz.gateway.filters.predicate.BizPredicate;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * ReqContentTypePredicate
 *
 * @author zhuzhenjie
 * @since 5/8/2023
 */
@Component
public class ReqContentTypeFilterPredicate implements BizPredicate {

    @Override
    public boolean useBiz(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        MediaType contentType = request.getHeaders().getContentType();
        // 如果是application/json 或者 text/plain 就使用业务的自定的filter
        return MediaType.TEXT_PLAIN.includes(contentType)
                || MediaType.APPLICATION_JSON.includes(contentType);
    }
}
