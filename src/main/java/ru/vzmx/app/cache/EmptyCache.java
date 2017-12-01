package ru.vzmx.app.cache;

import java.util.Optional;

public class EmptyCache<K, V> implements Cache<K, V> {

    @Override
    public void put(K key, V value) {

    }

    @Override
    public Optional<V> get(K key) {
        return Optional.empty();
    }

    @Override
    public void remove(K key) {

    }

    @Override
    public void clear() {

    }
}
