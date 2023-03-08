package stream.priority;

import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2023/3/7 15:54
 * @Description
 * @Version 1.0
 **/
public class First implements Mod {
    @Override
    public int getPriority() {
        return PriorityConstants.FIRST;
    }

    @Override
    public boolean isValid(Map<String, Object> params) {
        return true;
    }

    @Override
    public int getMod() {
        return 1;
    }
}
