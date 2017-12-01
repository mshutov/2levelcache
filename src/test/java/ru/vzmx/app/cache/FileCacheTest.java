package ru.vzmx.app.cache;

import org.junit.Test;
import ru.vzmx.app.cache.strategy.CacheStrategy;

import java.nio.file.Paths;

public class FileCacheTest extends CommonCacheTest {
    @Test
    public void fileCacheOneValueTest() {
        oneValueTest(new FileCache<>(CacheStrategy.FIFO, 10, Paths.get("one-value-dir")));
    }
}
