package serialization.dubbo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.serialize.ObjectInput;
import org.apache.dubbo.common.serialize.Serialization;
import org.apache.dubbo.common.serialize.java.JavaSerialization;
import org.junit.Test;

import java.io.*;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/5 16:00
 * @Description
 * @Version 1.0
 **/
public class DubboSerializationTest {

    @Test
    public void testDubboJavaSerialization() {
        Serialization serialization = new JavaSerialization();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("DubboJavaBean.ser"))) {
            DubboJavaBean dubboJavaBean = new DubboJavaBean();
            dubboJavaBean.setAge(88);
            oos.writeObject(dubboJavaBean);

            serialization.serialize(URL.valueOf("com.why.dubbo.serialization.test"), oos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDubboJavaDeSerialization() {
        Serialization serialization = new JavaSerialization();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("DubboJavaBean.ser"))) {
            ObjectInput objectInput = serialization.deserialize(URL.valueOf("com.why.dubbo.serialization.test"), ois);
            DubboJavaBean dubboJavaBean = objectInput.readObject(DubboJavaBean.class);

            System.out.println(dubboJavaBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
