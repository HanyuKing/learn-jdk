package leetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class P653 {
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

    class Solution {
        /**
         * tow sum. using sets
         *
         * @param root
         * @param k
         * @return
         */
        public boolean findTarget(TreeNode root, int k) {
            Set<Integer> values = new HashSet<>();
            return doFindTarget(root, values, k);
        }

        private boolean doFindTarget (TreeNode root, Set<Integer> nums, int k) {
            if (root == null) {
                return false;
            }
            if(nums.contains(k - root.val)) {
                return true;
            }
            nums.add(root.val);
            return doFindTarget(root.left, nums, k) || doFindTarget(root.right, nums, k);
        }
//        public boolean findTarget(TreeNode root, int k) {
//            List<Integer> values = new ArrayList<>();
//            sortedValues(root, values);
//            int i = 0;
//            int j = values.size() - 1;
//            while (i != j) {
//                int sum = values.get(i) + values.get(j);
//                if (sum > k) {
//                    j--;
//                } else if (sum < k) {
//                    i++;
//                } else {
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        private void sortedValues (TreeNode root, List<Integer> nums) {
//            if (root == null) {
//                return ;
//            }
//            sortedValues(root.left, nums);
//            nums.add(root.val);
//            sortedValues(root.right, nums);
//            return ;
//        }
    }
}
