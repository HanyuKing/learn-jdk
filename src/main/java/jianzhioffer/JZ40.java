package jianzhioffer;

import java.util.HashMap;

public class JZ40 {

    public int[] FindNumsAppearOnce (int[] array) {
        if (array == null || array.length == 0) return new int[]{};
        HashMap<Integer, Integer> data = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            Integer num = data.get(array[i]);
            if (num == null) {
                data.put(array[i], 1);
            } else {
                data.put(array[i], num + 1);
            }
        }
        int[] nums = new int[2];
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            if (data.get(array[i]) == 1) {
                nums[index++] = array[i];
            }
        }
        if (nums[0] > nums[1]) return new int[]{nums[1], nums[0]};
        else return nums;
    }
}
