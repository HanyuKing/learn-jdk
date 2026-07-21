package array;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/11 16:14
 * @Description
 * @Version 1.0
 **/
public class ArrayTest {

    @Test
    public void testArrayUtils() {
        String json1 = "{\"skuId\":\"D2026061700140002\",\"chanel\":11,\"bizType\":\"community_task_reward_want\",\"productId\":\"D202606170014\",\"activityId\":264,\"discountId\":\"prw_cfc966bf20ea947d2ec5241b08c95822\",\"discountType\":3,\"discountValue\":0.01,\"discountOrigin\":1}";
        String json2 = "[{\"skuId\": \"D2026061700140002\", \"chanel\": 11, \"bizType\": \"community_task_reward_want\", \"productId\": \"D202606170014\", \"activityId\": 264, \"discountId\": \"prw_cfc966bf20ea947d2ec5241b08c95822\", \"discountType\": 3, \"discountValue\": 0.01, \"discountOrigin\": 1},{\"skuId\": \"D2026061700140003\", \"chanel\": 11, \"bizType\": \"community_task_reward_want\", \"productId\": \"D202606170014\", \"activityId\": 264, \"discountId\": \"prw_cfc966bf20ea947d2ec5241b08c95822\", \"discountType\": 3, \"discountValue\": 0.01, \"discountOrigin\": 1}]";

        System.out.println(parseDiscountData(json1));
        System.out.println(parseDiscountData(json2));
    }

    @Data
    public static class DiscountDTO {
        private String skuId;
    }

    public static List<DiscountDTO> parseDiscountData(String jsonStr) {
        if (jsonStr == null || jsonStr.isEmpty()) {
            return Collections.emptyList();
        }
        Object obj = JSON.parse(jsonStr);
        if (obj instanceof JSONArray) {
            return JSON.parseArray(jsonStr, DiscountDTO.class);
        } else {
            DiscountDTO dto = JSON.parseObject(jsonStr, DiscountDTO.class);
            return Collections.singletonList(dto);
        }
    }
    /**
     * Arrays.copyOf 浅拷贝
     */
    @Test
    public void testArraysCopyOf() {
        int[] basicArray = new int[] {1, 2, 3};
        int[] copyedBasicArray = Arrays.copyOf(basicArray, basicArray.length);
        copyedBasicArray[0] = 11;
        System.out.println("basic before: " + JSON.toJSONString(copyedBasicArray));
        System.out.println("basic after : " + JSON.toJSONString(copyedBasicArray));

        ArrayCopyInfo[] arrayCopyInfos = new ArrayCopyInfo[]{
                new ArrayCopyInfo(9, "Alice"),
                new ArrayCopyInfo(10, "Bob")
        };

        ArrayCopyInfo[] copyedArrayInfos = Arrays.copyOf(arrayCopyInfos, arrayCopyInfos.length);
        copyedArrayInfos[0].setAge(99);

        System.out.println("object array before: " + JSON.toJSONString(arrayCopyInfos));
        System.out.println("object array after : " + JSON.toJSONString(copyedArrayInfos));
    }

    /**
     * Apache Commons Lang 的SerializationUtils.clone方法可以通过序列化实现深拷贝
     */
    @Test
    public void testDeepCopyArrayUseSerializationUtils() {
        ArrayCopyInfo[] arrayCopyInfos = new ArrayCopyInfo[]{
                new ArrayCopyInfo(9, "Alice"),
                new ArrayCopyInfo(10, "Bob")
        };

        ArrayCopyInfo[] copyedArrayInfos = SerializationUtils.clone(arrayCopyInfos);
        copyedArrayInfos[0].setAge(99);

        System.out.println("object array before: " + JSON.toJSONString(arrayCopyInfos));
        System.out.println("object array after : " + JSON.toJSONString(copyedArrayInfos));
    }

    @Test
    public void testDeepCopyArrayUseClone() throws CloneNotSupportedException {
        ArrayCopyInfo[] arrayCopyInfos = new ArrayCopyInfo[]{
                new ArrayCopyInfo(9, "Alice"),
                new ArrayCopyInfo(10, "Bob")
        };

        ArrayCopyInfo[] copyedArrayInfos = cloneArray(arrayCopyInfos);
        copyedArrayInfos[0].setAge(99);

        System.out.println("object array before: " + JSON.toJSONString(arrayCopyInfos));
        System.out.println("object array after : " + JSON.toJSONString(copyedArrayInfos));
    }

    private ArrayCopyInfo[] cloneArray(ArrayCopyInfo[] srcArray) throws CloneNotSupportedException {
        ArrayCopyInfo[] destArray = new ArrayCopyInfo[srcArray.length];
        int i = 0;
        for (ArrayCopyInfo src : srcArray) {
            destArray[i++] = src.clone();
        }
        return destArray;
    }

    @Test
    public void testDeepCopyObjectUseClone() throws CloneNotSupportedException {
        ArrayCopyInfo arrayCopyInfo = new ArrayCopyInfo(9, "Alice");

        ArrayCopyInfo copyedArrayInfo = arrayCopyInfo.clone();
        copyedArrayInfo.setAge(99);

        System.out.println("object before: " + JSON.toJSONString(arrayCopyInfo));
        System.out.println("object after : " + JSON.toJSONString(copyedArrayInfo));
    }
}
