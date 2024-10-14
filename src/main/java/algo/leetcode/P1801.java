package algo.leetcode;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @Author Hanyu.Wang
 * @Date 2023/1/2 17:45
 * @Description
 * @Version 1.0
 **/
public class P1801 {
    public static void main(String[] args) {

        System.out.println(new P1801().getNumberOfBacklogOrders(new int[][]
                {{16,4,0},{10,13,1},{14,26,0},{9,4,1},{29,24,0},{20,14,0},{8,1,1},{5,2,0}}
        ));
        /*
            0 表示这是一批采购订单 buy
            1 表示这是一批销售订单 sell
         */
    }

    private PriorityQueue<int[]> buyPQ = new PriorityQueue<>(new Comparator<int[]>() {
        @Override
        public int compare(int[] o1, int[] o2) {
            return o2[0] - o1[0];
        }
    });

    private PriorityQueue<int[]> sellPQ = new PriorityQueue<>(new Comparator<int[]>() {
        @Override
        public int compare(int[] o1, int[] o2) {
            return o1[0] - o2[0];
        }
    });

    private static final int BUY = 0;
    private static final int SELL = 1;

    public int getNumberOfBacklogOrders(int[][] orders) {
        for (int i = 0; i < orders.length; i++) {
            int[] currOrders = orders[i];
            if (currOrders[2] == BUY) {
                if (sellPQ.isEmpty() || sellPQ.peek()[0] > currOrders[0]) {
                    buyPQ.add(new int[]{currOrders[0], currOrders[1]});
                } else {
                    while (!sellPQ.isEmpty() && currOrders[1] > 0 && sellPQ.peek()[0] <= currOrders[0]) {
                        int[] sellOrders = sellPQ.poll();
                        if (currOrders[1] <= sellOrders[1]) {
                            sellOrders[1] = sellOrders[1] - currOrders[1];
                            sellPQ.add(sellOrders);
                            currOrders[1] = 0;
                            break;
                        } else {
                            currOrders[1] = currOrders[1] - sellOrders[1];
                        }
                    }
                    if (currOrders[1] > 0) {
                        buyPQ.add(new int[]{currOrders[0], currOrders[1]});
                    }
                }
            } else {
                if (buyPQ.isEmpty() || buyPQ.peek()[0] < currOrders[0]) {
                    sellPQ.add(new int[]{currOrders[0], currOrders[1]});
                } else {
                    while (!buyPQ.isEmpty() && currOrders[1] > 0 && buyPQ.peek()[0] >= currOrders[0]) {
                        int[] buyOrders = buyPQ.poll();
                        if (currOrders[1] <= buyOrders[1]) {
                            buyOrders[1] = buyOrders[1] - currOrders[1];
                            buyPQ.add(buyOrders);
                            currOrders[1] = 0;
                            break;
                        } else {
                            currOrders[1] = currOrders[1] - buyOrders[1];
                        }
                    }
                    if (currOrders[1] > 0) {
                        sellPQ.add(new int[]{currOrders[0], currOrders[1]});
                    }
                }
            }
        }

        long cnt = 0;
        while (!buyPQ.isEmpty()) {
            cnt += buyPQ.poll()[1];
        }
        while (!sellPQ.isEmpty()) {
            cnt += sellPQ.poll()[1];
        }

        return (int) (cnt % 1000000007);
    }
}
