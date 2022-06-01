package com.cx.juc;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

/**
 * @author chenxiang
 * @create 2022-01-07 12:59
 */
public abstract class AQS extends AOS implements Serializable {

    private static final long serialVersionUID = -9083917840737239416L;

    protected AQS() {

    }

    static final class Node {
        /**
         * 共享模式的等待节点标记
         */
        static final Node SHARED = new Node();

        /**
         * 排它模式的等待节点标记
         */
        static final Node EXCLUSIVE = null;

        /**
         * 等待状态：线程已取消
         */
        static final int CANCELLED = 1;

        /**
         * 等待状态：后继节点的线程需要去unparking（唤醒）
         */
        static final int SIGNAL = -1;

        /**
         * 等待状态：线程处于条件等待状态
         */
        static final int CONDITION = -2;

        /**
         * todo 不知道什么意思
         * 等待状态：下一个acquireShared应该无条件的传播
         */
        static final int PROPAGATE = -3;

        /**
         * 等待状态
         * SIGNAL(-1): 这个节点的后继节点是阻塞的（或者将要阻塞），所以当前节点
         * 释放锁或者被取消的时候，需要unpark它的后继节点。
         * 为了避免竞争，
         * acquire方法必须 首先表明他们需要唤醒，然后重试原子操作acquire
         * 如果失败则阻塞
         * CANCELLED(1): 这个节点已经被取消了，因为超时或者打断。
         * 这类节点将永远处于这个状态
         * 尤其是，一个取消状态的线程节点将不在阻塞
         * CONDITION(-2): 这个节点当前正在一个条件队列中
         * 它将不被使用作为一个同步队列节点直到从条件队列中出队，
         * 这个时候它的状态将被设置为0（这个值在这里的使用与字段的其他用途无关，只是简化了机制）
         * PROPAGATE(-3): 一个releaseShared应该被传播到其它节点。
         * 这个将在doReleaseShared方法的时候被设置，而且仅仅只有头节点可以设置
         * ，为了确保传播继续。即使后来有其他操作介入
         * 0：除上述的其他情况
         */
        volatile int waitStatus;

        /**
         * 前驱节点
         */
        volatile Node prev;

        /**
         * 后继节点
         */
        volatile Node next;

        /**
         * 线程
         */
        volatile Thread thread;

        /**
         * 条件队列
         */
        Node nextWaiter;

