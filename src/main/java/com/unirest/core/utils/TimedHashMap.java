package com.unirest.core.utils;

import lombok.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("All")
public class TimedHashMap<K, V> {
    private final Map<K, V> map = new ConcurrentHashMap<>();
    private final DelayQueue<DelayedKey<K>> delayQueue = new DelayQueue<>();

    public void put(K key, V value, long timeoutMillis) {
        map.put(key, value);
        delayQueue.put(new DelayedKey<>(key, timeoutMillis));
    }

    public V get(K key) {
        return map.get(key);
    }

    private static class DelayedKey<K> implements Delayed {
        private final K key;
        private final long expirationTime;

         public DelayedKey(K key, long timeoutMillis) {
            this.key = key;
            this.expirationTime = System.currentTimeMillis() + timeoutMillis;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expirationTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(@NonNull Delayed o) {
            long otherDelay = ((DelayedKey) o).expirationTime;
            return Long.compare(expirationTime, otherDelay);
        }

        public K getKey() {
            return key;
        }
    }

}