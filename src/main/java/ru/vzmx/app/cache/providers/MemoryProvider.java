package ru.vzmx.app.cache.providers;

import ru.vzmx.app.cache.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryProvider<K, V> implements Provider<K, V> {
    private final Map<K, V> holder = new HashMap<>();

    @Override
    public boolean containsKey(K key) {
        return holder.containsKey(key);
    }

    @Override
    public boolean remove(K key) {
        return holder.remove(key) != null;
    }

    @Override
    public void clear() {
        holder.clear();
    }

    @Override
    public void put(K key, V value) {
        holder.put(key, value);
    }

    @Override
    public int size() {
        return holder.size();
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.ofNullable(holder.get(key));
    }
}
