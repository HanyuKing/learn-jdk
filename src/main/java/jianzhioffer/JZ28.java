package jianzhioffer;

import java.util.HashMap;
import java.util.Map;

/**
 * JZ28 数组中出现次数超过一半的数字
 */
public class JZ28 {

    public int MoreThanHalfNum_Solution(int [] array) {
        int preNum = -1;
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (count == 0) {
                preNum = array[i];
                count++;
            } else {
                if (preNum == array[i]) {
                    count++;
                } else {
                    count--;
                }
            }
        }
        return preNum;
    }
}
