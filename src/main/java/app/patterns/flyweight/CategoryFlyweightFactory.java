package app.patterns.flyweight;

import app.model.Category;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CategoryFlyweightFactory {
    private static final Map<Integer, Category> CACHE = new ConcurrentHashMap<>();

    private CategoryFlyweightFactory() {}

    public static Category get(int id, String name  ) {
        return CACHE.compute(id, (k, existing) -> {
            if (existing == null) {
                Category c = new Category();
                c.setId(id);
                if (name != null) c.setName(name);
                return c;
            } else {

                if (existing.getName() == null && name != null) {
                    existing.setName(name);
                }
                return existing;
            }
        });
    }
    public static int size() { return CACHE.size(); }
    public static void clear() { CACHE.clear(); }
}

