package org.iproute.biz.gateway.filters.predicate.filter.components.queryparam;

import lombok.AllArgsConstructor;
import org.iproute.biz.gateway.filters.predicate.BizPredicate;
import org.iproute.biz.gateway.filters.predicate.filter.properties.EncryptDecryptProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

/**
 * ReqQueryParamFilterPredicate
 *
 * @author zhuzhenjie
 * @since 5/8/2023
 */
@AllArgsConstructor
@Component
public class ReqQueryParamFilterPredicate implements BizPredicate {

    private final EncryptDecryptProperties encryptDecryptProperties;

    @Override
    public boolean useBiz(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String key = encryptDecryptProperties.getQueryParam();
        // 存在key key存在值
        return queryParams.containsKey(key) &&
                !CollectionUtils.isEmpty(queryParams.get(key));
    }
}
