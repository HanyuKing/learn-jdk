package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/11/6 09:51
 * @Description
 * @Version 1.0
 **/
public class P318 {

    public static void main(String[] args) {
        String[] words = new String[] {"abcw","baz","foo","bar","xtfn","abcdef"};

        int result = new P318().maxProduct2(words);

        System.out.println(result);
    }

    public int maxProduct2(String[] words) {
        int max = 0;
        int[] mask = new int[words.length];

        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[i].length(); j++) {
                mask[i] |= 1 << words[i].charAt(j) - 'a';
            }
        }

        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j != i && j < words.length; j++) {
                if ((mask[i] & mask[j]) == 0) {
                    max = Math.max(max, words[i].length() * words[j].length());
                }
            }
        }
        return max;
    }

    public int maxProduct(String[] words) {
        int max = 0;
        int[][] array = new int[words.length][26];
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[i].length(); j++) {
                array[i][words[i].charAt(j) - 'a'] = 1;
            }
        }

        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j != i && j < words.length; j++) {
                if (!hasCommon(array[i], array[j])) {
                    max = Math.max(max, words[i].length() * words[j].length());
                }
            }
        }
        return max;
    }

    private boolean hasCommon(int[] a1, int[] a2) {
        int minLen = Math.min(a1.length, a2.length);
        for (int i = 0; i < minLen; i++) {
            if (a1[i] == 1 && a1[i] == a2[i]) {
                return true;
            }
        }
        return false;
    }
}
