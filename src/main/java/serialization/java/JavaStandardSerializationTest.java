package serialization.java;

import org.junit.Test;

import java.io.*;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/5 14:43
 * @Description
 * @Version 1.0
 **/
public class JavaStandardSerializationTest {

    @Test
    public void serialization() {
        JavaBean javaBean = new JavaBean();
        javaBean.setAge(99);

        File file = new File("JavaBean.ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(javaBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反序列化和序列化serialVersionUID不同时，反序列化会失败
     */
    @Test
    public void deSerialization() {
        File file = new File("JavaBean.ser");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            JavaBean javaBean = (JavaBean) ois.readObject();
            System.out.println(javaBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
