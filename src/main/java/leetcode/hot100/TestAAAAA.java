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



}
