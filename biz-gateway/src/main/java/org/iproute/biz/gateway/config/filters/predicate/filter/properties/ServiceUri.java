package org.iproute.biz.gateway.config.filters.predicate.filter.properties;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * ServiceUri
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
public class ServiceUri {
    private String service = "";
    private List<String> uri = Lists.newArrayList();

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<String> getUri() {
        return uri;
    }

    public void setUri(List<String> uri) {
        this.uri = uri;
    }
}
