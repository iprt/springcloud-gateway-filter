package org.iproute.biz.gateway.exception.bizjson;

import org.iproute.biz.gateway.BizGatewayApplication;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ExceptionCondition
 *
 * @author zhuzhenjie
 * @since 5/8/2023
 */
public class ExceptionCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext,
                           AnnotatedTypeMetadata annotatedTypeMetadata) {
        String[] activeProfiles = conditionContext.getEnvironment().getActiveProfiles();
        Set<String> ps = Stream.of(activeProfiles).collect(Collectors.toSet());

        return !ps.contains(BizGatewayApplication.PROFILES.ENCRYPT_DECRYPT);
    }
}
