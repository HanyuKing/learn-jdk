package array;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/11 16:14
 * @Description
 * @Version 1.0
 **/
public class ArrayTest {
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
