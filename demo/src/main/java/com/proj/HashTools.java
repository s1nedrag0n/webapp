package com.proj;

import java.security.*;

public class HashTools {


    private static MessageDigest digest;
 
    private HashTools(){}
 
    //将字节数组转换为16进制字符串
    public static String bytesToHex(byte[] bytes){
        StringBuilder ret =new StringBuilder();
        for (byte b :bytes) {
            //将字节数组转换为2位16进制字符串
            ret.append(String.format("%02x",b));
        }
        return ret.toString();
    }
 
    //按照MD5进行消息摘要计算（哈希计算）
    public static String digestByMD5(String source) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("MD5");
        return handler(source);
    }
 
    //按照SHA-1进行消息摘要计算（哈希计算）
    public static String digestBySHA1(String source) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-1");
        return handler(source);
    }
 
    //按照SHA-256进行消息摘要计算（哈希计算）
    public static String digestBySHA256(String source) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-256");
        return handler(source);
    }
 
    //按照SHA-512进行消息摘要计算（哈希计算）
    public static String digestBySHA512(String source) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-512");
        return handler(source);
    }
 
    //通过消息摘要对象 处理加密内容
    private static String handler(String source){
        digest.update(source.getBytes());
        return bytesToHex(digest.digest());
    }
}  