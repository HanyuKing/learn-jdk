package jacoco;

/**
 * @Author Hanyu.Wang
 * @Date 2024/12/12 15:36
 * @Description
 * @Version 1.0
 **/
public class Palindrome {
    public boolean isPalindrome(String inputString) {
        if (inputString.length() == 0) {
            return true;
        } else {
            char firstChar = inputString.charAt(0);
            char lastChar = inputString.charAt(inputString.length() - 1);
            String mid = inputString.substring(1, inputString.length() - 1);
            return (firstChar == lastChar) && isPalindrome(mid);
        }
    }
}
