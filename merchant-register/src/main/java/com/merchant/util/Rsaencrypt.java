package com.merchant.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * @program: muse-pay
 * @description:
 * @author: Vincent
 * @create: 2019-02-12 11:58
 **/
public class Rsaencrypt {

    // 加密算法RSA
    public static final String KEY_ALGORITHM = "RSA";

    // 签名算法
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    // 公钥的key
    private static final String PUBLIC_KEY = "RSAPublicKey";

    // 私钥的key
    public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKsN3HDm9jFRns3+AA6r7NQsvUbQGOax/7Q1Oj9+/+O+8XLuhE4ENiDQIwL3ZaFrWcpsGMc2K8ZByDTh/kUZ5NxzbxVJroUNWqw9t/T7EpgR2rOHVASB1MNvmMk1kLwp7lEAhuVe+UVEU/EzvoTGh0h4YDTGgra34uJGy1YMDVPvAgMBAAECgYAPJ2U2I2AAMojtGumKYzTR/zytf1YVagOLRYPpiCjHVm0xj2vtNXZOJeJUKvK4ADiuNDFlxKjQIZfJB5RtLsQVln3oKaiDnBJPBzgk/9PoCus4z4BqUX8bdpJKC2ATMUrsRWmMOhofspfFxKmEB7K64tGEpnI1FPTxHwPnS4/OIQJBAOGFwFlYtlN+8xDJwH4XnpcboJ6k4juYZn+wzouwVdLZxQEqjFwWlk9J6XWEQ0VCzizQLOLljUVp0ha+L0ZjGVcCQQDCK7S5+82kWEedUG7eHrHs48VGSDr4Tk91hE2o/UyePXvYTYxwOA4TqaDmjzw/n+ZAyUk8SjNy6avsdO5bh8MpAkBV4JTKcRmc2e69KdZ/LYdai06ymp77iqEdZrqAPvmvfPWj6Avi1UcPklmwQllLepEAR5WQIVGWaSFzP6DI8NupAkADaDztnouHPL1U91par3Mz0vNdPls6jDU8vKuYMYmspSuNKh8ywOkOU0Wthgnqm+WwcVfFpQ+uDoejyJF+La2xAkEAnqVsk00S5VpErMr1TLU+Opwp4bBFoS9jr9QXs09W1/un1OaKjvoQAaPHHxjOg7EwwhcclMxvaFMN2DB/sfbucQ==";

    // RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;

    // RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;


    public static final String deskey = "7ba87a2ecc6bd4d8fe133524";


    private static Signature signature;


    public static Signature getSignature() {
        return signature;
    }

    /**
     * 设置私钥
     *
     * @throws Exception
     */
    public static void setSignature() throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(PRIVATE_KEY);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
        sign.initSign(privateK);
        signature = sign;
    }

    /**
     * 使用私钥进行签名
     *
     * @param signStr
     * @return
     * @throws Exception
     */
    public static String sign(String signStr) throws Exception {
        if (signature == null) {
            setSignature();
        }
        signature.update(signStr.getBytes());
        return Base64.encode(signature.sign());
    }

    public static String signByMap(TreeMap<String, Object> map) {
        try {
            List<String> keys = new ArrayList<>(map.keySet());
            StringBuilder paramsSb = new StringBuilder();

            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);

                // 检查，如果为NULL或者空，则去掉
                if (map.get(key) == null) {
                    continue;
                }

                if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                    paramsSb.append(key).append("=").append(map.get(key));
                } else {
                    paramsSb.append(key).append("=").append(map.get(key)).append("&");
                }
            }
            return sign(paramsSb.toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv.getBytes()));
        String base64Str = java.util.Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        return base64Str;
    }

}
