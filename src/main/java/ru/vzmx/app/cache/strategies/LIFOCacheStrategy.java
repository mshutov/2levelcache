package ru.vzmx.app.cache.strategies;

import ru.vzmx.app.cache.Strategy;

public class LIFOCacheStrategy<K> extends CommonLIFOFIFOCacheStrategy<K>  implements Strategy<K> {

    @Override
    public K selectKeyToRemove() {
        if (holder.size() > 0) {
            return holder.peekLast();
        }
        throw new IllegalStateException("No keys exists");
    }
}
