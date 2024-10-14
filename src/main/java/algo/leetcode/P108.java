package algo.leetcode;

public class P108 {

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
        public TreeNode sortedArrayToBST(int[] nums) {
            if (nums.length == 0) {
                return null;
            }
            return doArrayToBST(0, nums.length - 1, nums);
        }

        private TreeNode doArrayToBST(int low, int high, int[] nums) {
            if (low > high) {
                return null;
            }
            int mid = (low + high) / 2;
            TreeNode root = new TreeNode(nums[mid]);

            root.left = doArrayToBST(low, mid - 1, nums);
            root.right = doArrayToBST(mid + 1, high, nums);

            return root;
        }

    }
}
