package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2022/12/13 10:27
 * @Description
 * @Version 1.0
 **/
public class P1832 {
    public static void main(String[] args) {

    }
    //全字母句 指包含英语字母表中每个字母至少一次的句子。
//
// 给你一个仅由小写英文字母组成的字符串 sentence ，请你判断 sentence 是否为 全字母句 。
//
// 如果是，返回 true ；否则，返回 false 。
//
//
//
// 示例 1：
//
//
//输入：sentence = "thequickbrownfoxjumpsoverthelazydog"
//输出：true
//解释：sentence 包含英语字母表中每个字母至少一次。
//
//
// 示例 2：
//
//
//输入：sentence = "leetcode"
//输出：false
//
//
//
//
// 提示：
//
//
// 1 <= sentence.length <= 1000
// sentence 由小写英语字母组成
//
//
// Related Topics 哈希表 字符串 👍 42 👎 0


    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean checkIfPangram(String sentence) {
            boolean[] array = new boolean[26];
            char[] s = sentence.toCharArray();
            for (int i = 0; i < s.length; i++) {
                array[s[i] - 'a'] = true;
            }

            for (int i = 0; i < array.length; i++) {
                if (!array[i]) {
                    return false;
                }
            }

            return true;
        }
    }
    class Solution2 {
        public boolean checkIfPangram(String sentence) {
            int state = 0;
            char[] s = sentence.toCharArray();
            for (int i = 0; i < s.length; i++) {
                state |= 1 << (s[i] - 'a');
            }
            return state == (1 << 26) - 1;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
