package ru.vzmx.app.cache.strategy;

public class NoStrategy<K> implements Strategy<K> {
    @Override
    public void added(K key) {

    }

    @Override
    public void accessed(K key) {

    }

    @Override
    public void removed(K key) {

    }

    @Override
    public void cleared() {

    }

    @Override
    public K selectKeyToRemove() {
        throw new IllegalStateException("I'm a strategy that don't know this");
    }
}
