package com.liuweiwei.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Liuweiwei
 * @since 2021-08-12
 */
@Log4j2
public class AESUtils {
    /**
     * 加密密钥
     */
    private static final String AES_KEY = "zydfgFyd1d1r1gbo";

    /**
     * 加密
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            /**算法/模式/补码方式*/
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            /**使用CBC模式，需要一个向量iv，可增加加密算法的强度*/
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            /**此处使用BASE64做转码功能，同时能起到2次加密的作用。*/
            return new Base64().encodeAsString(encrypted);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }

    /**
     * 加密
     * @param key 对称加密秘钥
     * @param content 被加密内容
     * @return
     */
    public static String encrypt(String key, String content) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            /**"算法/模式/补码方式"*/
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            /**使用CBC模式，需要一个向量iv，可增加加密算法的强度*/
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            /**此处使用BASE64做转码功能，同时能起到2次加密的作用。*/
            return new Base64().encodeAsString(encrypted);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }

    /**
     * 解密
     * @param content
     * @return
     */
    public static String decrypt(String content) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            /**先用bAES64解密*/
            byte[] encrypted = Base64.decodeBase64(content);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return "";
        }
    }

    /**
     * 解密
     * @param key 对称加密秘钥
     * @param content
     * @return
     */
    public static String decrypt(String key, String content) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            /**先用Base64解密*/
            byte[] encrypted = Base64.decodeBase64(content);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return "";
        }
    }

    public static void main(String[] args) {
        /**固定 16 位长度的加密串*/
        String key = "23asdwee2343afwe";
        /**被加密的密文内容*/
        String content = "123456";
        String enS = AESUtils.encrypt(key, content);
        log.info("原始加密：{}", enS);
        System.out.println("原始加密：" + enS);
        log.info("原始解密：{}", AESUtils.decrypt(key, enS));
        System.out.println("原始解密：" + AESUtils.decrypt(key, enS));
    }
}
