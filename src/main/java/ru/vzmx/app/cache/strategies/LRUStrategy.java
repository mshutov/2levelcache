package ru.vzmx.app.cache.strategies;

import ru.vzmx.app.cache.Strategy;

import java.time.LocalDateTime;

public class LRUStrategy<K> extends CommonLRUMRUStrategy<K> implements Strategy<K> {
    @Override
    protected boolean isFirstToEvict(LocalDateTime first, LocalDateTime second) {
        return first.isBefore(second);
    }
}
