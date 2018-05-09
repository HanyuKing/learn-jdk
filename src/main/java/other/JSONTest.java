package other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wanghanyu
 * @create 2017-09-14 15:05
 */
public class JSONTest {
    public static void main(String[] args) {
        String str = "{\"coupon\":\"{\\\"endTime\\\":\\\"2018-04-24 23:59:59\\\",\\\"quota\\\":100,\\\"discount\\\":99,\\\"startTime\\\":\\\"2018-04-21 00:00:00\\\"}\",\"points\":12,\"sendType\":2}";
        Ext ext = JSON.parseObject(str, Ext.class);
    }

    @Test
    public void testJson2() {
        JSONObject jsonObject = JSON.parseObject("");
        Map result = new HashMap();

        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
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

enum BusinessTypeEnum {
    AUCTION,//拍卖
    SELFSHOP,//自营店铺
    RED,//闪购
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