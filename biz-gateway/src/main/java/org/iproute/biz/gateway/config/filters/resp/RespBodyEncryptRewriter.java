package org.iproute.biz.gateway.config.filters.resp;

import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.config.filters.BizRewriteFunction;
import org.iproute.biz.gateway.config.filters.predicate.BizPredicate;
import org.iproute.biz.gateway.utils.EncryptDecrypt;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * TmpRewriter
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
@Profile(BizGatewayApplication.PROFILES.ENCRYPT_DECRYPT)
@Component
@Slf4j
public class RespBodyEncryptRewriter implements BizRewriteFunction {
    private final BizPredicate respEncryptDecryptRewriterPredicateChain;
    private final EncryptDecrypt encryptDecrypt;

    public RespBodyEncryptRewriter(BizPredicate respEncryptDecryptRewriterPredicateChain, EncryptDecrypt encryptDecrypt) {
        this.respEncryptDecryptRewriterPredicateChain = respEncryptDecryptRewriterPredicateChain;
        this.encryptDecrypt = encryptDecrypt;
    }

    @Override
    public boolean requireEmptyBytes() {
        return true;
    }

    @Override
    public Publisher<byte[]> bizApply(ServerWebExchange exchange, byte[] bytes) {
        // fix header
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);

        if (bytes == null || bytes.length == 0) {
            return Mono.empty();
        }
        return Mono.just(encryptDecrypt.encrypt(new String(bytes)).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public BizPredicate bizPredicate() {
        return this.respEncryptDecryptRewriterPredicateChain;
    }
}
