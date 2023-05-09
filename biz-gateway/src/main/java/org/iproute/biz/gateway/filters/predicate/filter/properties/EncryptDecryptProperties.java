package org.iproute.biz.gateway.filters.predicate.filter.properties;

import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * EncryptDecryptServiceUriProperties
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
@ConfigurationProperties(prefix = "spring.cloud.gateway.biz-external.encrypt-decrypt-filter", ignoreInvalidFields = true)
@Component
public class EncryptDecryptProperties {
    private String queryParam;
    private String appJsonKey;
    private List<ServiceUri> ignore = Lists.newArrayList();

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    public String getAppJsonKey() {
        return appJsonKey;
    }

    public void setAppJsonKey(String appJsonKey) {
        this.appJsonKey = appJsonKey;
    }

    public List<ServiceUri> getIgnore() {
        return ignore;
    }

    public void setIgnore(List<ServiceUri> ignore) {
        this.ignore = ignore;
    }
}
