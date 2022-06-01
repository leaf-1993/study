package com.cx.jni;


/**
 * @author chenxiang
 * @create 2022-01-16 12:15
 */
public class Math {
    public native int add(int num1, int num2);

    public native int mul(int num1, int num2);

    public native int sub(int num1, int num2);

    public native int div(int num1, int num2) throws IllegalArgumentException;
}
