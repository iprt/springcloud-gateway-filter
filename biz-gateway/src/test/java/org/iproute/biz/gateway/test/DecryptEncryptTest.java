package org.iproute.biz.gateway.test;

import org.iproute.biz.gateway.utils.EncryptDecrypt;
import org.iproute.biz.gateway.utils.ed.ReverseEncryptDecrypt;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

/**
 * DecryptEncryptTest
 *
 * @author zhuzhenjie
 * @since 5/9/2023
 */
@SpringBootTest
public class DecryptEncryptTest {
    final EncryptDecrypt encryptDecrypt = new ReverseEncryptDecrypt();

    final String data = "{\n"
            + "  \"title\": \"TestTitle1\",\n"
            + "  \"author\": \"TestAuthor1\"\n"
            + "}";

    private static String encryptData = "";

    @Test
    public void testEncrypt() {
        encryptData = encryptDecrypt.encrypt(data);
        System.out.println(encryptData);
    }

    @Test
    public void testDecrypt() {
        if (Objects.equals(encryptData, "")) {
            return;
        }
        System.out.println(encryptDecrypt.decrypt(encryptData));
    }

}
