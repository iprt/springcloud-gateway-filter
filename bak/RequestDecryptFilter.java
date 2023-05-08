package com.shdq.hfhz.gateway.config.filters.req;

import com.shdq.hfhz.gateway.GatewayApplication;
import com.shdq.hfhz.gateway.config.filters.BizGlobalFilter;
import com.shdq.hfhz.gateway.config.filters.predicate.FilterPredicateChain;
import com.shdq.hfhz.gateway.utils.EncryptDecrypt;
import lombok.AllArgsConstructor;
import org.bouncycastle.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * decrypt request body
 *
 * @author zhuzhenjie
 * @since 4/24/2023
 */
@Profile(GatewayApplication.PROFILES.ENCRYPT_DECRYPT)
@AllArgsConstructor
// @Component
public class RequestDecryptFilter implements BizGlobalFilter {

    private final EncryptDecrypt encryptDecrypt;

    private final FilterPredicateChain encryptDecryptFilterPredicateChain;

    @Override
    public int getOrder() {
        return -10;
    }

    @Override
    public Strategy trigger() {
        return Strategy.CLIENT_TO_REQUEST;
    }

    @Override
    public boolean filterPredicate(ServerWebExchange exchange) {
        return encryptDecryptFilterPredicateChain.useBizFilter(exchange);
    }

    @Override
    public Mono<Void> bizFilter(ServerWebExchange exchange, GatewayFilterChain chain) {
        DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
        DefaultDataBuffer defaultDataBuffer = defaultDataBufferFactory.allocateBuffer(0);

        Flux<DataBuffer> body = exchange.getRequest().getBody().defaultIfEmpty(defaultDataBuffer);

        return DataBufferUtils.join(body)
                .flatMap(
                        dataBuffer -> {
                            ServerHttpRequest mutateHttpRequest = mutateServerHttpRequest(exchange, dataBuffer);
                            return chain.filter(exchange.mutate().request(mutateHttpRequest).build());
                        }
                );
    }


    private ServerHttpRequest mutateServerHttpRequest(ServerWebExchange exchange, DataBuffer dataBuffer) {
        DataBufferUtils.retain(dataBuffer);

        Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));

        String body = toRaw(cachedFlux);

        String decryptedBody = encryptDecrypt.decrypt(body);
        byte[] decryptedBodyBytes = decryptedBody.getBytes(StandardCharsets.UTF_8);

        return new ServerHttpRequestDecorator(exchange.getRequest()) {

            /**
             * set new header
             *
             * @return the headers
             */
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(exchange.getRequest().getHeaders());

                if (decryptedBodyBytes.length > 0) {
                    httpHeaders.setContentLength(decryptedBodyBytes.length);
                }

                httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
                return httpHeaders;
            }

            /**
             * new body
             *
             * @return the body
             */
            @Override
            public Flux<DataBuffer> getBody() {
                return Flux.just(body)
                        .map(s -> new DefaultDataBufferFactory().wrap(decryptedBodyBytes));
            }
        };
    }

    static String toRaw(Flux<DataBuffer> body) {
        AtomicReference<String> rawRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            rawRef.set(Strings.fromUTF8ByteArray(bytes));
        });
        return rawRef.get();
    }
}
