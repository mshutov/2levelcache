package ru.vzmx.app.cache;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.vzmx.app.cache.providers.FileProvider;
import ru.vzmx.app.cache.strategies.FIFOCacheStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileCacheTest extends CommonCacheTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void fileCacheOneValueTest() throws IOException {
        oneValueTest(
                new LeveledCache<>(
                        new FIFOCacheStrategy<>(),
                        new FileProvider<>(temporaryFolder.newFolder("one-value-dir").toPath()),
                        10)
        );
    }

    @Test
    public void overwrittenKeyKeepsOnlyLatestFile() throws IOException {
        Path cacheDir = temporaryFolder.newFolder("overwrite-dir").toPath();
        Provider<String, Integer> provider = new FileProvider<>(cacheDir);

        provider.put(key1, 1);
        provider.put(key1, 2);

        Assert.assertEquals(1, provider.size());
        Assert.assertEquals(1, countFiles(cacheDir));
        Assert.assertEquals(2, (int) provider.get(key1).orElseThrow(AssertionError::new));
    }

    @Test
    public void failedWriteDoesNotRegisterKey() throws IOException {
        Path cacheDir = temporaryFolder.newFolder("failed-write-dir").toPath();
        Provider<String, Object> provider = new FileProvider<>(cacheDir);

        try {
            provider.put(key1, new Object());
            Assert.fail("Expected write to fail for non-serializable value");
        } catch (IllegalStateException expected) {
            // expected
        }

        Assert.assertFalse(provider.containsKey(key1));
        Assert.assertEquals(0, provider.size());
        Assert.assertEquals(0, countFiles(cacheDir));
    }

    private long countFiles(Path cacheDir) throws IOException {
        try (Stream<Path> files = Files.list(cacheDir)) {
            return files.count();
        }
    }
}
