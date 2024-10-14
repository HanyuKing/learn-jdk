package algo.leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/2 01:28
 * @Description
 * @Version 1.0
 **/
public class P6280 {
    public static void main(String[] args) {
        int[] result = new P6280().closestPrimes(19, 31);
        System.out.println("[" + result[0] + ", " + result[1] + "]");
    }
    static boolean[] notPrimes = new boolean[1000001];
    static{
        for(int i=2; i<1000001; i++){
            if(notPrimes[i]){
                continue;
            }
            for(int j=i+i; j<1000001; j+=i){
                notPrimes[j] = true;
            }
        }
    }
    public int[] closestPrimes(int left, int right) {
        int[] result = new int[]{-1, -1};
        int pre = -1;
        int minGap = Integer.MAX_VALUE;

        for (int i = left; i <= right; i++) {
            if (i == 1) {
                continue;
            }
            if (!notPrimes[i]) {
                if (pre != -1 && (i - pre < minGap)) {
                    minGap = i - pre;
                    result[0] = pre;
                    result[1] = i;
                }
                pre = i;
            }
        }
        return result;
    }
}

