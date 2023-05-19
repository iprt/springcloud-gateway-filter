package org.iproute.biz.gateway.filters.predicate.filterScope.components.reqresp;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.iproute.biz.gateway.filters.predicate.BizPredicate;
import org.iproute.biz.gateway.filters.predicate.filterScope.properties.EncryptDecryptProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * ReqUriFilterPredicate
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
@Component
@Slf4j
public class ReqUriFilterPredicate implements BizPredicate, CommandLineRunner {
    private final Set<String> URI_SET = Sets.newHashSet();

    @Autowired
    private EncryptDecryptProperties encryptDecryptProperties;

    @Override
    public boolean useBiz(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String uri = uri(request);
        // return URI_SET.contains(uri);
        return !URI_SET.contains(uri);
    }

    private String uri(ServerHttpRequest request) {
        return request.getURI().getPath();
    }


    @Override
    public void run(String... args) throws Exception {
        this.init();
    }

    private void init() {
        if (URI_SET.size() == 0) {
            log.info("init ignored uri set");
            encryptDecryptProperties.getIgnore().stream()
                    .filter(serviceUri -> StringUtils.isNoneBlank(serviceUri.getService()))
                    .filter(serviceUri -> !CollectionUtils.isEmpty(serviceUri.getUri()))
                    .map(serviceUri ->
                            serviceUri.getUri().stream()
                                    .map(uri -> fixSlash(serviceUri.getService()) + fixSlash(uri))
                                    .peek(uriPath -> log.info("request uri filter predicate uri path ==> {}", uriPath))
                                    .collect(Collectors.toList())
                    )
                    .forEach(URI_SET::addAll);
        }
    }

    private String fixSlash(String str) {
        return str.startsWith("/") ? str : "/" + str;
    }
}
