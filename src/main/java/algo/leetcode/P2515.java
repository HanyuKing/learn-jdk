package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/2 19:07
 * @Description
 * @Version 1.0
 **/
public class P2515 {
    public static void main(String[] args) {
        System.out.println(new P2515().closetTarget(
                new String[]{"odjrjznxpn","cyulttuabe","zqxkdoeszk",
                        "yeewpgriok","odjrjznxpn","btqpvxpjzv","ukyudladhk",
                        "ukyudladhk","odjrjznxpn","yeewpgriok"},
                "odjrjznxpn",
                5));
    }

    public int closetTarget(String[] words, String target, int startIndex) {
        boolean[] targets = new boolean[words.length];
        boolean exists = false;
        int L = -1, R = -1;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(target)) {
                targets[i] = true;
                exists = true;
                if (L == -1) {
                    L = i;
                }
                R = i;
            }
        }
        if (!exists) {
            return -1;
        }

        int leftIndex = -1;
        int rightIndex = -1;

        int i = startIndex;
        while (i >= 0) {
            if (targets[i]) {
                leftIndex = i;
                break;
            }
            i--;
        }
        i = startIndex;
        while (i < words.length) {
            if (targets[i]) {
                rightIndex = i;
                break;
            }
            i++;
        }

        if (startIndex == leftIndex || startIndex == rightIndex) {
            return 0;
        }

        if (leftIndex >= 0 && rightIndex >= 0) {
            return Math.min(startIndex - leftIndex, rightIndex - startIndex);
        } else if (rightIndex == -1) {
            return Math.min(startIndex - leftIndex, words.length - startIndex + L);
        } else {
            return Math.min(words.length - R + startIndex, rightIndex - startIndex);
        }
    }
}
