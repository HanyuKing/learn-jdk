package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/30 17:11
 * @Description
 * @Version 1.0
 **/
public class P3216 {
    public static void main(String[] args) {
        System.out.println(new P3216().getSmallestString("45320")); // 43520
    }

    public String getSmallestString(String s) {
        char[] arr = s.toCharArray();
        for (int i = 1; i < arr.length; i++) {
            if ((arr[i] & 1) == 1 && (arr[i - 1] & 1) == 1 && arr[i] < arr[i - 1]
                    || (arr[i] & 1) == 0 && (arr[i - 1] & 1) == 0 && arr[i] < arr[i - 1]) {
                char temp = arr[i];
                arr[i] = arr[i - 1];
                arr[i - 1] = temp;
                break;
            }
        }
        return String.valueOf(arr);
    }
}