        /**
         * @return 共享模式的等待
         */
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        /**
         * @return 当前节点的前驱节点
         * @throws NullPointerException
         */
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null) {
                throw new NullPointerException();
            }
            return p;
        }

        Node() {

        }

        /**
         * addWaiter使用
         *
         * @param thread
         * @param mode
         */
        Node(Thread thread, Node mode) {
            this.nextWaiter = mode;
            this.thread = thread;
        }

        /**
         * condition使用
         *
         * @param thread
         * @param waitStatus
         */
        Node(Thread thread, int waitStatus) {
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }

    /**
     * 等待队列的头结点，除了初始化外，其它情况都属于懒加载
     * 仅使用setHead方法进行修改
     * 如果head存在，则需要确保其waitStatus不能是CANCELLED
     */
    private transient volatile Node head;

    /**
     * 等待队列的尾结点，懒加载
     * 仅使用enq去添加新的等待节点
     */
    private transient volatile Node tail;

    /**
     * 同步状态
     */
    private volatile int state;

    /**
     * 当前同步状态的值
     * 该方法具有volatile read的内存语义
     *
     * @return 当前state值
     */
    protected final int getState() {
        return state;
    }

    /**
     * 设置同步状态的值
     * <p>
     * 该方法具有volatile write的内存语义
     *
     * @param newState 新的同步状态值
     */
    protected final void setState(int newState) {
        state = newState;
    }

    /**
     * cas设置state值
     *
     * @param expect 期待值
     * @param update 更新值
     * @return 是否交换成功，失败则代表当前值并非期待值expect
     */
    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    // 队列操作相关

    /**
     * 纳秒
     * 自旋和时间park选择的阈值
     * 如果过期时间大于1000纳秒就park
     * 如果小于则自旋
     */
    static final long spinForTimeoutThreshold = 1000L;

    /**
     * 入队操作
     * 必须入队成功
     *
     * @param node 入队元素
     * @return 入队元素的前驱节点
     * <p>
     * 1、判断队列是否初始化
     * 1.1、如果没有初始化则初始化，设置head并设置tail
     * 1.2、如果初始化化了，node.next设置为tail，并将tail设置为node，然后将之前的tail的next设置为node
     */
    private Node enq(final Node node) {
        for (; ; ) {
            Node t = tail;
            if (t == null) {
                // 还没有初始化，必须初始化
                if (compareAndSetHead(node)) {
                    tail = head;
                }
            } else {
                /*
                   head       tail
                *   |          |
                *   A -> B -> C
                *
                * */
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }


    /**
     * 为当前线程以给定的模式创建并且排队节点
     *
     * @param mode 节点模式 Node.EXCLUSIVE-独占 NODE.SHARED-共享模式
     * @return 新的节点
     */
    private Node addWaiter(Node mode) {
        // 创建节点
        Node node = new Node(Thread.currentThread(), mode);
        // 快速入队，如果失败则自旋入队
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);

        return node;
    }

    /**
     * 设置队列头为节点，因此出队
     * 仅会被acquire方法调用
     * <p>
     * 为了GC也会将未使用的字段空出来
     * 抑制不必要的信号和遍历
     *
     * @param node 节点
     *             当前节点的线程抢到了锁，设置当前节点为头节点来执行
     *             去除里面的线程引用帮助gc
     */
    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    /**
     * 唤醒给定节点的后继节点（如果后继节点存在）
     *
     * @param node 节点
     */
    private void unparkSuccessor(Node node) {
        int ws = node.waitStatus;
        if (ws < 0) {
            // 1-CANCELLED -1 SIGNAL -2 CONDITION -3 PROPAGATE
            // 将给定节点状态设置为0
            compareAndSetWaitStatus(node, ws, 0);
        }
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            // 后继节点不存在 或者已取消
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev) {
                if (t.waitStatus <= 0) {
                    s = t;
                }
            }
        }

        if (s != null) {
            LockSupport.unpark(s.thread);
        }
    }


    /**
     * 共享模式的释放操作
     * 信号后继，并且保证传播
     * <p>
     * （提示：对于独占模式，释放仅仅调用unparkSuccessor，如果头结点需要去signals）
     */
    private void doReleaseShared() {
        for (; ; ) {
            Node h = head;
            if (h != null && h != tail) {
                // 等待队列已经初始化，并且不是刚初始化
                int ws = h.waitStatus;
                if (ws == Node.SIGNAL) {
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0)) {
                        continue;
                    }
                    unparkSuccessor(h);
                } else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, Node.PROPAGATE)) {
                    continue;
                }
            }
            if (h == head) {
                break;
            }
        }
    }

    /**
     * 设置等待队列的头结点。检查后继节点是否可能以共享模式正在等待中。
     * 如果设置了PROPAGATE 状态 或者 propagate > 0则传播
     *
     * @param node 节点
     * @param propagate tryAcquireShared的返回值
     */
    private void setHeadAndPropagate(Node node, int propagate){
        Node h = head;
        setHead(node);
        if(propagate > 0 || h == null || h.waitStatus < 0 ||
                (h = head) == null || h.waitStatus < 0){
            Node s = node.next;
            if(s == null || s.isShared()){
                doReleaseShared();
            }
        }
    }
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
                acquireQueued(addWaiter(AQS.Node.EXCLUSIVE), arg)){
            selfInterrupt();
        }
    }

    /**
     * TODO
     * @param node
     * @param arg
     * @return
     */
    final boolean acquireQueued(final AQS.Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final AQS.Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt()) {
                    interrupted = true;
                }
            }
        } finally {
            if (failed) {
                cancelAcquire(node);
            }
        }
    }

    /**
     *
     * TODO
     * @param pred
     * @param node
     * @return
     */
    private static boolean shouldParkAfterFailedAcquire(AQS.Node pred, AQS.Node node) {
        int ws = pred.waitStatus;
        if (ws == AQS.Node.SIGNAL)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */ {
            return true;
        }
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            compareAndSetWaitStatus(pred, ws, AQS.Node.SIGNAL);
        }
        return false;
    }

    /**
     * TODO
     * Convenience method to park and then check if interrupted
     *
     * @return {@code true} if interrupted
     */
    private boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    /**
     *
     * TODO
     * Cancels an ongoing attempt to acquire.
     *
     * @param node the node
     */
    private void cancelAcquire(AQS.Node node) {
        // Ignore if node doesn't exist
        if (node == null) {
            return;
        }

        node.thread = null;

        // Skip cancelled predecessors
        AQS.Node pred = node.prev;
        while (pred.waitStatus > 0) {
            node.prev = pred = pred.prev;
        }

        // predNext is the apparent node to unsplice. CASes below will
        // fail if not, in which case, we lost race vs another cancel
        // or signal, so no further action is necessary.
        AQS.Node predNext = pred.next;

        // Can use unconditional write instead of CAS here.
        // After this atomic step, other Nodes can skip past us.
        // Before, we are free of interference from other threads.
        node.waitStatus = AQS.Node.CANCELLED;

        // If we are the tail, remove ourselves.
        if (node == tail && compareAndSetTail(node, pred)) {
            compareAndSetNext(pred, predNext, null);
        } else {
            // If successor needs signal, try to set pred's next-link
            // so it will get one. Otherwise wake it up to propagate.
            int ws;
            if (pred != head &&
                    ((ws = pred.waitStatus) == AQS.Node.SIGNAL ||
                            (ws <= 0 && compareAndSetWaitStatus(pred, ws, AQS.Node.SIGNAL))) &&
                    pred.thread != null) {
                AQS.Node next = node.next;
                if (next != null && next.waitStatus <= 0) {
                    compareAndSetNext(pred, predNext, next);
                }
            } else {
                unparkSuccessor(node);
            }

            node.next = node; // help GC
        }
    }

    /**
     * TODO
     * @return
     */
    public final boolean hasQueuedPredecessors() {
        // The correctness of this depends on head being initialized
        // before tail and on head.next being accurate if the current
        // thread is first in queue.
        AQS.Node t = tail; // Read fields in reverse initialization order
        AQS.Node h = head;
        AQS.Node s;
        return h != t &&
                ((s = h.next) == null || s.thread != Thread.currentThread());
    }

    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * todo
     * Convenience method to interrupt current thread.
     */
    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * cas
     */
    private static final Unsafe unsafe;
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static {
        try {
            unsafe = getUnsafe();
            stateOffset = unsafe.objectFieldOffset
                    (AQS.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AQS.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AQS.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("next"));
        } catch (Exception e) {
            throw new Error(e);
        }

    }

    private static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    // enq使用的cas操作

    private boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    private boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    private static boolean compareAndSetWaitStatus(
            Node node, int expect, int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset,
                expect, update);
    }

    private static final boolean compareAndSetNext(
            Node node, Node expect, Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset,
                expect, update);
    }
}
