package ru.vzmx.app.cache;

import java.util.Objects;
import java.util.Optional;

public class LeveledCache<K, V> implements Cache<K, V> {
    private final Strategy<K> strategy;
    private final int maxSize;
    private final Cache<K, V> nextCache;
    private final Provider<K, V> provider;

    LeveledCache(Strategy<K> strategy, Provider<K, V> provider, int maxSize, Cache<K, V> nextCache) {
        this.strategy = Objects.requireNonNull(strategy);
        this.maxSize = maxSize;
        this.provider = Objects.requireNonNull(provider);
        this.nextCache = Objects.requireNonNull(nextCache) ;
    }

    LeveledCache(Strategy<K> strategy, Provider<K, V> provider, int maxSize) {
        this(strategy, provider, maxSize, new EmptyCache<>());
    }

    @Override
    public void put(K key, V value) {
        boolean keyAlreadyPresent = provider.containsKey(key);
        if (provider.size() == maxSize && !keyAlreadyPresent) {
            K keyToRemove = strategy.selectKeyToRemove();
            V val = provider.get(keyToRemove).orElseThrow(IllegalStateException::new);
            remove(keyToRemove);
            nextCache.put(keyToRemove, val);
        }
        provider.put(key, value);
        if (keyAlreadyPresent) {
            strategy.removed(key);
        }
        strategy.added(key);
    }

    @Override
    public Optional<V> get(K key) {
        Optional<V> value = provider.get(key);
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
        if (provider.remove(key)) {
            strategy.removed(key);
        }
        nextCache.remove(key);
    }

    @Override
    public void clear() {
        provider.clear();
        strategy.cleared();
        nextCache.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return provider.containsKey(key) || nextCache.containsKey(key);
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
