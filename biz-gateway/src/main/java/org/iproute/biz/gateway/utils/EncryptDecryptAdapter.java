package org.iproute.biz.gateway.utils;

import org.iproute.biz.gateway.utils.ed.ReverseEncryptDecrypt;
import org.springframework.stereotype.Component;

/**
 * EncryptDecryptAdapter
 *
 * @author zhuzhenjie
 * @since 4/25/2023
 */
@Component
public class EncryptDecryptAdapter implements EncryptDecrypt {
    private static final EncryptDecrypt ed = new ReverseEncryptDecrypt();

    @Override
    public String encrypt(String str) {
        return ed.encrypt(str);
    }

    @Override
    public String decrypt(String str) {
        return ed.decrypt(str);
    }
}
