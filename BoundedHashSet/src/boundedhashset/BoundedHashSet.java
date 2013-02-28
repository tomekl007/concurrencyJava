/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package boundedhashset;

/**
 *
 * @author Tomasz Lelek
 */
import java.util.*;
import java.util.concurrent.*;

/**
 * BoundedHashSet
 * <p/>
 * Using Semaphore to bound a collection
 *
 * @author Brian Goetz and Tim Peierls
 */
/*Similarly, you can use a Semaphore to turn any collection into a blocking bounded collection, 
 * as illustrated by BoundedHashSet in Listing 5.14. The semaphore is initialized to the desired
 * maximum size of the collection. The add operation acquires a permit before adding the item into 
 * the underlying collection. If the underlying add operation does not actually add anything, it 
 * releases the permit immediately. Similarly, a successful remove operation releases a permit,
 * enabling more elements to be added. The underlying Set implementation knows nothing about the bound;
 * this is handled by BoundedHashSet. */
public class BoundedHashSet <T> {
    private final Set<T> set;
    private final Semaphore sem;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException {
        sem.acquire();
        //Thread.sleep(3000);
        System.out.println("add : " + o.toString());
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded)
                sem.release();
        }
    }

    public boolean remove(Object o) {
        boolean wasRemoved = set.remove(o);
        System.out.println("remove : " + o.toString());
        if (wasRemoved)
            sem.release();
        return wasRemoved;
    }
}
