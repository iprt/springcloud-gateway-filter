package org.iproute.biz.gateway.config.filters.req;

import com.alibaba.fastjson.JSONObject;
import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.config.filters.BizRewriteFunction;
import org.iproute.biz.gateway.config.filters.predicate.filter.properties.EncryptDecryptProperties;
import org.iproute.biz.gateway.utils.EncryptDecrypt;
import lombok.AllArgsConstructor;
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
public class ReqJsonBodyDecryptRewriter implements BizRewriteFunction {

    private final EncryptDecryptProperties encryptDecryptProperties;
    private final EncryptDecrypt encryptDecrypt;

    @Override
    public Publisher<byte[]> bizApply(ServerWebExchange exchange, byte[] bytes) {
        String jsonStr = new String(bytes, StandardCharsets.UTF_8);
        String encrypt = JSONObject.parseObject(jsonStr).getString(encryptDecryptProperties.getAppJsonKey());
        String decrypt = encryptDecrypt.decrypt(encrypt);
        byte[] decryptBytes = decrypt.getBytes(StandardCharsets.UTF_8);
        return Mono.just(decryptBytes);
    }

}
