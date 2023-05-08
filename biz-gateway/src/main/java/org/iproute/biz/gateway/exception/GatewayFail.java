package org.iproute.biz.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GatewayFail
 *
 * @author zhuzhenjie
 * @since 2020/10/27
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GatewayFail {
    private Integer code;
    private String msg;
}
