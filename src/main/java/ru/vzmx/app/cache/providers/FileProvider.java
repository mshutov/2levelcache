package ru.vzmx.app.cache.providers;

import ru.vzmx.app.cache.Provider;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileProvider<K, V> implements Provider<K, V> {
    private final Path cacheDir;
    private final Map<K, String> keyToFileMapping;

    public FileProvider(Path cacheDir) {
        this.cacheDir = Objects.requireNonNull(cacheDir);
        ensureCacheDir(cacheDir);
        keyToFileMapping = new HashMap<>();
    }

    private void ensureCacheDir(Path cacheDir) {
        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            throw new IllegalArgumentException("Provided cache dir cannot be used: " + cacheDir, e);
        }
    }

    @Override
    public boolean containsKey(K key) {
        return keyToFileMapping.containsKey(key);
    }

    @Override
    public boolean remove(K key) {
        String fileName = keyToFileMapping.remove(key);
        return fileName != null && removeFile(fileName);
    }

    @Override
    public void clear() {
        keyToFileMapping.values().forEach(this::removeFile);
        keyToFileMapping.clear();
    }

    @Override
    public void put(K key, V value) {
        String fileName = UUID.randomUUID().toString();
        keyToFileMapping.put(key, fileName);
        writeToFile(fileName, value);
    }

    @Override
    public int size() {
        return keyToFileMapping.size();
    }

    @Override
    public Optional<V> get(K key) {
        String fileName = keyToFileMapping.get(key);
        return Optional.ofNullable(fileName != null ? readFromFile(fileName) : null);
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
