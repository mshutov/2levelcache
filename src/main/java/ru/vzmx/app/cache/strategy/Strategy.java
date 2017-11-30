package ru.vzmx.app.cache.strategy;

public interface Strategy<K> {
    void added(K key);

    void accessed(K key);

    void removed(K key);

    void cleared();

    K selectKeyToRemove();
}
