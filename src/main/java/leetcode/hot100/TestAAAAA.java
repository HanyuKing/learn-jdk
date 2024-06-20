package leetcode.hot100;

import org.junit.Test;

import java.util.*;

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
}
