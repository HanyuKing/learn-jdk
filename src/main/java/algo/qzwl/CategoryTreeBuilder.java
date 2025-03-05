package algo.qzwl;

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
        System.out.println(prefix + name);
        for (Category child : children) {
            child.printTree(prefix + "  ");
        }
    }
}

public class CategoryTreeBuilder {

    // 根据category数组构建分类树
    public static Category buildCategoryTree(List<Map<String, Object>> categories) {
        Map<Integer, Category> categoryMap = new HashMap<>();
        Category root = new Category(0, 0, "Root");  // 根节点id=0, parent_id=0

        // 将所有Category对象存入Map中
        for (Map<String, Object> data : categories) {
            int id = (int) data.get("id");
            int parentId = (int) data.get("parent_id");
            String name = (String) data.get("name");

            Category category = new Category(id, parentId, name);
            categoryMap.put(id, category);
        }

        // 构建树结构
        for (Category category : categoryMap.values()) {
            if (category.parent_id == 0) {
                // 如果是根节点，直接加入根节点
                root.addChild(category);
            } else {
                // 否则，找到它的父节点，并加入父节点的子节点列表中
                Category parentCategory = categoryMap.get(category.parent_id);
                if (parentCategory != null) {
                    parentCategory.addChild(category);
                }
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
