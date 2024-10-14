package algo.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/26 11:05
 * @Description
 * @Version 1.0
 **/
public class P690 {
    public static void main(String[] args) {
        System.out.println();
    }

    public int getImportance(List<Employee> employees, int id) {
        Map<Integer, Employee> map = new HashMap<>();
        Employee currEmp = null;
        for (Employee e : employees) {
            map.put(e.id, e);
            if (e.id == id) {
                currEmp = e;
            }
        }

        if (currEmp == null) {
            return Integer.MIN_VALUE;
        }

        int result = currEmp.importance;

        List<Integer> subordinates = currEmp.subordinates;
        while (subordinates != null && subordinates.size() > 0) {
            List<Integer> newSubordinates = new ArrayList<>();
            for (int subEmpId : subordinates) {
                Employee subEmp = map.get(subEmpId);
                result += subEmp.importance;
                newSubordinates.addAll(subEmp.subordinates);
            }
            subordinates = newSubordinates;
        }

        return result;
    }

    class Employee {
        public int id;
        public int importance;
        public List<Integer> subordinates;
    };

}
