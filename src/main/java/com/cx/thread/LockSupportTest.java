package com.cx.thread;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author chenxiang
 * @create 2022-05-09 11:12
 */
public class LockSupportTest {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("t1 enter");
            try {
                TimeUnit.SECONDS.sleep(2L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("t1 sleep end");
            LockSupport.park();
            System.out.println("t1 park");
            System.out.println("t1");
        });
        Thread t2 = new Thread(() -> {
            LockSupport.unpark(t1);
            System.out.println("t1 unpark");
            System.out.println("t2");
        });
        t1.start();
        TimeUnit.SECONDS.sleep(1L);
        t2.start();

    }
}
