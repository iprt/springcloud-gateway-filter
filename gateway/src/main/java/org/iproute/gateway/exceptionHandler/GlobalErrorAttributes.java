package org.iproute.gateway.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

/**
 * GlobalErrorAttributes
 *
 * @author zhuzhenjie
 */
@Component
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private static final int ERROR_CODE = 9999;
    private static final String ERROR_MESSAGE = "error";
    private static final String ERROR_CODE_KEY = "code";
    private static final String ERROR_MESSAGE_KEY = "msg";
    private static final String DEFAULT_RESULT = "default";

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> originalErrorAttributes = super.getErrorAttributes(request, options);
        log.info("original error attribute is {}", originalErrorAttributes);

        return Map.of(
                ERROR_CODE_KEY, ERROR_CODE,
                ERROR_MESSAGE_KEY, ERROR_MESSAGE,
                DEFAULT_RESULT, originalErrorAttributes
        );
    }
}
