package org.iproute.biz.gateway.filters.predicate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * BizWrapperConfig
 *
 * @author zhuzhenjie
 * @since 5/1/2023
 */
@Configuration
@Slf4j
public class BizPredicateConfig {
    /**
     * req no.1
     */
    @Resource
    private BizPredicate reqContentTypeFilterPredicate;


    /**
     * req no.2
     */
    @Resource
    private BizPredicate reqWebSocketFilterPredicate;

    /**
     * req no.3
     */
    @Resource
    private BizPredicate reqUriFilterPredicate;

    @Bean(name = "encryptDecryptFilterPredicateChain")
    public BizPredicate encryptDecryptFilterPredicateChain() {
        log.info("Class :: {}", reqContentTypeFilterPredicate.getClass());
        log.info("Class : {}", reqUriFilterPredicate.getClass());
        log.info("Class : {}", reqWebSocketFilterPredicate.getClass());

        return BizPredicateChain.create(BizPredicateChain.Strategy.AND)
                .addFilterPredicate(reqContentTypeFilterPredicate)
                .addFilterPredicate(reqUriFilterPredicate)
                .addFilterPredicate(reqWebSocketFilterPredicate);
    }

    /**
     * resp no.1
     */
    @Resource
    private BizPredicate respHttpCodeRewritePredicate;

    /**
     * resp no.2
     */
    @Resource
    private BizPredicate respIgnoreHeaderRewritePredicate;

    /**
     * resp no.3
     */
    @Resource
    private BizPredicate respContentTypeRewritePredicate;

    /**
     * resp no.4
     */
    @Resource
    private BizPredicate respWebSocketRewritePredicate;

    @Bean(name = "wrapperRewriterPredicateChain")
    public BizPredicate wrapperRewriterPredicateChain() {
        log.info("Class : {}", respHttpCodeRewritePredicate.getClass());
        log.info("Class : {}", respIgnoreHeaderRewritePredicate.getClass());
        log.info("Class : {}", respContentTypeRewritePredicate.getClass());
        log.info("Class : {}", respWebSocketRewritePredicate.getClass());

        return BizPredicateChain.create(BizPredicateChain.Strategy.AND)
                .addFilterPredicate(respHttpCodeRewritePredicate)
                .addFilterPredicate(respIgnoreHeaderRewritePredicate)
                .addFilterPredicate(respContentTypeRewritePredicate)
                .addFilterPredicate(respWebSocketRewritePredicate);
    }

    @Resource
    private BizPredicate edRespContentTypeRewritePredicate;

    @Bean
    public BizPredicate respEncryptDecryptRewriterPredicateChain() {
        log.info("Class : {}", edRespContentTypeRewritePredicate.getClass());
        return BizPredicateChain.create(BizPredicateChain.Strategy.AND)
                .addFilterPredicate(edRespContentTypeRewritePredicate);
    }


    @Resource
    private BizPredicate reqQueryParamFilterPredicate;

    @Bean
    public BizPredicate reqQueryParamEncryptDecryptFilterPredicateChain() {
        log.info("Class : {}", reqQueryParamFilterPredicate.getClass());
        return BizPredicateChain.create(BizPredicateChain.Strategy.AND)
                .addFilterPredicate(reqQueryParamFilterPredicate);
    }

    @Resource
    private BizPredicate reqJsonContentTypeFilterPredicate;


    // app 用的 request body 的predicate
    @Bean
    public BizPredicate jsonContentTypeFilterPredicateChain() {
        log.info("Class : {}", reqJsonContentTypeFilterPredicate.getClass());
        return BizPredicateChain.create(BizPredicateChain.Strategy.AND)
                .addFilterPredicate(reqJsonContentTypeFilterPredicate)
                .addFilterPredicate(reqUriFilterPredicate);
    }

    @Resource
    private BizPredicate reqBackContentTypeFilterPredicate;

    @Bean
    public BizPredicate backContentTypeFilterPredicateChain() {
        log.info("Class : {}", reqBackContentTypeFilterPredicate.getClass());
        return BizPredicateChain.create(BizPredicateChain.Strategy.AND)
                .addFilterPredicate(reqBackContentTypeFilterPredicate)
                .addFilterPredicate(reqUriFilterPredicate);
    }

}
