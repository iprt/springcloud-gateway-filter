package org.iproute.biz.gateway.utils.ed;

import org.iproute.biz.gateway.utils.EncryptDecrypt;
import org.apache.commons.lang3.StringUtils;

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

    public static void main(String[] args) {

        String data = "{\n"
                + "  \"title\": \"TestTitle1\",\n"
                + "  \"author\": \"TestAuthor1\"\n"
                + "}";


        EncryptDecrypt encryptDecrypt = new ReverseEncryptDecrypt();

        System.out.println("Original String: " + data);

        String encryptedString = encryptDecrypt.encrypt(data);
        System.out.println("Encrypted String: " + encryptedString);


        String decryptedString = encryptDecrypt.decrypt(encryptedString);
        System.out.println("Decrypted String: " + decryptedString);


        System.out.println("query string ...");
        System.out.println(encryptDecrypt.encrypt("name=zzj"));

    }
}
