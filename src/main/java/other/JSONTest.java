package other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author wanghanyu
 * @create 2017-09-14 15:05
 */
public class JSONTest {
    public static void main(String[] args) {
        String str = "{\"coupon\":\"{\\\"endTime\\\":\\\"2018-04-24 23:59:59\\\",\\\"quota\\\":100,\\\"discount\\\":99,\\\"startTime\\\":\\\"2018-04-21 00:00:00\\\"}\",\"points\":12,\"sendType\":2}";
        Ext ext = JSON.parseObject(str, Ext.class);
        System.out.println(new JSONTest().getClass().getSimpleName());
    }

    @Test
    public void testNumberSerializeDefaultValue() throws JsonProcessingException {
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("aInteger", 1);
        dataMap.put("aLong", 2L);
        String jsonStr = JSON.toJSONString(dataMap);
        System.out.println(jsonStr);


        // fastjson
        System.out.println("--- fastjson -----");
        Map<String, Object> fastMap = JSON.parseObject(jsonStr, new com.alibaba.fastjson.TypeReference<Map<String, Object>>() {
        });
        printMap(fastMap);

        System.out.println("--- gson -----");
        Map<String, Object> gsonMap = new GsonBuilder().create()
                .fromJson(jsonStr, (new TypeReference<Map<String, Object>>(){}).getType() );
        printMap(gsonMap);

        System.out.println("--- jackson -----");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jacksonMap = objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {
        });
        printMap((jacksonMap));
    }

    private static void printMap(Map<String, Object> map) {
        map.forEach((key, value) -> {
            System.out.println("key:" + key + ",value=" + value + ",valueClass=" + value.getClass());
        });
    }

    @Test
    public void testParse() {
        String s = "{\"supplierId\":\"ves0002730\",\"pin\":\"prom_shop\",\"rebateInfo\":\"{ \"supplierId \": \"ves0002730 \", \"supportPercent \": \"100 \", \"fileInfoList \":[{ \"fileFolderName \": \"promo-rebate \", \"fileName \": \"1540799137951_d15d6e0c0ed9469d99963d3baea9d748.xlsx \"}]}\",\"ip\":\"127.0.0.1\"}\n";
        JSON.parseObject(s);
    }

    @Test
    public void testJson3() {
        String str = "{\"happened_time\":\"2018-06-09\", \"method\":\"title213\", \"time\":\"12312\", \"time_unit\":\"milliseconds\"}";
        JSONObject object = JSON.parseObject(str);

        System.out.println(object.get("happened_time"));
        System.out.println(object.get("method"));
        System.out.println(object.get("time"));
        System.out.println(object.get("time_unit"));
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

    @Test
    public void testJSONRegx() {
        String regx = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";
        Pattern p = Pattern.compile(regx);
        System.out.println(p.matcher("360424199506232155").matches());
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