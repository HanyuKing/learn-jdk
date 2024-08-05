package serialization.java;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/5 14:44
 * @Description
 * @Version 1.0
 **/
@Data
public class JavaBean implements Serializable {
    private static final long serialVersionUID = 1570692799853553965L;

    private int age;
}
