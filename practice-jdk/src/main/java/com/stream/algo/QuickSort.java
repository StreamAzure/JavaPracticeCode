package com.stream.algo;

import java.util.Random;

public class QuickSort {
    public void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    public int[] sortArray(int[] nums) {
        quickSort2(nums, 0, nums.length-1);
        return nums;
    }
    // l,r 左闭右闭
    public void quickSort(int[] nums, int l, int r) {
        if (l >= r) return;
        int pivotIdx = new Random().nextInt(r-l+1) + l;
        swap(nums, pivotIdx, r); // 换到最右边去
        int i = l - 1, j = r + 1;
        while (i < j) {
            do i++; while(i < j && nums[i] < nums[r]);
            do j--; while(i < j && nums[j] >= nums[r]);
            if (i < j) swap(nums, i, j);
        }
        int pos = i; // 归位位置
        swap(nums, pos, r);

        quickSort(nums, l, pos - 1);
        quickSort(nums, pos + 1, r);
    }

    public void quickSort2(int[] nums, int l , int r) {
        if (l >= r) return;
        int pivotIdx = new Random().nextInt(r - l + 1) + l;
        int pivot = nums[pivotIdx];
        int i = l -1, j = r + 1, k = l;
        while (k < r) {
            if (nums[k] < pivot) swap(nums, ++i, k++);
            else if (nums[k] == pivot) k++;
            else swap(nums, --j, k);
        }

        quickSort2(nums, l, i);
        quickSort2(nums, j, r);
    }

    public static void main(String[] args) {
        QuickSort quickSort = new QuickSort();
        int N = 100000000;
        int[] nums = new int[N];
        for (int i= 0;i < N;i++) {
            nums[i] = 2;
        }
        long start = System.nanoTime();
        quickSort.sortArray(nums);
        long end = System.nanoTime();
        long timeElapsed = end - start;
        System.out.println("耗时: " + timeElapsed);
    }
}
