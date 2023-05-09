package org.iproute.biz.gateway.filters.predicate.filter.components.reqresp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.iproute.biz.gateway.filters.predicate.BizPredicate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * ReqWebSocketFilterPredicate
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
@Component
@Slf4j
public class ReqWebSocketFilterPredicate implements BizPredicate {

    private static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";

    @Override
    public boolean useBiz(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String ws = request.getHeaders().getFirst(SEC_WEBSOCKET_KEY);
        if (StringUtils.isBlank(ws)) {
            return true;
        } else {
            log.info("websocket handshake");
            return false;
        }
    }
}
