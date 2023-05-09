package org.iproute.biz.gateway.utils.ed;

import org.apache.commons.lang3.StringUtils;
import org.iproute.biz.gateway.utils.EncryptDecrypt;

/**
 * ReverseEncryptDecrypt
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
public class ReverseEncryptDecrypt implements EncryptDecrypt {

    @Override
    public String encrypt(String str) {
        return StringUtils.reverse(str);
    }

    @Override
    public String decrypt(String str) {
        return StringUtils.reverse(str);
    }
}
