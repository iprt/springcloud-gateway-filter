package org.iproute.biz.gateway.config.filters.predicate;

import com.google.common.collect.Lists;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * BizWrapperChain
 *
 * @author zhuzhenjie
 * @since 4/30/2023
 */
public class BizPredicateChain implements BizPredicate {

    private final List<BizPredicate> bizPredicates;

    private final Strategy strategy;

    private BizPredicateChain(Strategy strategy) {
        this.bizPredicates = Lists.newArrayList();
        this.strategy = strategy;
    }

    public static BizPredicateChain create(Strategy strategy) {
        return new BizPredicateChain(strategy);
    }

    public BizPredicateChain addFilterPredicate(BizPredicate bizPredicate) {
        this.bizPredicates.add(bizPredicate);
        return this;
    }

    /**
     * 判断是否需要封装
     *
     * @param exchange the exchange
     * @return the boolean
     */
    @Override
    public boolean useBiz(ServerWebExchange exchange) {
        if (this.bizPredicates.size() == 0) {
            return false;
        }

        // or 策略
        if (this.strategy == Strategy.OR) {
            return orStrategy(exchange);
        }

        // and 策略
        if (this.strategy == Strategy.AND) {
            return andStrategy(exchange);
        }

        // 其他策略？
        return false;
    }


    private boolean orStrategy(ServerWebExchange exchange) {
        for (BizPredicate predicate : this.bizPredicates) {
            if (predicate.useBiz(exchange)) {
                return true;
            }
        }
        return false;
    }

    private boolean andStrategy(ServerWebExchange exchange) {
        for (BizPredicate predicate : this.bizPredicates) {
            if (!predicate.useBiz(exchange)) {
                return false;
            }
        }
        return true;
    }


    /**
     * filter 链的策略
     * <p>
     * AND: 每个FilterPredicate都要满足
     * <p>
     * OR: 满足一个即可
     */
    public enum Strategy {
        AND,
        OR
    }

}


