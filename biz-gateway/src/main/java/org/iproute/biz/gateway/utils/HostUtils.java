package org.iproute.biz.gateway.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * HostUtils
 *
 * @author zhuzhenjie
 */
public class HostUtils {

    private HostUtils() {
    }

    public static String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown host";
        }
    }
}
