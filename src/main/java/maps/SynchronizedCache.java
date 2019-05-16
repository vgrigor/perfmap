package maps;

import java.util.HashMap;
import java.util.Map;

public class SynchronizedCache<K, V> {

    private Map<K, V> cache = new HashMap<>();


    public void put(K key, V value) {
        synchronized (cache) {
            cache.put(key, value);
        }

    }

    public V get(K key) {
        synchronized (cache) {
            return cache.get(key);
        }
    }

    public V get(K key, V value) {
        synchronized (cache) {
            if (!cache.containsKey(key)) {
                cache.put(key, value);
                return value;
            }
            return cache.get(key);
        }
    }

/*        public static void main(String[] args) {

        }*/
}
