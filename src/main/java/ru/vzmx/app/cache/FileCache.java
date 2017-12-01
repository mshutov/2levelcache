package ru.vzmx.app.cache;

import ru.vzmx.app.cache.strategy.Strategy;

public class FileCache<K, V> extends CommonCache<K, V> implements Cache<K, V> {
    protected FileCache(Strategy<K> strategy, int maxSize, Cache<K, V> nextCache) {
        super(strategy, maxSize, nextCache);
    }

    @Override
    protected boolean containsKey(K key) {
        return false;
    }

    @Override
    protected boolean removeInternal(K key) {
        return false;
    }

    @Override
    protected void clearInternal() {

    }

    @Override
    protected void putInternal(K key, V value) {

    }

    @Override
    protected int sizeInternal() {
        return 0;
    }

    @Override
    protected V getInternal(K key) {
        return null;
    }
}
