package stream.priority;

import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2023/3/7 15:55
 * @Description
 * @Version 1.0
 **/
public class Default implements Mod {
    @Override
    public int getPriority() {
        return PriorityConstants.DEFAULT;
    }

    @Override
    public boolean isValid(Map<String, Object> params) {
        return false;
    }

    @Override
    public int getMod() {
        return 0;
    }
}
