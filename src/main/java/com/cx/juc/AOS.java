package com.cx.juc;

import java.io.Serializable;

/**
 * @author chenxiang
 * @create 2022-01-07 13:00
 */
public abstract class AOS implements Serializable {

    private static final long serialVersionUID = 8479279846901221131L;

    /**
     * 独占线程
     */
    private transient Thread exclusiveOwnerThread;

    protected AOS() {

    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }

    protected final void setExclusiveOwnerThread(Thread exclusiveOwnerThread) {
        this.exclusiveOwnerThread = exclusiveOwnerThread;
    }
}
