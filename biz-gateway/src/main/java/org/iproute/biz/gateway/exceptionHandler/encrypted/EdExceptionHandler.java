package org.iproute.biz.gateway.exceptionHandler.encrypted;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.exceptionHandler.GatewayFail;
import org.iproute.biz.gateway.utils.EncryptDecrypt;
import org.iproute.biz.gateway.utils.HostUtils;
import org.iproute.biz.gateway.utils.HttpWebUtils;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * EdExceptionHandler 网关异常通用处理器，只作用在webflux 环境下 , 优先级低于 {@link ResponseStatusExceptionHandler} 执行
 *
 * @author zhuzhenjie
 * @since 2020/10/27
 */
@Profile(BizGatewayApplication.PROFILES.ENCRYPT_DECRYPT)
@Component
@Slf4j
public class EdExceptionHandler implements ErrorWebExceptionHandler, Ordered {
    /**
     * 网关找不到的异常
     */
    public static final Integer BASIC_FAIL_GENERAL_CODE = 9999;

    @Resource
    private EdDataBufferWriter edDataBufferWriter;

    @Resource
    private EncryptDecrypt encryptDecrypt;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        log.error("method = {} | uri.host = {} | uri.path = {} | error.message = {}",
                HttpWebUtils.getMethod(exchange),
                HttpWebUtils.getUriHost(exchange),
                HttpWebUtils.getUriPath(exchange),
                ex.getMessage()
        );

        ServerHttpResponse response = exchange.getResponse();
        GatewayFail fail = GatewayFail.builder().build();

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);

        if (ex instanceof ResponseStatusException responseStatusException) {
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

        fail.setHost(HostUtils.hostname());

        String jsonString = JSONObject.toJSONString(fail);

        return edDataBufferWriter.write(exchange.getResponse(), encryptDecrypt.encrypt(jsonString));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
