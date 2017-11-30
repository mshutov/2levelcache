package ru.vzmx.app.cache;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;
import ru.vzmx.app.cache.strategy.CacheStrategy;

import java.util.Map;

public class CacheTest {
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

        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";

        cache.put(key1, 1);
        cache.put(key2, 2);
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true));

        cache.put(key3, 3);
        assertCacheState(cache, ImmutableMap.of(key1, false, key2, true, key3, true));
    }

    private void oneValueTest(Cache<String, Integer> cache) {
        String key = "some-test-key";
        Integer value = 123;
        assertCacheState(cache, ImmutableMap.of(key, false));

        cache.put(key, value);
        assertCacheState(cache, ImmutableMap.of(key, true));

        cache.remove(key);
        assertCacheState(cache, ImmutableMap.of(key, false));

        cache.put(key, value);
        assertCacheState(cache, ImmutableMap.of(key, true));

        cache.clear();
        assertCacheState(cache, ImmutableMap.of(key, false));
    }

    private <K> void assertCacheState(Cache<K, ?> cache, Map<K, Boolean> expected) {
        expected.forEach((key, isPresent) -> Assert.assertEquals(isPresent, cache.get(key).isPresent()));
    }
}
