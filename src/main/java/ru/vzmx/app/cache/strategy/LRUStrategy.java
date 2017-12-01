package ru.vzmx.app.cache.strategy;

import java.time.LocalDateTime;

public class LRUStrategy<K> extends CommonRUStrategy<K> implements Strategy<K> {
    @Override
    protected boolean isFirstToEvict(LocalDateTime first, LocalDateTime second) {
        return first.isBefore(second);
    }
}
