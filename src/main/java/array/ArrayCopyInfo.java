package array;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/11 16:16
 * @Description
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArrayCopyInfo implements Cloneable, Serializable {
    private static final long serialVersionUID = -4306168996292277341L;

    private int age;
    private String name;

    @Override
    protected ArrayCopyInfo clone() throws CloneNotSupportedException {
        return (ArrayCopyInfo) super.clone();
    }
}
