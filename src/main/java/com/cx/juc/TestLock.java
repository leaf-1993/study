package com.cx.juc;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author chenxiang
 * @create 2021-11-24 12:50
 */

public class TestLock {
    public static void main(String[] args) {
        Lock lock = null;
        lock.lock();
        try {

        }finally {
            lock.unlock();
        }
    }

    @Test
    public void testBlockingQueue() throws InterruptedException {
        MiniArrayBlockingQueue<Integer> queue = new MiniArrayBlockingQueue<>(10);
        Thread p = new Thread(() -> {
            int i = 10;
            while (true){
                if(i < 0){
                    i = 10;
                }
                try {
                    queue.put(i);
                    System.out.println("生产：" + i);
                    TimeUnit.MILLISECONDS.sleep(600L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
            }
        });
        p.start();

        Thread c = new Thread(() -> {
            while (true){
                try {
                    final Integer take = queue.take();
                    System.out.println("消费：" + take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        c.start();
        while (true){
            TimeUnit.SECONDS.sleep(2L);
        }
    }
}
