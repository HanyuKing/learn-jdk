package spi;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/18 01:14
 * @Description
 * @Version 1.0
 **/
public class SpiTestA implements SpiTestI {

    public SpiTestA() {

    }
    @Override
    public void print() {
        System.out.println("A...");
    }
}
