package ru.vzmx.app.cache;

import ru.vzmx.app.cache.strategy.CacheStrategy;

import java.util.HashMap;
import java.util.Map;

public class MemoryCache<K, V> extends CommonCache<K, V> implements Cache<K, V> {
    private final Map<K, V> holder = new HashMap<>();

    public MemoryCache(CacheStrategy strategy, int size, Cache<K, V> nextCache) {
        super(strategy.create(), size, nextCache);
    }

    public MemoryCache(CacheStrategy strategy, int size) {
        this(strategy, size, null);
    }

    @Override
    protected boolean containsKey(K key) {
        return holder.containsKey(key);
    }

    @Override
    protected boolean removeInternal(K key) {
        return holder.remove(key) != null;
    }

    @Override
    protected void clearInternal() {
        holder.clear();
    }

    @Override
    protected void putInternal(K key, V value) {
        holder.put(key, value);
    }

    @Override
    protected int sizeInternal() {
        return holder.size();
    }

    @Override
    protected V getInternal(K key) {
        return holder.get(key);
    }
}
