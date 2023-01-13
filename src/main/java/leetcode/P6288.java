package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/7 22:42
 * @Description
 * @Version 1.0
 **/

/**
 * 输入：
 * ["DataStream", "consec", "consec", "consec", "consec"]
 * [[4, 3], [4], [4], [4], [3]]
 * 输出：
 * [null, false, false, true, false]
 *
 * 解释：
 * DataStream dataStream = new DataStream(4, 3); // value = 4, k = 3
 * dataStream.consec(4); // 数据流中只有 1 个整数，所以返回 False 。
 * dataStream.consec(4); // 数据流中只有 2 个整数
 *                       // 由于 2 小于 k ，返回 False 。
 * dataStream.consec(4); // 数据流最后 3 个整数都等于 value， 所以返回 True 。
 * dataStream.consec(3); // 最后 k 个整数分别是 [4,4,3] 。
 *                       // 由于 3 不等于 value ，返回 False 。
 */
public class P6288 {
    public static void main(String[] args) {
        DataStream dataStream = new DataStream(4, 1); // value = 4, k = 3
        System.out.println(dataStream.consec(3)); // 数据流中只有 1 个整数，所以返回 False 。
        System.out.println(dataStream.consec(5)); // 数据流中只有 2 个整数
        // 由于 2 小于 k ，返回 False 。
        System.out.println(dataStream.consec(4)); // 数据流最后 3 个整数都等于 value， 所以返回 True 。
        System.out.println(dataStream.consec(4)); // 最后 k 个整数分别是 [4,4,3] 。
    }

    private static class DataStream {
        private int value;
        private int k;
        private int commonCount;
        private int preNum;

        public DataStream(int value, int k) {
            this.value = value;
            this.k = k;
            this.commonCount = 0;
            this.preNum = 0;
        }

        public boolean consec(int num) {
            if (num == value) {
                if (num == preNum) {
                    return ++this.commonCount >= k;
                } else {
                    this.commonCount = 1;
                    this.preNum = value;
                    return this.commonCount >= k;
                }
            } else {
                this.commonCount = 0;
                this.preNum = num;
            }
            return false;
        }
    }

/**
 * Your DataStream object will be instantiated and called as such:
 * DataStream obj = new DataStream(value, k);
 * boolean param_1 = obj.consec(num);
 */
}
