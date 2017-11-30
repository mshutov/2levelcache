package ru.vzmx.app.cache.strategy;

import java.util.LinkedList;

public class FIFOCacheStrategy<K> implements Strategy<K> {
    private final LinkedList<K> holder = new LinkedList<>();

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

    @Override
    public K selectKeyToRemove() {
        if (holder.size() > 0) {
            return holder.peekFirst();
        }
        throw new IllegalStateException("No keys exists");
    }
}
