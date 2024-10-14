package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/2/20 12:33
 * @Description
 * @Version 1.0
 **/
public class P6365 {
    public static void main(String[] args) {
        System.out.println(new Solution6365().minOperations(47));
    }

    static class Solution6365 {
        public int minOperations(int n) {
            int total = 0;
            while(n > 0) {
                int cnt = calCnt(n);
                if (n  == (int)Math.pow(2, cnt)) {
                    return total + 1;
                }

                total++;
                cnt--;

                if (n - (int)Math.pow(2, cnt) > (int)Math.pow(2, cnt + 1) - n) {
                    n = (int)Math.pow(2, cnt + 1) - n;
                } else {
                    n = n - (int)Math.pow(2, cnt);
                }
            }
            return total;
        }

        private int calCnt(int n) {
            int cnt = 0;
            while(n > 0) {
                n = n / 2;
                cnt++;
            }
            return cnt;
        }
    }
}
