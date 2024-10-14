package algo.hot200;

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

    @Test
    public void testP300() {
        // 输入：nums = [10,9,2,5,3,7,101,18] 输出：4 解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
        //             [2,3,5,7,9,10,18,101]
        // 输入：nums = [0,1,0,3,2,3] 输出：4
        // 输入：nums = [7,7,7,7,7,7,7] 输出：1

        int[] nums = new int[] {10,9,2,5,3,7,101,18};
        print(lengthOfLIS(nums));
    }

    @Test
    public void testP143() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        reorderList(node1);

        print(node1);
    }

    @Test
    public void testP42() {
        // todo
    }

    @Test
    public void testP142() {
        /*
            输入：head = [3,2,0,-4], pos = 1
            输出：返回索引为 1 的链表节点
            解释：链表中有一个环，其尾部连接到第二个节点。
         */
        ListNode node1 = new ListNode(3);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(0);
        ListNode node4 = new ListNode(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node2;

        ListNode node = detectCycle(node1);

        System.out.println(node == null ? null : node.val);

    }

    @Test
    public void testP19() {
        ListNode node1 = new ListNode(3);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(0);
        ListNode node4 = new ListNode(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

        ListNode head = removeNthFromEnd(node1, 2);

        print(head);
    }

    @Test
    public void testP124() {
        // todo
    }

    @Test
    public void testP56() {
        /*
        示例 1：

        输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
        输出：[[1,6],[8,10],[15,18]]
        解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].

        示例 2：

        输入：intervals = [[1,4],[4,5]]
        输出：[[1,5]]
        解释：区间 [1,4] 和 [4,5] 可被视为重叠区间。
         */

        int[][] intervals = new int[][] {
                {8,10},
                {1,3},
                {2,6},
                {15,18}
        };
        int[][] result = merge(intervals);
        print(result);
    }

    @Test
    public void testP72() {
        // todo
        /*
        示例 1：

        输入：word1 = "horse", word2 = "ros"
        输出：3
        解释：
        horse -> rorse (将 'h' 替换为 'r')
        rorse -> rose (删除 'r')
        rose -> ros (删除 'e')

        示例 2：

        输入：word1 = "intention", word2 = "execution"
        输出：5
        解释：
        intention -> inention (删除 't')
        inention -> enention (将 'i' 替换为 'e')
        enention -> exention (将 'n' 替换为 'x')
        exention -> exection (将 'n' 替换为 'c')
        exection -> execution (插入 'u')
         */
    }

    @Test
    public void testP94() {
        // 递归 PASS
        // todo 迭代
    }

    @Test
    public void testP1250() {
        // todo
    }

    @Test
    public void testP199() {
        /*
        输入: [1,2,3,null,5,null,4]
        输出: [1,3,4]
        示例 2:

        输入: [1,null,3]
        输出: [1,3]
        示例 3:

        输入: []
        输出: []
         */


        // BFS PASS
        // DFS PASS
    }

    @Test
    public void testP235() {
        // 1. 不是搜索树时：lowestCommonAncestor2
        // 2. 是搜索树时：lowestCommonAncestor
    }

    @Test
    public void testP2244() {
        int[] tasks = new int[] {2,2,3,3,2,4,4,4,4,4};
        print(minimumRounds(tasks));

        tasks = new int[] {2,3,3};
        print(minimumRounds(tasks));

        tasks = new int[] {5,5,5,5};
        print(minimumRounds(tasks));

        tasks = new int[] {9,9,11,9,22,22,11,552,22,22,55};
        print(minimumRounds(tasks));
    }

    @Test
    public void testP792() {
        // todo 优化暴力方法

        String s = "abcde";
        String[] words = new String[] {"a","bb","acd","ace"};
        int cnt = numMatchingSubseq(s, words);
        print(cnt);

        s = "dsahjpjauf";
        words = new String[] {"ahjpjau","ja","ahbwzgqnuk","tnmlanowax"};
        cnt = numMatchingSubseq(s, words);
        print(cnt);
    }

    @Test
    public void testP232() {
        // PASS
    }

    @Test
    public void testP82() {
        // 输入：head = [1,2,3,3,4,4,5] 输出：[1,2,5]

        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(3);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(4);
        ListNode node6 = new ListNode(5);

        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;

        print(head);

        head = deleteDuplicates(head);

        print(head);

        ///////////////////////////////////////////
        // 输入：head = [1,1,1,2,3] 输出：[2,3]
    }

    @Test
    public void testP82_1() {
        // // 输入：head = [1,1,1,2,3] 输出：[2,3]

        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
//        ListNode node3 = new ListNode(2);
//        ListNode node4 = new ListNode(3);

        head.next = node1;
        node1.next = node2;
//        node2.next = node3;
//        node3.next = node4;

        print(head);

        head = deleteDuplicates(head);

        print(head);
    }

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode tail = null;
        ListNode p1 = head;
        ListNode p2 = head.next;

        boolean duplicate = false;

        while (p2 != null) {
            if (p1.val == p2.val) {
                p2 = p2.next;
                duplicate = true;
                continue;
            }

            if (!duplicate) {
                if (tail == null) {
                    tail = p1;
                    head = p1;
                    tail.next = null;
                } else {
                    tail.next = p1;
                    tail = p1;
                    tail.next = null;
                }
            }

            duplicate = false;

            p1 = p2;
            p2 = p2.next;

        }

        if (!duplicate) {
            if (tail == null) {
                head = tail = p1;
            } else {
                tail.next = p1;
            }
        }

        return tail == null ? null : head;
    }

    class MyQueue {
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();
        public MyQueue() {

        }

        public void push(int x) {
            // 1,2,3
            s1.push(x);
        }

        public int pop() {
            // 1,2,3
            if (empty()) {
                return -1;
            }
            if (s2.isEmpty()) {
                while (!s1.isEmpty()) {
                    s2.push(s1.pop());
                }
            }
            return s2.pop();
        }

        public int peek() {
            if (empty()) {
                return -1;
            }
            if (s2.isEmpty()) {
                while (!s1.isEmpty()) {
                    s2.push(s1.pop());
                }
            }
            return s2.peek();
        }

        public boolean empty() {
            return s1.isEmpty() && s2.isEmpty();
        }
    }

    public int numMatchingSubseq(String s, String[] words) {
        int cnt = 0;
        for (String word : words) {
            if (matchingSubseq(s, word)) {
                cnt++;
            }
        }
        return cnt;
    }

    private boolean matchingSubseq(String s, String word) {
        if (s.length() < word.length()) {
            return false;
        }
        int i1 = 0, i2 = 0;
        while (i1 < word.length() && i2 < s.length()) {
            if (word.charAt(i1) == s.charAt(i2)) {
                i1++;
                i2++;
            } else {
                i2++;
            }
        }

        return i1 == word.length();
    }

    public int minimumRounds(int[] tasks) {
        Map<Integer, Integer> data = new HashMap<>();
        for (int t : tasks) {
            data.compute(t, (k, v) -> v == null ? 1 : v + 1);
        }
        int totalStep = 0;
        for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
            int step = taskRounds(entry.getValue());
            if (step == -1) {
                return -1;
            }
            totalStep += step;
        }
        return totalStep;
    }

    private int taskRounds(Integer count) {
        if (count == 1) {
            return -1;
        } else if (count % 3 == 0) {
            return count / 3;
        } else {
            return 1 + count / 3;
        }
    }

    public TreeNode lowestCommonAncestor3(TreeNode root, TreeNode p, TreeNode q) {
        if(root.val > p.val && root.val > q.val){
            return lowestCommonAncestor(root.left, p, q);
        }
        if(root.val < p.val && root.val < q.val){
            return lowestCommonAncestor(root.right, p, q);
        }
        return root;
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        int min = Math.min(p.val, q.val);
        int max = Math.max(p.val, q.val);

        if (root.val >= min && root.val <= max) {
            return root;
        } else if (root.val < min) {
            return lowestCommonAncestor(root.right, p, q);
        } else {
            return lowestCommonAncestor(root.left, p, q);
        }
    }

    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (root == p) {
            return p;
        }
        if (root == q) {
            return q;
        }
        TreeNode lp = findNode(root.left, p);
        TreeNode lq = findNode(root.left, q);
        TreeNode rp = findNode(root.right, p);
        TreeNode rq = findNode(root.right, q);
        if (lp != null && rq != null) {
            return root;
        } else if (lp != null && lq != null) {
            return lowestCommonAncestor(root.left, p, q);
        } else if (rp != null && rq != null) {
            return lowestCommonAncestor(root.right, p, q);
        } else if (rp != null && lq != null) {
            return root;
        }
        return null;
    }

    private TreeNode findNode(TreeNode root, TreeNode node) {
        if (root == null) {
            return null;
        }
        if (root == node) {
            return node;
        }
        TreeNode n = findNode(root.left, node);
        if (n != null) {
            return n;
        }
        n = findNode(root.right, node);
        if (n != null) {
            return n;
        }
        return null;
    }


    public List<Integer> rightSideView2(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        rightSideViewDFS(root, result, 0);
        return result;
    }

    private void rightSideViewDFS(TreeNode root, List<Integer> result, int depth) {
        if (root == null) {
            return;
        }
        if (depth == result.size()) {
            result.add(root.val);
        }
        depth++;
        rightSideViewDFS(root.right, result, depth);
        rightSideViewDFS(root.left, result, depth);
    }

    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int levelSize = 1;

        while (!queue.isEmpty()) {
            int nextLevelSize = 0;
            while (levelSize > 0) {
                TreeNode currNode = queue.poll();
                if (currNode.left != null) {
                    queue.add(currNode.left);
                    nextLevelSize++;
                }
                if (currNode.right != null) {
                    queue.add(currNode.right);
                    nextLevelSize++;
                }

                levelSize--;

                if (levelSize == 0) {
                    result.add(currNode.val);
                }
            }
            levelSize = nextLevelSize;
        }

        return result;
    }

    public List<Integer> inorderTraversal2(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.add(root);
        while (!stack.isEmpty()) {
            TreeNode currRoot = stack.pop();
            TreeNode leftNode = currRoot.left;
            TreeNode rightNode = currRoot.right;
            while (leftNode != null) {
                stack.add(leftNode);
                leftNode = leftNode.left;
            }

            result.add(currRoot.val);

            while (rightNode != null) {
                stack.add(rightNode);
                rightNode = rightNode.right;
            }
        }

        return result;
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        doInorderTraversal(result, root);
        return result;
    }

    private void doInorderTraversal(List<Integer> result, TreeNode root) {
        if (root == null) {
            return;
        }
        doInorderTraversal(result, root.left);
        result.add(root.val);
        doInorderTraversal(result, root.right);
    }

    public int minDistance(String word1, String word2) {
        return 0;
    }

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));

        int index = -1;

        int[][] res = new int[intervals.length][2];

        for (int[] currInterval : intervals) {
            if (index == -1 || currInterval[0] > res[index][1]) {
                res[++index] = currInterval;
            } else {
                res[index][1] = Math.max(res[index][1], currInterval[1]);
            }
        }

        return Arrays.copyOf(res, index + 1);
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode p1 = head;
        ListNode p2 = head;

        while (--n > 0) {
            p2 = p2.next;
        }

        if (p2.next == null) {
            return head.next;
        }

        ListNode pre = null;
        while (p2.next != null) {
            pre = p1;
            p1 = p1.next;
            p2 = p2.next;
        }

        pre.next = p1.next;

        return head;
    }

    public ListNode detectCycle(ListNode head) {
        ListNode p1 = head;
        ListNode p2 = head;
        while (p2 != null && p2.next != null) {
            p1 = p1.next;
            p2 = p2.next.next;
            if (p1 == p2) {
                break;
            }
        }

        if (p2 == null || p2.next == null) {
            return null;
        }

        p2 = head;
        while (p1 != p2) {
            p1 = p1.next;
            p2 = p2.next;
        }

        return p1;
    }

    /**
     * todo 快慢指针
     * @param head
     */
    public void reorderList(ListNode head) {
        ListNode headTemp = head;
        Stack<ListNode> stack = new Stack<>();
        while (headTemp != null) {
            stack.add(headTemp);
            headTemp = headTemp.next;
        }

        boolean isLinkStack = true;
        ListNode tail = head;
        ListNode newHead = head.next;

        int count = 2;
        // boolean isOdd = stack.size() % 2 == 0;
        // int limit = isOdd ? stack.size() / 2 : stack.size() / 2 + 1;
        int size = stack.size();

        while (count <= size) {
            if (isLinkStack) {
                tail.next = stack.pop();
                tail = tail.next;

                tail.next = null; // unlink

                isLinkStack = false;
            } else {
                tail.next = newHead;
                tail = newHead;
                newHead = newHead.next;

                tail.next = null; // unlink

                isLinkStack = true;
            }

            count++;
        }
    }

    public int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = 1;
        int max = 1;
        for (int i = 1; i < nums.length; i++) {
            int count = 1;
            for (int j = i - 1; j >= 0; j--) {
                if (nums[i] > nums[j] && dp[j] + 1 > count) {
                    count = dp[j] + 1;
                    max = Math.max(count, max);
                }
            }
            dp[i] = count;
        }
        return max;
    }

    public int lengthOfLIS2(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int[] dp = new int[nums.length];
        dp[0] = 1;
        int maxans = 1;
        for (int i = 1; i < nums.length; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxans = Math.max(maxans, dp[i]);
        }
        return maxans;
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
