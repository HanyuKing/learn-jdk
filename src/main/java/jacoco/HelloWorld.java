package jacoco;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Author Hanyu.Wang
 * @Date 2024/12/12 15:14
 * @Description
 * @Version 1.0
 **/
public class HelloWorld {
    @Test
    public void whenEmptyString_thenAccept() {
        Palindrome palindrome = new Palindrome();
        assertTrue(palindrome.isPalindrome("abba"));
    }

}
