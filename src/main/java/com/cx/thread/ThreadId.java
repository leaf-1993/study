package com.cx.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenxiang
 * @create 2021-12-06 10:42
 */
public class ThreadId {
    private static final AtomicInteger nextId = new AtomicInteger(0);

    private static final ThreadLocal<Integer> threadId = ThreadLocal.withInitial(() -> nextId.getAndIncrement());

    public static int get(){
        return threadId.get();
    }

    public static void remove(){
        threadId.remove();
    }

    public static void main(String[] args) {
        for(int i = 0; i< 100; i++){
            new Thread(() -> {
                System.out.println(threadId.get());
                threadId.remove();
            }).start();
        }
    }
}
