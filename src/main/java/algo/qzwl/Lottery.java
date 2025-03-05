package algo.qzwl;

import java.util.*;

public class Lottery {
    // 奖品信息（数量，概率）
    static class Prize {
        String name;
        int stock;
        double probability;

        Prize(String name, int stock, double probability) {
            this.name = name;
            this.stock = stock;
            this.probability = probability;
        }
    }

    // 奖品列表
    private static List<Prize> prizeList = new ArrayList<>();
    
    // 初始化奖品信息
    static {
        prizeList.add(new Prize("Prize1", 5, 0.001));
        prizeList.add(new Prize("Prize2", 20, 0.051));
        prizeList.add(new Prize("Prize3", 30, 0.084));
        prizeList.add(new Prize("Prize4", 40, 0.154));
        prizeList.add(new Prize("Prize5", 100, 0.215));
        prizeList.add(new Prize("Prize6", 200, 0.495));
    }

    // 进行一次抽奖
    public static String draw() {
        // 计算总库存
        int totalStock = 0;
        for (Prize prize : prizeList) {
            totalStock += prize.stock;
        }

        // 计算每个奖品的权重
        List<Double> weights = new ArrayList<>();
        for (Prize prize : prizeList) {
            if (prize.stock > 0) {
                double weight = (double) prize.stock / totalStock;
                weights.add(weight);
            } else {
                weights.add(0.0); // 如果库存为0，概率为0
            }
        }

        // 随机选择中奖奖品
        double random = Math.random();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < prizeList.size(); i++) {
            cumulativeProbability += weights.get(i);
            if (random <= cumulativeProbability) {
                Prize selectedPrize = prizeList.get(i);
                if (selectedPrize.stock > 0) {
                    selectedPrize.stock--; // 中奖品后减少库存
                    return selectedPrize.name; // 返回中奖奖品名称
                }
            }
        }
        return "No prize"; // 如果没有中奖（理论上不应该发生）
    }

    // 模拟多次抽奖，输出每个奖品中奖的次数
    public static void simulateLottery(int draws) {
        Map<String, Integer> result = new HashMap<>();
        for (int i = 0; i < draws; i++) {
            String prize = draw();
            result.put(prize, result.getOrDefault(prize, 0) + 1);
        }
        System.out.println("Lottery results after " + draws + " draws:");
        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " times");
        }
    }

    public static void main(String[] args) {
        int totalDraws = 10000;  // 进行10000次抽奖
        simulateLottery(totalDraws);
    }
}
