package ru.vzmx.app.cache.strategies;

import ru.vzmx.app.cache.Strategy;

public class FIFOCacheStrategy<K> extends CommonLIFOFIFOCacheStrategy<K>  implements Strategy<K> {

    @Override
    public K selectKeyToRemove() {
        if (holder.size() > 0) {
            return holder.peekFirst();
        }
        throw new IllegalStateException("No keys exists");
    }
}
