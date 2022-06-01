package com.cx.ref;

import java.lang.ref.WeakReference;

/**
 * @author chenxiang
 * @create 2022-02-10 22:00
 */
public class Wref{

    public static void main(String[] args) {
//        Test t = new Test();
//        WeakReference<Test> weakRef = new WeakReference<>(t);
//        // weakRef.get() == null
//        System.out.println("弱引用：" + weakRef.get());
//        t = null;
//        System.out.println("去除强应用，垃圾未回收：" + weakRef.get());
//        System.gc();
//        Test test = weakRef.get();
//        System.out.println("去除强引用，垃圾已回收：" + test);
        System.out.println(1 >>> 1);
    }


    public static class Test{

    }
}
