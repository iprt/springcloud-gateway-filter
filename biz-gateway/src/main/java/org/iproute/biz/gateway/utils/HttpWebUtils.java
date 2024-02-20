package org.iproute.biz.gateway.utils;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;

/**
 * HttpUtils
 *
 * @author zhuzhenjie
 */
public class HttpWebUtils {
    private HttpWebUtils() {
    }

    public static String getUri(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        return request.getURI().toString();
    }

    /**
     * Retrieves the path from the URI of the provided ServerWebExchange.
     *
     * @param exchange the server web exchange
     * @return the path from the URI of the request
     */
    public static String getUriPath(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        return request.getURI().getPath();
    }

    /**
     * Retrieves the host from the URI of the provided ServerWebExchange.
     *
     * @param exchange the server web exchange
     * @return the host from the URI of the request
     */
    public static String getUriHost(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        return request.getURI().getHost();
    }

    /**
     * Retrieves the HTTP method of the given server web exchange.
     *
     * @param exchange the server web exchange
     * @return the HTTP method as a string, or "unknown method" if the method is null
     */
    public static String getMethod(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpMethod method = request.getMethod();
        return Objects.nonNull(method) ? method.toString() : "unknown method";
    }
}
