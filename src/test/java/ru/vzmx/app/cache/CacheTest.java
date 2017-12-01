package ru.vzmx.app.cache;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;
import ru.vzmx.app.cache.strategy.CacheStrategy;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public class CacheTest {

    private final String key1 = "key1";
    private final String key2 = "key2";
    private final String key3 = "key3";
    private final String key4 = "key4";

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

    private void withSleepBefore(Runnable code) {
        try {
            TimeUnit.MILLISECONDS.sleep(30);
        } catch (InterruptedException e) {
            fail();
        }
        code.run();
    }

    private void oneValueTest(Cache<String, Integer> cache) {
        Integer value = 123;
        assertCacheState(cache, ImmutableMap.of(key1, false));

        cache.put(key1, value);
        assertCacheState(cache, ImmutableMap.of(key1, true));

        cache.remove(key1);
        assertCacheState(cache, ImmutableMap.of(key1, false));

        cache.put(key1, value);
        assertCacheState(cache, ImmutableMap.of(key1, true));

        cache.clear();
        assertCacheState(cache, ImmutableMap.of(key1, false));
    }

    private <K> void assertCacheState(Cache<K, ?> cache, Map<K, Boolean> expected) {
        expected.forEach((key, isPresent) -> Assert.assertEquals(isPresent, cache.get(key).isPresent()));
    }
}
