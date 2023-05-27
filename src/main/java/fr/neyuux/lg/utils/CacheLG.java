package fr.neyuux.lg.utils;

import java.util.HashMap;

public class CacheLG {

    private final HashMap<String, Object> cache = new HashMap<>();

    public void put(String key, Object value) {

        if (this.cache.containsKey(key)) this.cache.replace(key, value);
        else this.cache.put(key, value);
    }

    public boolean has(String key) {
        return this.cache.containsKey(key);
    }

    public Object get(String key) {
        return this.cache.get(key);
    }

    public Object remove(String key) {
        return this.cache.remove(key);
    }

    public void clear() {
        this.cache.clear();
    }
}
