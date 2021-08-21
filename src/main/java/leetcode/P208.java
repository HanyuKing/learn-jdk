package leetcode;

public class P208 {
    public static void main(String[] args) {
        Trie trie = new Trie();

        trie.insert("apple");

        System.out.println(trie.search("apple"));   // return True
        System.out.println(trie.search("app"));     // return False
        System.out.println(trie.startsWith("app")); // return True

        trie.insert("app");

        System.out.println( trie.search("app"));     // return True
    }

}

class Trie {
    private Node[] dict;

    /** Initialize your data structure here. */
    public Trie() {
        this.dict = new Node[26];
    }

    /** Inserts a word into the trie. */
    public void insert(String word) {
        if (word.length() == 0) throw new IllegalArgumentException();

        Node[] dictTemp = this.dict;
        Node targetNode = null;
        for(char c : word.toCharArray()) {
            targetNode = dictTemp[c - 'a'];
            if (targetNode == null) {
                targetNode = new Node();
                dictTemp[c - 'a'] = targetNode;
            }
            dictTemp = targetNode.getDict();
        }
        targetNode.setWord(true);
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        if (word.length() == 0) throw new IllegalArgumentException();

        Node targetNode = getLastNode(word);
        if (targetNode == null) return false;

        return targetNode.isWord();
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        if (prefix.length() == 0) throw new IllegalArgumentException();

        Node targetNode = getLastNode(prefix);
        if (targetNode == null) return false;

        return true;
    }

    private Node getLastNode(String word) {

        Node[] dictTemp = this.dict;
        Node targetNode = null;
        for (char c : word.toCharArray()) {
            if (dictTemp == null) {
                return null;
            }
            targetNode = dictTemp[c - 'a'];
            if (targetNode == null) {
                return null;
            }
            dictTemp = targetNode.getDict();
        }
        return targetNode;
    }

    class Node {
        private Node[] dict;
        private boolean isWord;

        public Node() {
            this.dict = new Node[26];
        }

        public Node getChar(int index) {
            if (index < 0 || index >= 26) {
                throw new IllegalArgumentException();
            }
            return this.dict[index];
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
    }

}
