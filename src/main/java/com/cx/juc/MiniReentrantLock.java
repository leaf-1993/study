package com.cx.juc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author chenxiang
 * @create 2021-11-24 12:52
 * 可重入锁
 */
public class MiniReentrantLock implements MiniLock{

    /**
     * 锁的是什么？
     * 锁的是资源 -> state
     * 0-表示未加锁
     * >0 表示当前lock是加锁状态
     */
    private volatile int state;

    /**
     * 独占模式？
     * 同一时刻只有一个线程可以持有锁，其它线程在没有获取到锁时会被阻塞
     */
    private Thread exclusiveOwnerThread;


    /**
     * 需要两个引用维护阻塞线程队列
     * head 指向队列的头节点
     * tail 指向队列的尾节点
     */
    private Node head;

    private Node tail;

    public int getState() {
        return state;
    }

    public Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    /**
     * 阻塞线程被封装成什么？
     * Node节点，然后放入FIFO队列中
     */
    static final class Node{
        /**
         * 前置节点引用
         */
        Node prev;
        /**
         * 后置节点引用
         */
        Node next;
        /**
         * 线程
         */
        Thread thread;
    }


    /**
     * 获取锁
     * 如果当前锁被占用，则会阻塞调用者线程，直到抢占到锁为止
     *
     * 模拟公平锁
     *
     * 公平锁？FIFO先来后到
     *
     * lock的过程？
     * 1、线程进来后发现 state == 0 ,直接抢锁
     * 2、线程进来后发现，当前state > 0, 当前线程入队
     */
    @Override
    public void lock() {

    }

    /**
     * 竞争资源
     * 1、尝试获取锁，成功则占用锁，且返回
     * 2、抢占失败，阻塞当前线程
     * @param arg
     */
    private void acquire(int arg){

    }

    /**
     * 尝试获取锁，不会阻塞线程
     * true 获取锁成功
     * false 获取锁失败
     * @param arg
     * @return
     */
    private boolean tryAcquire(int arg){
        if(state == 0){
            // 当前state == 0 时，是否可以直接抢占锁呢
            // 公平锁 不能直接抢占
            // 条件一：前面没有等待者线程
            // 条件二：当前线程抢占锁成功了
            if(!hasQueuedPredecessor() && compareAndSetState(0, arg)){
                this.exclusiveOwnerThread = Thread.currentThread();
                return true;
            }
        }else if(Thread.currentThread() == this.exclusiveOwnerThread){
            // 当前锁被占用
            // 当前锁线程就是抢占锁的线程
            // 线程安全的
            int c = getState();
            c +=arg;
            // 越界判断 超过Integer.MAX
            this.state = c;
            return true;
        }
        // cas 加锁失败
        // state > 0 且 占有线程不是当前线程
        return false;
    }

    /**
     * true 表示当前线程前面有等待者线程
     * false 当前线程前面没有其他等待者线程
     * @return
     */
    private boolean hasQueuedPredecessor(){
        return false;
    }

    @Override
    public void unlock() {

    }

    private void setHead(Node node){
        this.head = node;
        // 因为当前node已经是获取到锁的线程了
        node.thread = null;
        node.prev = null;
    }

    private static final Unsafe unsafe;
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;

    static {
        try {
            final Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            stateOffset = unsafe.objectFieldOffset(MiniReentrantLock.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset(MiniReentrantLock.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset(MiniReentrantLock.class.getDeclaredField("tail"));
        }catch (Exception e){
            throw new Error(e);
        }
    }

    private final boolean compareAndSetHead(Node update){
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    private final boolean compareAndSetTail(Node expect, Node update){
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    private final boolean compareAndSetState(int expect, int update){
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
}
