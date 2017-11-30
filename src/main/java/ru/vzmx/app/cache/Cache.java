package ru.vzmx.app.cache;

import java.util.Optional;

public interface Cache<K, V> {
    void put(K key, V value);

    Optional<V> get(K key);

    void remove(K key);

    void clear();
}
