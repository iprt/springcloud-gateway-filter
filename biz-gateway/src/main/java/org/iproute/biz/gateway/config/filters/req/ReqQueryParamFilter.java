package org.iproute.biz.gateway.config.filters.req;

import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.config.filters.BizGlobalFilter;
import org.iproute.biz.gateway.config.filters.predicate.BizPredicate;
import org.iproute.biz.gateway.config.filters.predicate.filter.properties.EncryptDecryptProperties;
import org.iproute.biz.gateway.utils.EncryptDecrypt;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * ReqQueryParamFilter
 *
 * @author zhuzhenjie
 * @since 5/8/2023
 */
@Profile(BizGatewayApplication.PROFILES.ENCRYPT_DECRYPT)
@AllArgsConstructor
@Component
public class ReqQueryParamFilter implements BizGlobalFilter {

    private final BizPredicate reqQueryParamEncryptDecryptFilterPredicateChain;
    private final EncryptDecryptProperties encryptDecryptProperties;
    private final EncryptDecrypt encryptDecrypt;

    @Override
    public Mono<Void> bizFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange.mutate()
                .request(mutateServerHttpRequest(exchange.getRequest()))
                .build());
    }

    @Override
    public int getOrder() {
        return -5;
    }

    private ServerHttpRequest mutateServerHttpRequest(ServerHttpRequest request) {

        return new ServerHttpRequestDecorator(request) {
            @Override
            public URI getURI() {
                return UriComponentsBuilder.fromHttpRequest(getDelegate())
                        .replaceQuery(decryptQuery())
                        .build().toUri();
            }

            private String decryptQuery() {
                MultiValueMap<String, String> queryParams = this.getQueryParams();
                String encrypt = queryParams.getFirst(encryptDecryptProperties.getQueryParam());
                return encryptDecrypt.decrypt(encrypt);
            }
        };
    }

    @Override
    public BizPredicate bizPredicate() {
        return this.reqQueryParamEncryptDecryptFilterPredicateChain;
    }
}
