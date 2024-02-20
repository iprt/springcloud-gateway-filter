package org.iproute.biz.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Gateway
 *
 * @author winterfell
 * @since 2021/11/8
 */
@EnableDiscoveryClient
@SpringBootApplication
public class BizGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizGatewayApplication.class, args);
    }

    public interface PROFILES {
        String WRAPPER = "wr";
        String ENCRYPT_DECRYPT = "ed";
    }

    public interface TransitAttribute {
        String WRAPPED = "WRAPPED";
    }

}
