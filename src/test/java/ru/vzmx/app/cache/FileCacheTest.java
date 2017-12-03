package ru.vzmx.app.cache;

import org.junit.Test;
import ru.vzmx.app.cache.providers.FileProvider;
import ru.vzmx.app.cache.strategies.FIFOCacheStrategy;

import java.nio.file.Paths;

public class FileCacheTest extends CommonCacheTest {
    @Test
    public void fileCacheOneValueTest() {
        oneValueTest(
                new LeveledCache<>(
                        new FIFOCacheStrategy<>(),
                        new FileProvider<>(Paths.get("one-value-dir")),
                        10)
        );
    }
}
