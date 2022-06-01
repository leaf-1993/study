package com.cx.thread;


/**
 * @author chenxiang
 * @create 2021-12-21 18:25
 */
public interface Rule {
    ThreadLocal<Integer> TL = ThreadLocal.withInitial(() -> 0);

    default ThreadLocal<Integer> getTl(){
        return TL;
    }
}
