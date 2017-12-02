package ru.vzmx.app.cache;

import ru.vzmx.app.cache.strategy.CacheStrategy;
import ru.vzmx.app.cache.strategy.Strategy;

import java.util.Optional;

abstract class CommonCache<K, V> implements Cache<K, V> {
    private final Strategy<K> strategy;
    private final int maxSize;
    private final Cache<K, V> nextCache;

    CommonCache(CacheStrategy strategy, int maxSize, Cache<K, V> nextCache) {
        this.strategy = strategy.create();
        this.maxSize = maxSize;
        this.nextCache = nextCache != null ? nextCache : new EmptyCache<>();
    }

    CommonCache(CacheStrategy strategy, int maxSize) {
        this(strategy, maxSize, null);
    }

    protected abstract boolean containsKeyInternal(K key);

    protected abstract boolean removeInternal(K key);

    protected abstract void clearInternal();

    protected abstract void putInternal(K key, V value);

    protected abstract int sizeInternal();

    protected abstract V getInternal(K key);

    @Override
    public void put(K key, V value) {
        boolean keyAlreadyPresent = containsKeyInternal(key);
        if (sizeInternal() == maxSize && !keyAlreadyPresent) {
            K keyToRemove = strategy.selectKeyToRemove();
            V val = getInternal(keyToRemove);
            remove(keyToRemove);
            nextCache.put(keyToRemove, val);
        }
        putInternal(key, value);
        if (keyAlreadyPresent) {
            strategy.removed(key);
        }
        strategy.added(key);
    }

    @Override
    public Optional<V> get(K key) {
        Optional<V> value = Optional.ofNullable(getInternal(key));
        if (value.isPresent()) {
            strategy.accessed(key);
        } else {
            Optional<V> v = nextCache.get(key);
            if (v.isPresent()) {
                nextCache.remove(key);
                put(key, v.get());
            }
            return v;
        }
        return value;
    }

    @Override
    public void remove(K key) {
        if (removeInternal(key)) {
            strategy.removed(key);
        }
        nextCache.remove(key);
    }

    @Override
    public void clear() {
        clearInternal();
        strategy.cleared();
        nextCache.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyInternal(key) || nextCache.containsKey(key);
    }

    private static class EmptyCache<K, V> implements Cache<K, V> {

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

        @Override
        public boolean containsKey(K key) {
            return false;
        }
    }
}
