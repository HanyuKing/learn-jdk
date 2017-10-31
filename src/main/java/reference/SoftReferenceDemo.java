package reference;

import java.lang.ref.SoftReference;
/*
 * 虚拟机参数配置
 * -Xms256m
 * -Xmx1024m
*/
public class SoftReferenceDemo {
    public static void main(String[] args){
         
        /*软引用对象中指向了一个长度为300000000个元素的整形数组*/
        SoftReference<int[]> softReference = 
                new SoftReference<int[]>(new int[300000000]);
         
        /*主动调用一次gc,由于此时JVM的内存够用，此时softReference引用的对象未被回收*/
        System.gc();
        System.out.println(softReference.get());
         
        /*消耗内存,会导致一次自动的gc,此时JVM的内存不够用
         *就回收softReference对象中指向的数组对象*/
        int[] strongReference = new int[100000000];
         
        System.out.println(softReference.get());
    }
}