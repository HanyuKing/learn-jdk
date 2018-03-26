package beanutils;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

/**
 * @author wanghanyu
 * @create 2018-01-12 12:45
 */
public class BeanUtilsTimeTest {
    public static void main(String[] args) {

    }

    @Test
    public void test2() {
        Person dest = new Person();
        dest.setAge(120);
        beanutils.dest.Person orin = new beanutils.dest.Person();
        orin.setName("HanyuKing");

        long start = System.currentTimeMillis();
        try {
            for(int i = 0; i < 100; i++) {
                BeanUtils.copyProperties(dest, orin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        System.out.println(end - start);
        System.out.println(dest.getName());
    }

    @Test
    public void test() {
        ApplyWare ware = new ApplyWare();
        ware.setImgRui("http://image.hanyuking.com");
        beanutils.dest.ApplyWare applyWare = new beanutils.dest.ApplyWare();

        long start = System.currentTimeMillis();
        try {
            BeanUtils.copyProperties(applyWare, ware);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        System.out.println(end - start);
        System.out.println(applyWare.getImgRui());
    }
}
