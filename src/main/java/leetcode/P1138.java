package leetcode;

/**
 * @Author Hanyu.Wang
 * @Date 2023/2/12 17:26
 * @Description
 * @Version 1.0
 **/
public class P1138 {
    public static void main(String[] args) {
        System.out.println(new Solution1138().alphabetBoardPath("zdz"));
    }

    static class Solution1138 {
        public String alphabetBoardPath(String target) {
            int currRow = 0;
            int currCol = 0;
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < target.length(); i++) {
                char targetChar = target.charAt(i);
                int targetRow = (targetChar - 'a') / 5;
                int targetCol = (targetChar - 'a') % 5;

                while (true) {
                    if (targetRow - currRow > 0) {
                        if (targetChar == 'z' && currCol != 0) {
                            int newTargetRow = Math.min(targetRow, 4);
                            result.append(appendMulti("D", newTargetRow - currRow));
                            currRow = newTargetRow;
                        } else {
                            result.append(appendMulti("D", targetRow - currRow));
                            currRow = targetRow;
                        }
                    }
                    if (targetRow - currRow < 0) {
                        result.append(appendMulti("U",  currRow - targetRow));
                        currRow = targetRow;
                    }
                    if (targetCol - currCol > 0) {
                        result.append(appendMulti("R", targetCol - currCol));
                        currCol = targetCol;
                    }
                    if (targetCol - currCol < 0) {
                        result.append(appendMulti("L", currCol - targetCol));
                        currCol = targetCol;
                    }

                    if (currCol == targetCol && currRow == targetRow) {
                        result.append("!");
                        break;
                    }
                }

            }

            return result.toString();
        }

        private String appendMulti(String s, int times) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < times; i++) {
                sb.append(s);
            }
            return sb.toString();
        }
    }
}


