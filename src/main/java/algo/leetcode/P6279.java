package algo.leetcode;

import java.util.*;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/1 22:43
 * @Description
 * @Version 1.0
 **/
public class P6279 {
    public static void main(String[] args) {
        System.out.println(new P6279().distinctPrimeFactors(new int[]{2,14,19,19,5,13,18,10,15,20}));
    }

    public int distinctPrimeFactors(int[] nums) {
        List<Integer> allPrime = listAllPrime();
        Set[] table = new HashSet[1001];
        table[0] = new HashSet();
        table[1] = new HashSet();

        for (int i = 2; i <= 1000; i++) {
            Set<Integer> sets = new HashSet<>();
            int n = i;
            for (int j = allPrime.size() - 1; j >= 0;) {
                if (n % allPrime.get(j) == 0) {
                    sets.add(allPrime.get(j));
                    n = n / allPrime.get(j);
                    continue;
                }
                j--;
            }
            table[i] = sets;
        }

        Set<Integer> cnt = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            cnt.addAll(table[nums[i]]);
        }
        return cnt.size();
    }

    private List<Integer> listAllPrime() {
        List<Integer> result = new ArrayList<>();
        for (int i = 2; i <= 1000; i++) {
            boolean is = true;
            for (int j = 2; j <= (int) Math.sqrt(i) + 1; j++) {
                if (i != j && i % j == 0) {
                    is = false;
                    break;
                }
            }
            if (is) {
                result.add(i);
            }
        }
        return result;
    }
}
