package leetcode.hot100;

class Trie {
    private boolean hasWorld;
    private Trie[] array = new Trie[26];

    public Trie() {

    }
    
    public void insert(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }
        Trie[] array = this.array;
        Trie last = null;
        for (char ch : word.toCharArray()) {
            int index = ch - 'a';
            if (array[index] == null) {
                array[index] = new Trie();
            }
            last = array[index];
            array = array[index].array;
        }
        last.hasWorld = true;
    }
    
    public boolean search(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        Trie[] array = this.array;
        Trie last = null;
        for (char ch : word.toCharArray()) {
            int index = ch - 'a';
            if (array[index] == null) {
                return false;
            }
            last = array[index];
            array = array[index].array;
        }
        return last.hasWorld;
    }
    
    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return false;
        }
        Trie[] array = this.array;
        for (char ch : prefix.toCharArray()) {
            int index = ch - 'a';
            if (array[index] == null) {
                return false;
            }
            array = array[index].array;
        }
        return true;
    }
}

/**
 * Your Trie object will be instantiated and called as such:
 * Trie obj = new Trie();
 * obj.insert(word);
 * boolean param_2 = obj.search(word);
 * boolean param_3 = obj.startsWith(prefix);
 */