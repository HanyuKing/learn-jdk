package algo.leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/27 10:02
 * @Description
 * @Version 1.0
 **/
public class P1465 {
    public static void main(String[] args) {
        int h = 5;
        int w = 4;
        int[] horizontalCuts = new int[]{1, 2, 4};
        int[] verticalCuts = new int[]{1, 3};

//        int[] horizontalCuts = new int[]{3, 1};
//        int[] verticalCuts = new int[]{1};

//        int[] horizontalCuts = new int[]{3};
//        int[] verticalCuts = new int[]{3};

//        int h = 2;
//        int w = 7;
//        int[] horizontalCuts = new int[]{1};
//        int[] verticalCuts = new int[]{2, 1, 5};

        System.out.println(new P1465().maxArea(h, w, horizontalCuts, verticalCuts));
    }

    public int maxArea(int h, int w, int[] horizontalCuts, int[] verticalCuts) {
        Arrays.sort(horizontalCuts);
        Arrays.sort(verticalCuts);

        long maxH = Math.max(horizontalCuts[0], h - horizontalCuts[horizontalCuts.length - 1]);
        long maxW = Math.max(verticalCuts[0], w - verticalCuts[verticalCuts.length - 1]);

        for (int i = 1; i < verticalCuts.length; i++) {
            if (verticalCuts[i] - verticalCuts[i - 1] > maxW) {
                maxW = verticalCuts[i] - verticalCuts[i - 1];
            }
        }

        for (int i = 1; i < horizontalCuts.length; i++) {
            if (horizontalCuts[i] - horizontalCuts[i - 1] > maxH) {
                maxH = horizontalCuts[i] - horizontalCuts[i - 1];
            }
        }

        return (int) ((maxH * maxW) % 1000000007);
    }
}
