package algo.hash;

import algo.hot200.Base;
import org.junit.Test;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/12 15:13
 * @Description
 * @Version 1.0
 **/
public class Answer extends Base {

    @Test
    public void testP128() {
        int[] nums = new int[] {100,4,200,1,3,2};
        print(longestConsecutive(nums)); // 4. 1,2,3,4

        nums = new int[] {0,3,7,2,5,8,4,6,0,1};
        print(longestConsecutive(nums)); // 9

        nums = new int[] {0};
        print(longestConsecutive2(nums)); // 9
    }

    /**
     * 官方
     *
     * @param nums
     * @return
     */
    public int longestConsecutive3(int[] nums) {
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

    /**
     * n--; // 为什么不行？超时
     *
     * @param nums
     * @return
     */
    public int longestConsecutive2(int[] nums) {
        Set<Integer> allNumSet = new HashSet<>();
        for (int n : nums) {
            allNumSet.add(n);
        }
        Set<Integer> viewedNumSet = new HashSet<>();
        int longest = 0;
        for (int n : allNumSet) {
            if (viewedNumSet.contains(n)) {
                continue;
            }

            int currLongest = 0;
            while (allNumSet.contains(n)) {
                viewedNumSet.add(n);
                currLongest++;
                n--; // 为什么不行？
            }

            longest = Math.max(currLongest, longest);
        }
        return longest;
    }

    public int longestConsecutive(int[] nums) {
        Set<Integer> allNumSet = new HashSet<>();
        for (int n : nums) {
            allNumSet.add(n);
        }
        Set<Integer> viewedNumSet = new HashSet<>();
        int longest = 0;
        for (int n : allNumSet) {
            if (viewedNumSet.contains(n)) {
                continue;
            }

            int currLongest = 0;
            while (allNumSet.contains(n)) {
                viewedNumSet.add(n);
                currLongest++;
                n++;
            }

            longest = Math.max(currLongest, longest);
        }
        return longest;
    }

    @Test
    public void testP49() {
        String[] strs = new String[] {"eat", "tea", "tan", "ate", "nat", "bat"}; // [["bat"],["nat","tan"],["ate","eat","tea"]]

        print(groupAnagrams(strs));

//        strs = new String[] {""};
//        print(groupAnagrams(strs));

//        strs = new String[] {"a"};
//        print(groupAnagrams(strs));
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            char[] keyCharArr = s.toCharArray();
            Arrays.sort(keyCharArr);
            String key = String.valueOf(keyCharArr);

            List<String> value = map.getOrDefault(key, new ArrayList<>());
            value.add(s);
            map.put(key, value);
        }
        return new ArrayList<>(map.values());
    }

    @Test
    public void testP1(){
        int[] nums = new int[] {2,7,11,15};
        int target = 9;

        print(twoSum(nums, target));
    }

    public int[] twoSum(int[] nums, int target) {
        // a + b = target
        // a = target - b
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            indexMap.put(target - nums[i], i);
        }
        for (int i = 0; i < nums.length; i++) {
            Integer aIndex = indexMap.get(nums[i]);
            if (aIndex != null && aIndex != i) {
                return new int[] {aIndex, i};
            }
        }
        return null;
    }
}
