package org.iproute.biz.gateway.filters.req;

import lombok.AllArgsConstructor;
import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.filters.BizRewriteFunction;
import org.iproute.biz.gateway.utils.EncryptDecrypt;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * ReqBodyDecryptRewriter
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
@Profile(BizGatewayApplication.PROFILES.ENCRYPT_DECRYPT)
@AllArgsConstructor
@Component
public class ReqBodyDecryptRewriter implements BizRewriteFunction {

    private final EncryptDecrypt encryptDecrypt;

    @Override
    public Publisher<byte[]> bizApply(ServerWebExchange exchange, byte[] bytes) {
        String param = new String(bytes, StandardCharsets.UTF_8);
        String decrypt = encryptDecrypt.decrypt(param);
        byte[] decryptBytes = decrypt.getBytes(StandardCharsets.UTF_8);
        return Mono.just(decryptBytes);
    }

}
