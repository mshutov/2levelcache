package ru.vzmx.app.cache;

import ru.vzmx.app.cache.strategy.CacheStrategy;
import ru.vzmx.app.cache.strategy.Strategy;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;

@NotThreadSafe
public class MemoryCache<K, V> implements Cache<K, V> {
    private final Strategy<K> strategy;
    private final Map<K, V> holder = new HashMap<>();
    private final long maxSize;

    @SuppressWarnings("WeakerAccess")
    public MemoryCache(CacheStrategy strategy, long size) {
        this.strategy = strategy.create();
        maxSize = size;
    }

    @Override
    public void put(K key, V value) {
        if (holder.size() == maxSize && !holder.containsKey(key)) {
            remove(strategy.selectKeyToRemove());
        }
        if (holder.put(key, value) != null) {
            // if key was present we need to remove it from strategy
            strategy.removed(key);
        }
        strategy.added(key);
    }

    @Override
    public Optional<V> get(K key) {
        Optional<V> value = Optional.ofNullable(holder.get(key));
        value.ifPresent(v -> strategy.accessed(key));
        return value;
    }

    @Override
    public void remove(K key) {
        Optional.ofNullable(holder.remove(key)).ifPresent(v -> strategy.removed(key));
    }

    @Override
    public void clear() {
        holder.clear();
        strategy.cleared();
    }
}
