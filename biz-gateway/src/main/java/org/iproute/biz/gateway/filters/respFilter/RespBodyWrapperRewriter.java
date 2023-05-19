package org.iproute.biz.gateway.filters.respFilter;

import lombok.extern.slf4j.Slf4j;
import org.iproute.biz.gateway.BizGatewayApplication;
import org.iproute.biz.gateway.filters.BizRewriteFunction;
import org.iproute.biz.gateway.filters.predicate.BizPredicate;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TmpRewriter
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
@Profile(BizGatewayApplication.PROFILES.WRAPPER)
@Component
@Slf4j
public class RespBodyWrapperRewriter implements BizRewriteFunction {

    /**
     * 定义正确的返回Code
     */
    private static final Integer SUCCESS_CODE = 200;

    /**
     * 定义错误的的返回Code
     */
    private static final Integer FAILURE_CODE = 9999;

    /**
     * 返回的json格式化模板
     */
    private final String RESULT_JSON_FORMAT = "{\"code\":%d,\"data\":%s}";

    private final byte[] EMPTY_SUCCESS = String.format(RESULT_JSON_FORMAT, SUCCESS_CODE, "null").getBytes(StandardCharsets.UTF_8);
    private final byte[] EMPTY_FAILURE = String.format(RESULT_JSON_FORMAT, FAILURE_CODE, "null").getBytes(StandardCharsets.UTF_8);

    /**
     * 数字判断的正则
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");


    private final BizPredicate wrapperRewriterPredicateChain;

    public RespBodyWrapperRewriter(@Qualifier("wrapperRewriterPredicateChain") BizPredicate wrapperRewriterPredicateChain) {
        this.wrapperRewriterPredicateChain = wrapperRewriterPredicateChain;
    }

    @Override
    public BizPredicate bizPredicate() {
        return this.wrapperRewriterPredicateChain;
    }


    @Override
    public boolean requireEmptyBytes() {
        return true;
    }

    @Override
    public Publisher<byte[]> bizApply(ServerWebExchange exchange, byte[] bytes) {
        ServerHttpResponse response = exchange.getResponse();
        this.fixHeader(response);
        return Mono.just(mutateServerHttpResponse(bytes, response));
    }

    /**
     * set content-type : application/json
     *
     * @param response the response
     */
    private void fixHeader(ServerHttpResponse response) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    }

    /**
     * wrapper
     *
     * @param originalBytes the original bytes
     * @param response      the response
     * @return the mono
     */
    private byte[] mutateServerHttpResponse(byte[] originalBytes, ServerHttpResponse response) {
        boolean isErrorStatus = Objects.requireNonNull(response.getStatusCode()).isError();

        if (originalBytes == null || originalBytes.length == 0) {
            // case1: httpCode == 200 && body == empty
            // case2: httpCode != 200 && body == empty
            // String bodyEmptyResponse = String.format(RESULT_JSON_FORMAT, isErrorStatus ? FAILURE_CODE : SUCCESS_CODE, StrUtil.NULL);
            // return bodyEmptyResponse.getBytes(StandardCharsets.UTF_8);
            return isErrorStatus ? EMPTY_FAILURE : EMPTY_SUCCESS;
        }

        // 错误是整体的约定 由controller层抛出异常统一处理
        if (isErrorStatus) {
            return originalBytes;
        } else {
            return this.wrapperResponseData(originalBytes,
                    response.getHeaders().getContentType());
        }
    }

    /**
     * 重新封装byte数组
     *
     * @param respBytes   the response bytes
     * @param contentType 对于 text/plain要进行特殊的处理
     * @return 封装后的byte数组 byte [ ]
     */
    private byte[] wrapperResponseData(byte[] respBytes, MediaType contentType) {
        String respData = new String(respBytes, StandardCharsets.UTF_8);
        // application/text
        if (MediaType.TEXT_PLAIN.includes(contentType) && !this.isNumber(respData)) {
            // 非数字
            respData = "\"" + respData + "\"";
        }
        // application/json 的 bug
        if (MediaType.APPLICATION_JSON.includes(contentType)
                && !this.quickIsJson(respData)) {
            respData = "\"" + respData + "\"";
        }
        return String.format(RESULT_JSON_FORMAT, SUCCESS_CODE, respData).getBytes(StandardCharsets.UTF_8);
    }


    /**
     * 简单快速判断是否是JSON
     *
     * @param str the str
     * @return boolean
     */
    private boolean quickIsJson(String str) {
        if (str.length() < 2) {
            return false;
        }
        return (str.startsWith("[") && str.endsWith("]"))
                || (str.startsWith("{") && str.endsWith("}"));
    }


    /**
     * 匹配是否为数字
     *
     * @param str bytes
     * @return boolean
     */
    private boolean isNumber(String str) {
        if (str.length() > 25) {
            return false;
        }
        // 该正则表达式可以匹配所有的数字 包括负数
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            // 异常 说明包含非数字。
            return false;
        }
        // matcher是全匹配
        Matcher isNum = NUMBER_PATTERN.matcher(bigStr);
        return isNum.matches();
    }

}