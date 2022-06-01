package com.cx.algorithm;

import java.util.Arrays;

/**
 * @author chenxiang
 * @create 2022-04-20 11:22
 */
public class TwoSum {

    public static void main(String[] args) {
        int[] nums = new int[] {-1,-2,-3,-4,-5};
        int target = -8;
        int[] ints = twoSum(nums, target);
        System.out.println(ints);
    }

    public static int[] twoSum(int[] nums, int target) {
        int[] ret = new int[2];
        int len = nums.length;
        int[] sortedNums = nums.clone();
        Arrays.sort(sortedNums);
        for(int i = 0 ; i < len ; i++){
            int cur = sortedNums[i];
//            if(cur > target){
//                return ret;
//            }
            int findTarget = target - cur;
            int index = binarySearchInSortedArray(sortedNums, findTarget, i + 1);
            if(index != -1){
                return remapping(nums, sortedNums[i], sortedNums[index]);
            }
        }
        return ret;
    }

    public static int[] remapping(int[] nums, int t1, int t2){
        int i = -1, j = -1;
        for(int index = 0; index < nums.length; index ++ ){
            if(i == -1 && t1 == nums[index]){
                i = index;
            }

            if(t2 == nums[index]){
                j = index;
            }
            if(i != -1 && j != -1 && i != j){
                break;
            }
        }

        return new int[] {i, j};
    }

    public static int binarySearchInSortedArray(int[] sortedNums, int target, int startIndex){
        int leftIndex = startIndex;
        int rightIndex = sortedNums.length - 1;
        while(leftIndex <= rightIndex){
            int midIndex = leftIndex + (rightIndex - leftIndex) / 2;
            int mid = sortedNums[midIndex];
            if(target == mid){
                return midIndex;
            }
            if(target < mid){
                rightIndex = midIndex - 1;
            }else{
                leftIndex = midIndex + 1;
            }
        }
        return -1;
    }
}
