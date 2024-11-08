package snappy;

import org.junit.Test;
import org.xerial.snappy.Snappy;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/7 17:22
 * @Description
 * @Version 1.0
 **/
public class HelloWorld {
    @Test
    public void testHelloWorld() throws Exception {
        String input = "Hello snappy-java! Snappy-java is a JNI-based wrapper of "
                + "Snappy, a fast compresser/decompresser.";
        byte[] compressed = Snappy.compress(input.getBytes("UTF-8"));
        byte[] uncompressed = Snappy.uncompress(compressed);

        String result = new String(uncompressed, "UTF-8");
        System.out.println(result);
        System.out.printf("compressed length: %d, uncompressed length: %d ", compressed.length, uncompressed.length);
    }

    @Test
    public void test2() throws Exception {
        long[] input = new long[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        byte[] compressed = Snappy.compress(input);
        byte[] uncompressed = Snappy.uncompress(compressed);

        String result = new String(uncompressed, "UTF-8");
        System.out.println(result);
        System.out.printf("compressed length: %d, uncompressed length: %d ", compressed.length, uncompressed.length);
    }
}
