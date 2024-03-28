package leetcode;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/27 17:10
 * @Description
 * @Version 1.0
 **/
public class P2580 {
    public static void main(String[] args) {
        int[][] ranges = new int[][] {
                {1, 3},
                {10, 20},
                {2, 5},
                {4,8}
        };

        System.out.println(new P2580().countWays(ranges));
    }

    public int countWays(int[][] ranges) {
        Arrays.sort(ranges, Comparator.comparingInt(o -> o[0]));

        // merge
        int index = 0;


        for (int i = 1; i < ranges.length; i++) {
            if (ranges[i][0] <= ranges[index][1]) {
                ranges[index][1] = Math.max(ranges[i][1], ranges[index][1]);
            } else {
                index++;
                ranges[index][0] = ranges[i][0];
                ranges[index][1] = ranges[i][1];
            }
        }

        int count = index + 1;

        return (int) (quickPow(2, count, (int) (Math.pow(10, 9)) + 7));
    }

    // 快速计算 n ^ k % p
    public long quickPow(long n, int k, int p) {
        if (n == 0) return 0;
        if (k == 0) return 1;
        return k > 0 ? myPow(n, k, p) : 1 / myPow(n, k, p * (-1));
    }

    private long myPow(long n, int k, int p) {
        long res = 1;
        while (k != 0) {
            if ((k & 1) == 1) {
                res = res * n % p;
            }
            k >>= 1;
            n = n * n % p;
        }
        return res;
    }
}
