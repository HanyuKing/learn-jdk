package stream.priority;

import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2023/3/7 15:55
 * @Description
 * @Version 1.0
 **/
public interface Mod {
    int getPriority();
    boolean isValid(Map<String, Object> params);
    int getMod();
}
