package readwriterlockmap;
import java.util.*;
import java.util.concurrent.locks.*;

/**
 * ReadWriteMap
 * <p/>
 * Wrapping a Map with a read-write lock
 *
 * Read-write locks can improve concurrency when locks are typically held for a moderately
 * long time and most operations do not modify the guarded resources. ReadWriteMap in 
 * Listing 13.7 uses a ReentrantReadWriteLock to wrap a Map so that it can be shared
 * safely by multiple readers and still prevent reader-writer or writer-writer conflicts.[7]
 * In reality, ConcurrentHashMap's performance is so good that you would probably use
 * it rather than this approach if all you needed was a concurrent hash-based map, but 
 * this technique would be useful if you want to provide more concurrent access to an
 * alternate Map implementation such as LinkedHashMap.
 */
public class ReadWriteMap <K,V> {
    private final Map<K, V> map;
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock r = lock.readLock();
    private final Lock w = lock.writeLock();

    public ReadWriteMap(Map<K, V> map) {
        this.map = map;
    }

    public V put(K key, V value) {
        w.lock();
        try {
            System.out.println("put " + Thread.currentThread().toString());
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }

    public V remove(Object key) {
        w.lock();
        try {
            return map.remove(key);
        } finally {
            w.unlock();
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        w.lock();
        try {
            map.putAll(m);
        } finally {
            w.unlock();
        }
    }

    public void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }

    public V get(Object key) {
        r.lock();
        try {
            System.out.println("get thread " + Thread.currentThread().toString());
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    public int size() {
        r.lock();
        try {
            return map.size();
        } finally {
            r.unlock();
        }
    }

    public boolean isEmpty() {
        r.lock();
        try {
            return map.isEmpty();
        } finally {
            r.unlock();
        }
    }

    public boolean containsKey(Object key) {
        r.lock();
        try {
            return map.containsKey(key);
        } finally {
            r.unlock();
        }
    }

    public boolean containsValue(Object value) {
        r.lock();
        try {
            return map.containsValue(value);
        } finally {
            r.unlock();
        }
    }
}
