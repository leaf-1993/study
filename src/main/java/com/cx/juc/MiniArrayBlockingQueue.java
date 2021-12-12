package com.cx.juc;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenxiang
 * @create 2021-11-30 10:01
 */
public class MiniArrayBlockingQueue<T> implements MiniBlockingQueue<T>, Serializable {

    /**
     * 数组长度
     */
    private int size;

    /**
     * 元素数量
     */
    private int count;

    /**
     * 下一次put元素的index
     */
    private int putIndex;

    /**
     * 下一次take元素的index
     */
    private int takeIndex;

    /**
     * 锁
     */
    private ReentrantLock lock;

    /**
     * 数组
     */
    private Object[] items;


    /**
     * takes的条件
     */
    private Condition notEmpty;

    /**
     * put的条件
     */
    private Condition notFull;

    public MiniArrayBlockingQueue(int size) {
        if(size <= 0){
            throw new IllegalArgumentException();
        }
        this.size = size;
        this.items = new Object[size];
        lock = new ReentrantLock();
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
    }

    @Override
    public void put(T element) throws InterruptedException {
        checkNotNull(element);
        lock.lockInterruptibly();
        try {
            while (count == size){
                notFull.await();
            }
            items[putIndex] = element;
            if(++putIndex == size){
                putIndex = 0;
            }
            count++;
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public T take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (count == 0){
                notEmpty.await();
            }
            final Object item = items[takeIndex];
            items[takeIndex] = null; // help GC
            if(++takeIndex == size){
                takeIndex = 0;
            }
            count--;
            notFull.signal();
            return (T) item;
        }finally {
            lock.unlock();
        }
    }

    private void checkNotNull(Object item){
        if(item == null){
            throw new NullPointerException();
        }
    }
}
