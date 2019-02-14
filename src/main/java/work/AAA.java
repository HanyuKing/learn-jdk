package work;

import org.junit.Test;

import java.util.*;

public class AAA {

    @Test
    public void test() {
        System.out.println(threeSum(new int[] {-1, 0, 1, 2, -1, -4}));
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        Set<String> sets = new TreeSet<String>();

        for(int i = 0; i < nums.length; i++) {
            for(int j = 0; j < nums.length; j++) {
                for(int k = 0; k < nums.length; k++) {
                    if(nums[i] + nums[j] + nums[k] == 0) {
                        if(i == j || j == k || i == k) {
                            continue;
                        }
                        int[] values = new int[] {nums[i], nums[j], nums[k]};
                        Arrays.sort(values);
                        String key = values[0] + "-" + values[1] + "-" + values[2];
                        if(!sets.contains(key)) {
                            result.add(new ArrayList<Integer>(Arrays.asList(nums[i], nums[j], nums[k])));
                            sets.add(key);
                        }
                    }
                }
            }
        }
        return result;
    }
}
