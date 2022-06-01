package com.cx.juc;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author chenxiang
 * @create 2022-01-09 20:07
 * 可重入锁
 */
public class RRLock implements Lock, Serializable {

    private static final long serialVersionUID = -2610329647968526582L;

    private final Sync sync;

    abstract static class Sync extends AQS{
        private static final long serialVersionUID = -5836259133816126279L;

        abstract void lock();

        /**
         * 非公平获取锁
         * tryAcquire子类实现
         */
        final boolean nonfairTryAcquire(int acquires){
            // 过来我就要抢锁
            final Thread current = Thread.currentThread();
            int c = getState();
            if(c == 0){
                if(compareAndSetState(0, acquires)){
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }else if(current == getExclusiveOwnerThread()){
                // todo 会不会在这个时候被其他线程拿到锁了
                int nextc = c + acquires;
                if(nextc < 0){// 重入次数太多了
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextc);
                return true;
            }
            return false;
        }

        protected final boolean tryRelease(int releases){
            int c = getState() - releases;
            if(Thread.currentThread() != getExclusiveOwnerThread()){
                throw new IllegalMonitorStateException();
            }
            boolean free = false;
            if(c == 0){
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

        protected final boolean isHeldExclusively(){
            // While we must in general read state before owner,
            // we don't need to do so to check if current thread is owner
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final Thread getOwner(){
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        final int getHoldCount(){
            return isHeldExclusively() ? getState() : 0;
        }

        final boolean isLocked(){
            return getState() != 0;
        }

        private void readObject(ObjectInputStream s)
                throws IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0);
        }

    }

    static final class NonfairSync extends Sync{
        private static final long serialVersionUID = -4136892298038068514L;

        @Override
        final void lock() {
            if(compareAndSetState(0, 1)){
                setExclusiveOwnerThread(Thread.currentThread());
            }else{
                acquire(1);
            }
        }

        @Override
        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    static final class FairSync extends Sync{
        private static final long serialVersionUID = 3349582358879982153L;

        @Override
        void lock() {

        }

        @Override
        protected final boolean tryAcquire(int acquires){
            final Thread current = Thread.currentThread();
            int c = getState();
            if(c == 0){
                if(!hasQueuedPredecessors() && compareAndSetState(0, 1)){
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }else if (current == getExclusiveOwnerThread()){
                int nextc = c + acquires;
                if(nextc < 0){
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextc);
                return true;
            }
            return false;
        }
    }

    public RRLock(){
        sync = new NonfairSync();
    }

    public RRLock(boolean fair){
        sync = fair ? new FairSync() : new NonfairSync();
    }

    @Override
    public void lock() {
        sync.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @NotNull
    @Override
    public Condition newCondition() {
        return null;
    }
}
