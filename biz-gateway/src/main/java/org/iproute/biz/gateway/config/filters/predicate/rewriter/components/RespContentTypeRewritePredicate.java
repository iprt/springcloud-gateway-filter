package org.iproute.biz.gateway.config.filters.predicate.rewriter.components;

import org.iproute.biz.gateway.config.filters.predicate.BizPredicate;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;

/**
 * RespContentTypeRewritePredicate
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
@Component
public class RespContentTypeRewritePredicate implements BizPredicate {

    @Override
    public boolean useBiz(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        // 判断 Content-Type 是否需要包装的
        MediaType contentType = Objects.isNull(response.getHeaders().getContentType())
                ? MediaType.TEXT_PLAIN
                : response.getHeaders().getContentType();
        return MediaType.APPLICATION_JSON.includes(contentType)
                || MediaType.TEXT_PLAIN.includes(contentType);
    }
}
