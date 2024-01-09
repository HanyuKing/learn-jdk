package leetcode.hot200;

import org.junit.Test;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2024/1/9 10:41
 * @Description
 * @Version 1.0
 **/
public class Answer21_40 extends Base {
    @Test
    public void testP103() {
        // print(zigzagLevelOrder(new TreeNode(1)));
        // PASS 1. 双端队列，2. reverse
    }

    /**
     * 双端队列
     *
     * @param root
     * @return
     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new LinkedList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int layerSize = 1;
        boolean isLeft = false;
        while (!queue.isEmpty()) {
            Deque<Integer> layerValues = new LinkedList<>();
            int nextLayerSize = 0;
            while (layerSize-- > 0) {
                TreeNode node = queue.poll();

                if (isLeft) {
                    layerValues.addLast(node.val);
                } else {
                    layerValues.addFirst(node.val);
                }

                if (node.left != null) {
                    queue.add(node.left);
                    nextLayerSize++;
                }
                if (node.right != null) {
                    queue.add(node.right);
                    nextLayerSize++;
                }
            }
            result.add(new LinkedList<>(layerValues));
            layerSize = nextLayerSize;
            isLeft = !isLeft;
        }

        return result;
    }

    public List<List<Integer>> zigzagLevelOrder2(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int layerSize = 1;
        boolean isLeft = true;
        while (!queue.isEmpty()) {
            List<Integer> layerValues = new ArrayList<>();
            int nextLayerSize = 0;
            while (layerSize-- > 0) {
                TreeNode node = queue.poll();
                layerValues.add(node.val);

                if (node.left != null) {
                    queue.add(node.left);
                    nextLayerSize++;
                }
                if (node.right != null) {
                    queue.add(node.right);
                    nextLayerSize++;
                }
            }
            layerSize = nextLayerSize;

            if (!isLeft) {
                Collections.reverse(layerValues);
                result.add(layerValues);
            } else {
                result.add(layerValues);
            }

            isLeft = !isLeft;
        }

        return result;
    }
}
