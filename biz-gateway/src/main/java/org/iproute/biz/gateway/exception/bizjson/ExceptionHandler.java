package org.iproute.biz.gateway.exception.bizjson;


import lombok.extern.slf4j.Slf4j;
import org.iproute.biz.gateway.exception.GatewayFail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * ExceptionHandler 网关异常通用处理器，只作用在webflux 环境下 , 优先级低于 {@link ResponseStatusExceptionHandler} 执行
 *
 * @author zhuzhenjie
 * @since 2020/10/27
 */
@Conditional(ExceptionCondition.class)
@Component
@Slf4j
public class ExceptionHandler implements ErrorWebExceptionHandler, Ordered {
    /**
     * 网关找不到的异常
     */
    public static final Integer BASIC_FAIL_GENERAL_CODE = 9999;

    @Autowired
    private DataBufferWriter bufferWriter;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error(ex.getMessage(), ex);
        ServerHttpResponse response = exchange.getResponse();
        GatewayFail fail = GatewayFail.builder().build();

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            int code = responseStatusException.getStatus().value();
            fail.setCode(code);
            fail.setMsg(responseStatusException.getMessage());
        } else {
            fail.setCode(BASIC_FAIL_GENERAL_CODE);
            fail.setMsg(ex.getMessage());
        }

        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        response.setStatusCode(HttpStatus.OK);

        return bufferWriter.write(exchange.getResponse(), fail);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
