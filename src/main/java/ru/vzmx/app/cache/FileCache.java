package ru.vzmx.app.cache;

import ru.vzmx.app.cache.strategy.CacheStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FileCache<K, V> extends CommonCache<K, V> implements Cache<K, V> {
    private final Path cacheDir;
    private final Map<K, String> keyToFileMapping = new HashMap<>();

    protected FileCache(CacheStrategy strategy, int maxSize, Path cacheDir) {
        super(strategy, maxSize);
        this.cacheDir = Objects.requireNonNull(cacheDir);
        ensureCacheDir(cacheDir);
    }

    private void ensureCacheDir(Path cacheDir) {
        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            throw new IllegalArgumentException("Provided cache dir cannot be used: " + cacheDir, e);
        }
    }

    @Override
    protected boolean containsKeyInternal(K key) {
        return keyToFileMapping.containsKey(key);
    }

    @Override
    protected boolean removeInternal(K key) {
        String fileName = keyToFileMapping.remove(key);
        return fileName != null && removeFile(fileName);
    }

    @Override
    protected void clearInternal() {
        keyToFileMapping.values().forEach(this::removeFile);
        keyToFileMapping.clear();
    }

    @Override
    protected void putInternal(K key, V value) {
        String fileName = UUID.randomUUID().toString();
        keyToFileMapping.put(key, fileName);
        writeToFile(fileName, value);
    }

    @Override
    protected int sizeInternal() {
        return keyToFileMapping.size();
    }

    @Override
    protected V getInternal(K key) {
        String fileName = keyToFileMapping.get(key);
        return fileName != null ? readFromFile(fileName) : null;
    }

    private void writeToFile(String fileName, V value) {
        try (
                FileOutputStream fout = new FileOutputStream(getFile(fileName), true);
                ObjectOutputStream oos = new ObjectOutputStream(fout)
        ) {
            oos.writeObject(value);
        } catch (Exception ex) {
            // in case of error we could made some cleanup but not for this simple implementation
            ex.printStackTrace();
        }
    }

    private File getFile(String fileName) {
        return cacheDir.resolve(fileName + ".binary").toFile();
    }

    private V readFromFile(String fileName) {
        try (
                FileInputStream fin = new FileInputStream(getFile(fileName));
                ObjectInputStream ois = new ObjectInputStream(fin)
        ) {
            //noinspection unchecked
            return (V) ois.readObject();
        } catch (Exception ex) {
            // it may be useful to delete file if it is corrupted but not for this simple implementation
            ex.printStackTrace();
        }
        return null;
    }

    private boolean removeFile(String fileName) {
        return getFile(fileName).delete();
    }
}
