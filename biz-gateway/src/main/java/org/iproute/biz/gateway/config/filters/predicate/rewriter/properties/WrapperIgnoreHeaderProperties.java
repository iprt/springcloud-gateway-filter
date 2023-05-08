package org.iproute.biz.gateway.config.filters.predicate.rewriter.properties;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * WrapperIgnoreHeader
 *
 * @author zhuzhenjie
 * @since 5/3/2023
 */
@ConfigurationProperties(prefix = "spring.cloud.gateway.biz-external.wrapper", ignoreInvalidFields = true)
@Component
@Slf4j
public class WrapperIgnoreHeaderProperties {

    private List<String> ignore = Lists.newArrayList();

    public List<String> getIgnore() {
        return ignore;
    }

    public void setIgnore(List<String> ignore) {
        this.ignore = ignore;
    }
}
