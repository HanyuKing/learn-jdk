package leetcode;

public class P677 {
    public static void main(String[] args) {
        MapSum mapSum = new MapSum();
        mapSum.insert("apple", 3);
        System.out.println(mapSum.sum("ap"));           // return 3 (apple = 3)
        mapSum.insert("app", 2);
        mapSum.insert("apple", 2);
        System.out.println(mapSum.sum("ap"));           // return 5 (apple + app = 3 + 2 = 5)
    }

}

class MapSum {
    private Node[] dict;

    /** Initialize your data structure here. */
    public MapSum() {
        this.dict = new Node[26];
    }

    public void insert(String key, int val) {
        if (key.length() == 0) throw new IllegalArgumentException();

        Node existsNode = findNode(key);

        Node[] dictTemp = this.dict;
        Node targetNode = null;
        for(char c : key.toCharArray()) {
            targetNode = dictTemp[c - 'a'];
            if (targetNode == null) {
                targetNode = new Node();
                dictTemp[c - 'a'] = targetNode;
            }
            if (existsNode != null) {
                targetNode.setSum(targetNode.getSum() - existsNode.getVal() + val);
            } else {
                targetNode.setSum(targetNode.getSum() + val);
            }

            dictTemp = targetNode.getDict();
        }
        targetNode.setVal(val);
        targetNode.setWord(true);
    }

    private Node findNode(String key) {

        if (key.length() == 0) throw new IllegalArgumentException();

        Node[] dictTemp = this.dict;
        Node targetNode = null;
        for(char c : key.toCharArray()) {
            targetNode = dictTemp[c - 'a'];
            if (targetNode == null) {
                return null;
            }
            dictTemp = targetNode.getDict();
        }
        return targetNode;
    }

    public int sum(String prefix) {
        Node targetNode = findNode(prefix);
        return targetNode == null ? 0 : targetNode.getSum();
    }

    class Node {
        private Node[] dict;
        private boolean isWord;
        private int sum;
        private int val;

        public Node() {
            this.dict = new Node[26];
        }

        public Node getChar(int index) {
            if (index < 0 || index >= 26) {
                throw new IllegalArgumentException();
            }
            return this.dict[index];
        }

        public int getVal() {
            return val;
        }

        public void setVal(int val) {
            this.val = val;
        }

        public void setWord(boolean word) {
            isWord = word;
        }

        public boolean isWord() {
            return isWord;
        }

        public Node[] getDict() {
            return dict;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }
    }

}
