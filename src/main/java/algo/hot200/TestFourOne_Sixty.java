package algo.hot200;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/5/15 15:11
 * @Description
 * @Version 1.0
 **/
public class TestFourOne_Sixty extends Base {
    @Test
    public void testP4() {
        // todo
    }

    @Test
    public void testP93() {
        // todo

        String s = "25525511135"; // ["255.255.11.135","255.255.111.35"]
        List<String> result = restoreIpAddresses(s);
        print(result);

        s = "101023";
        result = restoreIpAddresses(s);
        print(result);
    }

    @Test
    public void testP31() {
        int[] arr = new int[] {2,1,3};
        // arr = [1,2,3] 的下一个排列是 [1,3,2]
        // arr = [2,3,1] 的下一个排列是 [3,1,2]
        // [2,1,3] -> [2,3,1]
        nextPermutation(arr);
        print(arr);
    }

    @Test
    public void testP148() {
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(4);
        ListNode node2 = new ListNode(3);
        ListNode node3 = new ListNode(2);

        head.next = node1;
        node1.next = node2;
        node2.next = node3;

        sortList(head);

        print(head);
    }

    public ListNode sortList(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode newHead = new ListNode();
        ListNode newTail = newHead;

        while (head != null) {
            ListNode curr = head;
            head = head.next;
            curr.next = null;

            ListNode newPre = null;
            ListNode newCurr = newHead.next;
            while (newCurr != null) {
                if (curr.val <= newCurr.val) {
                    break;
                }
                newPre = newCurr;
                newCurr = newCurr.next;
            }
            if (newPre == null) {
                curr.next = newHead.next;
                newHead.next = curr;
                if (newTail == newHead) {
                    newTail = curr;
                }
            } else if (newCurr == null) {
                newTail.next = curr;
            } else {
                curr.next = newCurr;
                newPre.next = curr;
            }
        }

        return newHead.next;
    }

    public void nextPermutation(int[] nums) {
        if (nums.length == 1) {
            return;
        }
        int j = nums.length - 1;
        int i = j - 1;
        while (i >= 0 && nums[i] >= nums[j]) {
            i--;
            j--;
        }
        if (i == -1) {
            for (int l = 0; l < nums.length / 2; l++) {
                swap(nums, l, nums.length - l - 1);
            }
        } else if (j == nums.length - 1) {
            swap(nums, j, j  -1);
        } else {
            for (int k = nums.length - 1; k >= i; k--) {
                if (nums[i] < nums[k]) {
                    swap(nums, i, k);
                    for (int l = 0; l < (nums.length - j) / 2; l++) {
                        swap(nums, j + l, nums.length - l - 1);
                    }
                    break;
                }
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public List<String> restoreIpAddresses(String s) {
        // "25525511135"; // ["255.255.11.135","255.255.111.35"]
        // "101023"
        List<String> result = new ArrayList<>();
        doRestoreIpAddresses(s, 0, 1, new StringBuilder(), result);
        return result;
    }

    private void doRestoreIpAddresses(String s,
                                      int start,
                                      int level,
                                      StringBuilder currStr,
                                      List<String> result) {
        if (start > s.length()) {
            return;
        }
        if (start == s.length() && level == 5) {
            result.add(currStr.toString().substring(0, currStr.length() - 1));
            return;
        }
        for (int i = start; i < s.length(); i++) {
            String subStr = s.substring(start, i + 1);
            if (subStr.charAt(0) == '0' && subStr.length() > 1) {
                break;
            }
            int subStrVal = Integer.parseInt(subStr);
            if (subStrVal >= 0 && subStrVal <= 255) {
                currStr.append(subStr).append(".");
                doRestoreIpAddresses(s, i + 1, level + 1, currStr, result);
                currStr.delete(currStr.length() - subStr.length() - 1, currStr.length());
            } else {
                break;
            }
        }
    }
}
