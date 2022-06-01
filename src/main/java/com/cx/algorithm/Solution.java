package com.cx.algorithm;

import java.util.BitSet;

/**
 * @author chenxiang
 * @create 2022-04-21 11:18
 */
class Solution {
    public static int removeDuplicates(int[] nums) {
        int len = nums.length;
        if(len < 2){
            return len;
        }
        BitSet set = new BitSet();
        set.set(0);
        for(int i = 1; i < len; i++){
            if(nums[i - 1] != nums[i]){
                set.set(i);
            }
        }
        int tempIndex = 0;
        for(int i = 0; i< len; i++){
            if(set.get(i)){
                nums[tempIndex++] = nums[i];
            }
        }
        return tempIndex + 1;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1,1,2};
        System.out.println(removeDuplicates(nums));
    }
}
