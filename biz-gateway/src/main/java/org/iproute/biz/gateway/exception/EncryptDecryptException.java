package org.iproute.biz.gateway.exception;

/**
 * EncryptDecryptException
 *
 * @author zhuzhenjie
 */
public class EncryptDecryptException extends RuntimeException {

    @Override
    public String getMessage() {
        return "An error occurred with the encryption and decryption!";
    }
}
