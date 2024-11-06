package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/5 10:01
 * @Description
 * @Version 1.0
 **/
public class P3222 {
    public static void main(String[] args) {
        System.out.println(new P3222().losingPlayer(2, 7));

        System.out.println(new P3222().losingPlayer(4, 11));
    }
    public String losingPlayer(int x, int y) {
        return (Math.min(x, (y / 4)) & 1) == 1 ? "Alice" : "Bob";
    }

    public String losingPlayer2(int x, int y) {
        boolean isAlice = false;
        while (x > 0 && y > 0) {
            x -= 1;
            y -= 4;

            if (y < 0) {
                break;
            }

            isAlice = !isAlice;
        }
        return !isAlice ? "Alice" : "Bob";
    }
}
