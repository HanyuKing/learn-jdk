package leetcode.hot200;

import com.google.gson.Gson;

/**
 * @Author Hanyu.Wang
 * @Date 2024/1/9 10:43
 * @Description
 * @Version 1.0
 **/
public class Base {
    protected class TreeNode {
        int val;
        Answer1_20.TreeNode left;
        Answer1_20.TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, Answer1_20.TreeNode left, Answer1_20.TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    protected void print(Object o) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(o));
    }
}
