package maps;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class FastCache2<K, V> {

    private HashMap<K, V> cache = new HashMap<>();

    ReadWriteLock lock = new ReentrantReadWriteLock();
    StampedLock sl = new StampedLock();
    ReentrantLock lockR = new ReentrantLock();
    Lock rLock = lock.readLock();
    Lock wLock = lock.writeLock();


    public void put(K key, V value) {
        wLock.lock();
        try {
            cache.put(key, value);
        } finally {
            wLock.unlock();
        }
    }

    public V get(K key) {
        rLock.lock();
        try {
            return cache.get(key);
        } finally {
            rLock.unlock();
        }
    }

    /**
     * Fast implementation of cache
     *
     * @param key
     * @param value
     * @return
     */
    public V get(K key, V value) {

        boolean wasWritten = false;

        long stamp;
        V v = null;
        //OPTIMISTIC READ
        if ((stamp = sl.tryOptimisticRead()) != 0L) { // optimistic
            if (!cache.containsKey(key)) {
                wasWritten = true;
            }
            else {
                v = cache.get(key);
                if (sl.validate(stamp))
                    return v;

            }
        }
        // READ PESSIMISTIC
        if(wasWritten == false) {
            stamp = sl.readLock(); // fall back to read lock
            try {
                return cache.get(key);
            } finally {
                sl.unlockRead(stamp);
            }
        }//WRITE
        else{

            stamp = sl.writeLock();
            try {
                put(key, value);

                return value;//cache.get(key);
            } finally {
                sl.unlockWrite(stamp);
            }
        }

    }

    /**
     * Etalon implementation of cache method
     *
     * @param key
     * @param value
     * @return
     */
    public V getEtalon(K key, V value) {
        rLock.lock();
        if (!cache.containsKey(key)) {
            rLock.unlock();
            wLock.lock();
            try {
                if (!cache.containsKey(key)) {
                    cache.put(key, value);
                    return value;
                } else {
                    rLock.lock();
                }
            } finally {
                wLock.unlock();
            }
        }

        try {
            return cache.get(key);
        } finally {
            rLock.unlock();
        }
    }
}
