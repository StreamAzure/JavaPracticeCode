package com.stream.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permute {
    static List<List<Integer>> ans = new ArrayList<List<Integer>>();
    public static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
    public static void solve(int[] nums, int pos) {
        if (pos == nums.length - 1) {
            List<Integer> result = new ArrayList<Integer>();
            for (int num : nums) {
                result.add(num);
            }
            ans.add(result);
            return;
        }
        solve(nums, pos + 1);
        swap(nums, pos, pos + 1);
        solve(nums, pos + 1);
        swap(nums, pos, pos + 1);
    }
    public static List<List<Integer>> permute(int[] nums) {
        solve(nums, 0);
        return ans;
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> permute = permute(nums);
        for (List<Integer> list : permute) {
            System.out.println(Arrays.toString(list.toArray()));
        }
    }
}
