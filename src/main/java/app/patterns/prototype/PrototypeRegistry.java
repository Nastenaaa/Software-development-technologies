package app.patterns.prototype;

import java.util.HashMap;
import java.util.Map;

public class PrototypeRegistry {
    private final Map<String, Object> map = new HashMap<>();

    public <T> void put(String key, Prototype<T> proto) { map.put(key, proto); }

    @SuppressWarnings("unchecked")
    public <T> T create(String key) {
        Object p = map.get(key);
        if (p == null) throw new IllegalArgumentException("No prototype: " + key);
        return ((Prototype<T>) p).copy();
    }
}
