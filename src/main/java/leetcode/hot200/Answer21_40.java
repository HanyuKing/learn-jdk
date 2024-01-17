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

    @Test
    public void testP54() {
        int[][] matrix = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

//        print(spiralOrder(matrix));

        matrix = new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };

        print(spiralOrder(matrix));
    }

    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();

        int row = matrix.length;
        int col = matrix[0].length;

        int up = 0, left = 0;
        int down = row - 1;
        int right = col - 1;

        while (up <= down && left <= right) {
            int i = up;
            int j = left;

            while (j <= right) {
                result.add(matrix[i][j]);
                j++;
            }
            j = right;
            i++;
            while (i <= down) {
                result.add(matrix[i][j]);
                i++;
            }
            i = down;
            j--;
            while (j >= left && i != up) {
                result.add(matrix[i][j]);
                j--;
            }
            j = left;
            i--;
            while (i > up && j != right) {
                result.add(matrix[i][j]);
                i--;
            }

            left++; right--; up++; down--;
        }

        return result;
    }

    @Test
    public void testP92() {
        // Case1: [1,2,3,4,5], left = 2, right = 4
        ListNode head = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        head.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        head = reverseBetween(head, 2, 4);
        print(head);
//
        // Case2: [5], left = 1, right = 1
        head = new ListNode(5);
        head = reverseBetween(head, 2, 4);
        print(head);

        // Case3: [3, 5], left = 1, right = 2
        head = new ListNode(3);
        head.next = new ListNode(5);
        head = reverseBetween(head, 1, 2);
        print(head);

        head = new ListNode(1);
        node2 = new ListNode(2);
        node3 = new ListNode(3);
        head.next = node2;
        node2.next = node3;
        head = reverseBetween(head, 1, 2);
        print(head);
    }

    public ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode pHead = head;
        int count = 0;

        // find start
        ListNode pHeadPre = null;
        while (++count < left) {
            pHeadPre = pHead;
            pHead = pHead.next;
        }
        if (pHead == null || left == right) {
            return head;
        }

        ListNode q = pHead.next;
        ListNode pStart = pHead;
        pHead.next = null;

        while (q != null) {
            ListNode r = q.next;
            q.next = pHead;
            pHead = q;
            q = r;
            if (++count == right) {
                break;
            }
        }

        if (pHeadPre == null) {
            head = pHead;
            pStart.next = q;
        } else {
            pHeadPre.next = pHead;
            pStart.next = q;
        }
        return head;
    }

    @Test
    public void testP160() {
        // intersectVal = 8, listA = [4,1,8,4,5], listB = [5,6,1,8,4,5]
        ListNode node1 = new ListNode(5);
        ListNode node2 = new ListNode(6);
        ListNode node3 = new ListNode(1);
        ListNode node4 = new ListNode(8);
        ListNode node5 = new ListNode(4);
        ListNode node6 = new ListNode(5);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;

        ListNode node7 = new ListNode(4);
        ListNode node8 = new ListNode(1);

        node7.next = node8;
        node8.next = node4;

        node1 = getIntersectionNode(node7, node1);
        print(node1);
    }

    @Test
    public void testP23() {
        // todo
    }

    @Test
    public void testP415() {
//        示例 1：
//        输入：num1 = "11", num2 = "123"
//        输出："134"
        print(addStrings("11", "123"));
//        示例 2：
//        输入：num1 = "456", num2 = "77"
//        输出："533"
        print(addStrings("456", "77"));
//        示例 3：
//        输入：num1 = "0", num2 = "0"
//        输出："0"
        print(addStrings("0", "0"));
    }

    public String addStrings(String num1, String num2) {
        int carry = 0;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < Math.max(num1.length(), num2.length()); i++) {
            int a = 0, b = 0;
            if (i < num1.length()) {
                a = num1.charAt(num1.length() - i - 1) - '0';
            }
            if (i < num2.length()) {
                b = num2.charAt(num2.length() - i - 1) - '0';
            }
            int sumAB = a + b + carry;
            result.append(sumAB % 10);
            carry = sumAB / 10;
        }
        return carry > 0 ? result.append(carry).reverse().toString()
                : result.reverse().toString();
    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode pA = headA;
        ListNode pB = headB;
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        return pA;
    }

    public ListNode getIntersectionNodeReverse(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode p = head;
        ListNode q = p.next;
        head.next = null;
        while (q != null) {
            ListNode r = q.next;
            q.next = p;
            p = q;
            q = r;
        }
        return p;
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
