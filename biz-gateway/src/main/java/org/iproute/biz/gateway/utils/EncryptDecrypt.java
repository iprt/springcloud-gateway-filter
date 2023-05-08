package org.iproute.biz.gateway.utils;

/**
 * EncryptDecrypt
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
public interface EncryptDecrypt {

    /**
     * 加密
     *
     * @param str the str
     * @return the string
     */
    String encrypt(String str);

    /**
     * 解密
     *
     * @param str the str
     * @return the string
     */
    String decrypt(String str);

}
