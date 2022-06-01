package com.cx.aop.util;

import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author chenxiang
 * @create 2022-04-17 22:55
 */
public class ProxyUtils {

    public static void generateClassFile(Class clazz, String proxyName){
        byte[] classFile = ProxyGenerator.generateProxyClass(proxyName, clazz.getInterfaces());
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("C:\\Users\\leaf\\Desktop\\" + proxyName + ".class");
            out.write(classFile);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
