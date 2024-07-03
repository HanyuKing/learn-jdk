package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2024/7/3 10:28
 * @Description
 * @Version 1.0
 **/
public class P3099 {
    public static void main(String[] args) {
        System.out.println(new P3099().sumOfTheDigitsOfHarshadNumber(18));
    }
    public int sumOfTheDigitsOfHarshadNumber(int x) {
        int tempX = x;
        int sum = 0;
        int radix = 10;
        while (x > 0) {
            sum += x % radix;
            x /= radix;
        }
        return tempX % sum == 0 ? sum : -1;
    }
}
