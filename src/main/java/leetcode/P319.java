package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2024/7/11 17:08
 * @Description
 * @Version 1.0
 **/
public class P319 {
    public static void main(String[] args) {
        /*
         1 1 1
         1 0 1
         1 0 0

         1 1 1 1
         1 0 1 0
         1 0 0 0
         1 0 0 1

         1 1 1 1 1
         1 0 1 0 1
         1 0 0 0 1
         1 0 0 1 1
         1 0 0 1 0

         1 1 1 1 1 1
         1 0 1 0 1 0
         1 0 0 0 1 1
         1 0 0 1 1 1
         1 0 0 1 0 1
         1 0 0 1 0 0

         1 1 1 1 1 1 1
         1 0 1 0 1 0 1
         1 0 0 0 1 1 1
         1 0 0 1 1 1 1
         1 0 0 1 0 1 1
         1 0 0 1 0 0 1
         1 0 0 1 0 0 0
         */
        P319 p319 = new P319();
        System.out.println(p319.bulbSwitch(3));
        System.out.println(p319.bulbSwitch(4));
        System.out.println(p319.bulbSwitch(5));
        System.out.println(p319.bulbSwitch(6));
        System.out.println(p319.bulbSwitch(7));
        System.out.println(p319.bulbSwitch(8));
        System.out.println(p319.bulbSwitch(9));
        System.out.println(p319.bulbSwitch(16));
    }
    public int bulbSwitch(int n) {
        return (int) Math.sqrt(n);
    }
}
