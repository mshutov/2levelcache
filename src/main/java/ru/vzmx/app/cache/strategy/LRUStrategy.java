package ru.vzmx.app.cache.strategy;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LRUStrategy<K> implements Strategy<K> {
    private Map<K, Date> holder = new HashMap<>();

    @Override
    public void added(K key) {
        holder.put(key, new Date());
    }

    @Override
    public void accessed(K key) {
        holder.replace(key, new Date());
    }

    @Override
    public void removed(K key) {
        holder.remove(key);
    }

    @Override
    public void cleared() {
        holder.clear();
    }

    @Override
    public K selectKeyToRemove() {
        return holder.entrySet().stream().reduce(
                (pair1, pair2) -> pair1.getValue().before(pair2.getValue()) ? pair1 : pair2
        ).orElseThrow(() -> new IllegalStateException("No keys exists")).getKey();
    }
}
