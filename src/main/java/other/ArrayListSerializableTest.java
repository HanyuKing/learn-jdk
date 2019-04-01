package other;

import com.alibaba.fastjson.JSON;

public class ArrayListSerializableTest {
    public static void main(String[] args) {
        PageList<Person4> personPageList = new PageList<Person4>();
        personPageList.setTotalItem(3);
        personPageList.add(new Person4(1, "a"));
        personPageList.add(new Person4(2, "b"));
        personPageList.add(new Person4(3, "c"));
        System.out.println(JSON.toJSONString(personPageList));
    }
}



class PageList<T> extends MyArrayList {// extends ArrayList<T> {
    private int totalItem;

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    @Override
    public void add(Object t) {
        super.add(t);
    }
}

class MyArrayList {
    private transient Object[] datas = new Object[10];
    private int index = 0;
    public void add(Object o) {
        datas[index++] = o;
    }

    public Object[] getDatas() {
        return datas;
    }
}

class Person4  {
    private int age;
    private String name;

    public Person4(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}