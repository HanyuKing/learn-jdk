import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * @author wanghanyu
 * @create 2017-09-14 15:05
 */
public class JSONTest {
    public static void main(String[] args) {
        AAA a = new AAA();
        a.setAge(12);
        a.setName("hanyu");

        System.out.println(JSON.toJSONString(a));
    }

    @Test
    public void testJson() {
        JSONObject obj = new JSONObject();
        obj.put("code", "200");
        Person p = new Person();
        AAA a = new AAA();
        a.setAge(12);
        a.setName("hanyu");
        obj.put("aaa", a);
        System.out.println(obj.toString());
    }
}

class AAA {
    private Integer age;

    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}