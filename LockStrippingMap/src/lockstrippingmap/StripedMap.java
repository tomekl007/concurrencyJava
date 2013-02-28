/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lockstrippingmap;

/**
 *
 * @author Tomasz Lelek
 */
 /**
 * StripedMap
 * <p/>
 * Hash-based map using lock striping
 *
 * StripedMap in Listing 11.8 illustrates implementing a hash-based map using lock striping. 
 * There are N_LOCKS locks, each guarding a subset of the buckets. Most methods, like get, 
 * need acquire only a single bucket lock. Some methods may need to acquire all the locks
 * but, as in the implementation for clear, may not need to acquire them all simultaneously.
 */

//ThreadSafe
public class StripedMap {
    // Synchronization policy: buckets[n] guarded by locks[n%N_LOCKS]
    private static final int N_LOCKS = 16;
    private final Node[] buckets;
    private final Object[] locks;

    private static class Node {
        Node next;
        Object key;
        Object value;
    }

    public StripedMap(int numBuckets) {
        buckets = new Node[numBuckets];
        locks = new Object[N_LOCKS];
        for (int i = 0; i < N_LOCKS; i++)
            locks[i] = new Object();
    }

    private final int hash(Object key) {
       int hash =  Math.abs(key.hashCode() % buckets.length);
        System.out.println("hash : " +hash);
        return hash;
    }

    public Object get(Object key) {
        int hash = hash(key);
        synchronized (locks[hash % N_LOCKS]) {
            System.out.println("synch"+hash % N_LOCKS );
            for (Node m = buckets[hash]; m != null; m = m.next){
                if (m.key.equals(key)){
                    System.out.println("ret");
                    return m.value;
                }
            }
        }
        return null;
    }

    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            synchronized (locks[i % N_LOCKS]) {
                buckets[i] = null;
            }
        }
    }
}