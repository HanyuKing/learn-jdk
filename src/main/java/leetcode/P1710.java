package leetcode;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2022/11/15 17:19
 * @Description
 * @Version 1.0
 **/
public class P1710 {
    public static void main(String[] args) {
        // [[1,3],[2,2],[3,1]], 4  8
        // [[5,10],[2,5],[4,7],[3,9]], 10 91
//        System.out.println(new P1710().maximumUnits(
//                new int[][]{{1,3},{2,2},{3,1}},
//        4
//        ));

        System.out.println(new P1710().maximumUnits(
                new int[][]{{5,10},{2,5},{4,7},{3,9}},
                10
        ));
    }

    public int maximumUnits(int[][] boxTypes, int truckSize) {
        Arrays.sort(boxTypes, (o1, o2) -> o2[1] - o1[1]);

        int maxUnit = 0;

        for (int i = 0; i < boxTypes.length; i++) {
            int count = Math.min(boxTypes[i][0], truckSize);
            maxUnit += count * boxTypes[i][1];
            truckSize -= count;
        }

        return maxUnit;
    }
}
