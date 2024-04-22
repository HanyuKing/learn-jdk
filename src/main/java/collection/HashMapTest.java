package collection;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/28 15:46
 * @Description
 *
 * 二义性问题
 * 所谓的二义性问题是指含义不清或不明确。
 * 我们假设 ConcurrentHashMap 允许插入 null，那么此时就会有二义性问题，它的二义性含义有两个：
 *
 * 值没有在集合中，所以返回 null。
 * 值就是 null，所以返回的就是它原本的 null 值。
 * 可以看出这就是 ConcurrentHashMap 的二义性问题，那为什么 HashMap 就不怕二义性问题呢？
 *
 * 可证伪的 HashMap
 * 上面说到 HashMap 是不怕二义性问题的，为什么呢？
 * 这是因为 HashMap 的设计是给单线程使用的，所以如果查询到了 null 值，我们可以通过 hashMap.containsKey(key) 的方法来区分这个 null 值到底是存入的 null？还是压根不存在的 null？这样二义性问题就得到了解决，所以 HashMap 不怕二义性问题。
 *
 * 不可证伪的 ConcurrentHashMap
 * 而 ConcurrentHashMap 就不一样了，因为 ConcurrentHashMap 使用的场景是多线程，所以它的情况更加复杂。
 * 我们假设 ConcurrentHashMap 可以存入 null 值，有这样一个场景，现在有一个线程 A 调用了 concurrentHashMap.containsKey(key)，我们期望返回的结果是 false，但在我们调用 concurrentHashMap.containsKey(key) 之后，未返回结果之前，线程 B 又调用了 concurrentHashMap.put(key,null) 存入了 null 值，那么线程 A 最终返回的结果就是 true 了，这个结果和我们之前预想的 false 完全不一样。
 * 也就是说，多线程的状况非常复杂，我们没办法判断某一个时刻返回的 null 值，到底是值为 null，还是压根就不存在，也就是二义性问题不可被证伪，所以 ConcurrentHashMap 才会在源码中这样设计，直接杜绝 key 或 value 为 null 的歧义问题。
 *
 * ConcurrentHashMap 设计者的回答
 * 对于 ConcurrentHashMap 不允许插入 null 值的问题，有人问过 ConcurrentHashMap 的作者 Doug Lea，以下是他回复的邮件内容：
 *
 * The main reason that nulls aren't allowed in ConcurrentMaps (ConcurrentHashMaps, ConcurrentSkipListMaps) is that ambiguities that may be just barely tolerable in non-concurrent maps can't be accommodated. The main one is that if map.get(key) returns null, you can't detect whether the key explicitly maps to null vs the key isn't mapped.
 * In a non-concurrent map, you can check this via map.contains(key),but in a concurrent one, the map might have changed between calls.
 * ​
 * Further digressing: I personally think that allowing
 * nulls in Maps (also Sets) is an open invitation for programs
 * to contain errors that remain undetected until
 * they break at just the wrong time. (Whether to allow nulls even
 * in non-concurrent Maps/Sets is one of the few design issues surrounding
 * Collections that Josh Bloch and I have long disagreed about.)
 * ​
 *
 * It is very difficult to check for null keys and values
 * in my entire application .
 *
 * Would it be easier to declare somewhere
 *     static final Object NULL = new Object();
 * and replace all use of nulls in uses of maps with NULL?
 * ​
 * -Doug
 *
 * 以上信件的主要意思是，Doug Lea 认为这样设计最主要的原因是：不容忍在并发场景下出现歧义！
 *
 * 总结
 * 在 Java 语言中，HashMap 这种单线程下使用的集合是可以设置 null 值的，而并发集合如 ConcurrentHashMap 或 Hashtable 是不允许给 key 或 value 设置 null 值的，这是 JDK 源码层面直接实现的，这样设计的目的主要是为了防止并发场景下的歧义问题。
 *
 * 参考文档
 * www.cnblogs.com/fanguangdexiaoyuer/p/12335921.html
 *
 * @Version 1.0
 **/
public class HashMapTest {

    @Test
    public void testConcurrentHashMapPutNull() {
        ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();

        map.put(null, "null");

        System.out.println(map.get(null));
        System.out.println(map.get("null"));
    }

    @Test
    public void testConcurrentHashMapPutNull2() {
        ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<>();

        map.put("null", null);

        System.out.println(map.get("null"));

        // map.containsKey()
    }
    public static void main(String[] args) {

        Map<Object, Object> map = new HashMap<>();
        A a = new A(100);
        B b = new B(101);

        map.put(a, 100);

        map.put(null, "null");
        map.put("null", null);
        map.put(null, null);

        System.out.println(map.get(b));
        System.out.println(map.get(null));
        System.out.println(map.get("null"));
    }

    private static class A {
        private int value;

        public A(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            return true;
        }

        @Override
        public String toString() {
            return "value = " + this.value;
        }
    }

    private static class B {
        private int value;

        public B(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            return true;
        }

        @Override
        public String toString() {
            return "value = " + this.value;
        }
    }
}
