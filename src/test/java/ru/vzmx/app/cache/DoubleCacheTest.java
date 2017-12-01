package ru.vzmx.app.cache;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;
import ru.vzmx.app.cache.strategy.CacheStrategy;

import java.util.Optional;

public class DoubleCacheTest extends CommonCacheTest {

    private final Cache<String, Integer> secondLevelCache = new MemoryCache<>(CacheStrategy.FIFO, 2);
    private final Cache<String, Integer> cache = new MemoryCache<>(CacheStrategy.FIFO, 2, secondLevelCache);

    @Test
    public void oneValueTestForDoubleCache(){
        oneValueTest(cache);
    }

    @Test
    public void allValuesAreOnFirstCacheLevelIfSizeAllows() {
        cache.put(key1, 1);
        cache.put(key2, 2);

        assertCacheState(secondLevelCache, ImmutableMap.of(key1, false, key2, false));
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true));
    }

    @Test
    public void oldValueMovedToSecondLevelIfFirstIsFull() {
        cache.put(key1, 1);
        cache.put(key2, 2);
        cache.put(key3, 3);

        assertCacheState(secondLevelCache, ImmutableMap.of(key1, true));
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true, key3, true));
    }

    @Test
    public void valueReturnedToFirstLevel() {
        cache.put(key1, 1);
        cache.put(key2, 2);
        cache.put(key3, 3);

        Optional<Integer> firstVal = cache.get(key1);
        Assert.assertTrue(firstVal.isPresent());
        Assert.assertEquals(1, (int) firstVal.get());
        assertCacheState(secondLevelCache, ImmutableMap.of(key1, false, key2, true));
        assertCacheState(cache, ImmutableMap.of(key1, true, key2, true, key3, true));
    }
}
