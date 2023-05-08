package org.iproute.biz.gateway.config.filters.predicate.rewriter.components;

import org.iproute.biz.gateway.config.filters.predicate.BizPredicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * RespWebSocketBizWrapper
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
@Component
public class RespWebSocketRewritePredicate implements BizPredicate {

    @Override
    public boolean useBiz(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        /*
        The handshake from the server looks as follows:
           HTTP/1.1 101 Switching Protocols
           Upgrade: websocket
           Connection: Upgrade
           Sec-WebSocket-Accept: s3pPLMBiTxaQ9kYGzzhZRbK+xOo=
           Sec-WebSocket-Protocol: chat
         */

        // 这里可能存在一个无用的判断 因为 HTTP 协议的code为101
        String webSocketAccept = response.getHeaders().getFirst("Sec-WebSocket-Accept");
        return StringUtils.isBlank(webSocketAccept);
    }
}

