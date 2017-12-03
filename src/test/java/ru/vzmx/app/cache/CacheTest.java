package ru.vzmx.app.cache;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import ru.vzmx.app.cache.providers.MemoryProvider;
import ru.vzmx.app.cache.strategies.*;

public class CacheTest extends CommonCacheTest {

    @Test
    public void oneValueTestOfFIFOStrategyCache() {
        oneValueTest(new LeveledCache<>(new FIFOCacheStrategy<>(), new MemoryProvider<>(), 2));
    }

    @Test
    public void fifoStrategyTest() {
        Cache<String, Integer> cache = new LeveledCache<>(new FIFOCacheStrategy<>(), new MemoryProvider<>(), 2);

        cache.put(key1, 1);
        cache.put(key2, 2);
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true));

        cache.put(key3, 3);
        assertCacheState(cache, ImmutableMap.of(key1, false, key2, true, key3, true));
    }

    @Test
    public void lifoStrategyTest() {
        Cache<String, Integer> cache = new LeveledCache<>(new LIFOCacheStrategy<>(), new MemoryProvider<>(), 2);

        cache.put(key1, 1);
        cache.put(key2, 2);
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true));

        cache.put(key3, 3);
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, false, key3, true));
    }

    @Test
    public void lruStrategyTest() throws Exception {
        Cache<String, Integer> cache = new LeveledCache<>(new LRUStrategy<>(), new MemoryProvider<>(), 3);

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
        Cache<String, Integer> cache = new LeveledCache<>(new MRUStrategy<>(), new MemoryProvider<>(), 3);

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
