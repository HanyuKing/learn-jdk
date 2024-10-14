package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/1 22:36
 * @Description
 * @Version 1.0
 **/
public class P6278 {
    public static void main(String[] args) {
        System.out.println(new P6278().countDigits(121));
    }

    public int countDigits(int num) {
        int newN = num / 10;
        int n = num - newN * 10;
        int cnt = 0;
        while (n > 0) {
            if (num % n == 0) {
                cnt++;
            }
            n = newN - (newN / 10) * 10;
            newN = newN / 10;
        }
        return cnt;
    }
}
