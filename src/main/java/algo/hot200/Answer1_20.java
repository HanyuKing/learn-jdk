package algo.hot200;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author Hanyu.Wang
 * @Date 2024/1/2 16:26
 * @Description
 * @Version 1.0
 **/
public class Answer1_20 extends Base {
    @Test
    public void testP3() {
        System.out.println(lengthOfLongestSubstring("abcca"));
        // 滑动窗口，注意左边界更新
    }

    @Test
    public void testP206() {
        // reverse linklist node
        // PASS
    }

    @Test
    public void test146() {
        // LRUCache
        // 1. 用LinedHashMap 2. 自己实现，借用LinedHashMap思想
    }

    @Test
    public void test215() {
        // [3,2,3,1,2,4,5,5,6], k = 4
//        System.out.println(findKthLargest(new int[]{3,2,1,5,6,4}, 2));
//        System.out.println(findKthLargest(new int[]{3,2,3,1,2,4,5,5,6}, 4));
        System.out.println(findKthLargest2(new int[]{3,2,1,5,6,4}, 2));
        System.out.println(findKthLargest2(new int[]{3,2,3,1,2,4,5,5,6}, 4));
        System.out.println(findKthLargest2(new int[]{2,1}, 2));

        // 1.堆 2. 快排
    }

    @Test
    public void testP25() {
        // Hard
    }

    @Test
    public void testP15() {
        int[] nums = new int[] {-13,5,13,12,-2,-11,-1,12,-3,0,-3,-7,-7,-5,-3,-15,-2,14,14,13,6,-11,-11,5,-15,-14,5,-5,-2,0,3,-8,-10,-7,11,-5,-10,-5,-7,-6,2,5,3,2,7,7,3,-10,-2,2,-12,-11,-1,14,10,-9,-15,-8,-7,-9,7,3,-2,5,11,-13,-15,8,-3,-7,-12,7,5,-2,-6,-3,-10,4,2,-5,14,-3,-1,-10,-3,-14,-4,-3,-7,-4,3,8,14,9,-2,10,11,-10,-4,-15,-9,-1,-1,3,4,1,8,1};
        // int[] nums = new int[] {-4, -1, -1, 0, 1, 2};
        // [-1,0,1] 和 [-1,-1,2]
        // -4, -1, -1, 0, 1, 2

        Object result = threeSum(nums);

        print(result);
    }

    @Test
    public void testP53() {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println(maxSubArray(nums));
    }

    @Test
    public void test21() {
        mergeTwoLists(null, null);
    }

    @Test
    public void test1() {
        // nums = [2,7,11,15], target = 9 输出：[0,1]
        int[] nums = new int[] {2, 7, 11, 15};
        int target = 9;

        int[] result = twoSum(nums, target);

        print(result);
    }

    @Test
    public void test5() {
        String s = "aaaa"; // bab, aba, baab, cabacc
        System.out.println(longestPalindrome2(s));
//
//        s = "bab";
//        System.out.println(longestPalindrome(s));
//
//        s = "aba";
//        System.out.println(longestPalindrome2(s));
//
//        s = "baab";
//        System.out.println(longestPalindrome2(s));
//
//        s = "cabacc";
//        System.out.println(longestPalindrome2(s));
//
        s = "#c#c#c#";
        System.out.println(longestPalindrome2(s));

        s = "#c#b#b#d#";
        System.out.println(longestPalindrome3(s));
    }

    @Test
    public void test102() {
        TreeNode root = new TreeNode(3);
        TreeNode root9 = new TreeNode(9);
        TreeNode root20 = new TreeNode(20);
        TreeNode root15 = new TreeNode(15);
        TreeNode root7 = new TreeNode(7);
        root.left = root9;
        root.right = root20;
        root20.right = root7;
        root20.left = root15;
        levelOrder(root);

        Object result = levelOrder(root);
        print(result);
    }

    @Test
    public void test33() {
        int[] nums = new int[] {4,5,6,7,0,1,2}; // target = 0, 4
        print(search(nums, 0));

        nums = new int[] {6,7,0,1,2,4,5}; // target = 0, 2
        print(search(nums, 0));

        nums = new int[] {4,5,6,7,0,1,2}; // target = 3, -1
        print(search(nums, 3));

        nums = new int[]{1}; // target = 0, -1
        print(search(nums, 0));

        nums = new int[]{3, 1}; // target = 1, 1
        print(search(nums, 1));
    }

