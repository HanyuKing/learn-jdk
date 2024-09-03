package uml.recycle.management.activity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 为减少对象数量，减轻GC压力和减少停顿时间，这里将id的Object对象缓存
 * 注意：因调用方法频次会很高，这里提供的方法不会包含空等校验，也不会String.trim
 */
public class IdObjectCache {
    private static Map<Long, Long> longIdObjectCache = new ConcurrentHashMap<>();
    private static Map<Integer, Integer> integerIdObjectCache = new ConcurrentHashMap<>();
    private static Map<String, String> stringIdObjectCache = new ConcurrentHashMap<>();

    public static Long getId(Long objId) {
        Long ret = longIdObjectCache.get(objId);
        if (ret == null) {
            longIdObjectCache.put(objId, objId);
            return objId;
        }
        return ret;
    }

    public static Long getLongId(String idStr) {
        Long configId = Long.parseLong(idStr);
        return getId(configId);
    }

    public static Integer getId(Integer objId) {
        Integer ret = integerIdObjectCache.get(objId);
        if (ret == null) {
            integerIdObjectCache.put(objId, objId);
            return objId;
        }
        return ret;
    }

    public static Integer getIntegerId(String idStr) {
        Integer configId = Integer.parseInt(idStr);
        return getId(configId);
    }

    /**
     * 字符串类似的Id缓存需要慎用，只能存放有限长度有限数量的id（千万不要放其他的字符串）
     * @param objId
     * @return
     */
    public static String getId(String objId) {
        String ret = stringIdObjectCache.get(objId);
        if (ret == null) {
            stringIdObjectCache.put(objId, objId);
            return objId;
        }
        return ret;
    }

    public static String getSizeInfo() {
        return "IdObjectCache{longIdObjectCache.size:" + longIdObjectCache.size() +
                ",integerIdObjectCache.size:" + integerIdObjectCache +
                ",stringIdObjectCache.size:" + stringIdObjectCache + "}";
    }
}