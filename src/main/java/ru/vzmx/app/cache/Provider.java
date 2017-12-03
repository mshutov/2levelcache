package ru.vzmx.app.cache;

import java.util.Optional;

public interface Provider<K, V> {
    void put(K key, V value);

    Optional<V> get(K key);

    boolean remove(K key);

    void clear();

    boolean containsKey(K key);

    int size();
}
