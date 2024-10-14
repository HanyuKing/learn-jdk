package algo.leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/10 10:55
 * @Description
 * @Version 1.0
 **/
public class P2300 {

    public static void main(String[] args) {
        int[] spells = new int[] {5, 1, 3};
        int[] potions = new int[] {1, 2, 3, 4, 5};
        long success = 7;
        int[] result = new P2300().successfulPairs2(spells, potions, success);

        Arrays.stream(result).forEach(n -> System.out.println(n));
    }

    public int[] successfulPairs2(int[] spells, int[] potions, long success) {
        int[] result = new int[spells.length];

        Arrays.sort(potions);

        for (int i = 0; i < spells.length; i++) {
            int r = binaryFindMin2(spells[i], potions, success);
            if (r == -1) {
                result[i] = 0;
            } else {
                result[i] = potions.length - r;
            }
        }

        return result;
    }

    private int binaryFindMin2(long spell, int[] potions, long success) {
        int low = 0;
        int high = potions.length - 1;
        int r = -1;

        int target = (int) Math.ceil((double) success / spell);

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (potions[mid] >= target) {
                r = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return r;
    }

    private int binaryFindMin(long spell, int[] potions, long success) {
        int low = 0;
        int high = potions.length - 1;
        int r = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (spell * potions[mid] >= success) {
                r = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return r;
    }

    public int[] successfulPairs(int[] spells, int[] potions, long success) {
        int[] result = new int[spells.length];

        for (int i = 0; i < spells.length; i++) {
            for (int j = 0; j < potions.length; j++) {
                if ((long) spells[i] * (long) potions[j] >= success) {
                    result[i]++;
                }
            }
        }

        return result;
    }
}
