package BasicType;

import com.google.gson.Gson;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2022/10/17 11:37
 * @Description
 * @Version 1.0
 **/
public class MapTest {

    @Test
    public void testTypeConvert() {

        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson("{\"type\":1}", HashMap.class);

        System.out.println("number->" + NumberUtils.createNumber(data.get("type").toString()).intValue());

        Integer type = (Integer) data.get("type");

        System.out.println(type);
    }
}
