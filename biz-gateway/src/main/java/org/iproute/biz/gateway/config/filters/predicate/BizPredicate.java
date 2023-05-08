package org.iproute.biz.gateway.config.filters.predicate;

import org.springframework.web.server.ServerWebExchange;

/**
 * 业务上决定是否需要封装
 *
 * @author zhuzhenjie
 * @since 4/30/2023
 */
public interface BizPredicate {

    /**
     * 是否使用 业务定义的方法
     * <p>
     * true 使用
     * <p>
     * false 不使用
     *
     * @param exchange the exchange
     * @return the boolean
     */
    boolean useBiz(ServerWebExchange exchange);

}
