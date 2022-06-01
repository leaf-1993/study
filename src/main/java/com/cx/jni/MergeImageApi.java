package com.cx.jni;

/**
 * @author chenxiang
 * @create 2022-01-06 10:32
 */
public class MergeImageApi {
    native String decodeImage(String inData, int inLen, String outDate, Integer outLen, String maskImagePath);
}
