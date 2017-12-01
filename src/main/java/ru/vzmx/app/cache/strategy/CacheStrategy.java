package ru.vzmx.app.cache.strategy;

public enum CacheStrategy {
    FIFO {
        @Override
        public <K> Strategy<K> create() {
            return new FIFOCacheStrategy<>();
        }
    }, LIFO {
        @Override
        public <K> Strategy<K> create() {
            return new LIFOCacheStrategy<>();
        }
    }, LRU {
        @Override
        public <K> Strategy<K> create() {
            return new LRUStrategy<>();
        }
    }, MRU {
        @Override
        public <K> Strategy<K> create() {
            return new MRUStrategy<>();
        }
    }, NO_STRATEGY {
        @Override
        public <K> Strategy<K> create() {
            return new NoStrategy<>();
        }
    };

    public abstract <K> Strategy<K> create();

}
