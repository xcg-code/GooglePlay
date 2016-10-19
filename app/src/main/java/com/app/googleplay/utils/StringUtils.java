package com.app.googleplay.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {
    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }

    //MD5加密
    public static String md5(String psd) {
        byte[] hash;
        try {
        //1,指定加密算法类型(MD5),并将需要加密的字符串中转换成byte类型的数组,然后进行随机哈希过程
            hash = MessageDigest.getInstance("MD5").digest(psd.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 should be supported?", e);
        }
        //2,循环遍历bs,然后让其生成32位字符串,固定写法
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            //3,拼接字符串过程
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
