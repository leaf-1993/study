package com.cx.juc;

/**
 * @author chenxiang
 * @create 2021-11-24 12:52
 * 锁接口
 */
public interface MiniLock {
    /**
     * 获取锁
     */
    void lock();

    /**
     * 解锁
     */
    void unlock();
}
