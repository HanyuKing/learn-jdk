package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/30 10:16
 * @Description
 * @Version 1.0
 **/
public class P2917 {

    public static void main(String[] args) {
        int[] nums = new int[]{10,8,5,9,11,6,8};
        int k = 1;

        System.out.println(new P2917().findKOr(nums, k));
    }

    public int findKOr(int[] nums, int k) {
        String[] binaryStr = new String[nums.length];
        int maxLen = 0;
        for (int i = 0; i < nums.length; i++) {
            binaryStr[i] = Integer.toBinaryString(nums[i]);
            if (binaryStr[i].length() > maxLen) {
                maxLen = binaryStr[i].length();
            }
        }

        String resultBinary = "";

        for (int i = 0; i < maxLen; i++) {
            int cnt = 0;
            for (String s : binaryStr) {
                int index = s.length() - 1 - i;
                if (index >= 0 && s.charAt(index) == '1') {
                    cnt++;
                    if (cnt == k) {
                        break;
                    }
                }
            }
            if (cnt >= k) {
                resultBinary = "1" + resultBinary;
            } else {
                resultBinary = "0" + resultBinary;
            }
        }

        return Integer.valueOf(resultBinary, 2);

    }
}
