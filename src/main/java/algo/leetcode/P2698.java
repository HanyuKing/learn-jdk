package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/27 15:43
 * @Description
 * @Version 1.0
 **/
public class P2698 {
    public static void main(String[] args) {
        int result = new P2698().punishmentNumber(10);
        System.out.println(result);
    }

    public int punishmentNumber(int n) {
        int result = 0;
        for (int i = 1; i <= n; i++) {
            int number = i * i;
            String s = String.valueOf(number);
            if (check(s, 0, 0, i)) {
                result += number;
            }
        }
        return result;
    }

    /**
     * 1296 = 1 + 29 + 6 = 36
     * 100 = 10 + 0 = 10
     * 81 = 8 + 1 = 9
     *
     * @param s
     * @param start
     * @param total
     * @param target
     * @return
     */
    private boolean check(String s, int start, int total, int target) {
        if (start == s.length()) {
            return total == target;
        }

        int sum = 0;
        for (int i = start; i < s.length(); i++) {
            sum = sum * 10 + s.charAt(i) - '0';
            if (sum + total > target) {
                break;
            }
            if (check(s, i + 1, sum + total, target)) {
                return true;
            }
        }

        return false;
    }
}