    @Test
    public void testP200() {
//        char[][] grid = {
//                {'1', '1', '1', '1', '0'},
//                {'1', '1', '0', '1', '0'},
//                {'1', '1', '0', '0', '0'},
//                {'0', '0', '0', '0', '0'}}; // 1
//
//        print(numIslands(grid));

        char[][] grid = {
                {'1', '1', '0', '0', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '1', '0', '0'},
                {'0', '0', '0', '1', '1'}};
        print(numIslands(grid));

    }

    @Test
    public void testP419() {
        char[][] board = new char[][] {
                {'X','.','.','X'},
                {'.','.','.','X'},
                {'.','.','.','X'}
        };
        print(countBattleships(board));
    }
    public int countBattleships(char[][] grid) {
        int count = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '.') {
                    continue;
                }
                // or
                /*
                    if (grid[i][j] == '1')
                 */
                doCountBattleships(grid, i, j);
                count++;
            }
        }

        return count;
    }

    public void doCountBattleships(char[][] grid, int i, int j) {
        grid[i][j] = '.';
        if (i - 1 >= 0 && grid[i - 1][j] == 'X') doCountBattleships(grid, i - 1, j);
        if (j - 1 >= 0 && grid[i][j - 1] == 'X') doCountBattleships(grid, i, j - 1);
        if (j + 1 < grid[0].length && grid[i][j + 1] == 'X') doCountBattleships(grid, i, j + 1);
        if (i + 1 < grid.length && grid[i + 1][j] == 'X') doCountBattleships(grid, i + 1, j);
    }

    @Test
    public void testP695() {
        int[][] grid = {
                {0,0,1,0,0,0,0,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0},
                {0,1,1,0,1,0,0,0,0,0,0,0,0},
                {0,1,0,0,1,1,0,0,1,0,1,0,0},
                {0,1,0,0,1,1,0,0,1,1,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,1,0,0},
                {0,0,0,0,0,0,0,1,1,1,0,0,0},
                {0,0,0,0,0,0,0,1,1,0,0,0,0}};

        print(maxAreaOfIsland(grid));
    }

    public int maxAreaOfIsland(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int maxArea = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int currMax = getMaxAreaOfIsland(grid, m, n, i, j, 0);
                maxArea = Math.max(currMax, maxArea);
            }
        }
        return maxArea;
    }

    private int getMaxAreaOfIsland(int[][] grid, int m, int n, int i, int j, int area) {
        if (i < 0 || i == m || j < 0 || j == n
                || grid[i][j] != 1) {
            return area;
        }
        grid[i][j] = 2;
        area += 1;

        area += getMaxAreaOfIsland(grid, m, n, i + 1, j, 0);
        area += getMaxAreaOfIsland(grid, m, n, i - 1, j, 0);
        area += getMaxAreaOfIsland(grid, m, n, i, j + 1, 0);
        area += getMaxAreaOfIsland(grid, m, n, i, j - 1, 0);

        return area;
    }

    @Test
    public void test20() {
        String s = "()[]{}";
        print(isValid(s));
    }

    @Test
    public void testP141() {
        // PASS
    }

    @Test
    public void testP121() {
        int[] prices = new int[] {7, 1, 5, 3, 6, 4};
        int result = maxProfit2(prices);
        print(result);

        prices = new int[] {7,6,4,3,1};
        result = maxProfit2(prices); // todo 单调栈
        print(result);
    }

    @Test
    public void testP236() {
        // PASS
    }

    @Test
    public void testP88() {
        // PASS merge2, merge
    }

    @Test
    public void testP46() {
        print(permute(new int[] {1, 2, 3}));
    }

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        this.j = new boolean[nums.length];
        doPermute(nums, new ArrayList<>(), result);
        return result;

    }

    public void doPermute2(int[] nums, int start, int end, List<List<Integer>> result) {
        if (end - start == 1) {
            List<Integer> ans = new ArrayList<>();
            for (int num : nums) {
                ans.add(num);
            }
            result.add(ans);
            return;
        }

        for (int i = start; i < end; i++) {

            int temp = nums[start];
            nums[start] = nums[i];
            nums[i] = temp;

            doPermute2(nums, start + 1, nums.length, result);

            temp = nums[i];
            nums[i] = nums[start];
            nums[start] = temp;
        }

    }

    private boolean[] j;
    public void doPermute(int[] nums, List<Integer> permute, List<List<Integer>> result) {
        if (permute.size() == nums.length) {
            result.add(new ArrayList<>(permute));
        }

        for (int i = 0; i < nums.length; i++) {

            if (this.j[i]) {
                continue;
            }

            permute.add(nums[i]);
            this.j[i] = true;

            doPermute(nums, permute, result);

            this.j[i] = false;

            permute.remove(permute.size() - 1);
        }
    }

    public void merge2(int[] nums1, int m, int[] nums2, int n) {
        int i1 = m - 1;
        int i2 = n - 1;
        int index = m + n - 1;
        while (i1 >= 0 && i2 >= 0) {
            if (nums1[i1] >= nums2[i2]) {
                nums1[index--] = nums1[i1--];
            } else {
                nums1[index--] = nums2[i2--];
            }
        }
        while (i1 >= 0) { // 可以省略
            nums1[index--] = nums1[i1--];
        }
        while (i2 >= 0) {
            nums1[index--] = nums2[i2--];
        }
    }

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i1 = m - 1;
        int i2 = n - 1;
        int index = m + n - 1;
        while (i1 >= 0 || i2 >= 0) {
            if (i1 >= 0 && i2 >= 0) {
                if (nums1[i1] >= nums2[i2]) {
                    nums1[index--] = nums1[i1--];
                } else {
                    nums1[index--] = nums2[i2--];
                }
                continue;
            }
            if (i1 >= 0) {
                nums1[index--] = nums1[i1--];
            }
            if (i2 >= 0) {
                nums1[index--] = nums2[i2--];
            }
        }
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }
        if (root == q || root == p) {
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) {
            return root;
        }
        if (left == null) {
            return right;
        } else {
            return left;
        }
    }

    public int maxProfit2(int[] prices) {
        int max = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < prices.length; i++) {
            if (stack.isEmpty()) {
                stack.push(prices[i]);
            } else if (prices[i] < stack.peek()) {
                stack.push(prices[i]);
            } else {
                max = Math.max(max, prices[i] - stack.peek());
            }
        }
        return max;
    }


    public int maxProfit(int[] prices) {
        int max = 0;
        int minIndex = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] - prices[minIndex] > max) {
                max = prices[i] - prices[minIndex];
            } else if (prices[i] - prices[minIndex] < 0) {
                minIndex = i;
            }
        }
        return max;
    }

    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                char peek = stack.peek();
                if ((c == ')' && peek == '(')
                        || (c == ']' && peek == '[')
                        || (c == '}' && peek == '{')) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public int numIslands(char[][] grid) {
        int count = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '2' || grid[i][j] == '0') {
                    continue;
                }
                // or
                /*
                    if (grid[i][j] == '1')
                 */
                numIslandsDFS(grid, i, j);
                count++;
            }
        }

        return count;
    }

    public void numIslandsDFS(char[][] grid, int i, int j) {
        grid[i][j] = '2';
        if (i - 1 >= 0 && grid[i - 1][j] == '1') numIslandsDFS(grid, i - 1, j);
        if (j - 1 >= 0 && grid[i][j - 1] == '1') numIslandsDFS(grid, i, j - 1);
        if (j + 1 < grid[0].length && grid[i][j + 1] == '1') numIslandsDFS(grid, i, j + 1);
        if (i + 1 < grid.length && grid[i + 1][j] == '1') numIslandsDFS(grid, i + 1, j);
    }

    public int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (target == nums[mid]) {
                return mid;
            }
            if (nums[low] <= nums[mid] && nums[high] < nums[mid]) {
                if (target < nums[mid] && target >= nums[low]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else if (nums[low] > nums[mid] && nums[high] > nums[mid]) {
                if (target > nums[mid] && target <= nums[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            } else {
                if (target < nums[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
        }
        return -1;
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        Queue<TreeNode> queue = new LinkedBlockingQueue<>();
        queue.add(root);
        List<List<Integer>> levelList = new ArrayList<>();
        while (!queue.isEmpty()) {
            int currSize = queue.size();
            List<Integer> nodeValueList = new ArrayList<>();

            while (currSize-- > 0) {
                TreeNode currNode = queue.poll();
                nodeValueList.add(currNode.val);
                if (currNode.left != null) {
                    queue.add(currNode.left);
                }
                if (currNode.right != null) {
                    queue.add(currNode.right);
                }
            }

            levelList.add(nodeValueList);
        }
        return levelList;
    }

    public String longestPalindrome3(String s) {
        // Manacher 算法
        return null;
    }

    public String longestPalindrome2(String s) {
        String max = s.substring(0, 1);

        for (int i = 0; i < s.length(); i++) {
            int left = i;
            int right = i;

            String expandS = expand(s, left, right);
            if (expandS.length() > max.length()) {
                max = expandS;
            }

            if (i + 1 < s.length() && s.charAt(i) == s.charAt(i + 1)) {
                right = i + 1;
                expandS = expand(s, left, right);
                if (expandS.length() > max.length()) {
                    max = expandS;
                }
            }
        }

       return max;
    }

    private String expand(String s, int left, int right) {
        while (left >= 0 && right < s.length()) {
            if (s.charAt(left) != s.charAt(right)) {
                left++;
                right--;
                break;
            }
            left--;
            right++;
        }
        if (left < 0 || right >= s.length()) {
            left++;
            right--;
        }
        return s.substring(left, right + 1);
    }

    public String longestPalindrome(String s) {
        int[][] dp = new int[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            dp[i][i] = 1;
        }
        int max = 1;
        int maxStart = 0;
        int maxEnd = 0;

        for (int i = s.length() - 2; i >= 0; i--) {
            for (int j = i + 1; j < s.length(); j++) {
                if (s.charAt(i) == s.charAt(j) && (dp[i + 1][j - 1] > 0 || j - 1 == i)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                    if (dp[i][j] > max) {
                        max = dp[i][j];
                        maxStart = i;
                        maxEnd = j;
                    }
                }
            }
        }

        return s.substring(maxStart, maxEnd + 1);
    }

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer valIndex = map.get(nums[i]);
            if (valIndex != null) {
                return new int[]{valIndex, i};
            }
            map.put(target - nums[i], i);
        }
        return null;
    }

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        ListNode head = new ListNode();
        ListNode tail = head;
        while (list1 != null || list2 != null) {
            if (list1 != null && list2 != null) {
                if (list1.val <= list2.val) {
                    tail.next = list1;
                    tail = list1;
                    list1 = list1.next;
                    tail.next = null;
                } else {
                    tail.next = list2;
                    tail = list2;
                    list2 = list2.next;
                    tail.next = null;
                }
            } else if (list1 != null) {
                tail.next = list1;
                break;
            } else {
                tail.next = list2;
                break;
            }
        }
        return head.next;
    }

    private class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }

    public int maxSubArray(int[] nums) {
        int pre = nums[0];

        int max = pre;

        for (int i = 1; i < nums.length; i++) {
            pre = Math.max(pre + nums[i], nums[i]);
            max = Math.max(pre, max);
        }

        return max;
    }

    // todo: 四数之和
    public List<List<Integer>> threeSum(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList<>();
        }
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int first = 0; first < nums.length - 2; first++) {
            if (first > 0 && nums[first] == nums[first - 1]) {
                // 重复
                continue;
            }
            int target = -nums[first];
            int third = nums.length - 1;

            for (int second = first + 1; second < nums.length - 1; second++) {
                if (second > first + 1 && nums[second] == nums[second - 1]) {
                    // 重复
                    continue;
                }

                while (third > second && nums[second] + nums[third] > target) {
                    third--;
                }

                if (second == third) {
                    break;
                }

                if (nums[second] + nums[third] == target) {
                    List<Integer> vals = new ArrayList<>();
                    vals.add(nums[first]);
                    vals.add(nums[second]);
                    vals.add(nums[third]);
                    result.add(vals);
                }

            }
        }

        return result;
    }

    public List<List<Integer>> threeSum2(int[] nums) {

        Arrays.sort(nums);

        Map<Integer, List<List<Integer>>> sumIndexMap = new HashMap<>();
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                int sum = nums[i] + nums[j];
                List<List<Integer>> indexList = sumIndexMap.getOrDefault(sum, new ArrayList<>());

                if (!indexList.isEmpty()) {
                    int len = indexList.size();
                    for (int ii = 0; ii < len; ii++) {
                        List<Integer> index = indexList.get(ii);
                        if (nums[i] == nums[index.get(0)] && nums[j] == nums[index.get(1)]
                                || nums[j] == nums[index.get(0)] && nums[i] == nums[index.get(1)]) {
                            break;
                        } else {
                            List<Integer> newIndex = new ArrayList<>();
                            if (nums[i] > nums[j]) {
                                newIndex.add(j);
                                newIndex.add(i);
                            } else {
                                newIndex.add(i);
                                newIndex.add(j);
                            }
                            indexList.add(newIndex);
                        }
                    }
                } else {
                    List<Integer> newIndex = new ArrayList<>();
                    if (nums[i] > nums[j]) {
                        newIndex.add(j);
                        newIndex.add(i);
                    } else {
                        newIndex.add(i);
                        newIndex.add(j);
                    }
                    indexList.add(newIndex);
                    sumIndexMap.put(sum, indexList);
                }

            }
        }

        List<List<Integer>> result = new ArrayList<>();
        Set<String> added = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            int sum = 0 - nums[i];
            List<List<Integer>> indexList = sumIndexMap.get(sum);
            if (indexList != null) {
                for (List<Integer> index : indexList) {
                    if (index.get(0) != i && index.get(1) != i) {
                        List<Integer> newValue = new ArrayList<>();
                        newValue.add(nums[i]);
                        newValue.add(nums[index.get(0)]);
                        newValue.add(nums[index.get(1)]);
                        Collections.sort(newValue);

                        String s = String.valueOf(newValue.get(0)) + "_"
                                + String.valueOf(newValue.get(1)) + "_"
                                + String.valueOf(newValue.get(2));

                        if (added.contains(s)) {
                            continue;
                        }

                        added.add(s);

                        result.add(newValue);
                    }
                }
            }
        }

        return result;
    }

    // nlogk
    public int findKthLargest2(int[] nums, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();

        int count = 0;
        for (int num : nums) {
            priorityQueue.add(num);
            if (++count == k) {
                break;
            }
        }

        for (int i = k; i < nums.length; i++) {
            if (nums[i] > priorityQueue.peek()) {
                priorityQueue.poll();
                priorityQueue.add(nums[i]);
            }
        }
        return priorityQueue.peek();
    }

    // nlogn
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        for (int num : nums) {
            priorityQueue.add(num);
        }
        int result = priorityQueue.poll();
        while (--k > 0) {
            result = priorityQueue.poll();
        }
        return result;
    }

    class LRUCache extends LinkedHashMap<Integer, Integer> {
        private Integer capacity;
        public LRUCache(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        // todo: HashMap为什么允许为空，concurrentHashMap确不行
        public int get(int key) {
            return super.getOrDefault(key, -1);
        }

        // todo: 线程安全问题
        public void put(int key, int value) {
            super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
            return super.size() > capacity;
        }
    }

    public int lengthOfLongestSubstring2(String s) {
        int maxLen = 0;

        if (s == null || s.isEmpty()) {
            return maxLen;
        }

        maxLen = 1;

        int left = 0;
        int right = 1;

        Map<Character, Integer> charIndex = new HashMap<>();
        charIndex.put(s.charAt(0), 0);

        for (int j = right; j < s.length(); j++) {
            char ch = s.charAt(j);
            Integer index = charIndex.get(ch);
            if (index == null || index < left) {
                charIndex.put(ch, j);
                maxLen = Math.max(j - left + 1, maxLen);
            } else {
                charIndex.put(ch, j);
                left = index + 1;
                maxLen = Math.max(j - index, maxLen);
            }
        }

        return maxLen;
    }

    public int lengthOfLongestSubstring(String s) {
        int maxLen = 0;
        int left = 0;
        int right = 0;

        Map<Character, Integer> charIndex = new HashMap<>();

        while (right < s.length()) {
            char ch = s.charAt(right);
            if (charIndex.containsKey(ch)) {
                left = Math.max(left, charIndex.get(ch) + 1);
            }
            maxLen = Math.max(right - left + 1, maxLen);
            charIndex.put(ch, right++);
        }

        return maxLen;
    }


}
