package leetcode;

import java.util.ArrayList;
import java.util.List;

public class P46 {
    public static void main(String[] args) {
        System.out.println(new P46().permute(new int[]{1, 2, 3}));
    }

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        doPermute(result, nums, used, new ArrayList<Integer>(),  nums.length);
        return result;
    }

    private void doPermute(List<List<Integer>> result, int[] nums, boolean[] used, List<Integer> currNums, int length) {
        if (currNums.size() == length) {
            List<Integer> newList = new ArrayList<>();
            for (Integer i : currNums) {
                newList.add(i);
            }
            result.add(newList);
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;

            currNums.add(nums[i]);
            used[i] = true;
            doPermute(result, nums, used, currNums, length);
            currNums.remove(currNums.size() - 1);
            used[i] = false;
        }
    }
}
