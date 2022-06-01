package com.cx.jni;


import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author chenxiang
 * @create 2022-01-05 21:19
 */
public class Test {
    private int age;

    static {
        Path workingPath = Paths.get(System.getProperty("user.dir"));;

        Path targetPath = workingPath.resolve("test-jni.dll");

        System.load(targetPath.toString());
        targetPath = workingPath.resolve("libmath.dll");

        System.load(targetPath.toString());
    }

    public static void main(String[] args) {
        Math m = new Math();
        System.out.println(m.add(1, 2));
        System.out.println(m.div(1,0));
        Test test = new Test();
    }
}
