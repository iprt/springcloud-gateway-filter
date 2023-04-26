package org.iproute.gateway.exceptionHandler;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;


/**
 * ExceptionHandler
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
@Configuration
public class ExceptionHandler {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new ExtensionErrorAttributes(false);
    }


    /**
     * {@link DefaultErrorAttributes}
     */
    public static class ExtensionErrorAttributes implements ErrorAttributes {
        private static final String ERROR_ATTRIBUTE = ExtensionErrorAttributes.class.getName() + ".ERROR";

        private final Boolean includeException;

        public ExtensionErrorAttributes(Boolean includeException) {
            this.includeException = includeException;
        }

        /**
         * TODO
         *
         * @param request the request
         * @param options the options
         * @return the error attributes
         */
        @Override
        public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
            Map<String, Object> errorAttributes = getErrorAttributes(request, options.isIncluded(ErrorAttributeOptions.Include.STACK_TRACE));
            if (Boolean.TRUE.equals(this.includeException)) {
                options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
            }
            if (!options.isIncluded(ErrorAttributeOptions.Include.EXCEPTION)) {
                errorAttributes.remove("exception");
            }
            if (!options.isIncluded(ErrorAttributeOptions.Include.STACK_TRACE)) {
                errorAttributes.remove("trace");
            }
            if (!options.isIncluded(ErrorAttributeOptions.Include.MESSAGE) && errorAttributes.get("message") != null) {
                errorAttributes.put("message", "");
            }
            if (!options.isIncluded(ErrorAttributeOptions.Include.BINDING_ERRORS)) {
                errorAttributes.remove("errors");
            }
            return errorAttributes;
        }

        @Override
        public Throwable getError(ServerRequest request) {
            return (Throwable) request.attribute(ERROR_ATTRIBUTE)
                    .orElseThrow(() -> new IllegalStateException("Missing exception attribute in ServerWebExchange"));
        }

        @Override
        public void storeErrorInformation(Throwable error, ServerWebExchange exchange) {
            exchange.getAttributes().putIfAbsent(ERROR_ATTRIBUTE, error);
        }
    }

}
