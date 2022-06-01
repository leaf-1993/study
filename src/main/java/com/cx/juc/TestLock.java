package com.cx.juc;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * @author chenxiang
 * @create 2021-11-24 12:50
 */

public class TestLock {
    public static void main(String[] args) {
//        Lock lock = null;
//        lock.lock();
//        try {
//
//        }finally {
//            lock.unlock();
//        }
        List<Integer> list = null;
        list.toArray(new Integer[list.size()]);
    }

    public int[] compressArray(int[] nums){
        List<Integer> compressList = new ArrayList<>();
        int mark = mark(nums[0]);
        int sum = 0;
        for(int i = 0; i < nums.length; i++){
            if(mark == mark(nums[i])){
                sum +=nums[i];
            }else{
                compressList.add(sum);
                sum = nums[i];
                mark = -mark;
            }
        }
        compressList.add(sum);
        int[] retNums = new int[compressList.size()];
        for (int i = 0; i < compressList.size(); i++){
            retNums[i] = compressList.get(i);
        }
        return retNums;
    }

    public int mark(int target){
        return target >= 0 ? 1 : -1;
    }

    @Test
    public void testBlockingQueue() throws InterruptedException {
        MiniArrayBlockingQueue<Integer> queue = new MiniArrayBlockingQueue<>(10);
        Thread p = new Thread(() -> {
            int i = 10;
            while (true){
                if(i < 0){
                    i = 10;
                }
                try {
                    queue.put(i);
                    System.out.println("生产：" + i);
                    TimeUnit.MILLISECONDS.sleep(600L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
            }
        });
        p.start();

        Thread c = new Thread(() -> {
            while (true){
                try {
                    final Integer take = queue.take();
                    System.out.println("消费：" + take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        c.start();
        while (true){
            TimeUnit.SECONDS.sleep(2L);
        }
    }
}
