package ru.vzmx.app.cache.strategies;

import ru.vzmx.app.cache.Strategy;

import java.util.Deque;
import java.util.LinkedList;

abstract class CommonLIFOFIFOCacheStrategy<K> implements Strategy<K> {
    final Deque<K> holder = new LinkedList<>();

    @Override
    public void added(K key) {
        holder.add(key);
    }

    @Override
    public void accessed(K key) {
        // we don't need it
    }

    @Override
    public void removed(K key) {
        holder.remove(key);
    }

    @Override
    public void cleared() {
        holder.clear();
    }
}
