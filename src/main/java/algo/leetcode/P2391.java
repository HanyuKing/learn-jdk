package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2024/5/11 09:53
 * @Description
 * @Version 1.0
 **/
public class P2391 {
    public static void main(String[] args) {
        String[] garbage = new String[] {"G", "P", "GP", "GG"};
        int[] travel = new int[] {2, 4, 3};

        System.out.println(new P2391().garbageCollection(garbage, travel));
    }

    public int garbageCollection(String[] garbage, int[] travel) {
        int x = 0, y = 0, z = 0;

        int lastM = -1, lastP = -1, lastG = -1;

        for (int i = 0; i < garbage.length; i++) {
            String g = garbage[i];
            for (char c : g.toCharArray()) {
                switch (c) {
                    case 'M':
                        x++;
                        lastM = i;
                        break;
                    case 'P':
                        y++;
                        lastP = i;
                        break;
                    case 'G':
                        z++;
                        lastG = i;
                }
            }
        }

        int[] preSum = new int[travel.length];
        preSum[0] = travel[0];
        for (int i = 1; i < travel.length; i++) {
            preSum[i] = preSum[i - 1] + travel[i];
        }

        int sum1 = lastM <= 0 ? 0 : preSum[lastM - 1];
        int sum2 = lastP <= 0 ? 0 : preSum[lastP - 1];
        int sum3 = lastG <= 0 ? 0 : preSum[lastG - 1];

        return x + y + z + sum1 + sum2 + sum3;
    }
}
