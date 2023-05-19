package org.iproute.biz.gateway.filters.predicate.rewriterScope.components.encryptdecrypt;

import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.filters.predicate.BizPredicate;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * EdRespContentTypeRewritePredicate
 *
 * @author zhuzhenjie
 * @since 5/8/2023
 */
@Component
public class EdRespContentTypeRewritePredicate implements BizPredicate {
    /**
     * response的 content-type 如果是 application/json 或者 text/plain的话，需要解密
     *
     * @param exchange the exchange
     * @return the boolean
     */
    @Override
    public boolean useBiz(ServerWebExchange exchange) {
        boolean wrapped = (boolean) exchange.getAttributes()
                .get(BizGatewayApplication.TransitAttribute.WRAPPED);
        if (wrapped) {
            return true;
        }
        ServerHttpResponse response = exchange.getResponse();
        MediaType contentType = response.getHeaders().getContentType();

        // contentType == null : @GetMapping("/xxx") void()
        return contentType == null
                || MediaType.APPLICATION_JSON.includes(contentType)
                || MediaType.TEXT_PLAIN.includes(contentType);
    }
}
