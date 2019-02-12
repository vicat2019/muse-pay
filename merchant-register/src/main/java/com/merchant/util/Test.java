package com.merchant.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @program: muse-pay
 * @description:
 * @author: Vincent
 * @create: 2019-02-12 18:35
 **/
public class Test {

    public static void main(String[] args) {
        String data = "my_data";
        String passwrod = "my_password";
        String iv = "1234567890123456";

        try {
            String base64Str = java_openssl_encrypt(data, passwrod, iv);
            System.out.print(base64Str);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String java_openssl_encrypt(String data, String password, String iv) throws Exception {
        byte[] key = new byte[32];
        for (int i = 0; i < 32; i++) {
            if (i < password.getBytes().length) {
                key[i] = password.getBytes()[i];
            } else {
                key[i] = 0;
            }
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"),
                new IvParameterSpec(iv.getBytes()));

        String base64Str = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));


        return base64Str;
    }


}
