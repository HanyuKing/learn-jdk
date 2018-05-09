package jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hanyu King
 * @since 2018-04-27 14:42
 */
public class GCCollectionTest {
    public static void main(String[] args) {
        long createStartTime = System.currentTimeMillis();
        List<Customer> customers = new ArrayList<Customer>();
        for(int i = 0; i < 5000000; i++) {
            customers.add(new Customer("pin" + 0, i));
        }
        long createEndTime = System.currentTimeMillis();
        System.out.println("构造数据时间: " + (createEndTime - createStartTime) + " ms");

        StringBuilder pinStr = new StringBuilder();
        long length = customers.size();

        long loopStartTime = System.currentTimeMillis();
        for(int i = 0; i < length; i++) {
            Customer customer = customers.get(i);
            pinStr.append(customer.getPin()).append(",");
        }
        long loopEndTime = System.currentTimeMillis();

        System.out.println("循环时间: " + (loopEndTime - loopStartTime) + " ms");

        System.out.println(pinStr.length());
    }

    private static class Customer {
        private String pin;

        private Integer status;

        public Customer(String pin, Integer status) {
            this.pin = pin;
            this.status = status;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }
}
