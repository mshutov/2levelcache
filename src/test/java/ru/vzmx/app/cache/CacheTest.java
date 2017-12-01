package ru.vzmx.app.cache;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import ru.vzmx.app.cache.strategy.CacheStrategy;

public class CacheTest extends CommonCacheTest {

    @Test
    public void oneValueTestOfNoStrategyCache() {
        oneValueTest(new MemoryCache<>(CacheStrategy.NO_STRATEGY, 2));
    }

    @Test
    public void oneValueTestOfFIFOStrategyCache() {
        oneValueTest(new MemoryCache<>(CacheStrategy.FIFO, 2));
    }

    @Test
    public void fifoStrategyTest() {
        Cache<String, Integer> cache = new MemoryCache<>(CacheStrategy.FIFO, 2);

        cache.put(key1, 1);
        cache.put(key2, 2);
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true));

        cache.put(key3, 3);
        assertCacheState(cache, ImmutableMap.of(key1, false, key2, true, key3, true));
    }

    @Test
    public void lifoStrategyTest() {
        Cache<String, Integer> cache = new MemoryCache<>(CacheStrategy.LIFO, 2);

        cache.put(key1, 1);
        cache.put(key2, 2);
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true));

        cache.put(key3, 3);
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, false, key3, true));
    }

    @Test
    public void lruStrategyTest() throws Exception {
        Cache<String, Integer> cache = new MemoryCache<>(CacheStrategy.LRU, 3);

        cache.put(key1, 1);

        withSleepBefore(() -> cache.put(key2, 2));
        withSleepBefore(() -> cache.put(key3, 3));

        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true, key3, true));

        withSleepBefore(() -> cache.get(key1));
        withSleepBefore(() -> cache.get(key3));

        cache.put(key4, 4);
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, false, key3, true, key4, true));
    }

    @Test
    public void mruStrategyTest() throws Exception {
        Cache<String, Integer> cache = new MemoryCache<>(CacheStrategy.MRU, 3);

        cache.put(key1, 1);

        withSleepBefore(() -> cache.put(key2, 2));
        withSleepBefore(() -> cache.put(key3, 3));

        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true, key3, true));

        withSleepBefore(() -> cache.get(key1));
        withSleepBefore(() -> cache.get(key3));

        cache.put(key4, 4);
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true, key3, false, key4, true));
    }

}
