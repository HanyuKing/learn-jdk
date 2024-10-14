package algo.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2023/2/8 19:12
 * @Description
 * @Version 1.0
 **/
public class P1233 {
    public static void main(String[] args) {
        //输入：folder = ["/a","/a/b","/c/d","/c/d/e","/c/f"]
        //输出：["/a","/c/d","/c/f"]
        //解释："/a/b" 是 "/a" 的子文件夹，而 "/c/d/e" 是 "/c/d" 的子文件夹。

        System.out.println(new Solution1233().removeSubfolders(new String[]{"/a","/a/b","/c/d","/c/d/e","/c/f"}));
    }
    //你是一位系统管理员，手里有一份文件夹列表 folder，你的任务是要删除该列表中的所有 子文件夹，并以 任意顺序 返回剩下的文件夹。
//
// 如果文件夹 folder[i] 位于另一个文件夹 folder[j] 下，那么 folder[i] 就是 folder[j] 的 子文件夹 。
//
// 文件夹的「路径」是由一个或多个按以下格式串联形成的字符串：'/' 后跟一个或者多个小写英文字母。
//
//
// 例如，"/leetcode" 和 "/leetcode/problems" 都是有效的路径，而空字符串和 "/" 不是。
//
//
//
//
// 示例 1：
//
//
//输入：folder = ["/a","/a/b","/c/d","/c/d/e","/c/f"]
//输出：["/a","/c/d","/c/f"]
//解释："/a/b" 是 "/a" 的子文件夹，而 "/c/d/e" 是 "/c/d" 的子文件夹。
//
//
// 示例 2：
//
//
//输入：folder = ["/a","/a/b/c","/a/b/d"]
//输出：["/a"]
//解释：文件夹 "/a/b/c" 和 "/a/b/d" 都会被删除，因为它们都是 "/a" 的子文件夹。
//
//
// 示例 3：
//
//
//输入: folder = ["/a/b/c","/a/b/ca","/a/b/d"]
//输出: ["/a/b/c","/a/b/ca","/a/b/d"]
//
//
//
// 提示：
//
//
// 1 <= folder.length <= 4 * 10⁴
// 2 <= folder[i].length <= 100
// folder[i] 只包含小写字母和 '/'
// folder[i] 总是以字符 '/' 起始
// 每个文件夹名都是 唯一 的
//
//
// Related Topics 字典树 数组 字符串 👍 129 👎 0

    static class Solution1233 {
        public List<String> removeSubfolders(String[] folder) {
            TrieTree trieTree = new TrieTree(folder);
            return trieTree.calculate();
        }

        class TrieTree {
            public Map<String, TrieTree> value = new HashMap<>();;
            public boolean isFolder;
            public TrieTree(String[] folder) {
                for (String f : folder) {
                    create(f, 0);
                }
            }

            public TrieTree() {}

            private void create(String f, int folderIndex) {
                folderIndex = f.indexOf("/", folderIndex + 1);
                String fName = folderIndex > 0 ? f.substring(0, folderIndex) : f;

                TrieTree currValue = value.getOrDefault(fName, new TrieTree());
                value.put(fName, currValue);

                if (folderIndex < 0) {
                    currValue.isFolder = true;
                } else {
                    currValue.create(f, folderIndex);
                }
            }

            public List<String> calculate() {
                List<String> result = new ArrayList<>();
                for (Map.Entry<String, TrieTree> entry : this.value.entrySet()) {
                    findShortest(result, entry.getKey(), entry.getValue());
                }
                return result;
            }

            private void findShortest(List<String> paths, String fName, TrieTree node) {
                if (node.isFolder) {
                    paths.add(fName);
                    return;
                }

                for (Map.Entry<String, TrieTree> entry : node.value.entrySet()) {
                    findShortest(paths, entry.getKey(), entry.getValue());
                }

            }
        }
    }
}
