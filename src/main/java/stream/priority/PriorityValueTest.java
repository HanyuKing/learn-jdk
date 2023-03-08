package stream.priority;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2023/3/7 15:52
 * @Description
 * @Version 1.0
 **/
public class PriorityValueTest {

    @Test
    public void testGetFirstValidValue() {
        Map<String, Object> params = new HashMap<>();

        List<Mod> mods = Lists.newArrayList(new First(), new Second(), new Third(), new Default());

        Mod mod = mods.stream()
                .sorted(Comparator.comparingInt(Mod::getPriority))
                .filter(mod1 -> mod1.isValid(params))
                .findFirst()
                .orElse(new Default());

        System.out.println(mod.getMod());
    }
}
