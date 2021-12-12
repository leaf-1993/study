package com.cx.juc;

/**
 * @author chenxiang
 * @create 2021-11-30 10:00
 */
public interface MiniBlockingQueue<T> {

    /**
     * 生产
     * @param element
     */
    void put(T element) throws InterruptedException;

    /**
     * 消费
     * @return
     */
    T take() throws InterruptedException;
}
