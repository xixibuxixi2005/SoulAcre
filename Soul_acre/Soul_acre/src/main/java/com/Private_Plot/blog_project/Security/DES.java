package com.Private_Plot.blog_project.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class DES {

    private static final String ALGORITHM = "DES";
    private static final String TRANSFORMATION = "DES/ECB/PKCS5Padding";

    /**
     * 生成 DES 密钥
     *
     * @return 密钥的 Base64 编码字符串
     * @throws Exception 异常
     */
    public static String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }



    /**
     * DES 加密
     *
     * @param plainText 明文
     * @param key       密钥的 Base64 编码字符串
     * @return 加密后的 Base64 编码字符串
     * @throws Exception 异常
     */
    public static String encrypt(String plainText, String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * DES 解密
     *
     * @param encryptedText 加密后的 Base64 编码字符串
     * @param key           密钥的 Base64 编码字符串
     * @return 解密后的明文
     * @throws Exception 异常
     */
    public static String decrypt(String encryptedText, String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

//    public static void main(String[] args) {
//        try {
//            // 生成密钥
//            String key = generateKey();
//            System.out.println("生成的密钥: " + key);
//
//            // 明文
//            String plainText = "Hello, DES Encryption!";
//            System.out.println("明文: " + plainText);
//
//            // 加密
//            String encryptedText = encrypt(plainText, key);
//            System.out.println("加密后的文本: " + encryptedText);
//
//            // 解密
//            String decryptedText = decrypt(encryptedText, key);
//            System.out.println("解密后的文本: " + decryptedText);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
