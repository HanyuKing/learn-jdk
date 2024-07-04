package leetcode.hot100;

/**
 * @Author Hanyu.Wang
 * @Date 2024/7/4 19:12
 * @Description
 * @Version 1.0
 **/
public class PowerN {
    public static void main(String[] args) {
        double x = 0.00001;
        int n = Integer.MAX_VALUE; // 9.26100
        System.out.println(new PowerN().myPow(x, n));
    }

    public double myPow(double x, int n) {
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return x;
        }
        int absN = Math.abs(n);
        double absX = Math.abs(x);
        double result = absX;

        long i = 2;
        while (i < absN) {
            result *= result;
            i *= 2;
        }
        // 2 * 2
        // 4 * 4
        // 16 * 16
        i = absN - i / 2;
        while (--i >= 0) {
            result *= absX;
        }

        if (x < 0 && (n & 1) == 1) {
            result = -result;
        }

        if (n < 0) {
            result = 1 / result;
        }
        return result;
    }
}
