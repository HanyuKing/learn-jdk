package algo.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 501. Find Mode in Binary Search Tree (Easy)
 */
public class P501 {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public int[] findMode(TreeNode root) {
        List<Integer> values = new ArrayList<>();
        inOrder(root, values);

        Map<Integer, Integer> map = new HashMap<>();

        int max = Integer.MIN_VALUE;
        for (Integer val : values) {
            int count = map.getOrDefault(val, 0) + 1;
            map.put(val, count);

            max = Math.max(count, max);
        }

        int resultCount = 0;

        for (Map.Entry<Integer, Integer> val : map.entrySet()) {
            if (val.getValue() == max) {
                resultCount++;
            }
        }

        int[] result = new int[resultCount];
        int index = 0;
        for (Map.Entry<Integer, Integer> val : map.entrySet()) {
            if (val.getValue() == max) {
                result[index++] = val.getKey();
            }
        }

        return result;
    }

    private void inOrder(TreeNode root, List<Integer> values) {
        if(root == null) {
            return ;
        }
        inOrder(root.left, values);
        values.add(root.val);
        inOrder(root.right, values);
    }
}
