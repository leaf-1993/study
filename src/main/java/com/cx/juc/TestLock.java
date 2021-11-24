package com.cx.juc;

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
}
