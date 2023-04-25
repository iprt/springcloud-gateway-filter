package org.iproute.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * ResponseBodyUpperFilter
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
@Component
@Slf4j
public class ResponseBodyUpperFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return -50;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange.mutate().response(
                new ServerHttpResponseDecorator(exchange.getResponse()) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            log.info("other global filter do something ...");
                            Flux<? extends DataBuffer> flux = (Flux<? extends DataBuffer>) body;

                            return super.writeWith(
                                    flux.buffer().defaultIfEmpty(Collections.emptyList()).map(
                                            dataBuffers -> {
                                                DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
                                                DataBuffer joinedBuffers = bufferFactory.join(dataBuffers);
                                                if (joinedBuffers.readableByteCount() == 0) {
                                                    return joinedBuffers;
                                                }

                                                byte[] content = new byte[joinedBuffers.readableByteCount()];
                                                joinedBuffers.read(content);
                                                // https://gist.github.com/WeirdBob/b25569d461f0f54444d2c0eab51f3c48
                                                DataBufferUtils.release(joinedBuffers);

                                                byte[] upperContent = new String(content, StandardCharsets.UTF_8).toUpperCase().getBytes();
                                                return exchange.getResponse().bufferFactory().wrap(upperContent);
                                            }
                                    )
                            );

                        }
                        return super.writeWith(body);
                    }
                }
        ).build());
    }
}
