package ru.vzmx.app.cache.strategy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class CommonRUStrategy<K> implements Strategy<K> {
    private Map<K, LocalDateTime> holder = new HashMap<>();

    @Override
    public void added(K key) {
        holder.put(key, LocalDateTime.now());
    }

    @Override
    public void accessed(K key) {
        holder.replace(key, LocalDateTime.now());
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
                (pair1, pair2) -> isFirstToEvict(pair1.getValue(), pair2.getValue()) ? pair1 : pair2
        ).orElseThrow(() -> new IllegalStateException("No keys exists")).getKey();
    }

    protected abstract boolean isFirstToEvict(LocalDateTime first, LocalDateTime second);
}
