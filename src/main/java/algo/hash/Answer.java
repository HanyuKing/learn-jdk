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
    public void testP12() {
        // intToRoman
        // intToRoman2
        // intToRoman3
        print(intToRoman(3749));

        print(intToRoman(58));

        print(intToRoman(1994));
    }

    public String intToRoman(int num) {
        String[] thousands = new String[] {"", "M", "MM", "MMM"};
        String[] hundreds = new String[] {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = new String[] {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] digits = new String[] {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

        String result = "";

        if (num / 1000 > 0) {
            result += thousands[num / 1000];
            num = num - (num / 1000) * 1000;
        }
        if (num / 100 > 0) {
            result += hundreds[num / 100];
            num = num - (num / 100) * 100;
        }
        if (num / 10 > 0) {
            result += tens[num / 10];
            num = num - (num / 10) * 10;
        }
        if (num > 0) {
            result += digits[num];
            // num = 0;
        }

        return result;
    }

    public String intToRoman3(int num) {
//        TreeMap<Integer, String> map = new TreeMap<>((o1, o2) -> o2 - o1);
//        map.put(1, "I");
//        map.put(2, "II");
//        map.put(3, "III");
//        map.put(4, "IV");
//        map.put(5, "V");
//        map.put(9, "IX");
//        map.put(10, "X");
//        map.put(20, "XX");
//        map.put(30, "XXX");
//        map.put(40, "XL");
//        map.put(50, "L");
//        map.put(90, "XC");
//        map.put(100, "C");
//        map.put(200, "CC");
//        map.put(300, "CCC");
//        map.put(400, "CD");
//        map.put(500, "D");
//        map.put(900, "CM");
//        map.put(1000, "M");
//        map.put(2000, "MM");
//        map.put(3000, "MMM");

        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder result = new StringBuilder();

        while (num > 0) {
            for (int i = 0; i < values.length; i++) {
                int value = values[i];
                if (num - value >= 0) {
                    result.append(symbols[i]);
                    num -= value;
                    break;
                }
            }
        }

        return result.toString();
    }

    public String intToRoman2(int num) {
        TreeMap<Integer, String> map = new TreeMap<>((o1, o2) -> o2 - o1);
        map.put(1, "I");
        map.put(2, "II");
        map.put(3, "III");
        map.put(4, "IV");
        map.put(5, "V");
        map.put(9, "IX");
        map.put(10, "X");
        map.put(20, "XX");
        map.put(30, "XXX");
        map.put(40, "XL");
        map.put(50, "L");
        map.put(90, "XC");
        map.put(100, "C");
        map.put(200, "CC");
        map.put(300, "CCC");
        map.put(400, "CD");
        map.put(500, "D");
        map.put(900, "CM");
        map.put(1000, "M");
        map.put(2000, "MM");
        map.put(3000, "MMM");

        StringBuilder result = new StringBuilder();

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (map.containsKey(num)) {
                result.append(map.get(num));
                break;
            }
            while (num - entry.getKey() > 0) {
                result.append(entry.getValue());
                num -= entry.getKey();
                break;
            }
            if (num == 0) {
                break;
            }
        }

        return result.toString();
    }



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
