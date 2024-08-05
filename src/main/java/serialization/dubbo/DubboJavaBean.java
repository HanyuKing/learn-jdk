package serialization.dubbo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/5 15:59
 * @Description
 * @Version 1.0
 **/
@Data
public class DubboJavaBean implements Serializable {

    private static final long serialVersionUID = 8625446710030145064L;

    private int age;
}
