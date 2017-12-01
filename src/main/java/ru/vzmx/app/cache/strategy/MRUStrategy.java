package ru.vzmx.app.cache.strategy;

import java.time.LocalDateTime;

public class MRUStrategy<K> extends CommonRUStrategy<K> implements Strategy<K> {

    @Override
    protected boolean isFirstToEvict(LocalDateTime first, LocalDateTime second) {
        return first.isAfter(second);
    }
}
