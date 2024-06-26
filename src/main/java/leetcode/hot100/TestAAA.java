package leetcode.hot100;

import jdk.nashorn.internal.ir.LiteralNode;
import jol.A;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestAAA {

    @Test
    public void testP438() {
        String s = "cbaebabacd", p = "abc";
        System.out.println(findAnagrams(s, p));

        s = "abab";
        p = "ab";
        System.out.println(findAnagrams(s, p));
    }
    public List<Integer> findAnagrams(String s, String p) {
        Set<Character> characterSet = new HashSet<>();
        for (Character c : p.toCharArray()) {
            characterSet.add(c);
        }
        char[] sortedP = p.toCharArray();
        Arrays.sort(sortedP);

        List<Character> sb = new ArrayList<>();

        List<Integer> result = new ArrayList<>();

        int left = 0;
        int right = 0;

        while (right < s.length()) {
            Character rightCh = s.charAt(right);
            while (characterSet.contains(rightCh)) {
                sb.add(rightCh);
                right++;
                if (right == s.length()) {
                    break;
                }
                rightCh = s.charAt(right);
                if (right - left == p.length()) {
                    break;
                }
            }

            if (right - left == p.length()) {
                Collections.sort(sb);
                StringBuilder str = new StringBuilder();
                for (Character c : sb) {
                    str.append(c);
                }
                if (str.toString().equals(new String(sortedP))) { // ==
                    result.add(left);
                }
                sb.remove(Character.valueOf(s.charAt(left)));
                left++;
            } else {
                if (!sb.isEmpty()) {
                    sb.remove(Character.valueOf(s.charAt(left)));
                    left++;
                } else {
                    right++;
                    left++;
                }
            }

        }

        return result;
    }

    @Test
    public void testP560() {
        int[] nums = new int[] {100,1,2,3,4};
        int k = 6;

        System.out.println(subarraySum(nums, 6));
    }

    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> preSum = new HashMap<>();

        int ans = 0;
        // pre[i] = pre[i - 1] + nums[i]
        // pre[i] - pre[j - 1] = k
        int pre = 0;
        preSum.put(0, 1);
        for (int i = 0; i < nums.length; i++) {
            pre += nums[i];
            if (preSum.containsKey(pre - k)) {
                ans += preSum.get(pre - k);
            }
            preSum.put(pre, preSum.getOrDefault(pre, 0) + 1);
        }
        return ans;
    }

    @Test
    public void testP76() {
        /*
            输入：s = "ADOBECODEBANC", t = "ABC"
            输出："BANC"
            解释：最小覆盖子串 "BANC" 包含来自字符串 t 的 'A'、'B' 和 'C'。

         */
        String s = "ADOBECODEBANC", t = "ABC";
        String result = minWindow(s, t);
        System.out.println(result);
    }
    public String minWindow(String s, String t) {
        if (t.length() > s.length()) {
            return "";
        }
        Map<Character, Integer> needMap = new HashMap<>();
        Map<Character, Integer> existsMap = new HashMap<>();

        for (Character c : t.toCharArray()) {
            needMap.put(c, needMap.getOrDefault(c, 0) + 1);
        }

        int left = 0;
        int right = 0;
        int valid = 0;

        int start = 0;
        int minLen = Integer.MAX_VALUE;

        while (right < s.length()) {
            char ch = s.charAt(right);
            right++;

            if (needMap.containsKey(ch)) {
                existsMap.put(ch, existsMap.getOrDefault(ch, 0) + 1);
                if (existsMap.get(ch).equals(needMap.get(ch))) {
                    valid++;
                }
            }

            while (valid == needMap.size()) {
                if (right - left < minLen) {
                    start = left;
                    minLen = right - left;
                }
                Character leftCh = s.charAt(left);
                if (needMap.containsKey(leftCh)) {
                    existsMap.put(leftCh, existsMap.get(leftCh) - 1);
                    if (existsMap.get(leftCh) < needMap.get(leftCh)) {
                        valid--;
                    }
                }
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    @Test
    public void testP567() {
        /*
            输入：s1 = "ab" s2 = "eidbaooo"
            输出：true
            解释：s2 包含 s1 的排列之一 ("ba").
         */
        boolean result = checkInclusion("ba", "eidbaooo");
        System.out.println(result);
    }

    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) {
            return false;
        }
        Map<Character, Integer> needMap = new HashMap<>();
        Map<Character, Integer> existsMap = new HashMap<>();

        for (Character c : s1.toCharArray()) {
            needMap.put(c, needMap.getOrDefault(c, 0) + 1);
        }

        int left = 0;
        int right = 0;
        int valid = 0;

        while (right < s2.length()) {
            char rChar = s2.charAt(right);
            right++;
            if (needMap.containsKey(rChar)) {
                existsMap.put(rChar, existsMap.getOrDefault(rChar, 0) + 1);
                int need = needMap.get(rChar);
                int exists = existsMap.get(rChar);
                if (need == exists) {
                    valid++;
                }
            }

            while (right - left >= s1.length()) {
                if (valid == needMap.size()) {
                    return true;
                }
                char lChar = s2.charAt(left);
                if (needMap.containsKey(lChar)) {
                    if (existsMap.get(lChar).equals(needMap.get(lChar))) {
                        valid--;
                    }
                    existsMap.put(lChar, existsMap.get(lChar) - 1);
                }
                left++;
            }

        }

        return false;
    }

    public List<Integer> findAnagrams2(String s2, String s1) {
        if (s1.length() < s2.length()) {
            return new ArrayList<>();
        }
        Map<Character, Integer> needMap = new HashMap<>();
        Map<Character, Integer> existsMap = new HashMap<>();

        for (Character c : s1.toCharArray()) {
            needMap.put(c, needMap.getOrDefault(c, 0) + 1);
        }

        int left = 0;
        int right = 0;
        int valid = 0;

        List<Integer> result = new ArrayList<>();

        while (right < s2.length()) {
            char rChar = s2.charAt(right);
            right++;
            if (needMap.containsKey(rChar)) {
                existsMap.put(rChar, existsMap.getOrDefault(rChar, 0) + 1);
                int need = needMap.get(rChar);
                int exists = existsMap.get(rChar);
                if (need == exists) {
                    valid++;
                }
            }

            while (right - left >= s1.length()) {
                if (valid == needMap.size()) {
                    result.add(left);
                }
                char lChar = s2.charAt(left);
                if (needMap.containsKey(lChar)) {
                    if (existsMap.get(lChar).equals(needMap.get(lChar))) {
                        valid--;
                    }
                    existsMap.put(lChar, existsMap.get(lChar) - 1);
                }
                left++;
            }

        }

        return result;
    }

    @Test
    public void testP234() {
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(3);
        ListNode node3 = new ListNode(2);
        ListNode node4 = new ListNode(1);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

        System.out.println(isPalindrome(head));
    }

    public boolean isPalindrome(ListNode head) {
        if (head == null) {
            return false;
        }
        if (head.next == null) {
            return true;
        }

        int totalCount = 0;
        ListNode headTemp = head;
        while(headTemp != null) {
            totalCount++;
            headTemp = headTemp.next;
        }

        ListNode p = head;
        ListNode q = p.next;

        head.next = null;

        int count = 1;

        while(q.next != null && count < totalCount / 2) {
            ListNode r = q.next;
            q.next = p;
            p = q;
            q = r;
            count++;
        }

        if ((totalCount & 1) == 1) {
            q = q.next;
        }

        while(q != null && p != null && q.val == p.val) {
            q = q.next;
            p = p.next;
        }

        return q == null && p == null;
    }

    @Test
    public void testP25() {
        ListNode head = new ListNode(1);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(3);
        ListNode node3 = new ListNode(4);
        ListNode node4 = new ListNode(5);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;

        ListNode listNode = reverseKGroup(head, 1);
        while (listNode != null) {
            System.out.print(listNode.val + " ");
            listNode = listNode.next;
        }
        System.out.println();
    }

    public ListNode reverseKGroup(ListNode head, int k) {

        ListNode tail = new ListNode();
        ListNode newHead = tail;
        ListNode headTemp = head;

        while (headTemp != null) {
            ListNode[] list = reverse(headTemp, k);
            tail.next = list[0];
            tail = headTemp;
            headTemp = list[2];

        }

        return newHead.next;
    }

    private ListNode[] reverse(ListNode head, int k) {
        if (head == null || head.next == null || k <= 1) {
            return new ListNode[] {head, null, null};
        }

        int totalCount = 0;
        ListNode headTemp = head;
        while (headTemp != null) {
            totalCount++;
            headTemp = headTemp.next;
        }
        if (totalCount < k) {
            return new ListNode[] {head, null, null};
        }

        ListNode p = head;
        ListNode q = head.next;
        head.next = null;

        ListNode r = null;
        int count = 2;
        while (q.next != null) {
            r = q.next;

            if (count++ == k) {
                break;
            }

            q.next = p;
            p = q;
            q = r;
        }

        if (q.next == null) {
            r = null;
        }


        q.next = p;

        return new ListNode[] {q, p, r};
    }

    @Test
    public void testParseInt() {
        Integer.parseInt("-12");
    }

    @Test
    public void testP148() {
        ListNode head = new ListNode(4);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(1);
        ListNode node3 = new ListNode(3);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        sortList(head);
    }

    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        ListNode newHead = slow.next;
        slow.next = null;
        ListNode h1 = sortList(head);
        ListNode h2 = sortList(newHead);
        return merge(h1, h2);
    }

    private ListNode merge(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode head = null;
        ListNode tail = null;
        if (l1.val <= l2.val) {
            head = l1;
            l1 = l1.next;
            head.next = null;
            tail = head;
        } else {
            head = l2;
            l2 = l2.next;
            head.next = null;
            tail = head;
        }

        while (l1 != null && l2 != null) {
            int v1 = l1.val;
            int v2 = l2.val;
            if (v1 <= v2) {
                tail.next = l1;
                tail = l1;
                l1 = l1.next;
                tail.next = null;
            } else {
                tail.next = l2;
                tail = l2;
                l2 = l2.next;
                tail.next = null;
            }
        }
        tail.next = l1 == null ? l2 : l1;

        return head;
    }

    public ListNode sortList2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = new ListNode();
        ListNode firstNode = head;
        newHead.next = firstNode;
        head = head.next;
        firstNode.next = null;

        while (head != null) {

            ListNode currNode = head;
            head = head.next;
            currNode.next = null;

            ListNode newHeadTemp = newHead.next;
            ListNode newHeadPre = null;

            while(newHeadTemp != null && currNode.val > newHeadTemp.val) {
                newHeadPre = newHeadTemp;
                newHeadTemp = newHeadTemp.next;
            }
            if (newHeadPre == null) {
                currNode.next = newHead.next;
                newHead.next = currNode;
            } else {
                currNode.next = newHeadTemp;
                newHeadPre.next = currNode;
            }
        }
        return newHead.next;
    }

    @Test
    public void testP94() {
        TreeNode root = new TreeNode(2);
        TreeNode node1 = new TreeNode(3);
        TreeNode node2 = new TreeNode(1);
        root.left = node1;
        node1.left = node2;

        System.out.println(inorderTraversal(root));
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Stack<TreeNode> stack = new Stack<>();

        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            TreeNode node = stack.pop();
            result.add(node.val);
            if (node.right != null) {
                root = node.right;
            }
        }
        return result;
    }

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode currNode = stack.pop();
            result.add(currNode.val);

            if (currNode.right != null) {
                stack.push(currNode.right);
            }
            if (currNode.left != null) {
                stack.push(currNode.left);
            }
        }
        return result;
    }

    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Stack<TreeNode> stack = new Stack<>();
        Stack<TreeNode> stack2 = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            stack2.add(node);

            if (node.left != null) {
                stack.push(node.left);
            }

            if (node.right != null) {
                stack.push(node.right);
            }
        }
        while (!stack2.isEmpty()) {
            result.add(Integer.valueOf(stack2.pop().val));
        }
        return result;
    }

    @Test
    public void testP104() {

    }

    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return maxDepth(root, 1);
    }
    public int maxDepth(TreeNode root, int h) {
        if (root == null) {
            return h;
        }
        int leftMax = maxDepth(root.left, h + 1);
        int rightMax = maxDepth(root.right, h + 1);
        return Math.max(leftMax, rightMax);
    }

    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode left = invertTree(root.right);
        TreeNode right = invertTree(root.left);
        root.right = left;
        root.left = right;
        int i = 0;
        if ((i & 1) == 0) {

        }
        return root;
    }


    public class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }

    public class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }
  }
}
