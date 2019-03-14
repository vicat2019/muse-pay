package com.merchant.util;

import org.thymeleaf.util.StringUtils;

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

    // RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;

    // RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;


    // 滨农00020019, DESKEY
    //public static final String deskey = "f6103d939f7831bab920a141";

    // 滨农00020019, 私钥的key
    //public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANPcQofuGrcZLQd9pb7wKpFXFyR97eZlaMKtcxmE01dx0a8y5FPedXzO9XN8g8RkYKqwYoBLv8LeB9QA2fRkPogs2oNjDO+bNUU1EMHeRVVYxikPWw2wu0m2LXyxjxdfY79COMm3zqiulg6QnKE3jQ4zcyXm5RetG608VSY4pKh5AgMBAAECgYEAiyGoM5qDwuDuuG+kjLsuSVp3YUiuFlNctBSydPzMyi1+Nd9nIUkDwR4tBVWtNylwbkf9uCGG4pNrcqfIev3mLda45LEwEpGeJEHybxu904fnVZSz7KME0AzCY57K9GKKvpgfFcuvArR1D7x5hxB5Rvbm8pTVYfafn7+kosetlx0CQQD94DXNgBnQRXodAZbE7SVOdU6mwOet5SZtW49s8mUld4EfPoMTrhq2Vmn/FjG5bi+5mtlbX2UwauOVc8GtgOm7AkEA1aIN/6LC4S8tTWvjDHedorHkt2fKNujL7WaywvCzN3Wkk1O/ZpPTiSzgHhnO5/Dq29uuO1qj4mxbzo1LzC4JWwJBAPRmiwUZZEBXEjaoV++eaMK+NYhcvuxuZG70rP9x3qOSBzD/6gnhGwAb8pDKWfYsOC+S/dpz3KMe05cFyBbyuo8CQF6ldEQ8bNBtIUn6sVW1rN4GqEjlL76R9r8Pz8Tz9MJuWWJ4KQUeGwrcGdLcJkG2B6cuNYvQRWksDqR9Dwa/op8CQEPsn1ArlvgMYU3peBjMVhNsUZf/OiuaYfu9NHkDdpvNAmLwD6Nz1zOpxs4Jwna7/VMjdCPtEDZQgiY/o9jOqrw=";


    private Signature signature;


    public Signature getSignature() {
        return signature;
    }

    /**
     * 设置私钥
     *
     * @throws Exception
     */
    public void setSignature(String PRIVATE_KEY) throws Exception {
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
    public String sign(String signStr, String privateKey) throws Exception {
        if (signature == null) {
            setSignature(privateKey);
        }
        signature.update(signStr.getBytes());
        return Base64.encode(signature.sign());
    }

    public String signByMap(TreeMap<String, Object> map, String privateKey) {
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
            return sign(paramsSb.toString().trim(), privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String java_openssl_encrypt(String data, String desKey) throws Exception {
        if (StringUtils.isEmpty(data)) {
            return "";
        }

        String iv = "01234567";
        byte[] key = desKey.getBytes();

        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        IvParameterSpec aaa = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DESede"), aaa);
        return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

}
