package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/2 10:48
 * @Description
 * @Version 1.0
 **/
public class P2103 {
    public static void main(String[] args) {
        System.out.println(new P2103().countPoints("R2B2B1G2R1G1"));
    }

    public int countPoints(String rings) {
        int result = 0;

        int[][] array = new int[10][3];
        for (int i = 0; i < rings.length(); i += 2) {
            char ch = rings.charAt(i);
            int position = rings.charAt(i + 1) - '0';

            switch (ch) {
                case 'R':
                    array[position][0] = 1;
                    break;
                case 'G':
                    array[position][1] = 1;
                    break;
                case 'B':
                    array[position][2] = 1;
                    break;
            }
        }

        for (int i = 0; i < 10; i++) {
            int count = 0;
            for (int j = 0; j < 3; j++) {
                if (array[i][j] == 1) {
                    count++;
                }
            }
            if (count == 3) {
                result++;
            }
        }

        return result;
    }
}
