package algo.interview.qzwl;

import java.util.*;

class Category {
    int id;
    int parent_id;
    String name;
    List<Category> children;

    public Category(int id, int parent_id, String name) {
        this.id = id;
        this.parent_id = parent_id;
        this.name = name;
        this.children = new ArrayList<>();
    }

    // 添加子节点
    public void addChild(Category child) {
        children.add(child);
    }

    // 打印分类树
    public void printTree(String prefix) {
        System.out.println(prefix + this.name);
        for (Category category : this.children) {
            category.printTree(prefix + "  ");
        }
    }
}

public class CategoryTreeBuilder {

    // 根据category数组构建分类树
    public static Category buildCategoryTree(List<Map<String, Object>> categories) {
        Map<Integer, Category> categoryMap = new HashMap<>();
        for (Map<String, Object> category : categories) {
            int id = (int) category.get("id");
            int parentId = (int) category.get("parent_id");
            String name = (String) category.get("name");
            categoryMap.put(id, new Category(id, parentId, name));
        }
        Category root = new Category(0, 0, "Root");
        for (Map.Entry<Integer, Category> entry : categoryMap.entrySet()) {
            if (entry.getValue().parent_id == 0) {
                root.addChild(entry.getValue());
            } else {
                categoryMap.get(entry.getValue().parent_id).addChild(entry.getValue());
            }
        }
        return root;  // 返回树的根节点
    }

    public static void main(String[] args) {
        // 示例数据：模拟从数据库读取的category数组
        List<Map<String, Object>> categories = new ArrayList<>();
        
        categories.add(createCategory(1, 0, "Electronics"));
        categories.add(createCategory(2, 1, "Mobile Phones"));
        categories.add(createCategory(3, 1, "Laptops"));
        categories.add(createCategory(4, 0, "Home Appliances"));
        categories.add(createCategory(5, 4, "Refrigerators"));
        categories.add(createCategory(6, 4, "Washing Machines"));

        // 构建分类树
        Category root = buildCategoryTree(categories);

        // 打印树形结构
        root.printTree(""); 
    }

    // 用于创建category模拟数据的辅助方法
    private static Map<String, Object> createCategory(int id, int parentId, String name) {
        Map<String, Object> category = new HashMap<>();
        category.put("id", id);
        category.put("parent_id", parentId);
        category.put("name", name);
        return category;
    }
}
