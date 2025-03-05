package algo.interview.qzwl;
import java.util.List;
import java.util.Random;

public class Lottery {
    class Award {
        private String name;
        private int count;
        private int p;
        public Award(String name, int count, int p) {
            this.name = name;
            this.count = count;
            this.p = p;
        }
    }

    private List<Award> awardList = null;

    public Lottery(List<Award> awardList) {
        this.awardList = awardList;
    }

    private Random random = new Random();
    public Award draw() {
        int pTotal = 0;
        for (int i = 0; i < this.awardList.size(); i++) {
            if (this.awardList.get(i).count > 0) {
                pTotal += this.awardList.get(i).p;
            }
        }
        int randNum = random.nextInt(pTotal);
        int total = 0;
        for (Award award : this.awardList) {
            if (award.count == 0) {
                continue;
            }
            total += award.count;
            if (total >= randNum) {
                award.count--;
                return award;
            }
        }
        return null;
    }

}
