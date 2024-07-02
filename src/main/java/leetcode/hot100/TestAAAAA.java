package leetcode.hot100;

import org.junit.Test;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

/**
 * @Author Hanyu.Wang
 * @Date 2024/6/16 17:52
 * @Description
 * @Version 1.0
 **/
public class TestAAAAA {
    @Test
    public void testP49() {

    }

    // 49
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] chs = str.toCharArray();
            Arrays.sort(chs);
            map.putIfAbsent(String.valueOf(chs), new ArrayList<>());
            map.get(String.valueOf(chs)).add(str);
        }
        return new ArrayList<>(map.values());
    }

    @Test
    public void testP128() {
        int[] nums = new int[] {100,4,200,1,3,2};
        int result = longestConsecutive(nums);
        System.out.println(result);
    }

    // 128
    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }

        Set<Integer> readedNumSet = new HashSet<>();

        int longest = 1;

        for (int num : numSet) {
            if (!readedNumSet.contains(num)) {
                readedNumSet.add(num);
                int currLongest = 1;

                for (num = num + 1; numSet.contains(num); num++) {
                    currLongest++;
                    readedNumSet.add(num);
                }

                longest = Math.max(longest, currLongest);
            }
        }

        return longest;
    }

    // 128-2
    public int longestConsecutive2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }

        int longest = 1;

        for (int num : numSet) {
            if (!numSet.contains(num - 1)) {
                int currLongest = 1;

                for (num = num + 1; numSet.contains(num); num++) {
                    currLongest++;
                }

                longest = Math.max(longest, currLongest);
            }
        }

        return longest;
    }

    @Test
    public void testP42() {
        int[] height = new int[] {4,2,0,3,2,5};
        int rs = trap(height);
        System.out.println(rs);
    }
    public int trap(int[] height) {
        int ans = 0;
        int[] leftMax = new int[height.length];
        int[] rightMax = new int[height.length];
        for (int i = 1; i < height.length; i++) {
            leftMax[i] = Math.max(height[i - 1], leftMax[i - 1]);
        }
        for (int i = height.length - 2; i >= 0; i--) {
            rightMax[i] = Math.max(height[i + 1], rightMax[i + 1]);
        }
        for (int i = 1; i < height.length; i++) {
            int leftMaxH = leftMax[i];
            int rightMaxH = rightMax[i];
//            for (int left = i - 1; left >= 0; left--) {
//                if (height[left] > height[i]) {
//                    leftMaxH = Math.max(leftMaxH, height[left]);
//                }
//            }
//            for (int right = i + 1; right < height.length; right++) {
//                if (height[right] > height[i]) {
//                    rightMaxH = Math.max(rightMaxH, height[right]);
//                }
//            }
            int min = Math.min(leftMaxH, rightMaxH);
            if (min > height[i]) {
                ans += (min - height[i]);
            }

        }
        return ans;
    }

    public int trap2(int[] height) {
        int ans = 0;
        int leftMax = 0;
        int rightMax = 0;
        int left = 0;
        int right = height.length - 1;

        while (left < right) {
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);
            if (height[left] < height[right]) {
                ans += leftMax - height[left];
                left++;
            } else {
                ans += rightMax - height[right];
                right--;
            }
        }
        return ans;
    }

    @Test
    public void testP3() {
        int res = lengthOfLongestSubstring2("pwwkew");
        System.out.println(res);
    }
    public int lengthOfLongestSubstring(String s) {
        if (s.length() < 2) {
            return s.length();
        }
        int maxLen = 1;

        Set<Character> set = new HashSet<>();
        int left = 0;
        int right = 1;
        set.add(s.charAt(left));
        while (right < s.length()) {
            while (right < s.length() && !set.contains(s.charAt(right))) {
                set.add(s.charAt(right));
                right++;
            }
            maxLen = Math.max(maxLen, right - left);
            set.remove(s.charAt(left));
            left++;
        }

        return maxLen;
    }

    public int lengthOfLongestSubstring2(String s) {
        if (s.length() < 2) {
            return s.length();
        }
        int maxLen = 1;

        Map<Character, Integer> set = new HashMap<>();
        int left = 0;
        int right = 1;
        set.put(s.charAt(left), 0);
        while (right < s.length()) {
            while (right < s.length() && !set.containsKey(s.charAt(right))) {
                set.put(s.charAt(right), right);
                right++;
            }

            int newLeft = left;
            if (right < s.length() && set.containsKey(s.charAt(right))) {
                newLeft = set.get(s.charAt(right));
                set.put(s.charAt(left), right);
            }

            maxLen = Math.max(maxLen, right - left);

            if (newLeft != left) {
                left = newLeft;
            }
            left++;

        }

        return maxLen;
    }

    @Test
    public void testP239() {
        /*
            给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。

            返回 滑动窗口中的最大值 。

            示例 1：

            输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
            输出：[3,3,5,5,6,7]
        */
        int[] nums = new int[] {1,3,-1,-3,5,3,6,7};
        int k = 3;

        nums = new int[] {1, -1};
        k = 1;
        int[] result = maxSlidingWindowGuanFang(nums, k);
        for (int n : result) {
            System.out.print(n + " ");
        }
        System.out.println();
    }
    public int[] maxSlidingWindowGuanFang(int[] nums, int k) {
        int n = nums.length;
        Deque<Integer> deque = new LinkedList<Integer>();
        for (int i = 0; i < k; ++i) {
            while (!deque.isEmpty() && nums[i] >= nums[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
        }

        int[] ans = new int[n - k + 1];
        ans[0] = nums[deque.peekFirst()];
        for (int i = k; i < n; ++i) {
            while (!deque.isEmpty() && nums[i] >= nums[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            while (deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            ans[i - k + 1] = nums[deque.peekFirst()];
        }
        return ans;
    }
    public int[] maxSlidingWindow(int[] nums, int k) {
        // n
        //
        int[] result = new int[nums.length - k + 1];
        int index = 0;
        Deque<Integer> deque = new LinkedList<>();

        for (int i = 0; i < k - 1; i++) {
            while (!deque.isEmpty() && nums[i] >= nums[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
        }

        for (int i = k - 1; i < nums.length; i++) {
            while (!deque.isEmpty() && nums[i] >= nums[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            result[index++] = nums[deque.getFirst()];

            while (!deque.isEmpty() && deque.peekFirst() <= i - k + 1) {
                deque.pollFirst();
            }
        }

        return result;
    }
    public int[] maxSlidingWindow3(int[] nums, int k) {
        // n * k
        // n * logk
        //
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);
        int[] result = new int[nums.length - k + 1];
        for (int i = 0; i < k - 1; i++) {
            priorityQueue.add(nums[i]);
        }
        int index = 0;
        for (int i = k - 1; i < nums.length; i++) {
            priorityQueue.add(nums[i]);
            result[index++] = priorityQueue.peek();
            priorityQueue.remove(nums[i - (k - 1)]);
        }
        return result;
    }
    public int[] maxSlidingWindow2(int[] nums, int k) {
        // n * k
        // n * logk
        //
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] == o2[0] ? o1[1] - o2[1] : o2[0] - o1[0];
            }
        });
        int[] result = new int[nums.length - k + 1];
        for (int i = 0; i < k - 1; i++) {
            priorityQueue.add(new int[] {nums[i], i});
        }
        int index = 0;
        for (int i = k - 1; i < nums.length; i++) {
            priorityQueue.add(new int[] {nums[i], i});
            result[index++] = priorityQueue.peek()[0];

            while (!priorityQueue.isEmpty() && priorityQueue.peek()[1] <= i - k + 1) {
                priorityQueue.poll();
            }

        }
        return result;
    }

    @Test
    public void testP438() {
        String s = "cbaebabacd", p = "abc"; // [0, 6]

        System.out.println(findAnagrams(s, p));
    }

    public List<Integer> findAnagrams(String s, String p) {
        int[] counts = new int[26];
        int[] needCounts = new int[26];

        int needValidCount = 0;
        for (Character c : p.toCharArray()) {
            needCounts[c - 'a']++;
        }
        for (int c : needCounts) {
            if (c > 0) {
                needValidCount++;
            }
        }

        int left = 0;
        int right = 0;
        int validCount = 0;

        List<Integer> result = new ArrayList<>();

        while (right < s.length()) {
            char rChar = s.charAt(right);
            int rCharIndex = rChar - 'a';
            if (needCounts[rCharIndex] > 0) { // 存在
                counts[rCharIndex]++;
                if (counts[rCharIndex] == needCounts[rCharIndex]) { // 数量相等，合法字符数加1
                    validCount++;
                }
            }
            right++;

            while (validCount == needValidCount) {
                if (right - left == p.length()) {
                    result.add(left);
                }
                char lChar = s.charAt(left);
                int lCharIndex = lChar - 'a';
                if (counts[lCharIndex] > 0) {
                    counts[lCharIndex]--;
                    if (counts[lCharIndex] < needCounts[lCharIndex]) {
                        validCount--;
                    }
                }
                left++;
            }
        }

        return result;
    }

    @Test
    public void testP56() {
        /*
        输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
        输出：[[1,6],[8,10],[15,18]]
        解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
         */
        int[][] intervals = new int[][] {{1,3},{2,6},{8,10},{15,18}};
        int[][] result = merge(intervals);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < result.length; i++) {
            sb.append("[");
            for (int j = 0; j < result[0].length; j++) {
                sb.append(result[i][j]).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
        }
        sb.append("]");
        System.out.println(sb);
    }

    public int[][] merge(int[][] intervals) {
        int[][] result = new int[intervals.length][2];
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] -  o2[0];
            }
        });
        int index = 0;
        result[index] = intervals[index];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= result[index][1]) {
                result[index][1] = Math.max(result[index][1], intervals[i][1]);
            } else {
                result[++index] = intervals[i];
            }
        }
        return Arrays.copyOf(result, index + 1);
    }

    @Test
    public void testP189() {
        int[] nums = new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13};
        int k = 17;
        // [5,6,7,1,2,3,4]
        // 4,3,2,1,7,6,5
        // [-1,-100,3,99]
        rotate(nums, k);

        for (int n : nums) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    public void rotate(int[] nums, int k) {
        k = k % nums.length;
        reverse(nums, 0, nums.length - k - 1);
        reverse(nums, nums.length - k, nums.length - 1);
        reverse(nums, 0, nums.length - 1);
    }
    private void reverse(int[] nums, int start, int end) {
        if (start < 0 || end < 0 || end - start <= 0) {
            return;
        }
        int len = end - start + 1;
        for (int i = 0; i < len / 2; i++) {
            int temp = nums[start + i];
            nums[start + i] = nums[end - i];
            nums[end - i] = temp;
        }
    }

    @Test
    public void testP238() {
        /*
            输入: nums = [1,2,3,4]
            输出: [24,12,8,6]
         */
    }
    public int[] productExceptSelf(int[] nums) {
        int[] result = new int[nums.length];
        result[nums.length - 1] = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            result[i] = nums[i + 1] * result[i + 1];
        }
        int leftMulti = nums[0];
        for (int i = 1; i < nums.length; i++) {
            result[i] = leftMulti * result[i];
            leftMulti = leftMulti * nums[i];
        }
        return result;
    }

    @Test
    public void testP73() {
        int[][] matrix = {
                {1,1,1},
                {1,0,1},
                {1,1,1}};
                /*
                [[1,0,1],
                 [0,0,0],
                 [1,0,1]]
             */

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

        setZeroes(matrix);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

    }

    public void setZeroes3(int[][] matrix) {
        boolean[] rows = new boolean[matrix.length];
        boolean[] cols = new boolean[matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rows[i] = true;
                    cols[j] = true;
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (rows[i] || cols[j]) {
                    matrix[i][j] = 0;
                }
            }
        }
    }

    public void setZeroes(int[][] matrix) {
        boolean[][] changed = new boolean[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (!changed[i][j] && matrix[i][j] == 0) {
                    for (int k = 0; k < matrix[0].length; k++) {
                        if (matrix[i][k] != 0) {
                            matrix[i][k] = 0;
                            changed[i][k] = true;
                        }
                    }
                    for (int k = 0; k < matrix.length; k++) {
                        if (matrix[k][j] != 0) {
                            matrix[k][j] = 0;
                            changed[k][j] = true;
                        }
                    }
                }
            }
        }
    }

    public void setZeroes2(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        boolean flagCol0 = false, flagRow0 = false;
        for (int i = 0; i < n; i++) {
            if (matrix[0][i] == 0) {
                flagCol0 = true;
            }
        }
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) {
                flagRow0 = true;
            }
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }

        if (flagCol0) {
            for (int i = 0; i < n; i++) {
                matrix[0][i] = 0;
            }
        }
        if (flagRow0) {
            for (int i = 0; i < m; i++) {
                matrix[i][0] = 0;
            }
        }
    }

    @Test
    public void testP160() {

    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        // a + c
        // b + c
        // a + c + b + c = b + c + a + c
        ListNode pA = headA;
        ListNode pB = headB;
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        return pA;
    }

    public ListNode reverseList2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode p = head;
        ListNode q = p.next;
        p.next = null;
        // null<-p q->r->null
        // null<-1<-p  q->null
        while (q.next != null) {
            ListNode r = q.next;
            q.next = p;
            p = q;
            q = r;
        }

        q.next = p;
        return q;
    }

    @Test
    public void tesyP206() {
        ListNode head = new ListNode(1);
        ListNode n1 = new ListNode(2);
        ListNode n2 = new ListNode(3);
        head.next = n1;
        n1.next = n2;
        reverseList(head);
    }

    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        // 1->2>3->null
        ListNode newHead = reverseList(head.next);
        head.next.next = head;
        head.next = null;

        return newHead;
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        int nextLevelCount = 1;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            List<Integer> levelResult = new ArrayList<>();
            int currLevelCount = nextLevelCount;
            nextLevelCount = 0;
            for (int i = 0; i < currLevelCount; i++) {
                TreeNode node = queue.poll();
                levelResult.add(node.val);
                if (node.left != null) {
                    queue.add(node.left);
                    nextLevelCount++;
                }
                if (node.right != null) {
                    queue.add(node.right);
                    nextLevelCount++;
                }
            }
            result.add(levelResult);
        }
        return result;
    }

    public TreeNode sortedArrayToBST(int[] nums) {
        if (nums.length == 0) {
            return null;
        }
        int mid = nums.length / 2;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = sortedArrayToBST(Arrays.copyOfRange(nums, 0, mid));
        root.right = sortedArrayToBST(Arrays.copyOfRange(nums, mid + 1, nums.length));
        return root;
    }

    @Test
    public void testP98() {
        TreeNode root = new TreeNode(2);
        TreeNode node1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(3);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(7);
        root.left = node1;
        root.right = node2;
//        node2.left = node3;
//        node2.right = node4;

        System.out.println(isValidBST(root));
    }

    public boolean isValidBST2(TreeNode root) {
        if (root == null) {
            return true;
        }
        long min = Long.MIN_VALUE;
        long max = Long.MAX_VALUE;
        return isValidBST(root, min, max);
    }

    private boolean isValidBST(TreeNode root, long min, long max) {
        if (root == null) {
            return true;
        }
        if (root.val <= min || root.val >= max) {
            return false;
        }
        return isValidBST(root.left, min, root.val) && isValidBST(root.right, root.val, max);
    }

    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return false;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        long pre = Long.MIN_VALUE;
        while (!stack.isEmpty()) {
            root = stack.pop();
            while (root != null) {
                stack.push(root);
                root = root.left;
            }

            if (stack.isEmpty()) break;

            TreeNode node = stack.pop();

            System.out.println(node.val);

            if (node.val <= pre) {
                return false;
            }
            pre = node.val;

            if (node.right != null) {
                stack.push(node.right);
            } else {
                stack.push(null);
            }
        }
        return true;
    }
    @Test
    public void testP230() {
        TreeNode root = new TreeNode(3);
        TreeNode node1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(7);
        root.left = node1;
        node1.right = node2;
//        node2.left = node3;
//        node2.right = node4;

        System.out.println(kthSmallest(root, 3));
    }

    public int kthSmallest2(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();

        int count = 0;

        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            TreeNode node = stack.pop();
            if (++count == k) {
                return node.val;
            }
            if (node.right != null) {
                root = node.right;
            }
        }
        return -1;
    }

    private int count = 0;
    private int val = 0;
    public int kthSmallest(TreeNode root, int k) {
        doKthSmallest(root, k);
        return val;
    }

    public void doKthSmallest(TreeNode root, int k) {
        if (root == null) {
            return ;
        }
        kthSmallest(root.left, k);
        if (++count == k) {
            val = root.val;
        }
        kthSmallest(root.right, k);
    }

    @Test
    public void testP199() {
        TreeNode root = new TreeNode(1);
        TreeNode node1 = new TreeNode(3);
        root.right = node1;

        System.out.println(doRightSideView(root));
    }

    public List<Integer> doRightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int nextLevelCount = 1;
        while (!queue.isEmpty()) {
            TreeNode lastNode = null;
            int count = 0;
            for (int i = 0; i < nextLevelCount; i++) {
                lastNode = queue.poll();
                if (lastNode.left != null) {
                    queue.offer(lastNode.left);
                    count++;
                }
                if (lastNode.right != null) {
                    queue.offer(lastNode.right);
                    count++;
                }
            }
            nextLevelCount = count;
            result.add(lastNode.val);
        }

        return result;
    }

    @Test
    public void testP103() {
        TreeNode root = new TreeNode(1);
        TreeNode node1 = new TreeNode(2);
        TreeNode node2 = new TreeNode(3);
        TreeNode node3 = new TreeNode(4);
        TreeNode node4 = new TreeNode(5);
        root.left = node1;
        root.right = node2;
        node2.right = node4;
        node1.left = node3;

        System.out.println(zigzagLevelOrder(root));
    }

    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int nextLevelCount = 1;

        boolean layerLeftStart = true;

        while (!queue.isEmpty()) {
            List<Integer> values = new ArrayList<>();
            int count = 0;
            for (int i = 0; i < nextLevelCount; i++) {
                TreeNode node = queue.poll();
                values.add(node.val);

                if (node.left != null) {
                    queue.offer(node.left);
                    count++;
                }
                if (node.right != null) {
                    queue.offer(node.right);
                    count++;
                }
            }
            nextLevelCount = count;
            if (!layerLeftStart) {
                Collections.reverse(values);
            }
            result.add(values);
            layerLeftStart = !layerLeftStart;
        }

        return result;
    }

    @Test
    public void testP114() {
        TreeNode root = new TreeNode(1);
        TreeNode node1 = new TreeNode(2);
        TreeNode node2 = new TreeNode(5);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(6);
        root.left = node1;
        root.right = node2;
        node1.left = node3;
        node1.right = node4;
        node2.right = node5;
        flatten(root);
    }
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        List<TreeNode> nodeList = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            nodeList.add(node);
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        for (int i = 0; i < nodeList.size() - 1; i++) {
            nodeList.get(i).left = null;
            nodeList.get(i).right= nodeList.get(i + 1);
        }
        nodeList.get(nodeList.size() - 1).right = null;
        nodeList.get(nodeList.size() - 1).left = null;
    }

    @Test
    public void testP437() {

    }

    public int pathSum(TreeNode root, int targetSum) {
        int ans = 0;
        if (root == null) {
            return ans;
        }
        ans = rootSum(root, targetSum);
        ans += pathSum(root.left, targetSum);
        ans += pathSum(root.right, targetSum);
        return ans;
    }

    private int rootSum(TreeNode root, long targetSum) {
        int ret = 0;

        if (root == null) {
            return 0;
        }

        int val = root.val;
        if (val == targetSum) {
            ret++;
        }
        ret += rootSum(root.left, targetSum - val);
        ret += rootSum(root.right, targetSum - val);
        return ret;
    }

    @Test
    public void testP236() {

    }
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }
        if (root == p || root == q) {
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        if (left != null && right != null) {
            return root;
        }
        return left == null ? right : left;
    }

    @Test
    public void testP124() {
        TreeNode root = new TreeNode(1);
        TreeNode node1 = new TreeNode(2);
        TreeNode node2 = new TreeNode(3);
        TreeNode node3 = new TreeNode(15);
        TreeNode node4 = new TreeNode(7);
        root.left = node1;
        root.right = node2;
//        node2.left = node3;
//        node2.right = node4;

        System.out.println(maxPathSum(root));
    }

    public int maxPathSum(TreeNode root) {
        doMaxPathSum(root);
        return max;
    }

    public int doMaxPathSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftMax = Math.max(doMaxPathSum(root.left), 0);
        int rightMax = Math.max(doMaxPathSum(root.right), 0);
        max = Math.max(leftMax + rightMax + root.val, max);
        return Math.max(leftMax, rightMax) + root.val;
    }

    public int maxPathSum2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        rootMaxPathSum(root);
        return max;
    }
    int max = Integer.MIN_VALUE;
    public int rootMaxPathSum(TreeNode root) {
        if (root == null) {
            return Integer.MIN_VALUE;
        }
        int leftMax = rootMaxPathSum(root.left);
        int rightMax = rootMaxPathSum(root.right);

        int currMax = Integer.MIN_VALUE;
        int maxPath = Integer.MIN_VALUE;
        if (leftMax == Integer.MIN_VALUE && rightMax == Integer.MIN_VALUE) {
            currMax = root.val;
            maxPath = root.val;
        } else if (leftMax != Integer.MIN_VALUE && rightMax != Integer.MIN_VALUE){
            maxPath = Math.max(root.val, Math.max(leftMax, rightMax) + root.val);
            currMax = Math.max(leftMax + rightMax + root.val, Math.max(maxPath, root.val));
        } else if (leftMax == Integer.MIN_VALUE) {
            maxPath = Math.max(root.val, root.val + rightMax);
            currMax = Math.max(maxPath, rightMax);
        } else {
            maxPath = Math.max(root.val, root.val + leftMax);
            currMax = Math.max(maxPath, leftMax);
        }

        //System.out.println(String.format("leftMax: %d, root: %d, rightMax: %d, currMax: %d, maxPath: %d", leftMax, root.val, rightMax, currMax, maxPath));

        max = Math.max(root.val, Math.max(currMax, max));

        return maxPath;
    }

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

    protected class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    @Test
    public void testP200() {
        char[][] grid = {
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}};

        System.out.println(numIslands(grid));
    }

    public int numIslands(char[][] grid) {
        int r = grid.length;
        int c = grid[0].length;
        int num = 0;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (grid[i][j] == '1') {
                    visitLands(grid, i, j);
                    num++;
                }
            }
        }
        return num;
    }
    public void visitLands(char[][] grid, int r, int c) {
        if (grid[r][c] == '0') {
            return;
        }
        grid[r][c] = '0';
        if (c - 1 >= 0) visitLands(grid, r, c - 1);
        if (c + 1 < grid[0].length) visitLands(grid, r, c + 1);
        if (r - 1 >= 0) visitLands(grid, r - 1, c);
        if (r + 1 < grid.length) visitLands(grid, r + 1, c);
    }

    @Test
    public void testP994() {
        int[][] grid = new int[][]{
                {2,1,1},
                {1,1,0},
                {0,1,1}
        };
        /*
            [[2,1,1],
             [0,1,1],
             [1,0,1]]
         */

        System.out.println(orangesRotting(grid));
    }
    public int orangesRotting(int[][] grid) {
        int r = grid.length;
        int c = grid[0].length;
        int count = 0;
        int round = 0;

        Queue<int[]> queue = new LinkedList<>();

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (grid[i][j] == 1) {
                    count++;
                } else if (grid[i][j] == 2) {
                    queue.offer(new int[] {i, j});
                }
            }
        }

        while (count > 0 && !queue.isEmpty()) {
            int n = queue.size();
            for (int i = 0; i < n; i++) {
                int[] rc = queue.poll();
                int row = rc[0];
                int col = rc[1];
                if (col - 1 >= 0 && grid[row][col - 1] == 1) {
                    count--;
                    grid[row][col - 1] = 2;
                    queue.offer(new int[] {row, col - 1});
                }
                if (col + 1 < c && grid[row][col + 1] == 1) {
                    count--;
                    grid[row][col + 1] = 2;
                    queue.offer(new int[] {row, col + 1});
                }
                if (row - 1 >= 0 && grid[row - 1][col] == 1) {
                    count--;
                    grid[row - 1][col] = 2;
                    queue.offer(new int[] {row - 1, col});
                }
                if (row + 1 < r && grid[row + 1][col] == 1) {
                    count--;
                    grid[row + 1][col] = 2;
                    queue.offer(new int[] {row + 1, col});
                }
            }
            round++;
        }

        return count > 0 ? -1: round;
    }

    @Test
    public void testP207() {
        int numCourses = 2;
        int[][] prerequisites = new int[][]{{1,0}};
        System.out.println(canFinish(numCourses, prerequisites));
    }
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> edges = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            edges.add(new ArrayList<>());
        }
        int[] inedgs = new int[numCourses];
        for (int[] pre : prerequisites) {
            edges.get(pre[1]).add(pre[0]);
            inedgs[pre[0]]++;
        }

        Queue<Integer> ready = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inedgs[i] == 0) {
                ready.offer(i);
            }
        }

        int finished = 0;

        while (!ready.isEmpty()) {
            int curr = ready.poll();
            finished++;

            for (Integer next : edges.get(curr)) {
                if (--inedgs[next] == 0) {
                    ready.offer(next);
                }
            }
        }

        return finished == numCourses;
    }

    @Test
    public void testP208() {
        Trie trie = new Trie();
        boolean result = false;
        trie.insert("apple");
        result = trie.search("apple"); // 返回 True
        System.out.println(result);
        result = trie.search("app"); // 返回 False
        System.out.println(result);
        result = trie.startsWith("app"); // 返回 True
        System.out.println(result);
        trie.insert("app");
        result = trie.search("app"); // 返回 True
        System.out.println(result);
    }

    @Test
    public void testP17() {
        System.out.println(letterCombinations("23"));
    }

    private List<String> result = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    public List<String> letterCombinations(String digits) {
        if (digits == null || digits.isEmpty()) {
            return new ArrayList<>();
        }
        Map<Character, String> map = new HashMap<>();
        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");
        letterCombinations(digits, map, 0);
        return result;
    }

    public void letterCombinations(String digits, Map<Character, String> map, int index) {
        if (index == digits.length()) {
            result.add(sb.toString());
            return ;
        }
        Character c = digits.charAt(index);
        String s = map.get(c);
        for (int i = 0; i < s.length(); i++) {
            sb.append(s.charAt(i));
            letterCombinations(digits, map, index + 1);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    @Test
    public void testP22() {
        System.out.println(generateParenthesis(3));
    }

    List<String> list = new ArrayList<>();
    public List<String> generateParenthesis(int n) {
        generateParenthesis(n, n, "");
        return list;
    }

    private void generateParenthesis(int leftCount, int rightCount, String s) {
        if (leftCount < 0 || rightCount < leftCount) {
            return;
        }
        if (leftCount == 0 && rightCount == 0) {
            list.add(s);
            return;
        }
        generateParenthesis(leftCount - 1, rightCount, s + "(");
        generateParenthesis(leftCount, rightCount - 1, s + ")");
    }

    @Test
    public void testP79() {
        char[][] board = {
                {'A','B','C','E'},
                {'S','F','C','S'},
                {'A','D','E','E'}};

        String word = "ABCCED"; // false
        System.out.println(exist(board, word));
    }

    public boolean exist(char[][] board, String word) {
        visit = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == word.charAt(0)) {
                    if (exist(board, word, i, j, 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean[][] visit;
    private boolean exist(char[][] board, String word, int i, int j, int index) {
        if (board[i][j] != word.charAt(index) || visit[i][j]) {
            return false;
        }
        if (index == word.length() - 1) {
            return true;
        }
        boolean exist = false;
        visit[i][j] = true;
        if (i - 1 >= 0) {
            exist = exist(board, word, i - 1, j, index + 1);
        }
        if (!exist && i + 1 < board.length) {
            exist = exist(board, word, i + 1, j, index + 1);
        }
        if (!exist && j - 1 >= 0) {
            exist = exist(board, word, i, j - 1, index + 1);
        }
        if (!exist && j + 1 < board[0].length) {
            exist = exist(board, word, i, j + 1, index + 1);
        }
        visit[i][j] = false;
        return exist;
    }

    @Test
    public void testP131() {
        String s = "aab"; // [["a","a","b"],["aa","b"]]
        System.out.println(partition(s));
    }

    public List<List<String>> partition(String s) {
        partition(s, 0);
        return result131;
    }

    List<List<String>> result131 = new ArrayList<>();
    List<String> currResult131 = new ArrayList<>();

    public void partition(String s, int index) {
        System.out.println(currResult131);
        if (!currResult131.isEmpty() && isHuiWen(currResult131)) {
            result131.add(new ArrayList<>(currResult131));
        }
        if (index == s.length()) {
            return;
        }
        for (int i = index; i < s.length(); i++) {
            currResult131.add(s.charAt(i) + "");
            partition(s, i + 1);
            currResult131.remove(currResult131.size() - 1);
        }
    }
    private boolean isHuiWen(List<String> list) {
        int n = list.size();
        for (int i = 0; i < n / 2; i++) {
            if (!list.get(i).equals(list.get(n - 1 - i))) {
                return false;
            }
        }
        return true;
    }
    int left_bound(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        // 搜索区间为 [left, right]
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                // 搜索区间变为 [mid+1, right]
                left = mid + 1;
            } else if (nums[mid] > target) {
                // 搜索区间变为 [left, mid-1]
                right = mid - 1;
            } else if (nums[mid] == target) {
                // 收缩右侧边界
                right = mid - 1;
            }
        }
        // 判断 target 是否存在于 nums 中
        // 如果越界，target 肯定不存在，返回 -1
        if (left == nums.length) {
            return -1;
        }
        // 判断一下 nums[left] 是不是 target
        return nums[left] == target ? left : -1;
    }

    int right_bound(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else if (nums[mid] == target) {
                // 这里改成收缩左侧边界即可
                left = mid + 1;
            }
        }
        // 最后改成返回 left - 1
        if (left - 1 < 0 || left - 1 == nums.length) {
            return -1;
        }
        return nums[left - 1] == target ? (left - 1) : -1;
    }

    public int[] searchRange(int[] nums, int target) {
        return new int[] {left_bound(nums, target), right_bound(nums, target)};
    }

    // todo
    @Test
    public void testP4() {
        // 1,2,3 -> 2
        // 1,2,3,4 -> 2.5

        // 1,2,3 + 1,2,3,4 = 1,1,2,2,3,3,4 -> 2
        // 1,2,3 + 1,4,5,6 = 1,1,2,3,4,5,6 -> 3
        // 1,2,3 + 2,2,3,4 = 1,2,2,2,3,3,4 -> 2
        // 1,2,3 + 1,4,5 = 1,1,2,3,4,5 -> 2.5

        // 1,2,3 + 4,5,6,7 = 1,2,3,4,5,6,7 -> 4
        // 1,2,3 + 4,5 = 1,2,3,4,5 -> 3
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        return -1;
    }

    @Test
    public void testP20() {
        String s = "()[]{}";
        s = "(])";
        System.out.println(isValid(s));
    }
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
                continue;
            }
            if (stack.isEmpty()) {
                return false;
            }
            if (stack.peek() == '(' && c == ')'
                    || stack.peek() == '[' && c == ']'
                    || stack.peek() == '{' && c == '}') {
                stack.pop();
            } else {
                return false;
            }
        }
        return stack.isEmpty();
    }

    @Test
    public void testP155() {
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println(minStack.getMin()); // --> 返回 -3.
        minStack.pop();
        System.out.println(minStack.top()); // --> 返回 0.
        System.out.println(minStack.getMin()); // --> 返回 -2.
        /*
        ["MinStack","push","push","push","top","pop","getMin","pop","getMin","pop","push","top","getMin","push","top","getMin","pop","getMin"]
        [[],[2147483646],[2147483646],[2147483647],[],[],[],[],[],[],[2147483647],[],[],[-2147483648],[],[],[],[]]
        [null,null,null,null,2147483647,null,2147483646,null,2147483646,null,null,2147483647,2147483647,null,-2147483648,-2147483648,null,2147483647]...
        [null,null,null,null,2147483647,null,2147483646,null,2147483646,null,null,2147483647,2147483647,null,-2147483648,-2147483648,null,-2147483648]...
         */
    }

    @Test
    public void testP394() {
        // 输入：s = "3[a]2[bc]" 输出："aaabcbc"
        String s = "3[a]2[bc]";
        // 输入：s = "3[a2[c]]" 输出："accaccacc"
        //System.out.println(decodeString(s));

        //s = "3[a2[c]]";

        // s = "2[abc]3[cd]ef";
        s = "abc3[cd]xyz";
        // s = "100[leetcode]";
        System.out.println(decodeString(s));
    }

    public String decodeString(String s) {
        StringBuilder ans = new StringBuilder();
        Stack<Integer> multiStack = new Stack<>();
        Stack<StringBuilder> ansStack = new Stack<>();

        int multi = 0;

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                multi = multi * 10 + c - '0';
            } else if (Character.isLetter(c)) {
                ans.append(c);
            } else if (c == '[') {
                multiStack.push(multi);
                multi = 0;
                ansStack.push(ans);
                ans = new StringBuilder();
            } else {
                StringBuilder ansTmp = ansStack.pop();
                int tmp = multiStack.pop();
                for(int i = 0; i < tmp; i++) {
                    ansTmp.append(ans);
                }
                ans = ansTmp;
            }
        }

        return ans.toString();
    }

    public String decodeString2(String s) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || Character.isLetter(c) || c == '[') {
                sb.append(c);
            } else if (c == ']') {
                StringBuilder currSb = new StringBuilder();

                int lastIndex = sb.length() - 1;
                char currCh = sb.charAt(lastIndex);
                sb.deleteCharAt(lastIndex);

                while (currCh != '[') {
                    currSb.append(currCh);
                    lastIndex = sb.length() - 1;
                    currCh = sb.charAt(lastIndex);
                    sb.deleteCharAt(lastIndex);
                }

                currSb.reverse();

                int num = 0;
                int radix = 1;
                while (sb.length() > 0) {
                    lastIndex = sb.length() - 1;
                    currCh = sb.charAt(lastIndex);
                    if (!Character.isDigit(currCh)) {
                        break;
                    }
                    num = radix * Integer.parseInt(String.valueOf(currCh)) + num;
                    radix = radix * 10;
                    sb.deleteCharAt(lastIndex);
                }

                for (int repeat = 0; repeat < num; repeat++) {
                    sb.append(currSb);
                }
            }
        }

        return sb.toString();
    }

    @Test
    public void testP74() {
        int[][] matrix = new int[][] {{1, 1}};
        searchMatrix(matrix, 2);
    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int r = matrix.length;
        int c = matrix[0].length;
        int low = 0;
        int high = r * c - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            int midVal = matrix[mid / c][mid % c];
            if (target == midVal) {
                return true;
            } else if (target > midVal) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }

    @Test
    public void testP347() {

    }
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[1] - o2[1];
            }
        });
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (--k >= 0) {
                priorityQueue.offer(new int[] {entry.getKey(), entry.getValue()});
            } else {
                if (entry.getValue() > priorityQueue.peek()[1]) {
                    priorityQueue.poll();
                    priorityQueue.add(new int[] {entry.getKey(), entry.getValue()});
                }
            }
        }
        int[] result = new int[priorityQueue.size()];
        int index = 0;
        while (!priorityQueue.isEmpty()) {
            result[index++] = priorityQueue.poll()[0];
        }
        return result;
    }

    @Test
    public void testP295() {
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.println(medianFinder.findMedian());
        medianFinder.addNum(3);
        System.out.println(medianFinder.findMedian());
        System.out.println((int)Math.sqrt(12.0));
    }

    @Test
    public void testP322() {
        System.out.println(coinChange(new int[] {1, 2, 5}, 11));
    }
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Set<Integer> coinSet = new HashSet<>();
        for (int i = 0; i < coins.length; i++) {
            coinSet.add(coins[i]);
        }
        for (int i = 1; i <= amount; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j <= i; j++) {
                if (coinSet.contains(j) && dp[i - j] != Integer.MIN_VALUE) {
                    min = Math.min(min, dp[i - j]);
                }
            }
            dp[i] = min + 1;
        }
        for (int i = 1; i <= amount; i++) {
            System.out.print(dp[i] + " ");
        }
        return dp[amount];
    }

    @Test
    public void testP416() {
        System.out.println(canPartition(new int[] {1,5,11,5}));
    }
    public boolean canPartition(int[] nums) {
        int sum = Arrays.stream(nums).sum();
        if ((sum & 1) == 1) {
            return false;
        }
        Arrays.sort(nums);
        int target = sum / 2;
        Map<Integer, Integer> map = new HashMap<>();
        for (int n : nums) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        boolean[] f = new boolean[target + 1];
        for (int i = 1; i <= target; i++) {
            for (int j = 0; j < nums.length; j++) {
                int t = i - nums[j];
                if (t == 0) {
                    f[i] = true;
                } else if(map.containsKey(t)) {
                    f[i] = true;
                }
            }
        }
        return f[target];
    }

    @Test
    public void testP1143() {
        System.out.println(longestCommonSubsequence("abcde", "ace"));

        System.out.println(longestCommonSubsequence("abc", "abc"));

        System.out.println(longestCommonSubsequence("abc", "def"));
    }
    public int longestCommonSubsequence(String text1, String text2) {
        int[] pre = new int[text1.length() + 1];
        int[] curr = new int[text1.length() + 1];

        for (int i = 1; i <= text2.length(); i++) {
            for (int j = 1; j <= text1.length(); j++) {
                if (text2.charAt(i - 1) == text1.charAt(j - 1)) {
                    curr[j] = pre[j - 1] + 1;
                } else {
                    curr[j] = Math.max(pre[j], curr[j - 1]);
                }
            }
            pre = curr.clone();
        }
        return curr[text1.length()];
    }

    public int longestCommonSubsequence2(String text1, String text2) {
        int[][] f = new int[text2.length() + 1][text1.length() + 1];
        for (int i = 1; i <= text2.length(); i++) {
            for (int j = 1; j <= text1.length(); j++) {
                if (text2.charAt(i - 1) == text1.charAt(j - 1)) {
                    f[i][j] = f[i - 1][j - 1] + 1;
                } else {
                    f[i][j] = Math.max(f[i - 1][j], f[i][j - 1]);
                }
            }
        }
        return f[text2.length()][text1.length()];
    }

    @Test
    public void testP72() {
        System.out.println(minDistance("horse", "ros"));

        System.out.println(minDistance("pneumonoultramicroscopicsilicovolcanoconiosis", "ultramicroscopically"));

        System.out.println(minDistance("sea", "ate"));

        System.out.println(minDistance("park", "spake"));
    }
    public int minDistance(String word1, String word2) {
        if (word1.isEmpty() || word2.isEmpty()) {
            return word1.length() + word2.length();
        }

        int w1Len = word1.length();
        int w2Len = word2.length();

        char[] w1Array = word1.toCharArray();
        char[] w2Array = word2.toCharArray();

        int[] curr = new int[w1Len + 1];

        for (int j = 0; j <= w1Len; j++) {
            curr[j] = j;
        }

        for (int i = 1; i <= w2Len; i++) {
            int pre = curr[0];
            curr[0] = i;

            for (int j = 1; j <= w1Len; j++) {
                int temp = curr[j];
                if (w1Array[j - 1] == w2Array[i - 1]) {
                    curr[j] = pre;
                } else {
                    curr[j] = 1 + Math.min(pre, Math.min(curr[j], curr[j - 1]));
                }
                pre = temp;
            }
        }
        return curr[w1Len];
    }
    /*
        s e a
      a 1 2 2
      t 2 2 3
      e 3 2 3
     */
    public int minDistance3(String word1, String word2) {
        if (word1.isEmpty() || word2.isEmpty()) {
            return word1.length() + word2.length();
        }

        int w1Len = word1.length();
        int w2Len = word2.length();

        char[] w1Array = word1.toCharArray();
        char[] w2Array = word2.toCharArray();

        int[] curr = new int[w1Len + 1];
        int[] pre = new int[w2Len + 1];

        for (int j = 0; j <= w1Len; j++) {
            curr[j] = j;
        }
        pre = curr.clone();

        for (int i = 1; i <= w2Len; i++) {
            curr[0] = i;
            for (int j = 1; j <= w1Len; j++) {
                if (w1Array[j - 1] == w2Array[i - 1]) {
                    curr[j] = pre[j - 1];
                } else {
                    curr[j] = 1 + Math.min(pre[j - 1], Math.min(pre[j], curr[j - 1]));
                }
            }
            pre = curr.clone();
        }
        return curr[w1Len];
    }

    public int minDistance2(String word1, String word2) {
        if (word1.isEmpty() || word2.isEmpty()) {
            return word1.length() + word2.length();
        }

        int w1Len = word1.length();
        int w2Len = word2.length();

        int[][] f = new int[w2Len + 1][w1Len + 1];
        char[] w1Array = word1.toCharArray();
        char[] w2Array = word2.toCharArray();

        for (int j = 0; j <= w1Len; j++) {
            f[0][j] = j;
        }
        for (int i = 0; i <= w2Len; i++) {
            f[i][0] = i;
        }

        for (int i = 1; i <= w2Len; i++) {
            for (int j = 1; j <= w1Len; j++) {
                if (w1Array[j - 1] == w2Array[i - 1]) {
                    f[i][j] = f[i - 1][j - 1];
                } else {
                    f[i][j] = 1 + Math.min(f[i - 1][j - 1], Math.min(f[i - 1][j], f[i][j - 1]));
                }
            }
        }
        return f[w2Len][w1Len];
    }
    @Test
    public void testP75() {
        int[] nums = new int[] {1,0,2,1,1,0}; // [0,0,1,1,2,2]
        sortColors(nums);

        for (int n : nums) {
            System.out.print(n + " ");
        }

        System.out.println();
    }
    public void sortColors(int[] nums) {
        int a = 0;
        int b = nums.length - 1;
        int c = 0;
        while (c <= b) {
            if (nums[c] == 0) {
                swap(nums, a, c);
                a++;
                c++;
            } else if (nums[c] == 2) {
                swap(nums, c, b);
                b--;
            } else {
                c++;
            }
        }
    }
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    @Test
    public void testP31() {
        int[] nums = new int[] {1,1,5}; // 1,3,2
        nextPermutation(nums);
        for (int n : nums) {
            System.out.print(n + " ");
        }
        System.out.println();
    }
    public void nextPermutation(int[] nums) {
        int len = nums.length;
        if (len == 1) {
            return;
        }
        int j = len - 1;
        int i = j - 1;
        while (i >= 0 && nums[i] >= nums[j]) {
            i--;
            j--;
        }
        if (i == -1) {
            reverse(nums, 0, len - 1);
        } else if (j == len - 1) {
            reverse(nums, len - 2, len - 1);
        } else {
            for (int k = len - 1; k > i; k--) {
                if (nums[k] > nums[i]) {
                    swap(nums, k, i);
                    reverse(nums, i + 1, len - 1);
                    break;
                }
            }
        }
    }

    @Test
    public void test287() {
        int[] nums = new int[] {1,2,4,3,2};
        System.out.println(findDuplicate(nums));
    }

    public int findDuplicate(int[] nums) {
        while (true) {
            if (nums[0] == nums[nums[0]]) {
                return nums[0];
            } else {
                swap(nums, 0, nums[0]);
            }
        }
    }
}
