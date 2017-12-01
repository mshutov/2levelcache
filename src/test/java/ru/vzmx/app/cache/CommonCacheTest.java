package ru.vzmx.app.cache;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

class CommonCacheTest {
    final String key1 = "key1";
    final String key2 = "key2";
    final String key3 = "key3";
    final String key4 = "key4";

    void oneValueTest(Cache<String, Integer> cache) {
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

    void withSleepBefore(Runnable code) {
        try {
            TimeUnit.MILLISECONDS.sleep(30);
        } catch (InterruptedException e) {
            fail();
        }
        code.run();
    }

    <K> void assertCacheState(Cache<K, ?> cache, Map<K, Boolean> expected) {
        expected.forEach((key, isPresent) -> Assert.assertEquals(isPresent, cache.get(key).isPresent()));
    }
}
