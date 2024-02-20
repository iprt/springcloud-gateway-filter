package org.iproute.gateway.exceptionHandler;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
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


        private Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
            Map<String, Object> errorAttributes = new LinkedHashMap<>();
            errorAttributes.put("timestamp", new Date());
            errorAttributes.put("path", request.path());
            Throwable error = this.getError(request);
            MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations.from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus.class);
            HttpStatus errorStatus = this.determineHttpStatus(error, responseStatusAnnotation);
            errorAttributes.put("status", errorStatus.value());
            errorAttributes.put("error", errorStatus.getReasonPhrase());
            errorAttributes.put("message", this.determineMessage(error, responseStatusAnnotation));
            errorAttributes.put("requestId", request.exchange().getRequest().getId());
            this.handleException(errorAttributes, this.determineException(error), includeStackTrace);
            return errorAttributes;
        }


        private HttpStatus determineHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
            return error instanceof ResponseStatusException ? ((ResponseStatusException) error).getStatus() : (HttpStatus) responseStatusAnnotation.getValue("code", HttpStatus.class).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        private String determineMessage(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
            if (error instanceof BindingResult) {
                return error.getMessage();
            } else if (error instanceof ResponseStatusException) {
                return ((ResponseStatusException) error).getReason();
            } else {
                String reason = responseStatusAnnotation.getValue("reason", String.class).orElse("");
                if (StringUtils.hasText(reason)) {
                    return reason;
                } else {
                    return error.getMessage() != null ? error.getMessage() : "";
                }
            }
        }

        private Throwable determineException(Throwable error) {
            if (error instanceof ResponseStatusException) {
                return error.getCause() != null ? error.getCause() : error;
            } else {
                return error;
            }
        }

        private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
            StringWriter stackTrace = new StringWriter();
            error.printStackTrace(new PrintWriter(stackTrace));
            stackTrace.flush();
            errorAttributes.put("trace", stackTrace.toString());
        }

        private void handleException(Map<String, Object> errorAttributes, Throwable error, boolean includeStackTrace) {
            errorAttributes.put("exception", error.getClass().getName());
            if (includeStackTrace) {
                this.addStackTrace(errorAttributes, error);
            }

            if (error instanceof BindingResult result) {
                if (result.hasErrors()) {
                    errorAttributes.put("errors", result.getAllErrors());
                }
            }

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
