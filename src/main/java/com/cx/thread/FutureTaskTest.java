package com.cx.thread;

import javax.lang.model.element.VariableElement;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.*;

/**
 * @author chenxiang
 * @create 2022-02-23 19:54
 */
public class FutureTaskTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = newFixedThreadPool(5);

        Future<Integer> submit = pool.submit(() -> 1);
        Integer integer = submit.get();
    }
}
