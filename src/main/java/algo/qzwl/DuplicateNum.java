package algo.qzwl;

import org.junit.Test;

public class DuplicateNum {
  static class MaxNumCount {
    public int maxNum;
    public int maxCount;
    public MaxNumCount(int maxNum, int maxCount) {
      this.maxNum = maxNum;
      this.maxCount = maxCount;
    }

    @Override
    public String toString() {
      return this.maxNum + ", " + this.maxCount;
    }
  }
  public static MaxNumCount findMaxNumAndCount(int[] nums) {
    // if (nums == null) todo
    int maxNum = nums[0];
    int maxCount = 1;
    for (int i = 1; i < nums.length; i++) {
      if (nums[i] == maxNum) {
        maxCount++;
      } else if (nums[i] > maxNum) {
        maxNum = nums[i];
        maxCount = 1;
      } else { // 小于，表明已经不可能是最大数
        break;
      }
    }
    return new MaxNumCount(maxNum, maxCount);
  }

  @Test
  public void testFind() {
    int[] nums = new int[] {1,1,2,2,2,2,3,4,5,6,7,7,7,7,99,99,99,4,3,2,1};
    System.out.println(findMaxNumAndCount(nums));
  }
}