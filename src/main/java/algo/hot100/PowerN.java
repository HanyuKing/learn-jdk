package algo.hot100;

/**
 * @Author Hanyu.Wang
 * @Date 2024/7/4 19:12
 * @Description
 * @Version 1.0
 **/
public class PowerN {
    public static void main(String[] args) {
//        double x = 2;
//        int n = Integer.MIN_VALUE; // 9.26100
        double x = 2;
        int n = 10;
        System.out.println(new PowerN().myPow(x, n));
    }

    public double myPow(double x, int n) {
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return x;
        }
        double result = 1;
        // 11
        // 10
        // 110
        // 1010
        long absN = Math.abs((long)n);
        double c = x;
        while (absN > 0) {
            if ((absN & 1) == 1) {
                result = result * c;
            }
            c = c * c;
            absN = absN >> 1;
        }

        return n > 0 ? result : 1 / result;
    }
}
